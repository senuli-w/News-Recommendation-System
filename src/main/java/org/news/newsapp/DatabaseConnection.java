package org.news.newsapp;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {
    public static void connect(){
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        try (MongoClient mongoClient = MongoClients.create(connectionString) ){
            MongoDatabase database = mongoClient.getDatabase("news");
            System.out.println(database.getName());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
