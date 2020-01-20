package com.pedrogonic.swapi.application.utils;

import org.bson.types.ObjectId;

public class ConversionUtils {

    public static ObjectId stringToObjectId(String id) {
        try {
            return new ObjectId(id);
        } catch (Exception e) {
            return new ObjectId();
        }
    }

}
