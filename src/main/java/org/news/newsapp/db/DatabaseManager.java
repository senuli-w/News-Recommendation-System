package org.news.newsapp.db;

import java.util.List;

public interface DatabaseManager<T> {

    // Add a new record
    void createNew(T element);

    // Get a record by its ID
    T get(String uniqueValue);

    // Get all records of type T
    List<T> getAll();
}
