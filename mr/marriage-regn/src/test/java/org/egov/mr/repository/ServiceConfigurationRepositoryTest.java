package org.egov.mr.repository;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.mr.repository.querybuilder.ServiceConfigurationQueryBuilder;
import org.egov.mr.repository.rowmapper.ServiceConfigurationKeyValuesRowMapper;
import org.egov.mr.web.contract.ServiceConfigurationSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ServiceConfigurationRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private ServiceConfigurationQueryBuilder serviceConfigurationQueryBuilder;

	@Mock
	private ServiceConfigurationKeyValuesRowMapper serviceConfigurationKeyValuesRowMapper;

	@InjectMocks
	private ServiceConfigurationRepository serviceConfigurationRepository;

	@Test
	public void test() {
		Map<String, List<String>> value = new HashMap<>();
		List<String> values = new ArrayList<>();
		values.add("value1");
		value.put("key1", values);

		when(serviceConfigurationQueryBuilder.getSelectQuery(any(ServiceConfigurationSearchCriteria.class),
				any(List.class))).thenReturn("select * from table");

		when(jdbcTemplate.query(any(String.class), Matchers.<Object[]>any(),
				any(ServiceConfigurationKeyValuesRowMapper.class))).thenReturn(value);

		List<Integer> ids = new ArrayList<>();
		ids.add(Integer.valueOf("2"));
		ids.add(Integer.valueOf("6"));


		ServiceConfigurationSearchCriteria serviceConfigurationSearchCriteria = new ServiceConfigurationSearchCriteria();
		serviceConfigurationSearchCriteria.setIds(ids);
		serviceConfigurationSearchCriteria.setEffectiveFrom(Long.valueOf("246812648"));
		serviceConfigurationSearchCriteria.setName("service1");
		serviceConfigurationSearchCriteria.setTenantId("ap.kurnool");

		serviceConfigurationRepository.findForCriteria(serviceConfigurationSearchCriteria);
	}

}
