package com.luan.common.util.pagination;

import io.quarkus.panache.common.Parameters;

/**
 * Classe auxiliar para encapsular a query e os parâmetros associados.
 */
public record QueryAndParameters(String query, Parameters parameters) {

}