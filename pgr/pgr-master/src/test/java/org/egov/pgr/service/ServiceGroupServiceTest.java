package org.egov.pgr.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.domain.model.AuditDetails;
import org.egov.pgr.domain.model.ServiceGroup;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.repository.ServiceGroupRepository;
import org.egov.pgr.web.contract.ServiceGroupGetRequest;
import org.egov.pgr.web.contract.ServiceGroupRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ServiceGroupServiceTest {
	
	@Mock
    private ServiceGroupRepository serviceGroupRepository;

    @InjectMocks
    private ServiceGroupService serviceGroupService;
    
    @Mock
	private PGRProducer pgrProducer;

	@Test
	public void test_should_insert_service_group() {
		ServiceGroupRequest serviceGroupRequest = prepareServiceGroupRequest();
		when(serviceGroupRepository.persistCreateServiceGroup(any(ServiceGroupRequest.class))).thenReturn(serviceGroupRequest);
		assertTrue(serviceGroupRequest.equals(serviceGroupRepository.persistCreateServiceGroup(any(ServiceGroupRequest.class))));
	}
	
	@Test
	public void test_should_create_service_group() {
		ServiceGroupRequest serviceGroupRequest = prepareServiceGroupRequest();
		serviceGroupService.create(serviceGroupRequest);
		verify(serviceGroupRepository).persistCreateServiceGroup(serviceGroupRequest);
	}
	
	@Test
	public void test_should_update_service_group() {
		ServiceGroupRequest serviceGroupRequest = prepareServiceGroupRequest();
		when(serviceGroupRepository.persistUpdateServiceGroup(any(ServiceGroupRequest.class))).thenReturn(serviceGroupRequest);
		assertTrue(serviceGroupRequest.equals(serviceGroupRepository.persistUpdateServiceGroup(any(ServiceGroupRequest.class))));
	}
	
	@Test
	public void test_should_update_service_group_details() {
		ServiceGroupRequest serviceGroupRequest = prepareServiceGroupRequest();
		serviceGroupService.update(serviceGroupRequest);
		verify(serviceGroupRepository).persistUpdateServiceGroup(serviceGroupRequest);
	}
	
	@Test
	public void test_should_return_list_of_service_groups() {
		List<ServiceGroup> serviceGroupList = prepareServiceGroupObjectList();
		when(serviceGroupRepository.getAllServiceGroup(any(ServiceGroupGetRequest.class))).thenReturn(serviceGroupList);
		assertTrue(serviceGroupList.equals(serviceGroupRepository.getAllServiceGroup(any(ServiceGroupGetRequest.class))));
	}
	
	
	
	private ServiceGroupRequest prepareServiceGroupRequest(){
		RequestInfo requestInfo = Mockito.mock(RequestInfo.class);
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(1L);
		ServiceGroup serviceGroup = new ServiceGroup();
		serviceGroup.setAuditDetails(auditDetails);
		serviceGroup.setCode("SG01");
		serviceGroup.setDescription("1st Service Group");
		serviceGroup.setId(1L);
		serviceGroup.setName("ServiceGroup01");
		serviceGroup.setTenantId("default");
		serviceGroup.setVersion(1);
		ServiceGroupRequest request = new ServiceGroupRequest();
		request.setRequestInfo(requestInfo);
		request.setServiceGroup(serviceGroup);
		return request;
	}
	
	
	private List<ServiceGroup> prepareServiceGroupObjectList() {
		ServiceGroup sg01 = new ServiceGroup();
		sg01.setCode("SG001");
		sg01.setId(9L);
		sg01.setDescription("SG0019L");
		sg01.setName("SerGro001");
		sg01.setTenantId("default");
		sg01.setVersion(8);
		ServiceGroup sg02 = new ServiceGroup();
		sg02.setCode("SG002");
		sg02.setId(7L);
		sg02.setDescription("SG0027L");
		sg02.setName("SerGro002");
		sg02.setTenantId("default");
		sg02.setVersion(8);
		List<ServiceGroup> serviceGroupList = new ArrayList<>();
		serviceGroupList.add(sg01);
		serviceGroupList.add(sg02);
		return serviceGroupList;
	}

}
