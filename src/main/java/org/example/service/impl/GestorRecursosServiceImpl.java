package org.example.service.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.model.MongoDBConnection;
import org.example.model.RecursoTIC;
import org.example.service.GestorRecursosService;

import java.util.ArrayList;
import java.util.List;

public class GestorRecursosServiceImpl implements GestorRecursosService {

    // Instancia única del Singleton
    private static GestorRecursosServiceImpl instancia;

    /*
      Declara una lista de objetos RecursoTIC llamada 'recursos'.
      Esta lista almacenará los recursos en memoria.
      Se utiliza un ArrayList, lo que permite que la lista crezca dinámicamente
      a medida que se agregan nuevos elementos.
   */
    List<RecursoTIC> recursos = new ArrayList<>();



    /*
      Declara una variable privada 'coleccion', que representa una colección de MongoDB.
      La clase MongoCollection es parte de la API de MongoDB y está parametrizada con 'Document',
      que es el tipo de dato que representa los documentos almacenados en MongoDB.
   */
    private MongoCollection<Document> coleccion;

      /*
       Constructor privado para evitar la creación de múltiples instancias (patrón Singleton).
       Inicializa la lista de recursos y la colección de MongoDB.
    */

    private GestorRecursosServiceImpl() {
        recursos = new ArrayList<>(); // Inicializa la lista de recursos
        MongoDBConnection mongoDB = new MongoDBConnection(); // Instancia de MongoDBConnection
        this.coleccion = mongoDB.getCollection();// Asigna la colección de MongoDB a la variable 'coleccion'
    }


       /*
       Método público para obtener la única instancia del Singleton.
       Este método revisa si la variable estática 'instancia' es null. Si es así, crea una nueva instancia de GestorRecursosServiceImpl.
       De lo contrario, retorna la instancia ya existente.
    */
    public static GestorRecursosServiceImpl getInstance() {
        if (instancia == null) {
            instancia = new GestorRecursosServiceImpl();
        }
        return instancia;
    }


    @Override
    public void agregarRecurso(RecursoTIC recurso) {
        recursos.add(recurso);
        System.out.println("Recurso agregado exitosamente: " + recurso);
       /*
           BASE DE DATOS:
           Crea un objeto Document para representar los datos del recurso.
           El documento contiene los campos "codigo", "nombre", "tipo" y "estado",
           que se rellenan con los valores correspondientes del objeto recurso.
        */
        Document documento = new Document("codigo", recurso.getCodigo())
                .append("nombre", recurso.getNombre())
                .append("tipo", recurso.getTipo())
                .append("estado", recurso.getEstado());
        // Inserta el documento en MongoDB
        coleccion.insertOne(documento);
        System.out.println("Recurso agregado a MongoDB: " + recurso);
    }

    @Override
    public List<RecursoTIC> listarRecursos() {
        // Lista para almacenar los recursos de MongoDB
        List<RecursoTIC> recursosDB = new ArrayList<>();

          /*
           Lee todos los documentos de la colección de MongoDB y convierte cada uno en un objeto RecursoTIC.
           Luego, los agrega a la lista 'recursosDB'.
        */
        for (Document doc : coleccion.find()) {
            // Convertir cada documento en un objeto RecursoTIC y agregarlo a la lista
            RecursoTIC recurso = new RecursoTIC.Builder(doc.getString("codigo"))
                    .setNombre(doc.getString("nombre"))
                    .setTipo(doc.getString("tipo"))
                    .setEstado(doc.getString("estado"))
                    .build();
            recursosDB.add(recurso);
        }

          /*
           Combina los recursos en memoria y los recursos obtenidos de MongoDB en una lista
           y los devuelve como el conjunto completo de recursos.
        */
        List<RecursoTIC> todosRecursos = new ArrayList<>(recursos); // recursos en memoria
        todosRecursos.addAll(recursosDB); // agregar recursos de MongoDB

        return todosRecursos; // devolver todos los recursos
    }
    /*
          El patrón Builder se utiliza para crear nuevas instancias de RecursoTIC en los métodos como 'crearRecurso'
          y en el proceso de actualización.
          Otros métodos como los controladores no necesitan el patrón Builder ya que solo manejan instancias existentes.
       */
    @Override
    public boolean actualizarRecurso(String codigo, RecursoTIC nuevoRecurso) {
        // Busca el recurso en la lista en memoria
        RecursoTIC recurso = buscarRecurso(codigo);

        if (recurso != null) {
            // Actualiza el recurso en memoria
            RecursoTIC recursoActualizado = new RecursoTIC.Builder(recurso.getCodigo())
                    .setNombre(nuevoRecurso.getNombre())
                    .setTipo(nuevoRecurso.getTipo())
                    .setEstado(nuevoRecurso.getEstado())
                    .build();

            recursos.remove(recurso);
            recursos.add(recursoActualizado);

            // Actualiza el recurso en MongoDB
            Bson filtro = Filters.eq("codigo", codigo);// Filtro para buscar por código

           /*
               Actualización de los campos en MongoDB utilizando la API de MongoDB en Java.
               Se utiliza Bson para representar los filtros y las actualizaciones a realizar.
            */
            Bson actualizacion = Updates.combine(
                    Updates.set("nombre", nuevoRecurso.getNombre()),
                    Updates.set("tipo", nuevoRecurso.getTipo()),
                    Updates.set("estado", nuevoRecurso.getEstado())
            );

            // Busca y actualiza el documento en MongoDB
            Document recursoActualizadoEnDB = coleccion.findOneAndUpdate(filtro, actualizacion);

            if (recursoActualizadoEnDB != null) {
                System.out.println("Recurso actualizado en MongoDB: " + recursoActualizado);
                return true;
            } else {
                System.out.println("No se encontró el recurso con el código: " + codigo);
            }
        }

        return false;
    }

    @Override
    public RecursoTIC buscarRecurso(String codigo) {
        // Busca el recurso en la lista en memoria
        for (RecursoTIC recurso : recursos) {
            if (recurso.getCodigo().equals(codigo)) {
                return recurso;
            }
        }

        // Si no se encuentra en memoria, busca en MongoDB
        Document doc = coleccion.find(Filters.eq("codigo", codigo)).first();

        if (doc != null) {
            // Convierte el documento en un objeto RecursoTIC
            /*
             Aquí, estamos creando una nueva instancia de la clase interna Builder.
             El constructor del Builder requiere un parámetro obligatorio: el código del recurso (codigo).
             El valor de codigo se obtiene del documento de MongoDB (doc), usando doc.getString("codigo").
             */

            RecursoTIC recurso = new RecursoTIC.Builder(doc.getString("codigo"))
                    .setNombre(doc.getString("nombre"))
                    .setTipo(doc.getString("tipo"))
                    .setEstado(doc.getString("estado"))
                    .build();

            return recurso;
        }

        // Si no se encuentra el recurso, retorna null
        return null;
    }


}
