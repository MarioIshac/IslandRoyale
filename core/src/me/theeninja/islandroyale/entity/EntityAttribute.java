package me.theeninja.islandroyale.entity;

import java.lang.annotation.*;
import java.util.function.Function;

/**
 * Represents that this instance variable of the entity is an attribute, that is, it is level-dependent. When an
 * entity is upgraded, these attributes will change value.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EntityAttribute {
    String value();
}