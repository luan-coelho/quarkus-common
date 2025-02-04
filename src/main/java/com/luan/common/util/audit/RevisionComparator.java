package com.luan.common.util.audit;

import com.luan.common.model.user.AuditRevisionEntity;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;

import java.lang.reflect.Field;
import java.util.*;

public final class RevisionComparator<T> {

    private AuditReader auditReader;
    private T previousEntity;
    private T currentEntity;
    private AuditRevisionEntity currentRevisionEntity;
    private int currentRevisionIndex;

    public RevisionComparison compare() {
        List<FieldChange> fieldChanges = new ArrayList<>();

        if (previousEntity != null && currentEntity != null) {
            compareEntities(previousEntity, currentEntity, fieldChanges, "");
        } else if (currentEntity != null) {
            handleInsert(currentEntity, fieldChanges, "");
        } else if (previousEntity != null) {
            handleDelete(previousEntity, fieldChanges, "");
        }

        return new RevisionComparison(
                currentRevisionEntity != null ? currentRevisionEntity.getUsername() : null,
                currentRevisionEntity != null ? currentRevisionEntity.getRevisionDate() : null,
                fieldChanges,
                currentRevisionEntity != null ? RevisionType.MOD.name() : null
        );
    }

    private void compareEntities(Object oldEntity, Object newEntity, List<FieldChange> fieldChanges, String parentPath) {
        Class<?> clazz = oldEntity.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object oldValue = field.get(oldEntity);
                Object newValue = field.get(newEntity);
                compareField(field, oldValue, newValue, fieldChanges, parentPath);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao acessar campo: " + field.getName(), e);
            }
        }
    }

    private void compareField(Field field, Object oldValue, Object newValue, List<FieldChange> fieldChanges, String parentPath) {
        String fieldName = parentPath.isEmpty() ? field.getName() : parentPath + "." + field.getName();

        if (!Objects.deepEquals(oldValue, newValue)) {
            if (isPrimitiveOrWrapperOrString(field.getType())) {
                fieldChanges.add(new FieldChange(
                        fieldName,
                        field.getName(),
                        oldValue,
                        newValue,
                        fieldChanges.size(),
                        null
                ));
            } else {
                List<FieldChange> nestedChanges = new ArrayList<>();
                if (oldValue != null && newValue != null) {
                    compareEntities(oldValue, newValue, nestedChanges, fieldName);
                } else {
                    nestedChanges.add(new FieldChange(
                            fieldName,
                            field.getName(),
                            oldValue,
                            newValue,
                            0,
                            null
                    ));
                }
                if (!nestedChanges.isEmpty()) {
                    fieldChanges.add(new FieldChange(
                            fieldName,
                            field.getName(),
                            null,
                            null,
                            fieldChanges.size(),
                            nestedChanges
                    ));
                }
            }
        }
    }

    private void handleInsert(Object entity, List<FieldChange> fieldChanges, String parentPath) {
        Class<?> clazz = entity.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                String fieldName = parentPath.isEmpty() ? field.getName() : parentPath + "." + field.getName();
                addInsertChange(field, value, fieldChanges, fieldName);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao acessar campo: " + field.getName(), e);
            }
        }
    }

    private void addInsertChange(Field field, Object value, List<FieldChange> fieldChanges, String fieldName) {
        if (isPrimitiveOrWrapperOrString(field.getType())) {
            fieldChanges.add(new FieldChange(
                    fieldName,
                    field.getName(),
                    null,
                    value,
                    fieldChanges.size(),
                    null
            ));
        } else if (value != null) {
            List<FieldChange> nestedChanges = new ArrayList<>();
            handleInsert(value, nestedChanges, fieldName);
            fieldChanges.add(new FieldChange(
                    fieldName,
                    field.getName(),
                    null,
                    null,
                    fieldChanges.size(),
                    nestedChanges
            ));
        }
    }

    private void handleDelete(Object entity, List<FieldChange> fieldChanges, String parentPath) {
        Class<?> clazz = entity.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                String fieldName = parentPath.isEmpty() ? field.getName() : parentPath + "." + field.getName();
                addDeleteChange(field, value, fieldChanges, fieldName);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao acessar campo: " + field.getName(), e);
            }
        }
    }

    private void addDeleteChange(Field field, Object value, List<FieldChange> fieldChanges, String fieldName) {
        if (isPrimitiveOrWrapperOrString(field.getType())) {
            fieldChanges.add(new FieldChange(
                    fieldName,
                    field.getName(),
                    value,
                    null,
                    fieldChanges.size(),
                    null
            ));
        } else if (value != null) {
            List<FieldChange> nestedChanges = new ArrayList<>();
            handleDelete(value, nestedChanges, fieldName);
            fieldChanges.add(new FieldChange(
                    fieldName,
                    field.getName(),
                    null,
                    null,
                    fieldChanges.size(),
                    nestedChanges
            ));
        }
    }

    private boolean isPrimitiveOrWrapperOrString(Class<?> type) {
        return type.isPrimitive() ||
               type.equals(String.class) ||
               type.equals(Integer.class) ||
               type.equals(Long.class) ||
               type.equals(Double.class) ||
               type.equals(Boolean.class) ||
               type.equals(Byte.class) ||
               type.equals(Character.class) ||
               type.equals(Short.class) ||
               type.equals(Float.class);
    }

}