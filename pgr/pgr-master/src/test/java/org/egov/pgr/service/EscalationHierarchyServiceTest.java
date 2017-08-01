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
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.pgr.domain.model.AuditDetails;
import org.egov.pgr.domain.model.EscalationHierarchy;
import org.egov.pgr.producers.PGRProducer;
import org.egov.pgr.repository.EscalationHierarchyRespository;
import org.egov.pgr.web.contract.EscalationHierarchyGetReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EscalationHierarchyServiceTest {
	
	@Mock
    private EscalationHierarchyRespository escalationHierarchyRepository;

    @Mock
    private PGRProducer pgrProducer;

    @InjectMocks
    private EscalationHierarchyService escalationHierarchyService;

	@Test
	public void test_should_create_escalation_hierarchy_using_request() {
		List<EscalationHierarchy> escHierarchyList = prepareEscalationHierarchyList();
		when(escalationHierarchyRepository.persistEscalationHierarchy(anyListOf(EscalationHierarchy.class),any(AuditDetails.class))).thenReturn(escHierarchyList);
		assertTrue(escHierarchyList.equals(escalationHierarchyRepository.persistEscalationHierarchy(escHierarchyList,new AuditDetails().builder().createdBy(1L).build())));
	}
	
	@Test
	public void test_should_update_escalation_hierarchy_using_request() {
		List<EscalationHierarchy> escHierarchyList = prepareEscalationHierarchyList();
		when(escalationHierarchyRepository.persistEscalationHierarchy(anyListOf(EscalationHierarchy.class),any(AuditDetails.class))).thenReturn(escHierarchyList);
		assertTrue(escHierarchyList.equals(escalationHierarchyRepository.persistEscalationHierarchy(escHierarchyList,new AuditDetails().builder().createdBy(1L).build())));
	}
	
	@Test
	public void test_should_fetch_all_escalation_hierarchy() {
		List<EscalationHierarchy> escHierarchyList = prepareEscalationHierarchyList();
		when(escalationHierarchyRepository.getAllEscalationHierarchy(any(EscalationHierarchyGetReq.class))).thenReturn(escHierarchyList);
		assertTrue(escHierarchyList.equals(escalationHierarchyRepository.getAllEscalationHierarchy(prepareGetRequest())));
	}
	
	@Test
	public void test_should_fetch_specific_escalation_hierarchy_record_for_from_position() {
		List<EscalationHierarchy> escHierarchyList = prepareEscalationHierarchySingleRecordList();
		when(escalationHierarchyRepository.getAllEscalationHierarchy(any(EscalationHierarchyGetReq.class))).thenReturn(escHierarchyList);
		assertTrue(escHierarchyList.equals(escalationHierarchyRepository.getAllEscalationHierarchy(prepareSpecificGetRequest())));
		assertTrue(escalationHierarchyRepository.getAllEscalationHierarchy(prepareSpecificGetRequest()).size() == 1);
	}
	
	private List<EscalationHierarchy> prepareEscalationHierarchyList() {
		List<EscalationHierarchy> escHierarchyList = new ArrayList<>();
		EscalationHierarchy escHierarchyOne = new EscalationHierarchy().builder()
				.tenantId("default")
				.fromPosition(1L)
				.toPosition(9L)
				.serviceCode("SRVCODEONE")
				.auditDetails(new AuditDetails().builder().createdBy(1L).build())
				.build();
		EscalationHierarchy escHierarchyTwo = new EscalationHierarchy().builder()
				.tenantId("default")
				.fromPosition(2L)
				.toPosition(8L)
				.serviceCode("SRVCODETWO")
				.auditDetails(new AuditDetails().builder().createdBy(1L).build())
				.build();
		escHierarchyList.add(escHierarchyOne);
		escHierarchyList.add(escHierarchyTwo);
		return escHierarchyList;
	}
	
	private List<EscalationHierarchy> prepareEscalationHierarchySingleRecordList() {
		List<EscalationHierarchy> escHierarchyList = new ArrayList<>();
		EscalationHierarchy escHierarchyTwo = new EscalationHierarchy().builder()
				.tenantId("default")
				.fromPosition(2L)
				.toPosition(8L)
				.serviceCode("SRVCODETWO")
				.auditDetails(new AuditDetails().builder().createdBy(1L).build())
				.build();
		escHierarchyList.add(escHierarchyTwo);
		return escHierarchyList;
	}
	
	private EscalationHierarchyGetReq prepareGetRequest() {
		return new EscalationHierarchyGetReq().builder()
				.tenantId("default")
				.build();
	}
	
	private EscalationHierarchyGetReq prepareSpecificGetRequest() {
		List<String> serviceCodeList = new ArrayList<>();
		serviceCodeList.add("SRVCODETWO");
		return new EscalationHierarchyGetReq().builder()
				.tenantId("default")
				.serviceCode(serviceCodeList)
				.build();
	}

}
