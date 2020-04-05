package edu.carleton.comp4601.dao;

import org.bson.Document;

/**
 * Base Mongo edu.carleton.comp4601.dao interface
 * @param <T> Entity class
 */

public interface MongoDAO<T> {

    Document map(T entity);

    T map(Document document);
}
