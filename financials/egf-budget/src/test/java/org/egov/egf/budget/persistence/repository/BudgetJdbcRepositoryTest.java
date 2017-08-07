package org.egov.egf.budget.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.model.EstimationType;
import org.egov.egf.budget.persistence.entity.BudgetEntity;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BudgetJdbcRepositoryTest {

    private BudgetJdbcRepository budgetJdbcRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() throws Exception {
        budgetJdbcRepository = new BudgetJdbcRepository(namedParameterJdbcTemplate);
    }

    @Test
    @Sql(scripts = { "/sql/budget/clearBudget.sql" })
    public void test_create() {

        BudgetEntity budget = BudgetEntity.builder().name("name").parentId("1").active(true).financialYearId("1")
                .estimationType("BE").primaryBudget(true).build();
        budget.setTenantId("default");
        BudgetEntity actualResult = budgetJdbcRepository.create(budget);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_budget",
                new BudgetResultExtractor());
        Map<String, Object> row = result.get(0);
        assertThat(row.get("id")).isEqualTo(actualResult.getId());
        assertThat(row.get("name")).isEqualTo(actualResult.getName());
        assertThat(row.get("parentId")).isEqualTo(actualResult.getParentId());
        assertThat(row.get("financialYearId")).isEqualTo(actualResult.getFinancialYearId());
        assertThat(row.get("estimationType")).isEqualTo(actualResult.getEstimationType());
        assertThat(row.get("primaryBudget")).isEqualTo(actualResult.getPrimaryBudget());

    }

    @Test(expected = DataIntegrityViolationException.class)
    @Sql(scripts = { "/sql/budget/clearBudget.sql" })
    public void test_create_with_tenantId_null() {

        BudgetEntity budget = BudgetEntity.builder().name("name").parentId("1").active(true).financialYearId("1")
                .estimationType("BE").primaryBudget(true).build();
        budgetJdbcRepository.create(budget);

    }

    @Test
    @Sql(scripts = { "/sql/budget/clearBudget.sql", "/sql/budget/insertBudgetData.sql" })
    public void test_update() {

        BudgetEntity budget = BudgetEntity.builder().name("name").parentId("1").active(true).financialYearId("1")
                .estimationType("BE").primaryBudget(true).id("1").build();
        budget.setTenantId("default");
        BudgetEntity actualResult = budgetJdbcRepository.update(budget);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_budget",
                new BudgetResultExtractor());
        Map<String, Object> row = result.get(0);
        assertThat(row.get("id")).isEqualTo(actualResult.getId());
        assertThat(row.get("name")).isEqualTo(actualResult.getName());

    }

    @Test
    @Sql(scripts = { "/sql/budget/clearBudget.sql", "/sql/budget/insertBudgetData.sql" })
    public void test_search() {

        Pagination<Budget> page = (Pagination<Budget>) budgetJdbcRepository.search(getBudgetSearch());
        assertThat(page.getPagedData().get(0).getName()).isEqualTo("name");
        assertThat(page.getPagedData().get(0).getActive()).isEqualTo(true);

    }

    @Test
    @Sql(scripts = { "/sql/budget/clearBudget.sql", "/sql/budget/insertBudgetData.sql" })
    public void test_invalid_search() {

        Pagination<Budget> page = (Pagination<Budget>) budgetJdbcRepository.search(getBudgetSearch1());
        assertThat(page.getPagedData().size()).isEqualTo(0);

    }

    @Test
    @Sql(scripts = { "/sql/budget/clearBudget.sql", "/sql/budget/insertBudgetData.sql" })
    public void test_find_by_id() {

        BudgetEntity budgetEntity = BudgetEntity.builder().id("1").build();
        budgetEntity.setTenantId("default");
        BudgetEntity result = budgetJdbcRepository.findById(budgetEntity);
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("name");

    }

    @Test
    @Sql(scripts = { "/sql/budget/clearBudget.sql", "/sql/budget/insertBudgetData.sql" })
    public void test_find_by_invalid_id_should_return_null() {

        BudgetEntity budgetEntity = BudgetEntity.builder().id("5").build();
        budgetEntity.setTenantId("default");
        BudgetEntity result = budgetJdbcRepository.findById(budgetEntity);
        assertNull(result);

    }

    @Test(expected = InvalidDataException.class)
    @Sql(scripts = { "/sql/budget/clearBudget.sql", "/sql/budget/insertBudgetData.sql" })
    public void test_search_invalid_sort_option() {

        BudgetSearch search = getBudgetSearch();
        search.setSortBy("desc");
        budgetJdbcRepository.search(search);

    }

    @Test
    @Sql(scripts = { "/sql/budget/clearBudget.sql", "/sql/budget/insertBudgetData.sql" })
    public void test_search_without_pagesize_offset_sortby() {

        BudgetSearch search = getBudgetSearch();
        search.setSortBy(null);
        search.setPageSize(null);
        search.setOffset(null);
        Pagination<Budget> page = (Pagination<Budget>) budgetJdbcRepository.search(getBudgetSearch());
        assertThat(page.getPagedData().get(0).getName()).isEqualTo("name");
        assertThat(page.getPagedData().get(0).getActive()).isEqualTo(true);

    }

    class BudgetResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
        @Override
        public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<Map<String, Object>> rows = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<String, Object>() {
                    {
                        put("id", resultSet.getString("id"));
                        put("name", resultSet.getString("name"));
                        put("parentId", resultSet.getString("parentId"));
                        put("active", resultSet.getBoolean("active"));
                        put("financialYearId", resultSet.getString("financialYearId"));
                        put("estimationType", resultSet.getString("estimationType"));
                        put("primaryBudget", resultSet.getBoolean("primaryBudget"));
                        put("createdBy", resultSet.getString("createdBy"));
                        put("createdDate", resultSet.getString("createdDate"));
                        put("lastModifiedBy", resultSet.getString("lastModifiedBy"));
                        put("lastModifiedDate", resultSet.getString("lastModifiedDate"));

                    }
                };

                rows.add(row);
            }
            return rows;
        }
    }

    private BudgetSearch getBudgetSearch1() {
        BudgetSearch budgetSearch = new BudgetSearch();
        budgetSearch.setId("id");
        budgetSearch.setName("name");
        budgetSearch.setFinancialYear(FinancialYearContract.builder().id("1").build());
        budgetSearch.setParent(Budget.builder().id("1").build());
        budgetSearch.setReferenceBudget(Budget.builder().id("1").build());
        budgetSearch.setStatus(FinancialStatusContract.builder().id("1").build());
        budgetSearch.setEstimationType(EstimationType.BE);
        budgetSearch.setDescription("description");
        budgetSearch.setActive(true);
        budgetSearch.setDocumentNumber("documentNumber");
        budgetSearch.setMaterializedPath("materializedPath");
        budgetSearch.setPrimaryBudget(true);
        budgetSearch.setTenantId("tenantId");
        budgetSearch.setPageSize(500);
        budgetSearch.setOffset(0);
        budgetSearch.setSortBy("name desc");
        return budgetSearch;
    }

    private BudgetSearch getBudgetSearch() {
        BudgetSearch budgetSearch = new BudgetSearch();
        budgetSearch.setName("name");
        budgetSearch.setPageSize(500);
        budgetSearch.setOffset(0);
        budgetSearch.setSortBy("name desc");
        return budgetSearch;
    }
}