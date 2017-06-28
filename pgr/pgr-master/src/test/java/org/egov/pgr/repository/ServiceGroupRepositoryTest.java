package org.egov.pgr.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.pgr.model.ServiceGroup;
import org.egov.pgr.repository.builder.ServiceGroupQueryBuilder;
import org.egov.pgr.repository.rowmapper.ServiceGroupRowMapper;
import org.egov.pgr.web.contract.ServiceGroupGetRequest;
import org.egov.pgr.web.contract.ServiceGroupRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ServiceGroupRepositoryTest {
	
	@Mock
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private ServiceGroupQueryBuilder serviceGroupQueryBuilder;

    @Mock
    private ServiceGroupRowMapper serviceGroupRowMapper;

    @InjectMocks
    private ServiceGroupRepository serviceGroupRepository;

	@Test
	public void test_should_persist_service_groups() {
		ServiceGroupRequest serviceGroupReq = prepareServiceGroupRequest(); 
		when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(serviceGroupReq.equals(serviceGroupRepository.persistCreateServiceGroup(serviceGroupReq)));
	}
	
	@Test 
	public void test_should_update_service_groups() {
		ServiceGroupRequest serviceGroupReq = prepareServiceGroupRequest(); 
		when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(serviceGroupReq.equals(serviceGroupRepository.persistUpdateServiceGroup(serviceGroupReq)));
	}
	
	@Test
	public void test_should_fetch_all_service_groups_for_given_tenantid() {
		final List<Object> preparedStatementValues = new ArrayList<>();
		ServiceGroupGetRequest serviceGroupGetRequest = prepareServiceGroupGetRequest();
		String queryString = "QueryForFetch";
		List<ServiceGroup> serviceGroups = new ArrayList<>();
		when(jdbcTemplate.query(queryString, preparedStatementValues.toArray(), serviceGroupRowMapper))
				.thenReturn(serviceGroups);

		assertTrue(serviceGroups.equals(serviceGroupRepository.getAllServiceGroup(serviceGroupGetRequest)));
	}
	
	private ServiceGroupGetRequest prepareServiceGroupGetRequest() {
		ServiceGroupGetRequest serviceGroupGetReq = new ServiceGroupGetRequest();
		serviceGroupGetReq.setTenantId("default");
		return serviceGroupGetReq;
	}
	
	private ServiceGroupRequest prepareServiceGroupRequest() {
		ServiceGroupRequest serviceGroupReq = new ServiceGroupRequest();
		ServiceGroup serviceGroup = new ServiceGroup();
		serviceGroup.setCode("SG01");
		serviceGroup.setDescription("1st Service Group");
		serviceGroup.setId(1L);
		serviceGroup.setName("ServiceGroup01");
		serviceGroup.setTenantId("default");
		serviceGroup.setVersion(1);
		RequestInfo reqInfo = new RequestInfo();
		User user = new User();
		user.setId(1L);
		reqInfo.setUserInfo(user);
		serviceGroupReq.setRequestInfo(reqInfo);
		serviceGroupReq.setServiceGroup(serviceGroup);
		return serviceGroupReq;
	}

}
