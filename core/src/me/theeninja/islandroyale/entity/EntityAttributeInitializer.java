package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.utils.Array;

import java.lang.reflect.Field;

final class EntityAttributeInitializer {
    static void initializeAttributes(final Entity<?, ?> entity) {
        final Class<? extends Entity> entityClass = entity.getClass();
        final Class<? extends EntityType> entityTypeClass = entity.getEntityType().getClass();

        final Array<Field> entityHierarchyFields = getEntityHiearchyFields(entityClass);
        final Array<Field> entityTypeHierarchyFields = getEntityTypeHiearchyFields(entityTypeClass);

        // For every field of every access level in the hiearchy of entity.getClass() until Entity.class
        for (final Field hierarchyField : entityHierarchyFields) {
            final EntityAttribute entityAttribute = hierarchyField.getAnnotation(EntityAttribute.class);

            if (entityAttribute == null) {
                continue;
            }

            final String baseValueFieldName = entityAttribute.value();
            Field baseValueField = null;

            // We must search the hiearchy again, rather than just fetching the declared field by name from entity.getEntityType().getClass()
            for (final Field potentialBaseValueField : entityTypeHierarchyFields) {
                System.out.println("potentialBaseValueField.getName() = " + potentialBaseValueField.getName());

                if (potentialBaseValueField.getName().equals(baseValueFieldName)) {
                    baseValueField = potentialBaseValueField;

                    break;
                }
            }

            // Indicates that entity attribute value is not the name of an actual base field
            if (baseValueField == null) {
                throw new RuntimeException("Field " + baseValueFieldName + " between " + entityTypeClass.getSimpleName() + " and " + EntityType.class.getSimpleName());
            }

            // Attempt to set starting value of entity's enttiy attribute to its base value
            try {
                Object baseValue = baseValueField.get(entity.getEntityType());
                hierarchyField.set(entity, baseValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    private static final Class<Entity> ENTITY_SUPER_CLASS = Entity.class;
    private static final Class<EntityType> ENTITY_TYPE_SUPER_CLASS = EntityType.class;

    /**
     * @param currentEntityClass The class of a subclass of Entity
     * @return The collected declared fields of classes between (on the hiearchy, inclusively) {@code currentEntityClass} and {@link Entity}.class
     */
    private static Array<Field> getEntityHiearchyFields(Class<?> currentEntityClass) {
        return getHiearchyFields(currentEntityClass, ENTITY_SUPER_CLASS);
    }

    private static Array<Field> getEntityTypeHiearchyFields(Class<?> currentEntityTypeClass) {
        return getHiearchyFields(currentEntityTypeClass, ENTITY_TYPE_SUPER_CLASS);
    }

    private static Array<Field> getHiearchyFields(Class<?> currentClass, Class<?> superclass) {
        final Array<Field> entityAttributeFields = new Array<>();

        do {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            entityAttributeFields.addAll(declaredFields);

            currentClass = currentClass.getSuperclass();
        } while (currentClass != superclass);

        for (Field field : entityAttributeFields) {
            field.setAccessible(true);
        }

        return entityAttributeFields;
    }
}
