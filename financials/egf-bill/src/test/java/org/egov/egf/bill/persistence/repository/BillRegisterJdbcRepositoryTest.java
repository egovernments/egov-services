package org.egov.egf.bill.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.persistence.entity.BillRegisterEntity;
import org.egov.egf.bill.web.contract.Boundary;
import org.egov.egf.bill.web.contract.Department;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BillRegisterJdbcRepositoryTest {

	private BillRegisterJdbcRepository billRegisterJdbcRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {
		billRegisterJdbcRepository = new BillRegisterJdbcRepository(namedParameterJdbcTemplate, jdbcTemplate);
	}

	@Test
	@Sql(scripts = { "/sql/billregister/clearbillregister.sql" })
	public void test_create() {

		BillRegisterEntity billRegister = BillRegisterEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").billType("billtype4321").billSubType("billsubtype1")
				.billNumber("1234").billAmount(new BigDecimal(4321)).passedAmount(new BigDecimal(4321)).moduleName("billmodule").statusId("1").fundId("1").functionId("1").
				functionaryId("1").fundsourceId("1").schemeId("1").subSchemeId("1").statusId("1").billDate(new Date())
				.build();
		billRegister.setTenantId("default");
		BillRegisterEntity actualResult = billRegisterJdbcRepository.create(billRegister);

		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_billregister",
				new BillRegisterResultExtractor());
		Map<String, Object> row = result.get(0);

		assertThat(row.get("billType").toString()).isEqualTo("billtype4321");

	}

	@Test
	@Sql(scripts = { "/sql/billregister/clearbillregister.sql", "/sql/billregister/insertbillregisterdata.sql" })
	public void test_update() {

		BillRegisterEntity billRegister = BillRegisterEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").billType("billtype4321").billSubType("billsubtype1")
				.billNumber("1234").billAmount(new BigDecimal(4321)).passedAmount(new BigDecimal(4321)).moduleName("billmodule").statusId("1").fundId("1").functionId("1").
				functionaryId("1").fundsourceId("1").schemeId("1").subSchemeId("1").statusId("1").billDate(new Date())
				.build();
		billRegister.setTenantId("default");
		BillRegisterEntity actualResult = billRegisterJdbcRepository.update(billRegister);

		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_billregister",
				new BillRegisterResultExtractor());
		Map<String, Object> row = result.get(0);

		assertThat(row.get("billType").toString()).isEqualTo("billtype4321");
		
	}

	@Test
	@Sql(scripts = { "/sql/billregister/clearbillregister.sql", "/sql/billregister/insertbillregisterdata.sql" })
	public void test_search() {

		Pagination<BillRegister> page = (Pagination<BillRegister>) billRegisterJdbcRepository.search(getBillRegisterSearch());

	}

	@Test
	@Sql(scripts = { "/sql/billregister/clearbillregister.sql", "/sql/billregister/insertbillregisterdata.sql" })
	public void test_invalid_search() {

		Pagination<BillRegister> page = (Pagination<BillRegister>) billRegisterJdbcRepository.search(getBillRegisterSearch1());
		assertThat(page.getPagedData().size()).isEqualTo(0);

	}

	@Test
	@Sql(scripts = { "/sql/billregister/clearbillregister.sql", "/sql/billregister/insertbillregisterdata.sql" })
	public void test_find_by_id() {

		BillRegisterEntity billRegisterEntity = BillRegisterEntity.builder().id("1").build();
		billRegisterEntity.setTenantId("default");
		BillRegisterEntity result = billRegisterJdbcRepository.findById(billRegisterEntity);

	}

	@Test
	@Sql(scripts = { "/sql/billregister/clearbillregister.sql", "/sql/billregister/insertbillregisterdata.sql" })
	public void test_find_by_invalid_id_should_return_null() {

		BillRegisterEntity billRegisterEntity = BillRegisterEntity.builder().id("5").build();
		billRegisterEntity.setTenantId("default");
		BillRegisterEntity result = billRegisterJdbcRepository.findById(billRegisterEntity);
		assertNull(result);

	}

	@Test(expected = InvalidDataException.class)
	@Sql(scripts = { "/sql/billregister/clearbillregister.sql", "/sql/billregister/insertbillregisterdata.sql" })
	public void test_search_invalid_sort_option() {

		BillRegisterSearch search = getBillRegisterSearch();
		search.setSortBy("desc");
		billRegisterJdbcRepository.search(search);

	}

	@Test
	@Sql(scripts = { "/sql/billregister/clearbillregister.sql", "/sql/billregister/insertbillregisterdata.sql" })
	public void test_search_without_pagesize_offset_sortby() {

		BillRegisterSearch search = getBillRegisterSearch();
		search.setSortBy(null);
		search.setPageSize(null);
		search.setOffset(null);
		Pagination<BillRegister> page = (Pagination<BillRegister>) billRegisterJdbcRepository.search(getBillRegisterSearch());

	}
	
    @Test
    @Sql(scripts = { "/sql/billregister/clearbillregister.sql", "/sql/billregister/insertbillregisterdata.sql" })
    public void test_delete() {

		BillRegisterEntity billRegister = BillRegisterEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").billType("billtype4321").build();
		billRegister.setTenantId("default");
		BillRegisterEntity actualResult = billRegisterJdbcRepository.delete(billRegister);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_billregister",
                new BillRegisterResultExtractor());
        assertTrue("Result set length is zero", result.size() == 0);
    }
    @Ignore
	@Test
	@Sql(scripts = { "/sql/billregister/clearbillregister.sql", "/sql/billregister/insertbillregisterdata.sql" })
	public void test_delete_reason() {
		BillRegisterEntity billRegister = BillRegisterEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").billType("billtype4321").build();
		billRegister.setTenantId("default");
		BillRegisterSearch search = new BillRegisterSearch();

		boolean actual = billRegisterJdbcRepository.delete(billRegister, "reason");
		
		search.setId("b96561462fdc484fa97fa72c3944ad89");
		search.setSortBy(null);
		search.setPageSize(null);
		search.setOffset(null);
		Pagination<BillRegister> page = (Pagination<BillRegister>) billRegisterJdbcRepository.search(getBillRegisterSearch());
		assertEquals(page.getPagedData().isEmpty(), Boolean.TRUE);
	}

	class BillRegisterResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
		@Override
		public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			List<Map<String, Object>> rows = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> row = new HashMap<String, Object>() {
					{
						put("id", resultSet.getString("id"));
						put("billType", resultSet.getString("billType"));
						put("billSubType", resultSet.getString("billSubType"));
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

	private BillRegisterSearch getBillRegisterSearch1() {
		BillRegisterSearch billRegisterSearch = new BillRegisterSearch();
		billRegisterSearch.setId("id");
		billRegisterSearch.setTenantId("default");
		billRegisterSearch.setPageSize(500);
		billRegisterSearch.setOffset(0);
		billRegisterSearch.setSortBy("id desc");
		return billRegisterSearch;
	}

	private BillRegisterSearch getBillRegisterSearch() {
		BillRegisterSearch billRegisterSearch = new BillRegisterSearch();
		billRegisterSearch.setId("1");
		billRegisterSearch.setIds("1");
		billRegisterSearch.setBillType("billtype4321");
		billRegisterSearch.setBillSubType("billsubtype1");
		billRegisterSearch.setBillNumber("1234");
		billRegisterSearch.setBillDate(new Date());
		billRegisterSearch.setBillAmount(new BigDecimal(4321));
		billRegisterSearch.setPassedAmount(new BigDecimal(4321));
		billRegisterSearch.setModuleName("billmodule");
		billRegisterSearch.setStatus(FinancialStatusContract.builder().id("1").build());
		billRegisterSearch.setStatuses("1");
		billRegisterSearch.setFund(getFundContract());
		billRegisterSearch.setFunction(getFunctionContract());
		billRegisterSearch.setFundsource(getFundsourceContract());
		billRegisterSearch.setScheme(getSchemeContract());
		billRegisterSearch.setSubScheme(getSubSchemeContract());
		billRegisterSearch.setFunctionary(getFunctionaryContract());
		billRegisterSearch.setDivision(getDivision());
		billRegisterSearch.setDepartment(getDepartment());
		billRegisterSearch.setSourcePath("1");
		billRegisterSearch.setBudgetCheckRequired(true);
		billRegisterSearch.setBudgetAppropriationNo("1234");
		billRegisterSearch.setPartyBillNumber("1234");
		billRegisterSearch.setPartyBillDate(new Date());
		billRegisterSearch.setDescription("description");
		billRegisterSearch.setTenantId("default");
		billRegisterSearch.setPageSize(500);
		billRegisterSearch.setOffset(0);
		billRegisterSearch.setSortBy("id desc");
		return billRegisterSearch;
	}
	
	private FunctionContract getFunctionContract() {
		FunctionContract function = FunctionContract.builder().id("1").build();
		function.setTenantId("Default");
		return function;
	}
	
	private FundContract getFundContract() {
		FundContract fund = FundContract.builder().id("1").build();
		fund.setTenantId("Default");
		return fund;
	}
	
	private FundsourceContract getFundsourceContract() {
		FundsourceContract fundsource = FundsourceContract.builder().id("1").build();
		fundsource.setTenantId("Default");
		return fundsource;
	}
	
	private SchemeContract getSchemeContract() {
		SchemeContract scheme = SchemeContract.builder().id("1").build();
		scheme.setTenantId("Default");
		return scheme;
	}
	
	private SubSchemeContract getSubSchemeContract() {
		SubSchemeContract subScheme = SubSchemeContract.builder().id("1").build();
		subScheme.setTenantId("Default");
		return subScheme;
	}
	
	private FunctionaryContract getFunctionaryContract() {
		FunctionaryContract functionary = FunctionaryContract.builder().id("1").build();
		functionary.setTenantId("Default");
		return functionary;
	}
	
	private Boundary getDivision() {
		Boundary boundary = Boundary.builder().id("1").build();
		boundary.setTenantId("Default");
		return boundary;
	}
	
	private Department getDepartment() {
		Department department = Department.builder().id("1").build();
		department.setTenantId("Default");
		return department;
	}
}
