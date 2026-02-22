package com.example.tingeso1.exceptions;

/**
 * Exception thrown when there is an error during data persistence operations
 * such as save, update, or delete operations in the database.
 */
public class DataPersistenceException extends RuntimeException {

    public DataPersistenceException(String message) {
        super(message);
    }

    public DataPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
