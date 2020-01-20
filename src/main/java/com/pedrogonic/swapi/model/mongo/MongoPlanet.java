package com.pedrogonic.swapi.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = MongoPlanet.PLANET_COLLECTION)
public class MongoPlanet {

    public static final String PLANET_COLLECTION = "planets";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_NAME = "name";

    @Id
    private ObjectId id;

    // TODO Index and Validation

    private String name;


    private String climate;


    private String terrain;

}
