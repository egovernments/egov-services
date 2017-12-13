package org.egov.egf.bill.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.egf.bill.persistence.entity.BillPayeeDetailEntity;
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
public class BillPayeeDetailJdbcRepositoryTest {

	private BillPayeeDetailJdbcRepository billPayeeDetailJdbcRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {
		billPayeeDetailJdbcRepository = new BillPayeeDetailJdbcRepository(namedParameterJdbcTemplate, jdbcTemplate);
	}
	
	@Test
	@Sql(scripts = { "/sql/billpayeedetail/clearbillpayeedetail.sql" })
	public void test_create() {

		BillPayeeDetailEntity billPayeeDetail = BillPayeeDetailEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").accountDetailTypeId("1").
				accountDetailKeyId("1").amount(new BigDecimal(1234)).billDetailId("1")
				.build();
		billPayeeDetail.setTenantId("default");
		BillPayeeDetailEntity actualResult = billPayeeDetailJdbcRepository.create(billPayeeDetail);

		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_billpayeedetail",
				new BillPayeeDetailResultExtractor());
		Map<String, Object> row = result.get(0);

		assertThat(row.get("accountdetailtypeid").toString()).isEqualTo("1");
		assertThat(row.get("accountdetailkeyid").toString()).isEqualTo("1");
		assertThat(row.get("amount").toString()).isEqualTo("1234.00");

	}
	
	@Test
	@Sql(scripts = { "/sql/billpayeedetail/clearbillpayeedetail.sql", "/sql/billpayeedetail/insertbillpayeedetaildata.sql" })
	public void test_update() {

		BillPayeeDetailEntity billPayeeDetail = BillPayeeDetailEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").accountDetailTypeId("1").
				accountDetailKeyId("1").amount(new BigDecimal(1234)).billDetailId("1")
				.build();
		billPayeeDetail.setTenantId("default");
		BillPayeeDetailEntity actualResult = billPayeeDetailJdbcRepository.update(billPayeeDetail);

		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_billpayeedetail",
				new BillPayeeDetailResultExtractor());
		Map<String, Object> row = result.get(0);

		assertThat(row.get("accountdetailtypeid").toString()).isEqualTo("1");
		assertThat(row.get("accountdetailkeyid").toString()).isEqualTo("1");
		assertThat(row.get("amount").toString()).isEqualTo("1234.00");

	}
	
	@Test
	@Sql(scripts = { "/sql/billpayeedetail/clearbillpayeedetail.sql", "/sql/billpayeedetail/insertbillpayeedetaildata.sql" })
	public void test_find_by_id() {

		BillPayeeDetailEntity billPayeeDetail = BillPayeeDetailEntity.builder().id("1").build();
		billPayeeDetail.setTenantId("default");
		BillPayeeDetailEntity result = billPayeeDetailJdbcRepository.findById(billPayeeDetail);

	}

	@Test
	@Sql(scripts = { "/sql/billpayeedetail/clearbillpayeedetail.sql", "/sql/billpayeedetail/insertbillpayeedetaildata.sql" })
	public void test_find_by_invalid_id_should_return_null() {

		BillPayeeDetailEntity billPayeeDetail = BillPayeeDetailEntity.builder().id("5").build();
		billPayeeDetail.setTenantId("default");
		BillPayeeDetailEntity result = billPayeeDetailJdbcRepository.findById(billPayeeDetail);
		assertNull(result);

	}

    @Test
    @Sql(scripts = { "/sql/billpayeedetail/clearbillpayeedetail.sql", "/sql/billpayeedetail/insertbillpayeedetaildata.sql" })
    public void test_delete() {

		BillPayeeDetailEntity billPayeeDetail = BillPayeeDetailEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").accountDetailTypeId("1").
				accountDetailKeyId("1").amount(new BigDecimal(1234)).billDetailId("1")
				.build();
		billPayeeDetail.setTenantId("default");
		
		BillPayeeDetailEntity actualResult = billPayeeDetailJdbcRepository.delete(billPayeeDetail);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_billpayeedetail",
                new BillPayeeDetailResultExtractor());
        assertTrue("Result set length is zero", result.size() == 0);
    }
    @Ignore
    @Test
    @Sql(scripts = { "/sql/billpayeedetail/clearbillpayeedetail.sql", "/sql/billpayeedetail/insertbillpayeedetaildata.sql" })
    public void test_delete_reason() {

		BillPayeeDetailEntity billPayeeDetail = BillPayeeDetailEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").accountDetailTypeId("1").
				accountDetailKeyId("1").amount(new BigDecimal(1234)).billDetailId("1")
				.build();
		billPayeeDetail.setTenantId("default");
		
		boolean actualResult = billPayeeDetailJdbcRepository.delete(billPayeeDetail, "reason");

        List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_billpayeedetail",
                new BillPayeeDetailResultExtractor());
        assert(actualResult);
    }
	
	class BillPayeeDetailResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
		@Override
		public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			List<Map<String, Object>> rows = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> row = new HashMap<String, Object>() {
					{
						put("id", resultSet.getString("id"));
						put("accountdetailtypeid", resultSet.getString("accountdetailtypeid"));
						put("accountdetailkeyid", resultSet.getString("accountdetailkeyid"));
						put("amount", resultSet.getString("amount"));
						put("billdetailid", resultSet.getString("billdetailid"));
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
