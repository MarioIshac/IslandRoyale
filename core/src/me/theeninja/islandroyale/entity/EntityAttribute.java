package me.theeninja.islandroyale.entity;

import java.lang.annotation.*;

/**
 * Represents that this instance variable of the entity is an attribute, that is, it is level-dependent. When an
 * entity is upgraded, these attributes will change value.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface EntityAttribute {
    // No implementation, simply used as source comment
}
