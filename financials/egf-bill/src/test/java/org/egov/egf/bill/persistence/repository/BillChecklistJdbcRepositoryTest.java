package org.egov.egf.bill.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillChecklistSearch;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.persistence.entity.BillChecklistEntity;
import org.junit.Before;
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
public class BillChecklistJdbcRepositoryTest {

	private BillChecklistJdbcRepository billChecklistJdbcRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {
		billChecklistJdbcRepository = new BillChecklistJdbcRepository(namedParameterJdbcTemplate);
	}
	
	@Test
	@Sql(scripts = { "/sql/billchecklist/clearbillchecklist.sql" })
	public void test_create() {

		BillChecklistEntity billChecklist = BillChecklistEntity.builder().id("6").billId("29").checklistId("4").checklistValue("newValue").build();
		billChecklist.setTenantId("default");
		BillChecklistEntity actualResult = billChecklistJdbcRepository.create(billChecklist);

		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_billchecklist",
				new BillChecklistResultExtractor());
		Map<String, Object> row = result.get(0);

		assertThat(row.get("checklistValue").toString()).isEqualTo("newValue");

	}
	
	@Test
	@Sql(scripts = { "/sql/billchecklist/clearbillchecklist.sql", "/sql/billchecklist/insertbillchecklistdata.sql" })
	public void test_update() {

		BillChecklistEntity billChecklist = BillChecklistEntity.builder().id("6").billId("29").checklistId("4").checklistValue("newValue").build();
		billChecklist.setTenantId("default");
		BillChecklistEntity actualResult = billChecklistJdbcRepository.update(billChecklist);

		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_billchecklist",
				new BillChecklistResultExtractor());
		Map<String, Object> row = result.get(0);

		assertThat(row.get("checklistValue").toString()).isEqualTo("newValue");

	}
	
	@Test
	@Sql(scripts = { "/sql/billchecklist/clearbillchecklist.sql", "/sql/billchecklist/insertbillchecklistdata.sql" })
	public void test_find_by_id() {

		BillChecklistEntity billChecklist = BillChecklistEntity.builder().id("1").build();
		billChecklist.setTenantId("default");
		BillChecklistEntity result = billChecklistJdbcRepository.findById(billChecklist);

	}

	@Test
	@Sql(scripts = { "/sql/billchecklist/clearbillchecklist.sql", "/sql/billchecklist/insertbillchecklistdata.sql" })
	public void test_find_by_invalid_id_should_return_null() {

		BillChecklistEntity billChecklist = BillChecklistEntity.builder().id("5").build();
		billChecklist.setTenantId("default");
		BillChecklistEntity result = billChecklistJdbcRepository.findById(billChecklist);
		assertNull(result);

	}
	
	@Test
	@Sql(scripts = { "/sql/billchecklist/clearbillchecklist.sql", "/sql/billchecklist/insertbillchecklistdata.sql" })
	public void test_search() {

		Pagination<BillChecklist> page = (Pagination<BillChecklist>) billChecklistJdbcRepository.search(getBillChecklistSearch());

	}

	@Test
	@Sql(scripts = { "/sql/billchecklist/clearbillchecklist.sql", "/sql/billchecklist/insertbillchecklistdata.sql" })
	public void test_invalid_search() {

		Pagination<BillChecklist> page = (Pagination<BillChecklist>) billChecklistJdbcRepository.search(getBillChecklistSearch1());
		assertThat(page.getPagedData().size()).isEqualTo(0);

	}
	
	class BillChecklistResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
		@Override
		public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			List<Map<String, Object>> rows = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> row = new HashMap<String, Object>() {
					{
						put("id", resultSet.getString("id"));
						put("billId", resultSet.getString("billId"));
						put("checklistId", resultSet.getString("checklistId"));
						put("checklistValue", resultSet.getString("checklistValue"));
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
	
	private BillChecklistSearch getBillChecklistSearch() {
		BillChecklistSearch billChecklistSearch = new BillChecklistSearch();
		billChecklistSearch.setId("6");
		billChecklistSearch.setIds("6");
		billChecklistSearch.setBill(BillRegister.builder().id("29").build());
		billChecklistSearch.setChecklist(Checklist.builder().id("4").build());
		billChecklistSearch.setChecklistValue("newValue");
		billChecklistSearch.setTenantId("default");
		billChecklistSearch.setPageSize(500);
		billChecklistSearch.setOffset(0);
		billChecklistSearch.setSortBy("id desc");
		return billChecklistSearch;
	}
	
	private BillChecklistSearch getBillChecklistSearch1() {
		BillChecklistSearch billChecklistSearch = new BillChecklistSearch();
		billChecklistSearch.setId("id");
		billChecklistSearch.setTenantId("default");
		billChecklistSearch.setPageSize(500);
		billChecklistSearch.setOffset(0);
		billChecklistSearch.setSortBy("id desc");
		return billChecklistSearch;
	}
}
