package com.pedrogonic.swapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Planet implements Serializable {

    private String id;

    private String name;
    private String climate;
    private String terrain;
    private int filmCount;

}
