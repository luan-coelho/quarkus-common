package unit.com.luan.common.util.pagination;

import com.luan.common.model.module.MenuItem;
import com.luan.common.util.pagination.PanacheFilterUtils;
import com.luan.common.util.pagination.QueryAndParameters;
import io.quarkus.panache.common.Parameters;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
@QuarkusTest
class PanacheFilterUtilsUnitTest {

    @Test
    void testBuildQueryFromFiltersWithValidInput() {
        String filters = "address.name:eq:JohnDoe,pessoa.casa.endereco:like:RuaPrincipal";

        QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        String expectedQuery = "address.name = :param0 AND pessoa.casa.endereco LIKE :param1";
        assertEquals(expectedQuery, result.query(), "A query gerada está incorreta!");

        Parameters expectedParams = Parameters.with("param0", "JohnDoe").and("param1", "%RuaPrincipal%");
        assertEquals(expectedParams.map(), result.parameters().map(), "Os parâmetros gerados estão incorretos!");
    }

    @Test
    void testBuildQueryFromFiltersWithEmptyInput() {
        String filters = "";

        QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        assertEquals("", result.query(), "A query deve estar vazia para filtros vazios!");
        assertTrue(result.parameters().map().isEmpty(), "Os parâmetros devem estar vazios para filtros vazios!");
    }

    @Test
    void testBuildQueryFromFiltersWithNullInput() {
        String filters = null;

        QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        assertEquals("", result.query(), "A query deve estar vazia para filtros nulos!");
        assertTrue(result.parameters().map().isEmpty(), "Os parâmetros devem estar vazios para filtros nulos!");
    }

    @Test
    void testBuildQueryFromFiltersWithMultipleOperators() {
        String filters = "price:gt:1000,stock:lt:50";

        QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        String expectedQuery = "price > :param0 AND stock < :param1";
        assertEquals(expectedQuery, result.query(), "A query gerada está incorreta!");

        Parameters expectedParams = Parameters.with("param0", "1000").and("param1", "50");
        assertEquals(expectedParams.map(), result.parameters().map(), "Os parâmetros gerados estão incorretos!");
    }

    @Test
    void testBuildQueryFromFiltersWithSpecialCharacters() {
        String filters = "description:like:%40special,category:eq:Electronics";

        QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        String expectedQuery = "description LIKE :param0 AND category = :param1";
        assertEquals(expectedQuery, result.query(), "A query gerada está incorreta!");

        Parameters expectedParams = Parameters.with("param0", "%%40special%").and("param1", "Electronics");
        assertEquals(expectedParams.map(), result.parameters().map(), "Os parâmetros gerados estão incorretos!");
    }

    @Test
    void testBuildQueryFromFiltersWithEdgeCaseOperators() {
        String filters = "count:eq:0,value:lt:-1";

        QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        String expectedQuery = "count = :param0 AND value < :param1";
        assertEquals(expectedQuery, result.query(), "A query gerada está incorreta!");

        Parameters expectedParams = Parameters.with("param0", "0").and("param1", "-1");
        assertEquals(expectedParams.map(), result.parameters().map(), "Os parâmetros gerados estão incorretos!");
    }

    @Test
    void testBuildQueryFromFiltersWithMixedSpaces() {
        // Cenário com filtros contendo espaços extras
        String filters = "field1:like:   value1 , field2:eq:value2";

        QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        // Verificação da query gerada
        String expectedQuery = "field1 LIKE :param0 AND field2 = :param1";
        assertEquals(expectedQuery, result.query(), "A query gerada está incorreta!");

        // Verificação dos parâmetros
        Parameters expectedParams = Parameters.with("param0", "%   value1%").and("param1", "value2");
        assertEquals(expectedParams.map(), result.parameters().map(), "Os parâmetros gerados estão incorretos!");
    }

    @Test
    void testValidateFilterQueryParams_WithInvalidFilters_ShouldThrowException() {
        // Cenário: Filtros inválidos
        String invalidFilters1 = "address.name:eq"; // Faltando valor
        String invalidFilters2 = "address.name"; // Faltando operador e valor
        String invalidFilters3 = "address.name:like:"; // Faltando valor

        // Verifica se uma exceção é lançada para cada caso
        Exception exception1 = assertThrows(IllegalArgumentException.class, () ->
                PanacheFilterUtils.validateFilterQueryParams(invalidFilters1));
        assertEquals("Filtro inválido: address.name:eq. Formato esperado: 'campo:operador:valor'.", exception1.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () ->
                PanacheFilterUtils.validateFilterQueryParams(invalidFilters2));
        assertEquals("Filtro inválido: address.name. Formato esperado: 'campo:operador:valor'.", exception2.getMessage());

        Exception exception3 = assertThrows(IllegalArgumentException.class, () ->
                PanacheFilterUtils.validateFilterQueryParams(invalidFilters3));
        assertEquals("Filtro inválido: address.name:like:. Formato esperado: 'campo:operador:valor'.", exception3.getMessage());
    }

}*/
