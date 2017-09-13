package org.egov.mr.repository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.mr.model.Fee;
import org.egov.mr.repository.querybuilder.FeeQueryBuilder;
import org.egov.mr.repository.rowmapper.FeeRowMapper;
import org.egov.mr.web.contract.FeeCriteria;
import org.egov.mr.web.contract.FeeRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class FeeRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private FeeQueryBuilder feeQueryBuilder;

	@Mock
	private FeeRowMapper feeRowMapper;

	@InjectMocks
	private FeeRepository feeRepository;

	@Test
	public void testFindForCriteria() {
		Set<String> ids = new HashSet<>();
		ids.add("2");

		FeeCriteria feeCriteria = new FeeCriteria();
		feeCriteria.setFeeCriteria("100.0");
		feeCriteria.setFromDate(Long.valueOf("987456321"));
		feeCriteria.setId(ids);
		feeCriteria.setPageSize(Short.valueOf("2"));
		feeCriteria.setTenantId("ap.kurnool");
		feeCriteria.setToDate(Long.valueOf("987456321"));

		when(jdbcTemplate.query(any(String.class), any(FeeRowMapper.class))).thenReturn(getFeeList());
		feeRepository.findForCriteria(feeCriteria);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateFee() {
		FeeRequest feeRequest =FeeRequest.builder().fees(getFeeList()).build();
		int[] value = new int[] { 2 };

		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(value);
		feeRepository.createFee(feeRequest);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateFee() {
		FeeRequest feeRequest = FeeRequest.builder().fees(getFeeList()).build();
		int[] value = new int[] { 2 };

		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(value);
		feeRepository.updateFee(feeRequest);
	}

	public List<Fee> getFeeList() {
		List<Fee> value = new ArrayList<>();
		Fee fee1 = new Fee();
		fee1.setFee(BigDecimal.valueOf(6.0));
		fee1.setFeeCriteria("100.0");
		fee1.setFromDate(Long.valueOf("987456321"));
		fee1.setId("2");
		fee1.setTenantId("ap.kurnool");
		fee1.setToDate(Long.valueOf("987456321"));
		value.add(fee1);
		return value;
	}
}
