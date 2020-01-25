package com.pedrogonic.swapi.model.dtos.db.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Document(collection = MongoPlanet.PLANET_COLLECTION)
public class MongoPlanet {

    public static final String PLANET_COLLECTION = "planets";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_NAME = "name";

    @Id
    private ObjectId id;

    // TODO Auto index creation policy - See WARNING on startup
    @Indexed(unique = true, background = true)
    @NotBlank(message = "{planet.name.NotBlank}")
    private String name;

    @NotBlank(message = "{planet.climate.NotBlank}")
    private String climate;

    @NotBlank(message = "{planet.terrain.NotBlank}")
    private String terrain;

}
