package org.example.diagramnewsrecommendation.db;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;

public class Database{
    protected static MongoClient mongoClient;
    protected static MongoDatabase database;
    public static void connectToDatabase(){
        mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost:27017"));
        database = mongoClient.getDatabase("newswithdiagram");
    }
}
