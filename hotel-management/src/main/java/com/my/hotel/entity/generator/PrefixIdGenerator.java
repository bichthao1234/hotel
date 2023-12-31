package com.my.hotel.entity.generator;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;

import java.io.Serializable;
import java.util.Properties;
import java.util.stream.Stream;

public class PrefixIdGenerator implements IdentifierGenerator {

    private String prefix;

    @Override
    public void configure(org.hibernate.type.Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        prefix = params.getProperty("prefix");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {

        String query = String.format("select %s from %s",
                session.getEntityPersister(obj.getClass().getName(), obj)
                        .getIdentifierPropertyName(),
                obj.getClass().getSimpleName());

        @SuppressWarnings("unchecked")
        Stream<String> ids = session.createQuery(query).stream();

        long max = ids.map(o -> o.replace(prefix, ""))
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0L);
        String formattedMax = String.format("%05d", max + 1);

        return prefix + formattedMax;
    }
}