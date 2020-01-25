package com.pedrogonic.swapi.repositories.impl;

import com.pedrogonic.swapi.application.components.OrikaMapper;
import com.pedrogonic.swapi.domain.Planet;
import com.pedrogonic.swapi.model.filters.PlanetFilter;
import com.pedrogonic.swapi.model.dtos.db.mongo.MongoPlanet;
import com.pedrogonic.swapi.repositories.IPlanetRepository;
import com.pedrogonic.swapi.repositories.IMongoPlanetRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

public class MongoPlanetRepository implements IPlanetRepository {

    // TODO Cleanup

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
//        return Optional.of( orikaMapper.map(mongoRepository.findById( new ObjectId(id.toString()) ), Planet.class) );
        // TODO conversion
        ObjectId objectId = new ObjectId( (String) id );
        Optional<MongoPlanet> optional = mongoRepository.findById( objectId );
        Optional<Planet> planet;
//        optional.ifPresent(mongoPlanet ->  { planet = Optional.of( orikaMapper.map(mongoPlanet, Planet.class) );});
//        optional.orElse(() -> planet = );
//        Optional<Planet> planet = Optional.of(orikaMapper.map(mongoPlanet );
        if (optional.isPresent())
            planet = Optional.of(orikaMapper.map(optional.get(), Planet.class ));
        else
            planet =  Optional.empty();
        return planet;
    }

    @Override
    public Planet save(Planet planet) {
        MongoPlanet mongoPlanet = orikaMapper.map(planet, MongoPlanet.class);
        return orikaMapper.map(mongoRepository.save(mongoPlanet), Planet.class);
    }

    @Override
    public Planet insert(Planet planet) {
        MongoPlanet mongoPlanet = orikaMapper.map(planet, MongoPlanet.class);
        return orikaMapper.map(mongoRepository.insert(mongoPlanet), Planet.class);
    }

    @Override
    public void deleteById(Object id) {
        // TODO conversion
        mongoRepository.deleteById( new ObjectId( id.toString() ) );
    }

    private Query createFilterQuery(final PlanetFilter planetFilter) {
        Query query = new Query();

        if (planetFilter.getName() != null)
            query.addCriteria(Criteria.where(MongoPlanet.FIELD_NAME).is(planetFilter.getName()));

        return query;
    }

//    @Override
//    public Iterable<DbPlanetDTO> findAll(Sort sort) {
//        return orikaMapper.mapAsList(mongoRepository.findAll(sort), DbPlanetDTO.class);
//    }
//
//    @Override
//    public Page<DbPlanetDTO> findAll(Pageable pageable) {
//
//        int pageSize = pageable.getPageSize();
//        long count = count();
//
//        return new PlanetPage<>(pageable
//                                        , (int) count/pageSize
//                                        , count
//                                        , orikaMapper.mapAsList(mongoRepository.findAll(), DbPlanetDTO.class));
//    }
//
//    @Override
//    public <S extends DbPlanetDTO> S save(S s) {
//        return mongoTemplate.save(s);
//    }
//
//    @Override
//    public <S extends DbPlanetDTO> Iterable<S> saveAll(Iterable<S> iterable) {
//        List<S> list = new ArrayList<>();
//        iterable.forEach(planet -> list.add(save(planet)));
//        return list;
//    }
//
//    @Override
//    public Optional<DbPlanetDTO> findById(Object o) {
//        return Optional.of(orikaMapper.map( mongoRepository.findById(new ObjectId(o.toString())), DbPlanetDTO.class ));
//    }
//
//    @Override
//    public boolean existsById(Object o) {
//        return mongoRepository.existsById( new ObjectId(o.toString()) );
//    }
//
//    @Override
//    public Iterable<DbPlanetDTO> findAll() {
//        return orikaMapper.mapAsList(mongoRepository.findAll(), DbPlanetDTO.class);
//    }
//
//    @Override
//    public Iterable<DbPlanetDTO> findAllById(Iterable<Object> iterable) {
//        List<DbPlanetDTO> list = new ArrayList<>();
//        iterable.forEach(planet -> findById(planet).ifPresent(p -> list.add(p)));
//        return list;
//    }
//
//    @Override
//    public long count() {
//        return mongoRepository.count();
//    }
//
//    @Override
//    public void deleteById(Object o) {
//        mongoRepository.deleteById(new ObjectId(o.toString()));
//    }
//
//    @Override
//    public void delete(DbPlanetDTO dbPlanetDTO) {
//        deleteById(dbPlanetDTO.getId());
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends DbPlanetDTO> iterable) {
//        iterable.forEach(planet -> delete(planet));
//    }
//
//    @Override
//    public void deleteAll() {
//        deleteAll(findAll());
//    }
}


//class PlanetPage<T> implements Page<T> {
//
//    Pageable pageable;
//    int totalPages;
//    long totalNumbers;
//    List<T> content;
//
//    public PlanetPage(Pageable pageable, int totalPages,
//                    long totalNumbers, List<T> content) {
//        super();
//        this.pageable = pageable;
//        this.totalPages = totalPages;
//        this.totalNumbers = totalNumbers;
//        this.content = content;
//    }
//
//    @Override
//    public int getNumber() {
//        return pageable.getPageNumber();
//    }
//
//    @Override
//    public int getSize() {
//        return pageable.getPageSize();
//    }
//
//    @Override
//    public int getTotalPages() {
//        return this.totalPages;
//    }
//
//    @Override
//    public int getNumberOfElements() {
//        return this.getContent().size();
//    }
//
//    @Override
//    public long getTotalElements() {
//        return this.totalNumbers;
//    }
//
//    @Override
//    public <U> Page<U> map(Function<? super T, ? extends U> function) {
//        return null;
//    }
//
//    @Override
//    public Iterator<T> iterator() {
//        return content.iterator();
//    }
//
//    @Override
//    public List<T> getContent() {
//        return content;
//    }
//
//    @Override
//    public boolean hasContent() {
//        return !content.isEmpty();
//    }
//
//    @Override
//    public Sort getSort() {
//        return this.pageable.getSort();
//    }
//
//    @Override
//    public boolean isFirst() {
//        return pageable.getPageNumber() == 0;
//    }
//
//    @Override
//    public boolean isLast() {
//        return pageable.getPageNumber() == totalPages;
//    }
//
//    @Override
//    public boolean hasNext() {
//        return pageable.getPageNumber() != totalPages;
//    }
//
//    @Override
//    public boolean hasPrevious() {
//        return pageable.getPageNumber() != 0;
//    }
//
//    @Override
//    public Pageable nextPageable() {
//        return null;
//    }
//
//    @Override
//    public Pageable previousPageable() {
//        return null;
//    }
//}