package com.my.hotel.common;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectMapperUtils {

	private static ModelMapper modelMapper = new ModelMapper();
	
	/**
     * Model mapper property setting are specified in the following block.
     * Default property matching strategy is set to Strict see {@link MatchingStrategies}
     * Custom mappings are added using {@link ModelMapper#addMappings(PropertyMap)}
     */	
	static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }
	
	/**
     * Hide from public usage.
     */	
	private ObjectMapperUtils() {
    }
	
	/**
     * <p>Note: outClass object must have default constructor with no arguments</p>
     *
     * @param <D>      type of result object.
     * @param <T>      type of source object to map from.
     * @param entity   entity that needs to be mapped.
     * @param outClass class of result object.
     * @return new object of <code>outClass</code> type.
     */
    public static <D, T> D map(final T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    /**
     * <p>Note: outClass object must have default constructor with no arguments</p>
     *
     * @param entityList list of entities that needs to be mapped
     * @param outCLass   class of result list element
     * @param <D>        type of objects in result list
     * @param <T>        type of entity in <code>entityList</code>
     * @return list of mapped object with <code><D></code> type.
     */
    public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass) {
        return entityList.stream()
                .map(entity -> map(entity, outCLass))
                .collect(Collectors.toList());
    }
    
    /**
     * Maps {@code source} to {@code destination}.
     *
     * @param source      object to map from
     * @param destination object to map to
     */
    public static <S, D> D map(final S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }

    /**
     * Map from one object to another, allowing for field exclusions.
     *
     * @param <D>            type of result object.
     * @param <T>            type of source object to map from.
     * @param entity         entity that needs to be mapped.
     * @param outClass       class of result object.
     * @param fieldsToIgnore an array of field names to be ignored in the mapping.
     * @return new object of <code>outClass</code> type.
     */
    public static <D, T> D mapWithIgnore(final T entity, Class<D> outClass, String... fieldsToIgnore) {
        D mappedObject = modelMapper.map(entity, outClass);

        for (String fieldName : fieldsToIgnore) {
            try {
                Field field = outClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(mappedObject, null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Handle exceptions as needed (e.g., log or ignore)
            }
        }

        return mappedObject;
    }

    /**
     * <p>Note: outClass object must have default constructor with no arguments</p>
     *
     * @param entityList        list of entities that needs to be mapped
     * @param outCLass          class of result list element
     * @param <D>               type of objects in result list
     * @param <T>               type of entity in <code>entityList</code>
     * @param fieldsToIgnore    an array of field names to be ignored in the mapping.
     * @return list of mapped object with <code><D></code> type.
     */
    public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass, String... fieldsToIgnore) {
        return entityList.stream()
                .map(entity -> mapWithIgnore(entity, outCLass, fieldsToIgnore))
                .collect(Collectors.toList());
    }
    
}
