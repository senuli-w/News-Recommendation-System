package org.example.diagramnewsrecommendation.db;

import java.util.List;

public interface DatabaseManager<T> {
    void createNew(T element); // Add a new record
    T get(String uniqueValue);         // Get a record by its ID
    List<T> getAll();          // Get all records of type T
}
