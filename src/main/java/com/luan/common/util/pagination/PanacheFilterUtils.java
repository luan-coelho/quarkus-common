package com.luan.common.util.pagination;

import io.quarkus.panache.common.Parameters;

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

    /**
     * Processa os filtros recebidos e constrói a consulta dinâmica.
     *
     * @param filterQueryParams Filtros recebidos como string no formato "campo:operador:valor,campo:operador:valor".
     * @param entityClass       Classe da entidade principal para validar os campos dinamicamente.
     * @return Um objeto contendo a query construída e os parâmetros associados.
     */
    public static QueryAndParameters buildQueryFromFilters(String filterQueryParams, Class<?> entityClass) {
        validateFilterQueryParams(filterQueryParams); // Valida os filtros no início.

        if (filterQueryParams == null || filterQueryParams.isEmpty()) {
            return new QueryAndParameters("", new Parameters());
        }

        StringJoiner query = new StringJoiner(" AND ");
        Parameters params = new Parameters();
        String[] filters = filterQueryParams.split(",");

        for (int i = 0; i < filters.length; i++) {
            String[] parts = filters[i].split(";"); // Usando ponto e vírgula como delimitador
            String fieldPath = parts[0].trim();
            String operator = parts[1].trim();
            String paramName = "param" + i;
            String value = parts[2].trim();

            // Ajusta o caminho do campo para coleções
            String adjustedFieldPath = adjustFieldPathForCollections(entityClass, fieldPath);

            // Detecta o tipo do campo e converte, se necessário
            Field field = getFieldFromPath(entityClass, fieldPath);
            Object convertedValue = convertValueToFieldType(field, value);

            // Constrói a consulta com base no operador
            switch (operator.toLowerCase()) {
                case "eq":
                    query.add(adjustedFieldPath + " = :" + paramName);
                    params.and(paramName, convertedValue);
                    break;
                case "like":
                    query.add(adjustedFieldPath + " LIKE :" + paramName);
                    params.and(paramName, "%" + convertedValue + "%");
                    break;
                case "gt":
                    query.add(adjustedFieldPath + " > :" + paramName);
                    params.and(paramName, convertedValue);
                    break;
                case "lt":
                    query.add(adjustedFieldPath + " < :" + paramName);
                    params.and(paramName, convertedValue);
                    break;
                default:
                    throw new IllegalArgumentException("Operador desconhecido: " + operator);
            }
        }

        return new QueryAndParameters(query.toString(), params);
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
                adjustedPath.append("ELEMENTS(").append(part).append(")");
            } else {
                adjustedPath.append(part);
            }

            if (i < fieldParts.length - 1) {
                adjustedPath.append("."); // Adiciona o separador para os próximos níveis
            }

            // Navega para o próximo nível
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
            // Converte a string para LocalDateTime
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
        }
        // Adicione outras conversões necessárias, por exemplo, para Integer, Long, etc.
        return value; // Retorna a string diretamente para tipos que não precisam de conversão
    }

    private static Field getFieldFromClass(Class<?> entityClass, String fieldName) {
        Class<?> currentClass = entityClass;

        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName); // Busca o campo na classe atual
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass(); // Continua na classe pai
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
            return; // Nenhum filtro enviado, válido.
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
                field = getFieldIncludingSuperclasses(currentClass, part); // Busca o campo na classe ou superclasses

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
                    "O campo '" + fieldPath + "' não existe na entidade ou em suas superclasses: " + entityClass.getSimpleName());
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
                "Não foi possível determinar o tipo dos elementos da coleção para o campo: " + field.getName());
    }


}