package com.bombasticoctocat.bomberman;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@interface InjectLog {}

class Slf4jLoggerTypeListener implements TypeListener {
    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        for (Field field: typeLiteral.getRawType().getDeclaredFields()) {
            if (field.getType() == Logger.class && field.isAnnotationPresent(InjectLog.class)) {
                typeEncounter.register(new Slf4jLoggerMembersInjector<>(field));
            }
        }
    }
}

class Slf4jLoggerMembersInjector<T> implements MembersInjector<T> {
    private final Field field;
    private final Logger logger;

    Slf4jLoggerMembersInjector(Field field) {
        this.field = field;
        logger = LoggerFactory.getLogger(this.field.getDeclaringClass());
        this.field.setAccessible(true);
    }

    public void injectMembers(T arg) {
        try {
            field.set(arg, logger);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
