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

package org.egov.wcms.repositoryTest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.PropertyUsageType;
import org.egov.wcms.repository.PropertyUsageTypeRepository;
import org.egov.wcms.repository.builder.PropertyUsageTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.PropertyUsageTypeRowMapper;
import org.egov.wcms.web.contract.PropertyUsageTypeGetRequest;
import org.egov.wcms.web.contract.PropertyUsageTypeRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class PropertyUsageTypeRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private PropertyUsageTypeRowMapper propUsageTypeMapper;

	@Mock
	private PropertyUsageTypeQueryBuilder propUsageTypeQueryBuilder;

	@InjectMocks
	private PropertyUsageTypeRepository propUsageTypeRepository;

	@Test
	public void test_Should_Create_PropertyUsageType() {
		PropertyUsageTypeRequest propUsageTypeRequest = new PropertyUsageTypeRequest();
		RequestInfo requestInfo = new RequestInfo();
		User user = new User();
		user.setId(2L);
		requestInfo.setUserInfo(user);
		propUsageTypeRequest.setRequestInfo(requestInfo);
		PropertyUsageType propUsageType = Mockito.mock(PropertyUsageType.class);
		propUsageTypeRequest.setPropertyUsageType(propUsageType);

		when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
		assertTrue(propUsageTypeRequest.equals(propUsageTypeRepository.persistCreateUsageType(propUsageTypeRequest)));
	}
	
	@Test
	public void test_Should_Create_PropertyUsageType_NotNullCheck() {
		
		PropertyUsageTypeRequest propUsageTypeRequest = getPropertyUsageType();
		Object[] obj = new Object[] {propUsageTypeRequest.getPropertyUsageType().getId(), propUsageTypeRequest.getPropertyUsageType().getPropertyType(), propUsageTypeRequest.getPropertyUsageType().getUsageType(), propUsageTypeRequest.getPropertyUsageType().isActive(),
        		propUsageTypeRequest.getPropertyUsageType().getTenantId(), new Date(new java.util.Date().getTime()), propUsageTypeRequest.getRequestInfo().getUserInfo().getId() };
	    when(jdbcTemplate.update("query", obj)).thenReturn(1);
		assertNotNull(propUsageTypeRepository.persistCreateUsageType(propUsageTypeRequest));
	}

	@Test
	public void test_Should_Find_PropertyUsageType_Valid() {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		PropertyUsageTypeGetRequest propUsageTypeGetRequest = Mockito.mock(PropertyUsageTypeGetRequest.class);
		String queryString = "MyQuery" ;
		when(propUsageTypeQueryBuilder.getQuery(propUsageTypeGetRequest, preparedStatementValues)).thenReturn(queryString);
		List<PropertyUsageType> propUsageTypes = new ArrayList<>();
		when(jdbcTemplate.query(queryString, preparedStatementValues.toArray(), propUsageTypeMapper))
				.thenReturn(propUsageTypes);

		assertTrue(
				propUsageTypes.equals(propUsageTypeRepository.getPropertyUsageType(propUsageTypeGetRequest)));
	}

	@Test
	public void test_Should_Find_PropertyUsageType_Invalid() {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		PropertyUsageTypeGetRequest propUsageTypeGetRequest = Mockito.mock(PropertyUsageTypeGetRequest.class);
		String queryString = "MyQuery" ;
		when(propUsageTypeQueryBuilder.getQuery(propUsageTypeGetRequest, preparedStatementValues)).thenReturn(null);
		List<PropertyUsageType> propUsageTypes = new ArrayList<>();
		when(jdbcTemplate.query(queryString, preparedStatementValues.toArray(), propUsageTypeMapper))
				.thenReturn(propUsageTypes);

		assertTrue(
				propUsageTypes.equals(propUsageTypeRepository.getPropertyUsageType(propUsageTypeGetRequest)));
	}
	
	private PropertyUsageTypeRequest getPropertyUsageType() {
		PropertyUsageTypeRequest propUsageTypeRequest = new PropertyUsageTypeRequest();
		PropertyUsageType propUsageType = new PropertyUsageType();
		propUsageType.setActive(true);
		propUsageType.setId(2L);
		propUsageType.setPropertyType("RES");
		propUsageType.setTenantId("DEFAULT");
		propUsageType.setUsageType("COM");
		User user = new User();
		user.setId(2L);
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setUserInfo(user);
		propUsageTypeRequest.setPropertyUsageType(propUsageType);
		propUsageTypeRequest.setRequestInfo(requestInfo);
		return propUsageTypeRequest;
	}
}
