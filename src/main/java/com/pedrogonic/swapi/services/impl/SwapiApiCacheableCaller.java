package com.pedrogonic.swapi.services.impl;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.model.dtos.http.RestResponseDTO;
import com.pedrogonic.swapi.model.dtos.swapi.SwapiPlanetDTO;
import com.pedrogonic.swapi.services.ExternalApiCacheableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SwapiApiCacheableCaller extends ExternalApiCacheableService {

    private final String URI = "https://swapi.co/api/planets";

    private final Class SEARCH_DTO_CLASS = SwapiPlanetDTO.class;

    public SwapiApiCacheableCaller(RestTemplate restTemplate, OrikaMapper orikaMapper) {
        super(restTemplate, orikaMapper);
        this.uri = URI;
        this.searchDtoClass = SEARCH_DTO_CLASS;
    }

    @Cacheable(cacheNames = "planets")
    public List<SwapiPlanetDTO> getAll( /*Map<String, String> params*/ ) throws HttpServerErrorException {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri);

//        for (Map.Entry<String, String> entry : params.entrySet())
//            uriBuilder.queryParam(entry.getKey(), entry.getValue());


        List results = new ArrayList<>();

        RestResponseDTO searchDto;

//        try {
        searchDto = new RestResponseDTO();

        searchDto.setNext(uriBuilder.build().toString());

        while (searchDto.getNext() != null) {
            log.info("Calling SWAPI URI: " + searchDto.getNext());
            searchDto = restTemplate.getForObject(searchDto.getNext(), RestResponseDTO.class);
            results.addAll(searchDto.getResults());
        }

//        } catch (final HttpClientErrorException e) {
//            throw e;
//            // Throwing error if SWAPI is unreachable by any reason. (E.g.: 500 Internal Server Error, 429 Too Many Requests , ...
//            // TODO
////            throw new SwapiUnreachableException(messages.getErrorSwapiUnreachable(e.getStatusCode().toString()));
//        }

        results = orikaMapper.mapAsList(results, searchDtoClass);

        return results;
    }

}
