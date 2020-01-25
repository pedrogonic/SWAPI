package com.pedrogonic.swapi.repositories.mongo;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.model.dtos.db.mongo.MongoPlanet;
import com.pedrogonic.swapi.repositories.IPlanetRepository;
import com.pedrogonic.swapi.repositories.IMongoPlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

public class MongoPlanetRepository implements IPlanetRepository {

    @Autowired
    IMongoPlanetRepository mongoRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    OrikaMapper orikaMapper;

    @Override
    public List<Planet> findAll(final Pageable pageable, PlanetFilter planetFilter) {
        Query query = createFilterQuery(planetFilter);

        query.with(pageable);

        return orikaMapper.mapAsList(mongoTemplate.find(query, MongoPlanet.class), Planet.class);
    }

    @Override
    public Optional<Planet> findById(Object id) {
        Optional<MongoPlanet> optional = mongoRepository.findById( IdConverter.stringToObjectId( (String) id) );
        Optional<Planet> planet;

        if (optional.isPresent())
            planet = Optional.of(orikaMapper.map(optional.get(), Planet.class));
        else
            planet = Optional.empty();
        return planet;
    }

    @Override
    public Planet save(Planet planet) {
        String id = planet.getId();
        planet.setId(null);
        MongoPlanet mongoPlanet = orikaMapper.map(planet, MongoPlanet.class);
        mongoPlanet.setId( IdConverter.stringToObjectId(id) );
        return orikaMapper.map(mongoRepository.save(mongoPlanet), Planet.class);
    }

    @Override
    public Planet insert(Planet planet) {
        MongoPlanet mongoPlanet = orikaMapper.map(planet, MongoPlanet.class);
        return orikaMapper.map(mongoRepository.insert(mongoPlanet), Planet.class);
    }

    @Override
    public void deleteById(Object id) {
        mongoRepository.deleteById( IdConverter.stringToObjectId( (String) id) );
    }

    private Query createFilterQuery(final PlanetFilter planetFilter) {
        Query query = new Query();

        if (planetFilter.getName() != null)
            query.addCriteria(Criteria.where(MongoPlanet.FIELD_NAME).is(planetFilter.getName()));

        return query;
    }
}