package org.egov.egf.budget.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.persistence.entity.BudgetDetailEntity;
import org.egov.egf.budget.web.contract.Boundary;
import org.egov.egf.budget.web.contract.Department;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
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
public class BudgetDetailJdbcRepositoryTest {

    private BudgetDetailJdbcRepository budgetDetailJdbcRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() throws Exception {
        budgetDetailJdbcRepository = new BudgetDetailJdbcRepository(namedParameterJdbcTemplate);
    }

    @Test
    @Sql(scripts = { "/sql/budgetdetail/clearBudgetDetail.sql" })
    public void test_create() {

        BudgetDetailEntity budgetDetail = BudgetDetailEntity.builder().anticipatoryAmount(BigDecimal.ONE)
                .approvedAmount(BigDecimal.ONE).budgetAvailable(BigDecimal.ONE).originalAmount(BigDecimal.ONE)
                .budgetId("1").budgetGroupId("1").planningPercent(BigDecimal.valueOf(1500)).build();
        budgetDetail.setTenantId("default");
        BudgetDetailEntity actualResult = budgetDetailJdbcRepository.create(budgetDetail);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_budgetdetail",
                new BudgetDetailResultExtractor());
        Map<String, Object> row = result.get(0);
        assertThat(row.get("id")).isEqualTo(actualResult.getId());
        assertThat(row.get("anticipatoryAmount")).isEqualTo(actualResult.getAnticipatoryAmount().intValue());
        assertThat(row.get("approvedAmount")).isEqualTo(actualResult.getApprovedAmount().intValue());
        assertThat(row.get("budgetAvailable")).isEqualTo(actualResult.getBudgetAvailable().intValue());
        assertThat(row.get("originalAmount")).isEqualTo(actualResult.getOriginalAmount().intValue());

    }

    @Test(expected = DataIntegrityViolationException.class)
    @Sql(scripts = { "/sql/budgetdetail/clearBudgetDetail.sql" })
    public void test_create_with_tenantId_null() {

        BudgetDetailEntity budgetDetail = BudgetDetailEntity.builder().anticipatoryAmount(BigDecimal.ONE)
                .approvedAmount(BigDecimal.ONE).budgetAvailable(BigDecimal.ONE).originalAmount(BigDecimal.ONE)
                .budgetId("1").budgetGroupId("1").planningPercent(BigDecimal.valueOf(1500)).build();
        budgetDetailJdbcRepository.create(budgetDetail);

    }

    @Test
    @Sql(scripts = { "/sql/budgetdetail/clearBudgetDetail.sql", "/sql/budgetdetail/insertBudgetDetailData.sql" })
    public void test_update() {

        BudgetDetailEntity budgetDetail = BudgetDetailEntity.builder().anticipatoryAmount(BigDecimal.ONE)
                .approvedAmount(BigDecimal.ONE).budgetAvailable(BigDecimal.ONE).originalAmount(BigDecimal.ONE)
                .budgetId("1").budgetGroupId("1").planningPercent(BigDecimal.valueOf(1500)).id("1").build();
        budgetDetail.setTenantId("default");
        BudgetDetailEntity actualResult = budgetDetailJdbcRepository.update(budgetDetail);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_budgetdetail",
                new BudgetDetailResultExtractor());
        Map<String, Object> row = result.get(0);
        assertThat(row.get("id")).isEqualTo(actualResult.getId());
        assertThat(row.get("anticipatoryAmount")).isEqualTo(actualResult.getAnticipatoryAmount().intValue());
        assertThat(row.get("approvedAmount")).isEqualTo(actualResult.getApprovedAmount().intValue());
        assertThat(row.get("budgetAvailable")).isEqualTo(actualResult.getBudgetAvailable().intValue());
        assertThat(row.get("originalAmount")).isEqualTo(actualResult.getOriginalAmount().intValue());

    }

    @Test
    @Sql(scripts = { "/sql/budgetdetail/clearBudgetDetail.sql", "/sql/budgetdetail/insertBudgetDetailData.sql" })
    public void test_search() {

        Pagination<BudgetDetail> page = (Pagination<BudgetDetail>) budgetDetailJdbcRepository
                .search(getBudgetDetailSearch1());
        assertThat(page.getPagedData().get(0).getAnticipatoryAmount().intValue()).isEqualTo(1);
        assertThat(page.getPagedData().get(0).getApprovedAmount().intValue()).isEqualTo(1);
        assertThat(page.getPagedData().get(0).getBudgetAvailable().intValue()).isEqualTo(1);
        assertThat(page.getPagedData().get(0).getOriginalAmount().intValue()).isEqualTo(1);

    }

    @Test
    @Sql(scripts = { "/sql/budgetdetail/clearBudgetDetail.sql", "/sql/budgetdetail/insertBudgetDetailData.sql" })
    public void test_invalid_search() {

        Pagination<BudgetDetail> page = (Pagination<BudgetDetail>) budgetDetailJdbcRepository
                .search(getBudgetDetailSearch());
        assertThat(page.getPagedData().size()).isEqualTo(0);

    }

    @Test
    @Sql(scripts = { "/sql/budgetdetail/clearBudgetDetail.sql", "/sql/budgetdetail/insertBudgetDetailData.sql" })
    public void test_find_by_id() {

        BudgetDetailEntity budgetDetailEntity = BudgetDetailEntity.builder().id("1").build();
        budgetDetailEntity.setTenantId("default");
        BudgetDetailEntity result = budgetDetailJdbcRepository.findById(budgetDetailEntity);
        assertThat(result.getId()).isEqualTo("1");

    }

    @Test
    @Sql(scripts = { "/sql/budgetdetail/clearBudgetDetail.sql", "/sql/budgetdetail/insertBudgetDetailData.sql" })
    public void test_find_by_invalid_id_should_return_null() {

        BudgetDetailEntity budgetDetailEntity = BudgetDetailEntity.builder().id("5").build();
        budgetDetailEntity.setTenantId("default");
        BudgetDetailEntity result = budgetDetailJdbcRepository.findById(budgetDetailEntity);
        assertNull(result);

    }

    @Test(expected = InvalidDataException.class)
    @Sql(scripts = { "/sql/budgetdetail/clearBudgetDetail.sql", "/sql/budgetdetail/insertBudgetDetailData.sql" })
    public void test_search_invalid_sort_option() {

        BudgetDetailSearch search = getBudgetDetailSearch();
        search.setSortBy("desc");
        budgetDetailJdbcRepository.search(search);

    }

    @Test
    @Sql(scripts = { "/sql/budgetdetail/clearBudgetDetail.sql", "/sql/budgetdetail/insertBudgetDetailData.sql" })
    public void test_search_without_pagesize_offset_sortby() {

        BudgetDetailSearch search = getBudgetDetailSearch1();
        search.setSortBy(null);
        search.setPageSize(null);
        search.setOffset(null);
        Pagination<BudgetDetail> page = (Pagination<BudgetDetail>) budgetDetailJdbcRepository
                .search(getBudgetDetailSearch1());
        assertThat(page.getPagedData().get(0).getAnticipatoryAmount().intValue()).isEqualTo(1);
        assertThat(page.getPagedData().get(0).getApprovedAmount().intValue()).isEqualTo(1);
        assertThat(page.getPagedData().get(0).getBudgetAvailable().intValue()).isEqualTo(1);
        assertThat(page.getPagedData().get(0).getOriginalAmount().intValue()).isEqualTo(1);

    }

    class BudgetDetailResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
        @Override
        public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<Map<String, Object>> rows = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<String, Object>() {
                    {
                        put("id", resultSet.getString("id"));
                        put("anticipatoryAmount", resultSet.getInt("anticipatoryAmount"));
                        put("approvedAmount", resultSet.getInt("approvedAmount"));
                        put("budgetAvailable", resultSet.getInt("budgetAvailable"));
                        put("originalAmount", resultSet.getInt("originalAmount"));
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

    private BudgetDetailSearch getBudgetDetailSearch() {
        BudgetDetailSearch budgetDetailSearch = new BudgetDetailSearch();
        budgetDetailSearch.setId("");
        budgetDetailSearch.setBudget(Budget.builder().id("1").build());
        budgetDetailSearch.setBudgetGroup(BudgetGroupContract.builder().id("1").build());
        budgetDetailSearch.setFund(FundContract.builder().id("1").build());
        budgetDetailSearch.setFunction(FunctionContract.builder().id("1").build());
        budgetDetailSearch.setFunctionary(FunctionaryContract.builder().id("1").build());
        budgetDetailSearch.setUsingDepartment(Department.builder().id("1").build());
        budgetDetailSearch.setExecutingDepartment(Department.builder().id("1").build());
        budgetDetailSearch.setScheme(SchemeContract.builder().id("1").build());
        budgetDetailSearch.setSubScheme(SubSchemeContract.builder().id("1").build());
        budgetDetailSearch.setBoundary(Boundary.builder().id("1").build());
        budgetDetailSearch.setStatus(FinancialStatusContract.builder().id("1").build());
        budgetDetailSearch.setOriginalAmount(BigDecimal.ONE);
        budgetDetailSearch.setApprovedAmount(BigDecimal.ONE);
        budgetDetailSearch.setAnticipatoryAmount(BigDecimal.ONE);
        budgetDetailSearch.setBudgetAvailable(BigDecimal.ONE);
        budgetDetailSearch.setPlanningPercent(BigDecimal.valueOf(1500));
        budgetDetailSearch.setDocumentNumber("");
        budgetDetailSearch.setUniqueNo("");
        budgetDetailSearch.setMaterializedPath("");
        budgetDetailSearch.setPageSize(500);
        budgetDetailSearch.setOffset(0);
        budgetDetailSearch.setSortBy("id desc");
        return budgetDetailSearch;
    }

    private BudgetDetailSearch getBudgetDetailSearch1() {
        BudgetDetailSearch budgetDetailSearch = new BudgetDetailSearch();
        budgetDetailSearch.setPageSize(500);
        budgetDetailSearch.setOffset(0);
        budgetDetailSearch.setSortBy("id desc");
        return budgetDetailSearch;
    }
}