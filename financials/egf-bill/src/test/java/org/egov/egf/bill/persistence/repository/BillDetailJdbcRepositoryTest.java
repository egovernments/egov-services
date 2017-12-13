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

import org.egov.egf.bill.domain.model.BillDetailSearch;
import org.egov.egf.bill.persistence.entity.BillDetailEntity;
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
public class BillDetailJdbcRepositoryTest {

	private BillDetailJdbcRepository billDetailJdbcRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {
		billDetailJdbcRepository = new BillDetailJdbcRepository(namedParameterJdbcTemplate, jdbcTemplate);
	}
	
	@Test
	@Sql(scripts = { "/sql/billdetail/clearbilldetail.sql" })
	public void test_create() {

		BillDetailEntity billDetail = BillDetailEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").orderId(1).chartOfAccountId("1").glcode("1").
				creditAmount(new BigDecimal(1234)).debitAmount(new BigDecimal(1234)).functionId("1").billRegisterId("1")
				.build();
		billDetail.setTenantId("default");
		BillDetailEntity actualResult = billDetailJdbcRepository.create(billDetail);

		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_billdetail",
				new BillDetailResultExtractor());
		Map<String, Object> row = result.get(0);

		assertThat(row.get("orderid").toString()).isEqualTo("1");

	}
	
	@Test
	@Sql(scripts = { "/sql/billdetail/clearbilldetail.sql", "/sql/billdetail/insertbilldetaildata.sql" })
	public void test_update() {

		BillDetailEntity billDetail = BillDetailEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").orderId(1).chartOfAccountId("1").glcode("1").
				creditAmount(new BigDecimal(1234)).debitAmount(new BigDecimal(1234)).functionId("1").billRegisterId("1")
				.build();
		billDetail.setTenantId("default");
		BillDetailEntity actualResult = billDetailJdbcRepository.update(billDetail);

		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_billdetail",
				new BillDetailResultExtractor());
		Map<String, Object> row = result.get(0);

		assertThat(row.get("orderid").toString()).isEqualTo("1");

	}
	
	@Test
	@Sql(scripts = { "/sql/billdetail/clearbilldetail.sql", "/sql/billdetail/insertbilldetaildata.sql" })
	public void test_find_by_id() {

		BillDetailEntity billDetail = BillDetailEntity.builder().id("1").build();
		billDetail.setTenantId("default");
		BillDetailEntity result = billDetailJdbcRepository.findById(billDetail);

	}

	@Test
	@Sql(scripts = { "/sql/billdetail/clearbilldetail.sql", "/sql/billdetail/insertbilldetaildata.sql" })
	public void test_find_by_invalid_id_should_return_null() {

		BillDetailEntity billDetail = BillDetailEntity.builder().id("5").build();
		billDetail.setTenantId("default");
		BillDetailEntity result = billDetailJdbcRepository.findById(billDetail);
		assertNull(result);

	}
	
    @Test
    @Sql(scripts = { "/sql/billdetail/clearbilldetail.sql", "/sql/billdetail/insertbilldetaildata.sql" })
    public void test_delete() {

    	BillDetailEntity billDetail = BillDetailEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").build();
    	billDetail.setTenantId("default");
        BillDetailEntity actualResult = billDetailJdbcRepository.delete(billDetail);

        List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_billdetail",
                new BillDetailResultExtractor());
        assertTrue("Result set length is zero", result.size() == 0);
    }
    @Ignore
    @Test
    @Sql(scripts = { "/sql/billdetail/clearbilldetail.sql", "/sql/billdetail/insertbilldetaildata.sql" })
    public void test_delete_reason() {

    	BillDetailEntity billDetail = BillDetailEntity.builder().id("b96561462fdc484fa97fa72c3944ad89").build();
    	billDetail.setTenantId("default");
        boolean actualResult = billDetailJdbcRepository.delete(billDetail, "reason");

        List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egf_billdetail",
                new BillDetailResultExtractor());
        assert(actualResult);
    }
	
	class BillDetailResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
		@Override
		public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			List<Map<String, Object>> rows = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> row = new HashMap<String, Object>() {
					{
						put("id", resultSet.getString("id"));
						put("orderid", resultSet.getString("orderid"));
						put("chartofaccountid", resultSet.getString("chartofaccountid"));
						put("debitamount", resultSet.getString("debitamount"));
						put("creditamount", resultSet.getString("creditamount"));
						put("functionid", resultSet.getString("functionid"));
						put("billregisterid", resultSet.getString("billregisterid"));
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
	
	private BillDetailSearch getBillDetailSearch() {
		BillDetailSearch billDetailSearch = new BillDetailSearch();
		billDetailSearch.setId("1");
		billDetailSearch.setIds("1");
		billDetailSearch.setTenantId("default");
		billDetailSearch.setPageSize(500);
		billDetailSearch.setOffset(0);
		billDetailSearch.setSortBy("id desc");
		return billDetailSearch;
	}
}
