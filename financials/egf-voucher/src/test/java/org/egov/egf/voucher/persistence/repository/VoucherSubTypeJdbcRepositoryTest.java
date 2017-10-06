package org.egov.egf.voucher.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.voucher.domain.model.VoucherSubType;
import org.egov.egf.voucher.domain.model.VoucherSubTypeSearch;
import org.egov.egf.voucher.domain.model.VoucherType;
import org.egov.egf.voucher.persistence.entity.VoucherSubTypeEntity;
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
public class VoucherSubTypeJdbcRepositoryTest {

	private VoucherSubTypeJdbcRepository voucherSubTypeJdbcRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {
		voucherSubTypeJdbcRepository = new VoucherSubTypeJdbcRepository(namedParameterJdbcTemplate);
	}

	@Test
	@Sql(scripts = { "/sql/vouchersubtype/clearvouchersubtype.sql" })
	public void test_create() {

		VoucherSubTypeEntity voucherSubType = VoucherSubTypeEntity.builder().id("b96561462fdc484fa97fa72c3944ad89")
				.voucherType(VoucherType.STANDARD_VOUCHER_TYPE_CONTRA.toString()).voucherName("BankToBankTest")
				.voucherNamePrefix("CSL").exclude(true).build();
		voucherSubType.setTenantId("default");
		VoucherSubTypeEntity actualResult = voucherSubTypeJdbcRepository.create(voucherSubType);

		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_vouchersubtype",
				new VoucherSubTypeResultExtractor());
		Map<String, Object> row = result.get(0);

		assertThat(row.get("id").toString()).isEqualTo("b96561462fdc484fa97fa72c3944ad89");
		assertThat(row.get("voucherType")).isEqualTo(actualResult.getVoucherType());
		assertThat(row.get("voucherName")).isEqualTo(actualResult.getVoucherName());
		assertThat(row.get("voucherNamePrefix")).isEqualTo(actualResult.getVoucherNamePrefix());
	}

	@Test
	@Sql(scripts = { "/sql/vouchersubtype/clearvouchersubtype.sql",
			"/sql/vouchersubtype/insertvouchersubtypeData.sql" })
	public void test_update() {

		VoucherSubTypeEntity voucherSubType = VoucherSubTypeEntity.builder().id("b96561462fdc484fa97fa72c3944ad89")
				.voucherType(VoucherType.STANDARD_VOUCHER_TYPE_CONTRA.toString()).voucherName("BankToBankTestU")
				.voucherNamePrefix("CSL").exclude(true).build();
		voucherSubType.setTenantId("default");
		VoucherSubTypeEntity actualResult = voucherSubTypeJdbcRepository.update(voucherSubType);

		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_vouchersubtype",
				new VoucherSubTypeResultExtractor());
		Map<String, Object> row = result.get(0);

		assertThat(row.get("id").toString()).isEqualTo("b96561462fdc484fa97fa72c3944ad89");
		assertThat(row.get("voucherType")).isEqualTo(actualResult.getVoucherType());
		assertThat(row.get("voucherName")).isEqualTo(actualResult.getVoucherName());
		assertThat(row.get("voucherNamePrefix")).isEqualTo(actualResult.getVoucherNamePrefix());

	}

	@Test
	@Sql(scripts = { "/sql/vouchersubtype/clearvouchersubtype.sql",
			"/sql/vouchersubtype/insertvouchersubtypeData.sql" })
	public void test_search() {

		Pagination<VoucherSubType> page = (Pagination<VoucherSubType>) voucherSubTypeJdbcRepository
				.search(getVoucherSubTypeSearch());

		assertThat(page.getPagedData().get(0).getId()).isEqualTo("b96561462fdc484fa97fa72c3944ad89");
		assertThat(page.getPagedData().get(0).getVoucherName()).isEqualTo("BankToBankTest");
		assertThat(page.getPagedData().get(0).getVoucherType()).isEqualTo(VoucherType.STANDARD_VOUCHER_TYPE_CONTRA);
		assertThat(page.getPagedData().get(0).getVoucherNamePrefix()).isEqualTo("CSL");
	}

	private VoucherSubTypeSearch getVoucherSubTypeSearch() {
		VoucherSubTypeSearch voucherSubTypeSearch = new VoucherSubTypeSearch();
		voucherSubTypeSearch.setId("b96561462fdc484fa97fa72c3944ad89");
		voucherSubTypeSearch.setVoucherName("BankToBankTest");
		voucherSubTypeSearch.setVoucherNamePrefix("CSL");
		voucherSubTypeSearch.setVoucherType(VoucherType.STANDARD_VOUCHER_TYPE_CONTRA);
		voucherSubTypeSearch.setTenantId("default");
		voucherSubTypeSearch.setPageSize(500);
		voucherSubTypeSearch.setOffset(0);
		voucherSubTypeSearch.setSortBy("id desc");
		return voucherSubTypeSearch;
	}

	class VoucherSubTypeResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
		@Override
		public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			List<Map<String, Object>> rows = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> row = new HashMap<String, Object>() {
					{
						put("id", resultSet.getString("id"));
						put("voucherType", resultSet.getString("voucherType"));
						put("voucherName", resultSet.getString("voucherName"));
						put("voucherNamePrefix", resultSet.getString("voucherNamePrefix"));
						put("exclude", resultSet.getString("exclude"));
						put("cutOffDate", resultSet.getString("cutOffDate"));
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
