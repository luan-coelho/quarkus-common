package com.luan.common.util.pagination;

import io.quarkus.panache.common.Parameters;

import java.util.StringJoiner;

public class PanacheFilterUtils {

    /**
     * Processa os filtros recebidos em query params e constrói a consulta dinâmica.
     *
     * @param filterQueryParams Filtros recebidos como string no formato "campo:operador:valor,campo:operador:valor"
     * @return Um objeto contendo a query construída e os parâmetros associados.
     */
    public static QueryAndParameters buildQueryFromFilters(String filterQueryParams) {
        if (filterQueryParams == null || filterQueryParams.isEmpty()) {
            return new QueryAndParameters("", new Parameters());
        }

        StringJoiner query = new StringJoiner(" AND ");
        Parameters params = new Parameters(); // Instância vazia para construir os parâmetros
        String[] filters = filterQueryParams.split(",");

        for (int i = 0; i < filters.length; i++) {
            // Limpa espaços desnecessários no filtro
            String filter = filters[i].trim();
            String[] parts = filter.split(":");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Filtro inválido: " + filter);
            }

            String field = parts[0].trim();
            String operator = parts[1].trim();
            String paramName = "param" + i;
            String value = parts[2]; // Preserva os espaços internos no valor

            switch (operator.toLowerCase()) {
                case "eq":
                    query.add(field + " = :" + paramName);
                    params.and(paramName, value);
                    break;
                case "like":
                    query.add(field + " LIKE :" + paramName);
                    params.and(paramName, "%" + value + "%");
                    break;
                case "gt":
                    query.add(field + " > :" + paramName);
                    params.and(paramName, value);
                    break;
                case "lt":
                    query.add(field + " < :" + paramName);
                    params.and(paramName, value);
                    break;
                default:
                    throw new IllegalArgumentException("Operador desconhecido: " + operator);
            }
        }

        return new QueryAndParameters(query.toString(), params);
    }

    /**
     * Classe auxiliar para encapsular a query e os parâmetros associados.
     */
    public record QueryAndParameters(String query, Parameters parameters) {
    }

}