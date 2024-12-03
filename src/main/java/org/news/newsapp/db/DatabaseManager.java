package org.news.newsapp.db;

import java.util.List;

/**
 * @Interface: DatabaseManager
 * @Author: Senuli Wickramage
 * @Description:
 * The DatabaseManager interface defines the essential methods required for managing database records of a generic type T.
 * It provides a contract for creating, retrieving, and listing records in a database. This interface allows flexibility for
 * different types of objects to be stored and retrieved from the database (e.g., Article, User, etc.).
 *
 * @param <T> The type of object that this manager will handle (e.g., Article, User).
 */
public interface DatabaseManager<T> {

    /**
     * Adds a new record of type T to the database.
     *
     * @param element The record to be added to the database.
     */
    void createNew(T element);

    /**
     * Retrieves a record of type T from the database by its unique value (e.g., ID).
     *
     * @param uniqueValue The unique identifier of the record to be retrieved.
     * @return The record of type T corresponding to the unique identifier.
     */
    T get(String uniqueValue);

    /**
     * Retrieves all records of type T from the database.
     *
     * @return A list of all records of type T in the database.
     */
    List<T> getAll();
}
