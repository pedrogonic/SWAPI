package com.pedrogonic.swapi.services.impl;

import com.pedrogonic.swapi.application.components.Messages;
import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.application.exception.PlanetNotFoundException;
import com.pedrogonic.swapi.application.exception.SwapiUnreachableException;
import com.pedrogonic.swapi.application.utils.ConversionUtils;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.repositories.IPlanetRepository;
import com.pedrogonic.swapi.services.IPlanetService;
import com.pedrogonic.swapi.services.ISwapiService;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PlanetService implements IPlanetService {

    @Autowired
    IPlanetRepository planetRepository;

    @Autowired
    ISwapiService swapiService;

    @Autowired
    OrikaMapper orikaMapper;

    @Autowired
    Messages messages;

    @Override
    public List<Planet> findAll(Pageable pageable, PlanetFilter planetFilter) throws SwapiUnreachableException {

        List<Planet> planets;

        planets = planetRepository.findAll(pageable, planetFilter);

        switch (planets.size()) {
            case 1:
                try {

                    planets.get(0).setFilmCount( swapiService.findPlanetByName( planets.get(0).getName() ) .getFilmCount() );

                } catch (PlanetNotFoundException e) {
                    // Only happens if Planet is included directly in the database
                    // since the api checks against the original SWAPI when creating
                    // a new planet.
                    log.error(e.getMessage());
                }
                break;
            case 0: break;
            default:
                List<Planet> swapiPlanets = swapiService.findAll();

                planets = planets.stream().map(
                        planet -> {
                            Planet swapiPlanet = swapiPlanets.stream().filter(
                                    sp -> sp.getName().equals(planet.getName())
                                            ).findFirst().orElse(new Planet());
                            planet.setFilmCount(swapiPlanet.getFilmCount());
                            return planet;
                        }
                ).collect(Collectors.toList());

                break;
        }

        return planets;
    }

    @Override
    public Planet findById(String id) throws PlanetNotFoundException, SwapiUnreachableException {

        Planet planet = planetRepository.findById(id)
                .orElseThrow(() -> new PlanetNotFoundException(messages.getErrorPlanetNotFoundById(id)));

        planet.setFilmCount(swapiService.findPlanetByName(planet.getName()).getFilmCount());

        return planet;
    }

    @Override
    public Planet updatePlanet(Planet planet) throws PlanetNotFoundException, SwapiUnreachableException {

        int filmCount = swapiService.findPlanetByName(planet.getName()).getFilmCount();

        String id = planet.getId();

        planetRepository.findById(planet.getId()).orElseThrow(() -> new PlanetNotFoundException(messages.getErrorPlanetNotFoundById(id)));

        planet = planetRepository.save(planet);

        planet.setFilmCount(filmCount);

        return planet;
    }

    @Override
    public Planet createPlanet(Planet planet) throws PlanetNotFoundException, SwapiUnreachableException {

        int filmCount = swapiService.findPlanetByName(planet.getName()).getFilmCount();

        planet = planetRepository.insert(planet);

        planet.setFilmCount(filmCount);

        return planet;
    }

    @Override
    public void deletePlanetById(String id) {
        ObjectId objectId = ConversionUtils.stringToObjectId(id);
        planetRepository.deleteById(objectId);
    }
}
