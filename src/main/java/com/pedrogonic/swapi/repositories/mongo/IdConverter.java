package com.pedrogonic.swapi.repositories.mongo;

import org.bson.types.ObjectId;

public class IdConverter {

    /**
     * Converts a String to a valid ObjectId
     * <p>
     * If the given Id is String representation of an ObjectId, it returns an ObjectId object in which its toString
     * method is the given String.
     * </p>
     * <p>
     * Otherwise, it creates a new ObjectId object with a random value.
     * </p>
     * @param id
     * @return a valid ObjectId
     */
    public static ObjectId stringToObjectId(String id) {
        try {
            return new ObjectId(id);
        } catch (Exception e) {
            return new ObjectId();
        }
    }

}
