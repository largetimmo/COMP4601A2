package edu.carleton.comp4601.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public abstract class AbstractDAO {

    private static final String DB_NAME = "sda";

    protected String collectionName;

    protected MongoClient mongoClient;

    protected MongoDatabase database;

    protected MongoCollection<Document> collection;

    public AbstractDAO(String collectionName){
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase(DB_NAME);
        collection = database.getCollection(collectionName);
        this.collectionName = collectionName;
        collection.drop();
    }


}
