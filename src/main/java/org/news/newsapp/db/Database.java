package org.news.newsapp.db;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;

/**
 * @Class: Database
 * @Author: Senuli Wickramage
 * @Description:
 * The Database class manages the connection to the MongoDB database. It provides a method to connect to the database
 * and makes the database and MongoClient instances accessible to other components of the application.
 * This class handles establishing a connection to the database and selecting the appropriate database.
 */
public class Database{
    // The MongoClient instance used to interact with the MongoDB server
    protected static MongoClient mongoClient;
    // The MongoClient instance used to interact with the MongoDB server
    protected static MongoDatabase database;

    /**
     * Establishes a connection to the MongoDB server and selects the "newswithdiagram" database.
     * This method is invoked to initialize the connection before interacting with the database.
     */
    public static void connectToDatabase(){
        mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost:27017"));
        database = mongoClient.getDatabase("newswithdiagram");
    }
}
