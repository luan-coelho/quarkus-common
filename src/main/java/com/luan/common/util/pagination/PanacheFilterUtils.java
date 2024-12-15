package com.luan.common.util.pagination;

import io.quarkus.panache.common.Parameters;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * Classe utilitária para processar filtros recebidos e construir consultas dinâmicas.
 */
public class PanacheFilterUtils {

    public static <T> QueryAndParameters buildQueryFromFiltersAndSort(
            String filterQueryParams,
            String sort,
            Class<T> entityClass) {

        QueryAndParameters queryAndParameters = buildQueryFromFilters(filterQueryParams, entityClass);

        String orderByClause = buildOrderByClause(sort);

        String finalQuery = queryAndParameters.query();
        if (!orderByClause.isEmpty()) {
            finalQuery += " order by " + orderByClause;
        }

        return new QueryAndParameters(finalQuery.trim(), queryAndParameters.parameters());
    }

    /**
     * Processa os filtros recebidos e constrói a consulta dinâmica.
     *
     * @param filterQueryParams Filtros recebidos como string no formato "campo:operador:valor,campo:operador:valor".
     * @param entityClass       Classe da entidade principal para validar os campos dinamicamente.
     * @return Um objeto contendo a query construída e os parâmetros associados.
     */
    public static QueryAndParameters buildQueryFromFilters(String filterQueryParams, Class<?> entityClass) {
        validateFilterQueryParams(filterQueryParams);

        if (filterQueryParams == null || filterQueryParams.isEmpty()) {
            return new QueryAndParameters("", new Parameters());
        }

        StringJoiner query = new StringJoiner(" and ");
        Parameters params = new Parameters();
        String[] filters = filterQueryParams.split(",");

        for (int i = 0; i < filters.length; i++) {
            String[] parts = filters[i].split(";");
            String fieldPath = parts[0].trim();
            Operator operator = Operator.fromValue(parts[1].trim());
            String paramName = "param" + i;
            String value = parts[2].trim();

            String adjustedFieldPath = adjustFieldPathForCollections(entityClass, fieldPath);

            Field field = getFieldFromPath(entityClass, fieldPath);
            Object convertedValue = convertValueToFieldType(field, value);

            switch (operator) {
                case Operator.EQUALS:
                    query.add(adjustedFieldPath + " = :" + paramName);
                    params.and(paramName, convertedValue);
                    break;
                case Operator.LIKE:
                    query.add(adjustedFieldPath + " like :" + paramName);
                    params.and(paramName, "%" + convertedValue + "%");
                    break;
                case Operator.ILIKE:
                    query.add("lower(" + adjustedFieldPath + ") like lower(:" + paramName + ")");
                    params.and(paramName, "%" + convertedValue + "%");
                    break;
                case Operator.GREATER_THAN:
                    query.add(adjustedFieldPath + " > :" + paramName);
                    params.and(paramName, convertedValue);
                    break;
                case Operator.LESS_THAN:
                    query.add(adjustedFieldPath + " < :" + paramName);
                    params.and(paramName, convertedValue);
                    break;
                default:
                    throw new IllegalArgumentException("Operador desconhecido: " + operator);
            }
        }

        return new QueryAndParameters(query.toString(), params);
    }

    public static String buildOrderByClause(String sort) {
        String orderBy = "id asc";
        if (sort == null || sort.trim().isEmpty()) {
            return orderBy;
        }

        String[] sortFields = sort.split(",");
        StringJoiner orderByClause = new StringJoiner(", ");

        for (String sortField : sortFields) {
            String[] parts = sortField.split(":");
            if (parts.length != 2) {
                String msg = "Parâmetro de ordenação inválido: " + sortField + ". Formato esperado: 'campo:asc|desc'.";
                throw new IllegalArgumentException(msg);
            }

            String fieldName = parts[0].trim();
            String direction = parts[1].trim().toLowerCase();

            if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
                return orderBy;
            }

            orderByClause.add(fieldName + " " + direction.toLowerCase());
        }

