package com.pedrogonic.swapi.application.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoConverter mongoConverter;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("Mongo InitIndicesAfterStartup init");
        var init = System.currentTimeMillis();

        var mappingContext = this.mongoConverter.getMappingContext();

        if (mappingContext instanceof MongoMappingContext) {
            var mongoMappingContext = (MongoMappingContext) mappingContext;

            for (BasicMongoPersistentEntity<?> persistentEntity : mongoMappingContext.getPersistentEntities()) {
                var clazz = persistentEntity.getType();
                if (clazz.isAnnotationPresent(Document.class)) {
                    var indexOps = mongoTemplate.indexOps(clazz);
                    var resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
                    resolver.resolveIndexFor(clazz).forEach(indexOps::ensureIndex);
                }
            }
        }
        log.info("Mongo InitIndicesAfterStartup take: {}", (System.currentTimeMillis() - init));
    }
}
