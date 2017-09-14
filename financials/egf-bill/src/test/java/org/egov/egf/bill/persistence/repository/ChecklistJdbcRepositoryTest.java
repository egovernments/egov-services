package org.egov.egf.bill.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.persistence.entity.ChecklistEntity;
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
public class ChecklistJdbcRepositoryTest {

	private ChecklistJdbcRepository checklistJdbcRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {
		checklistJdbcRepository = new ChecklistJdbcRepository(namedParameterJdbcTemplate);
	}
	
	@Test
	@Sql(scripts = { "/sql/checklist/clearchecklist.sql" })
	public void test_create() {

		ChecklistEntity checklist = ChecklistEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").type("checklisttype").subType("checklistSubType")
				.key("checklistkey").description("description")
				.build();
		checklist.setTenantId("default");
		ChecklistEntity actualResult = checklistJdbcRepository.create(checklist);

		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_checklist",
				new ChecklistResultExtractor());
		Map<String, Object> row = result.get(0);

		assertThat(row.get("key").toString()).isEqualTo("checklistkey");

	}
	
	@Test
	@Sql(scripts = { "/sql/checklist/clearchecklist.sql", "/sql/checklist/insertchecklistdata.sql" })
	public void test_update() {

		ChecklistEntity checklist = ChecklistEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").type("checklisttype").subType("checklistSubType")
				.key("checklistkey").description("description")
				.build();
		checklist.setTenantId("default");
		ChecklistEntity actualResult = checklistJdbcRepository.update(checklist);

		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_checklist",
				new ChecklistResultExtractor());
		Map<String, Object> row = result.get(0);

		assertThat(row.get("key").toString()).isEqualTo("checklistkey");

	}
	
	@Test
	@Sql(scripts = { "/sql/checklist/clearchecklist.sql", "/sql/checklist/insertchecklistdata.sql" })
	public void test_search() {

		Pagination<Checklist> page = (Pagination<Checklist>) checklistJdbcRepository.search(getChecklistSearch());
		
		assertThat(page.getPagedData().get(0).getId()).isEqualTo("b96561462fdc484fa97fa72c3944ad89");

	}
	
	private ChecklistSearch getChecklistSearch() {
		ChecklistSearch checklistSearch = new ChecklistSearch();
		checklistSearch.setId("b96561462fdc484fa97fa72c3944ad89");
		checklistSearch.setTenantId("default");
		checklistSearch.setPageSize(500);
		checklistSearch.setOffset(0);
		checklistSearch.setSortBy("id desc");
		return checklistSearch;
	}
	
	class ChecklistResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
		@Override
		public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			List<Map<String, Object>> rows = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> row = new HashMap<String, Object>() {
					{
						put("id", resultSet.getString("id"));
						put("key", resultSet.getString("key"));
						put("type", resultSet.getString("type"));
						put("subtype", resultSet.getString("subtype"));
						put("description", resultSet.getString("description"));
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
}