        return orderByClause.toString();
    }

    /**
     * Ajusta o caminho do campo para incluir `ELEMENTS()` se necessário.
     */
    private static String adjustFieldPathForCollections(Class<?> entityClass, String fieldPath) {
        String[] fieldParts = fieldPath.split("\\.");
        Class<?> currentClass = entityClass;
        StringBuilder adjustedPath = new StringBuilder();

        for (int i = 0; i < fieldParts.length; i++) {
            String part = fieldParts[i];
            Field field = getFieldFromClass(currentClass, part);

            // Adiciona `ELEMENTS()` se o campo for uma coleção
            if (isCollectionField(field)) {
                adjustedPath.append("elements(").append(part).append(")");
            } else {
                adjustedPath.append(part);
            }

            if (i < fieldParts.length - 1) {
                adjustedPath.append(".");
            }

            currentClass = field.getType();
            if (isCollectionField(field)) {
                currentClass = getCollectionElementType(field);
            }
        }

        return adjustedPath.toString();
    }

    /**
     * Converte o valor para o tipo esperado pelo campo.
     */
    private static Object convertValueToFieldType(Field field, String value) {
        Class<?> fieldType = field.getType();

        if (fieldType.equals(LocalDateTime.class)) {
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
        }

        if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
            return Boolean.parseBoolean(value);
        }

        return value;
    }

    private static Field getFieldFromClass(Class<?> entityClass, String fieldName) {
        Class<?> currentClass = entityClass;

        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }

        throw new IllegalArgumentException("O campo '" + fieldName + "' não existe na entidade ou em suas superclasses: " + entityClass.getSimpleName());
    }

    /**
     * Valida se um campo é uma coleção.
     */
    private static boolean isCollectionField(Field field) {
        return field != null && java.util.Collection.class.isAssignableFrom(field.getType());
    }

    /**
     * Valida se a string de filtros está no formato esperado.
     *
     * @param filterQueryParams Filtros recebidos como string.
     * @throws IllegalArgumentException Se os filtros não forem válidos.
     */
    public static void validateFilterQueryParams(String filterQueryParams) {
        if (filterQueryParams == null || filterQueryParams.trim().isEmpty()) {
            return;
        }

        String[] filters = filterQueryParams.split(",");

        for (String filter : filters) {
            String[] parts = filter.split(";");
            if (parts.length != 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
                throw new IllegalArgumentException("Filtro inválido: " + filter + ". Formato esperado: 'campo;operador;valor'.");
            }
        }
    }

    private static Field getFieldFromPath(Class<?> entityClass, String fieldPath) {
        try {
            String[] fieldParts = fieldPath.split("\\.");
            Class<?> currentClass = entityClass;
            Field field = null;

            for (String part : fieldParts) {
                field = getFieldIncludingSuperclasses(currentClass, part);

                // Se o campo for uma coleção, verifica o tipo genérico
                if (Collection.class.isAssignableFrom(field.getType())) {
                    currentClass = getCollectionElementType(field);
                } else {
                    currentClass = field.getType();
                }
            }
            return field;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(
                    "O campo '" + fieldPath + "' não existe na entidade ou em suas superclasses: " + entityClass.getSimpleName()
            );
        }
    }

    /**
     * Busca um campo em uma classe, incluindo suas superclasses.
     */
    private static Field getFieldIncludingSuperclasses(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> currentClass = clazz;

        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass(); // Continua na hierarquia
            }
        }

        throw new NoSuchFieldException("Campo '" + fieldName + "' não encontrado na classe ou em suas superclasses.");
    }

    /**
     * Obtém o tipo dos elementos de uma coleção (ex.: `List<SubItem>` retorna `SubItem`).
     */
    private static Class<?> getCollectionElementType(Field field) {
        if (field.getGenericType() instanceof ParameterizedType parameterizedType) {
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        }
        throw new IllegalArgumentException(
                "Não foi possível determinar o tipo dos elementos da coleção para o campo: " + field.getName()
        );
    }

    @Getter
    @RequiredArgsConstructor
    public enum Operator {

        EQUALS("eq"),
        LIKE("like"),
        ILIKE("ilike"),
        GREATER_THAN("gt"),
        LESS_THAN("lt");

        private final String value;

        public static Operator fromValue(String value) {
            for (Operator operator : Operator.values()) {
                if (operator.getValue().equals(value)) {
                    return operator;
                }
            }
            throw new IllegalArgumentException("Invalid operator value: " + value);
        }

    }

}