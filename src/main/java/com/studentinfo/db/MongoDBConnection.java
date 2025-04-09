package com.studentinfo.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "studentInfoSystem";
    
    private static MongoDBConnection instance;
    private MongoClient mongoClient;
    private MongoDatabase database;
    
    private MongoDBConnection() {
        try {
            // Create a MongoDB client
            mongoClient = MongoClients.create(CONNECTION_STRING);
            
            // Connect to the database
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Connected to MongoDB successfully!");
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static synchronized MongoDBConnection getInstance() {
        if (instance == null) {
            instance = new MongoDBConnection();
        }
        return instance;
    }
    
    public MongoDatabase getDatabase() {
        return database;
    }
    
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }
} 