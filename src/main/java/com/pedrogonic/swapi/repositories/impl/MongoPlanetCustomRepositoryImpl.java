package com.pedrogonic.swapi.repositories.impl;

import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.model.mongo.MongoPlanet;
import com.pedrogonic.swapi.repositories.MongoPlanetCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class MongoPlanetCustomRepositoryImpl implements MongoPlanetCustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<MongoPlanet> findAll(final Pageable pageable,PlanetFilter planetFilter) {
        Query query = new Query();

        // TODO review. should always be pageable?

        // If the filter is empty, find all with pagination
        if (planetFilter.equals(new PlanetFilter()))
            query.with(pageable);
        // Else, create a query
        else
            query = createFilterQuery(planetFilter);

        return mongoTemplate.find(query, MongoPlanet.class);
    }

    private Query createFilterQuery(final PlanetFilter planetFilter) {
        Query query = new Query();

        if (planetFilter.getId() != null)
            query.addCriteria(Criteria.where(MongoPlanet.FIELD_ID).is(planetFilter.getId()));

        if (planetFilter.getName() != null)
            query.addCriteria(Criteria.where(MongoPlanet.FIELD_NAME).is(planetFilter.getName()));

        return query;
    }

}
