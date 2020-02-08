package com.pedrogonic.swapi.services;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.SwapiUnreachableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public abstract class ExternalApiCacheableService {

    protected String uri;
    protected Class searchDtoClass;

    protected final RestTemplate restTemplate;

    protected final OrikaMapper orikaMapper;

    public ExternalApiCacheableService(RestTemplate restTemplate, OrikaMapper orikaMapper) {
        this.restTemplate = restTemplate;
        this.orikaMapper = orikaMapper;
    }

    public abstract List getAll( Map<String, String> params ) throws SwapiUnreachableException;

}
