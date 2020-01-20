package com.pedrogonic.swapi.application.components;

import ma.glasnost.orika.Converter;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class OrikaMapper extends ConfigurableMapper implements ApplicationContextAware {

  private MapperFactory factory;
  private ApplicationContext applicationContext;

  public OrikaMapper() {
    super(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void configure(final MapperFactory factory) {
    this.factory = factory;
    addAllSpringBeans(applicationContext);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void configureFactoryBuilder(final DefaultMapperFactory.Builder factoryBuilder) {
    // Nothing to configure for now
  }

  /**
   * Constructs and registers a {@link ClassMapBuilder} into the {@link MapperFactory} using a
   * {@link Mapper}.
   */
  @SuppressWarnings("rawtypes")
  private void addMapper(final Mapper<?, ?> mapper) {
    factory.classMap(mapper.getAType(), mapper.getBType()).byDefault().customize((Mapper) mapper).register();
  }

  /**
   * Registers a {@link Converter} into the {@link ConverterFactory}.
   */
  private void addConverter(final Converter<?, ?> converter) {
    factory.getConverterFactory().registerConverter(converter);
  }

  /**
   * Scans the appliaction context and registers all Mappers and Converters found in it.
   */
  @SuppressWarnings("rawtypes")
  private void addAllSpringBeans(final ApplicationContext applicationContext) {
    Map<String, Mapper> mappers = applicationContext.getBeansOfType(Mapper.class);
    for (Mapper mapper : mappers.values()) {
      addMapper(mapper);
    }
    Map<String, Converter> converters = applicationContext.getBeansOfType(Converter.class);
    for (Converter converter : converters.values()) {
      addConverter(converter);
    }
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    init();
  }
}
