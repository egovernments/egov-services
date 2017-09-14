package org.egov.egf.bill.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.persistence.entity.BillRegisterEntity;
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
public class BillRegisterJdbcRepositoryTest {

	private BillRegisterJdbcRepository billRegisterJdbcRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {
		billRegisterJdbcRepository = new BillRegisterJdbcRepository(namedParameterJdbcTemplate);
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
		
		assertThat(page.getPagedData().get(0).getId()).isEqualTo("b96561462fdc484fa97fa72c3944ad89");

	}
	
	private BillRegisterSearch getBillRegisterSearch() {
		BillRegisterSearch billRegisterSearch = new BillRegisterSearch();
		billRegisterSearch.setId("b96561462fdc484fa97fa72c3944ad89");
		billRegisterSearch.setTenantId("default");
		billRegisterSearch.setPageSize(500);
		billRegisterSearch.setOffset(0);
		billRegisterSearch.setSortBy("id desc");
		return billRegisterSearch;
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
}
