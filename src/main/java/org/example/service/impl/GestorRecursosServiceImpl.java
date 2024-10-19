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

    //Declara una lista de objetos RecursoTIC llamada recursos. Esta lista almacenará los recursos en memoria.
    // Es un ArrayList, lo que significa que puede crecer dinámicamente a medida que se agregan elementos.
    List<RecursoTIC> recursos = new ArrayList<>();



    //Declara una variable privada coleccion, que representa una colección de MongoDB.
    // La clase MongoCollection es parte de la API de MongoDB,y está parametrizada con Document,
    // que es el tipo de dato que representa los documentos almacenados en MongoDB
    private MongoCollection<Document> coleccion;

    // Constructor privado para evitar creación de múltiples instancias (singleton)

    private GestorRecursosServiceImpl() {
        recursos = new ArrayList<>();//Inicializa la lista de recursos (recursos = new ArrayList<>();).
        MongoDBConnection mongoDB = new MongoDBConnection();//Se instancia un objeto de tipo MongoDBConnection
        this.coleccion = mongoDB.getCollection();//se asigna a la variable coleccion.Esta es la colección donde se almacenarán los documentos de los recursos en MongoDB.
    }

    // Método público para obtener la única instancia del Singleton(Este método es clave para el patrón Singleton.)

    //static: Esto permite que el método sea accedido sin necesidad de una instancia previa de la clase.

    //Instancia única: El método revisa si la variable estática instancia
    // (que no aparece aquí pero debe estar declarada en la clase) es null.
    // Si lo es, entonces crea una nueva instancia de GestorRecursosServiceImpl.
    // Si ya existe una instancia, simplemente retorna la existente.
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
        //base de datos
        //Aquí se está creando un objeto de tipo Document (que es parte de la API de MongoDB en Java)
        // para representar los datos que se insertarán en la base de datos.
        //Se está creando el documento con varios campos: "codigo", "nombre", "tipo" y "estado",
        // que se rellenan con los valores correspondientes de los métodos getCodigo(), getNombre(), getTipo() y getEstado()
        // del objeto recurso.
        Document documento = new Document("codigo", recurso.getCodigo())
                .append("nombre", recurso.getNombre())
                .append("tipo", recurso.getTipo())
                .append("estado", recurso.getEstado());
        coleccion.insertOne(documento);
        System.out.println("Recurso agregado a MongoDB: " + recurso);
    }

    @Override
    public List<RecursoTIC> listarRecursos() {
        // Lista para almacenar los recursos de MongoDB
        List<RecursoTIC> recursosDB = new ArrayList<>();

        // Leer todos los documentos de la colección MongoDB
        for (Document doc : coleccion.find()) {
            // Convertir cada documento en un objeto RecursoTIC y agregarlo a la lista
            RecursoTIC recurso = new RecursoTIC.Builder(doc.getString("codigo"))
                    .setNombre(doc.getString("nombre"))
                    .setTipo(doc.getString("tipo"))
                    .setEstado(doc.getString("estado"))
                    .build();
            recursosDB.add(recurso);
        }

        // Combinar los recursos en memoria y los de la base de datos
        List<RecursoTIC> todosRecursos = new ArrayList<>(recursos); // recursos en memoria
        todosRecursos.addAll(recursosDB); // agregar recursos de MongoDB

        return todosRecursos; // devolver todos los recursos
    }
//El patrón Builder se usa para crear nuevas instancias de RecursoTIC en el método crearRecurso
// y en el proceso de actualización donde se crea un nuevo recurso.

//Los demás métodos en RecursoTICController y GestorRecursosService manejan instancias ya creadas
// y no necesitan implementar el patrón Builder.
    @Override
    public boolean actualizarRecurso(String codigo, RecursoTIC nuevoRecurso) {
        // Buscar el recurso en la lista en memoria primero
        RecursoTIC recurso = buscarRecurso(codigo);

        if (recurso != null) {
            // Actualizar en memoria
            RecursoTIC recursoActualizado = new RecursoTIC.Builder(recurso.getCodigo())
                    .setNombre(nuevoRecurso.getNombre())
                    .setTipo(nuevoRecurso.getTipo())
                    .setEstado(nuevoRecurso.getEstado())
                    .build();

            recursos.remove(recurso);
            recursos.add(recursoActualizado);

            // Ahora, actualizar el recurso en MongoDB
            Bson filtro = Filters.eq("codigo", codigo);

            // Actualizamos los campos necesarios
            //Bson es una interfaz en la API de MongoDB para Java que representa una estructura de datos en formato BSON (Binary JSON).
            //Ejemplo de uso de Bson en tu código: Filtros, Actualizaciones
            Bson actualizacion = Updates.combine(
                    Updates.set("nombre", nuevoRecurso.getNombre()),
                    Updates.set("tipo", nuevoRecurso.getTipo()),
                    Updates.set("estado", nuevoRecurso.getEstado())
            );

            // Se utiliza findOneAndUpdate para buscar y actualizar el recurso en la base de datos.
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
        // Primero busca en la lista en memoria
        for (RecursoTIC recurso : recursos) {
            if (recurso.getCodigo().equals(codigo)) {
                return recurso;
            }
        }

        // Si no se encuentra en memoria, busca en MongoDB
        Document doc = coleccion.find(Filters.eq("codigo", codigo)).first();

        if (doc != null) {
            // Convierte el documento de MongoDB en un objeto RecursoTIC
            RecursoTIC recurso = new RecursoTIC.Builder(doc.getString("codigo"))
                    .setNombre(doc.getString("nombre"))
                    .setTipo(doc.getString("tipo"))
                    .setEstado(doc.getString("estado"))
                    .build();

            return recurso;
        }

        // Si no se encuentra en ningún lado, retorna null
        return null;
    }


}
