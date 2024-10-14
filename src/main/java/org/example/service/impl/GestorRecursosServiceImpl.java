package org.example.service.impl;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
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
        return recursos;//retorna el recurso
    }
//El patrón Builder se usa para crear nuevas instancias de RecursoTIC en el método crearRecurso
// y en el proceso de actualización donde se crea un nuevo recurso.

//Los demás métodos en RecursoTICController y GestorRecursosService manejan instancias ya creadas
// y no necesitan implementar el patrón Builder.
    @Override
    public boolean actualizarRecurso(String codigo, RecursoTIC nuevoRecurso) {
        RecursoTIC recurso = buscarRecurso(codigo);
        if (recurso != null) {
            // Creamos un nuevo recurso usando el Builder con los valores actualizados
            RecursoTIC recursoActualizado = new RecursoTIC.Builder(recurso.getCodigo())
                    .setNombre(nuevoRecurso.getNombre())
                    .setTipo(nuevoRecurso.getTipo())
                    .setEstado(nuevoRecurso.getEstado())
                    .build();

            // Reemplazar el recurso antiguo con el actualizado
            recursos.remove(recurso);
            recursos.add(recursoActualizado);
            return true;
        }
        return false;
    }

    @Override
    public RecursoTIC buscarRecurso(String codigo) {
        for (RecursoTIC recurso : recursos) {
            if (recurso.getCodigo().equals(codigo)) {
                return recurso;
            }
        }
        return null;
    }


}
