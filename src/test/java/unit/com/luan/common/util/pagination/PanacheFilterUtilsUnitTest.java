package unit.com.luan.common.util.pagination;

import com.luan.common.util.pagination.PanacheFilterUtils;
import io.quarkus.panache.common.Parameters;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PanacheFilterUtilsUnitTest {

    @Test
    void testBuildQueryFromFiltersWithValidInput() {
        String filters = "address.name:eq:JohnDoe,pessoa.casa.endereco:like:RuaPrincipal";

        PanacheFilterUtils.QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        String expectedQuery = "address.name = :param0 AND pessoa.casa.endereco LIKE :param1";
        assertEquals(expectedQuery, result.query(), "A query gerada está incorreta!");

        Parameters expectedParams = Parameters.with("param0", "JohnDoe").and("param1", "%RuaPrincipal%");
        assertEquals(expectedParams.map(), result.parameters().map(), "Os parâmetros gerados estão incorretos!");
    }

    @Test
    void testBuildQueryFromFiltersWithEmptyInput() {
        String filters = "";

        PanacheFilterUtils.QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        assertEquals("", result.query(), "A query deve estar vazia para filtros vazios!");
        assertTrue(result.parameters().map().isEmpty(), "Os parâmetros devem estar vazios para filtros vazios!");
    }

    @Test
    void testBuildQueryFromFiltersWithNullInput() {
        String filters = null;

        PanacheFilterUtils.QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        assertEquals("", result.query(), "A query deve estar vazia para filtros nulos!");
        assertTrue(result.parameters().map().isEmpty(), "Os parâmetros devem estar vazios para filtros nulos!");
    }

    @Test
    void testBuildQueryFromFiltersWithInvalidInput() {
        String filters = "invalidFilterFormat";

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> PanacheFilterUtils.buildQueryFromFilters(filters),
                "Deveria lançar IllegalArgumentException para formato inválido!"
        );

        assertEquals("Filtro inválido: invalidFilterFormat", exception.getMessage(), "Mensagem de erro inesperada!");
    }

    @Test
    void testBuildQueryFromFiltersWithMultipleOperators() {
        String filters = "price:gt:1000,stock:lt:50";

        PanacheFilterUtils.QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        String expectedQuery = "price > :param0 AND stock < :param1";
        assertEquals(expectedQuery, result.query(), "A query gerada está incorreta!");

        Parameters expectedParams = Parameters.with("param0", "1000").and("param1", "50");
        assertEquals(expectedParams.map(), result.parameters().map(), "Os parâmetros gerados estão incorretos!");
    }

    @Test
    void testBuildQueryFromFiltersWithSpecialCharacters() {
        String filters = "description:like:%40special,category:eq:Electronics";

        PanacheFilterUtils.QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        String expectedQuery = "description LIKE :param0 AND category = :param1";
        assertEquals(expectedQuery, result.query(), "A query gerada está incorreta!");

        Parameters expectedParams = Parameters.with("param0", "%%40special%").and("param1", "Electronics");
        assertEquals(expectedParams.map(), result.parameters().map(), "Os parâmetros gerados estão incorretos!");
    }

    @Test
    void testBuildQueryFromFiltersWithEdgeCaseOperators() {
        String filters = "count:eq:0,value:lt:-1";

        PanacheFilterUtils.QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        String expectedQuery = "count = :param0 AND value < :param1";
        assertEquals(expectedQuery, result.query(), "A query gerada está incorreta!");

        Parameters expectedParams = Parameters.with("param0", "0").and("param1", "-1");
        assertEquals(expectedParams.map(), result.parameters().map(), "Os parâmetros gerados estão incorretos!");
    }

    @Test
    void testBuildQueryFromFiltersWithMixedSpaces() {
        // Cenário com filtros contendo espaços extras
        String filters = "field1:like:   value1 , field2:eq:value2";

        PanacheFilterUtils.QueryAndParameters result = PanacheFilterUtils.buildQueryFromFilters(filters);

        // Verificação da query gerada
        String expectedQuery = "field1 LIKE :param0 AND field2 = :param1";
        assertEquals(expectedQuery, result.query(), "A query gerada está incorreta!");

        // Verificação dos parâmetros
        Parameters expectedParams = Parameters.with("param0", "%   value1%").and("param1", "value2");
        assertEquals(expectedParams.map(), result.parameters().map(), "Os parâmetros gerados estão incorretos!");
    }

    @Test
    void testBuildQueryFromFiltersWithMalformedFilter() {
        String filters = "field1:gt:";

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> PanacheFilterUtils.buildQueryFromFilters(filters),
                "Deveria lançar IllegalArgumentException para filtros malformados!"
        );

        assertEquals("Filtro inválido: field1:gt:", exception.getMessage(), "Mensagem de erro inesperada!");
    }

    @Test
    void testBuildQueryFromFiltersWithMultipleValidAndInvalidFilters() {
        String filters = "field1:eq:value1,invalidFilter,field2:like:value2";

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> PanacheFilterUtils.buildQueryFromFilters(filters),
                "Deveria lançar IllegalArgumentException para filtros com entradas inválidas!"
        );

        assertEquals("Filtro inválido: invalidFilter", exception.getMessage(), "Mensagem de erro inesperada!");
    }

}