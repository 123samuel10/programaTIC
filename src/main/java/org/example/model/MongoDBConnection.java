package org.example.model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBConnection {
    private static final String URI = "mongodb+srv://root:123@cluster-2.0pffj.mongodb.net/proyectoTic?retryWrites=true&w=majority&appName=Cluster-2";
    private static final String DATABASE_NAME = "proyectoTic";
    private static final String COLLECTION_NAME = "proyecto";
    private MongoDatabase database;

    public MongoDBConnection() {
        // Crear cliente de MongoDB
        MongoClient mongoClient = MongoClients.create(URI);
        // Obtener base de datos
        this.database = mongoClient.getDatabase(DATABASE_NAME);
    }

    public MongoCollection<Document> getCollection() {
        // Obtener la colección
        return database.getCollection(COLLECTION_NAME);
    }
}
