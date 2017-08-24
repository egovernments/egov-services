package org.egov.mr.repository.rowmapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.ResultSet;

import org.egov.mr.model.Fee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FeeRowMapperTest {

	@Mock
	ResultSet rs;

	@InjectMocks
	private FeeRowMapper feeRowMapper;

	@Test
	public void testFeeRowMapper() throws Exception {

		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);

		when(rs.getString("id")).thenReturn("2");
		when(rs.getString("tenantId")).thenReturn("ap.kurnool");
		when(rs.getBigDecimal("fee")).thenReturn(BigDecimal.valueOf(10.1));
		when(rs.getString("feeCriteria")).thenReturn("10.1");
		when(rs.getLong("fromDate")).thenReturn(Long.valueOf("987456321"));
		when(rs.getLong("toDate")).thenReturn(Long.valueOf("987456321"));
		FeeRowMapper feeRowMapper = new FeeRowMapper();
		Fee fee = feeRowMapper.mapRow(rs, 1);
		assertEquals(fee, getFee());
	}

	private Fee getFee() {
		Fee fee = new Fee();
		fee.setId("2");
		fee.setTenantId("ap.kurnool");
		fee.setFeeCriteria("10.1");
		fee.setFee(BigDecimal.valueOf(10.1));
		fee.setFromDate(Long.valueOf("987456321"));
		fee.setToDate(Long.valueOf("987456321"));
		return fee;
	}

}
