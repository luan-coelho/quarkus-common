package com.luan.common.util.audit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AuditObjectComparator {

    public static <T> List<FieldChange> compareObjects(T oldObject, T newObject) throws IllegalAccessException {
        List<FieldChange> changes = new ArrayList<>();

        if (oldObject == null || newObject == null) {
            throw new IllegalArgumentException("Objects to compare cannot be null");
        }

        Class<?> clazz = oldObject.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            Object oldValue = field.get(oldObject);
            Object newValue = field.get(newObject);

            if ((oldValue == null && newValue != null) ||
                (oldValue != null && !oldValue.equals(newValue))) {
                changes.add(new FieldChange(field.getName(), oldValue, newValue));
            }
        }

        return changes;
    }

}

