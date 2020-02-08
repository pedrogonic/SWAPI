package com.pedrogonic.swapi.model.dtos.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestResponseDTO {

    protected String next;

    protected List  results;

}
