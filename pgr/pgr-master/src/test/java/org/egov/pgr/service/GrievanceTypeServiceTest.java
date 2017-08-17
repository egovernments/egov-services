/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.pgr.domain.model.GrievanceType;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.repository.GrievanceTypeRepository;
import org.egov.pgr.web.contract.ServiceGetRequest;
import org.egov.pgr.web.contract.ServiceRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GrievanceTypeServiceTest {
	
	@Mock
    private GrievanceTypeRepository grievanceTypeRepository;

    @InjectMocks
    private GrievanceTypeService grievanceTypeService;
    
    @Mock
	private PGRProducer pgrProducer;

	@Test
	public void test_should_create_new_service_type() {
		ServiceRequest sRequest = prepareServiceRequest(); 
		when(grievanceTypeRepository.persistServiceType(any(ServiceRequest.class))).thenReturn(sRequest);
		assertTrue(sRequest.equals(grievanceTypeRepository.persistServiceType(sRequest)));
	}
	
	@Test
	public void test_should_update_existing_service_type() { 
		ServiceRequest sRequest = prepareServiceRequest(); 
		when(grievanceTypeRepository.persistModifyServiceType(any(ServiceRequest.class))).thenReturn(sRequest);
		assertTrue(sRequest.equals(grievanceTypeRepository.persistModifyServiceType(sRequest)));
	}
	
	@Test
	public void test_should_fetch_service_type_from_db() {
		List<GrievanceType> grievanceTypeList = prepareServiceTypeList();
		when(grievanceTypeRepository.findForCriteria(any(ServiceGetRequest.class))).thenReturn(grievanceTypeList);
		assertTrue(grievanceTypeList.equals(grievanceTypeRepository.findForCriteria(any(ServiceGetRequest.class))));
		
	}
	
	private ServiceRequest prepareServiceRequest() {
		ServiceRequest serviceRequest = new ServiceRequest();
		RequestInfo rInfo = new RequestInfo(); 
		User user = new User();
		user.setId(1L);
		rInfo.setUserInfo(user);
		serviceRequest.setRequestInfo(rInfo);
		GrievanceType service = new GrievanceType();
		service.setServiceCode("SCODE");
		service.setServiceName("ServiceName");
		service.setActive(true);
		service.setTenantId("default");
		service.setCategory(4);
		service.setSlaHours(24);
		serviceRequest.setService(service);
		return serviceRequest;
	}
	
	private List<GrievanceType> prepareServiceTypeList() {
		List<GrievanceType> grievanceTypeList = new ArrayList<>();
		grievanceTypeList.add(new GrievanceType().builder().serviceCode("SCODEONE")
				.serviceName("ServiceNameOne").active(true).tenantId("default")
				.slaHours(24).category(4).build());
		grievanceTypeList.add(new GrievanceType().builder().serviceCode("SCODETWO")
				.serviceName("ServiceNameTwo").active(true).tenantId("default")
				.slaHours(12).category(2).build());
		return grievanceTypeList;
				
	}

}
