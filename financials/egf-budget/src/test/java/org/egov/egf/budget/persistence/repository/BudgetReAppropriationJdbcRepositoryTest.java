package org.egov.egf.budget.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.persistence.entity.BudgetReAppropriationEntity;
import org.egov.egf.master.web.contract.FinancialStatusContract;
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
public class BudgetReAppropriationJdbcRepositoryTest {

    private BudgetReAppropriationJdbcRepository budgetReAppropriationJdbcRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() throws Exception {
        budgetReAppropriationJdbcRepository = new BudgetReAppropriationJdbcRepository(namedParameterJdbcTemplate);
    }

    @Test
    @Sql(scripts = { "/sql/budgetreappropriation/clearBudgetReAppropriation.sql" })
    public void test_create() {

        BudgetReAppropriationEntity budgetReAppropriation = BudgetReAppropriationEntity.builder().budgetDetailId("1")
                .build();
        budgetReAppropriation.setTenantId("default");
        BudgetReAppropriationEntity actualResult = budgetReAppropriationJdbcRepository.create(budgetReAppropriation);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_budgetReAppropriation",
                new BudgetReAppropriationResultExtractor());
        Map<String, Object> row = result.get(0);
        assertThat(row.get("id")).isEqualTo(actualResult.getId());
        assertThat(row.get("budgetDetailId")).isEqualTo(actualResult.getBudgetDetailId());

    }

    @Test(expected = DataIntegrityViolationException.class)
    @Sql(scripts = { "/sql/budgetreappropriation/clearBudgetReAppropriation.sql" })
    public void test_create_with_tenantId_null() {

        BudgetReAppropriationEntity budgetReAppropriation = BudgetReAppropriationEntity.builder().budgetDetailId("1")
                .build();
        budgetReAppropriationJdbcRepository.create(budgetReAppropriation);

    }

    @Test
    @Sql(scripts = { "/sql/budgetreappropriation/clearBudgetReAppropriation.sql",
            "/sql/budgetreappropriation/insertBudgetReAppropriationData.sql" })
    public void test_update() {

        BudgetReAppropriationEntity budgetReAppropriation = BudgetReAppropriationEntity.builder().budgetDetailId("1")
                .id("1").build();
        budgetReAppropriation.setTenantId("default");
        BudgetReAppropriationEntity actualResult = budgetReAppropriationJdbcRepository.update(budgetReAppropriation);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_budgetReAppropriation",
                new BudgetReAppropriationResultExtractor());
        Map<String, Object> row = result.get(0);
        assertThat(row.get("id")).isEqualTo(actualResult.getId());
        assertThat(row.get("budgetDetailId")).isEqualTo(actualResult.getBudgetDetailId());

    }

    @Test
    @Sql(scripts = { "/sql/budgetreappropriation/clearBudgetReAppropriation.sql",
            "/sql/budgetreappropriation/insertBudgetReAppropriationData.sql" })
    public void test_search() {

        Pagination<BudgetReAppropriation> page = (Pagination<BudgetReAppropriation>) budgetReAppropriationJdbcRepository
                .search(getBudgetReAppropriationSearch());
        assertThat(page.getPagedData().get(0).getId()).isEqualTo("1");
    }

    @Test
    @Sql(scripts = { "/sql/budgetreappropriation/clearBudgetReAppropriation.sql",
            "/sql/budgetreappropriation/insertBudgetReAppropriationData.sql" })
    public void test_invalid_search() {

        Pagination<BudgetReAppropriation> page = (Pagination<BudgetReAppropriation>) budgetReAppropriationJdbcRepository
                .search(getBudgetReAppropriationSearch1());
        assertThat(page.getPagedData().size()).isEqualTo(0);

    }

    @Test
    @Sql(scripts = { "/sql/budgetreappropriation/clearBudgetReAppropriation.sql",
            "/sql/budgetreappropriation/insertBudgetReAppropriationData.sql" })
    public void test_find_by_id() {

        BudgetReAppropriationEntity budgetReAppropriationEntity = BudgetReAppropriationEntity.builder().id("1").build();
        budgetReAppropriationEntity.setTenantId("default");
        BudgetReAppropriationEntity result = budgetReAppropriationJdbcRepository.findById(budgetReAppropriationEntity);
        assertThat(result.getId()).isEqualTo("1");

    }

    @Test
    @Sql(scripts = { "/sql/budgetreappropriation/clearBudgetReAppropriation.sql",
            "/sql/budgetreappropriation/insertBudgetReAppropriationData.sql" })
    public void test_find_by_invalid_id_should_return_null() {

        BudgetReAppropriationEntity budgetReAppropriationEntity = BudgetReAppropriationEntity.builder().id("5").build();
        budgetReAppropriationEntity.setTenantId("default");
        BudgetReAppropriationEntity result = budgetReAppropriationJdbcRepository.findById(budgetReAppropriationEntity);
        assertNull(result);

    }

    @Test(expected = InvalidDataException.class)
    @Sql(scripts = { "/sql/budgetreappropriation/clearBudgetReAppropriation.sql",
            "/sql/budgetreappropriation/insertBudgetReAppropriationData.sql" })
    public void test_search_invalid_sort_option() {

        BudgetReAppropriationSearch search = getBudgetReAppropriationSearch();
        search.setSortBy("desc");
        budgetReAppropriationJdbcRepository.search(search);

    }

    @Test
    @Sql(scripts = { "/sql/budgetreappropriation/clearBudgetReAppropriation.sql",
            "/sql/budgetreappropriation/insertBudgetReAppropriationData.sql" })
    public void test_search_without_pagesize_offset_sortby() {

        BudgetReAppropriationSearch search = getBudgetReAppropriationSearch();
        search.setSortBy(null);
        search.setPageSize(null);
        search.setOffset(null);
        Pagination<BudgetReAppropriation> page = (Pagination<BudgetReAppropriation>) budgetReAppropriationJdbcRepository
                .search(getBudgetReAppropriationSearch());
        assertThat(page.getPagedData().get(0).getId()).isEqualTo("1");

    }

    class BudgetReAppropriationResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
        @Override
        public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<Map<String, Object>> rows = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<String, Object>() {
                    {
                        put("id", resultSet.getString("id"));
                        put("budgetDetailId", resultSet.getString("budgetDetailId"));
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

    private BudgetReAppropriationSearch getBudgetReAppropriationSearch1() {
        BudgetReAppropriationSearch budgetReAppropriationSearch = new BudgetReAppropriationSearch();
        budgetReAppropriationSearch.setId("id");
        budgetReAppropriationSearch.setAdditionAmount(BigDecimal.ONE);
        budgetReAppropriationSearch.setDeductionAmount(BigDecimal.ONE);
        budgetReAppropriationSearch.setOriginalAdditionAmount(BigDecimal.ONE);
        budgetReAppropriationSearch.setOriginalDeductionAmount(BigDecimal.ONE);
        budgetReAppropriationSearch.setAnticipatoryAmount(BigDecimal.ONE);
        budgetReAppropriationSearch.setBudgetDetail(BudgetDetail.builder().id("1").build());
        budgetReAppropriationSearch.setStatus(FinancialStatusContract.builder().id("1").build());
        budgetReAppropriationSearch.setAsOnDate(new Date());
        budgetReAppropriationSearch.setTenantId("tenantId");
        budgetReAppropriationSearch.setPageSize(500);
        budgetReAppropriationSearch.setOffset(0);
        budgetReAppropriationSearch.setSortBy("id desc");
        return budgetReAppropriationSearch;
    }

    private BudgetReAppropriationSearch getBudgetReAppropriationSearch() {
        BudgetReAppropriationSearch budgetReAppropriationSearch = new BudgetReAppropriationSearch();
        budgetReAppropriationSearch.setPageSize(500);
        budgetReAppropriationSearch.setOffset(0);
        budgetReAppropriationSearch.setSortBy("id desc");
        return budgetReAppropriationSearch;
    }
}