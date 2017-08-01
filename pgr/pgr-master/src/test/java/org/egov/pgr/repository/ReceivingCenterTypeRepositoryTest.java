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

package org.egov.pgr.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.pgr.domain.model.ReceivingCenterType;
import org.egov.pgr.repository.builder.ReceivingCenterTypeQueryBuilder;
import org.egov.pgr.repository.rowmapper.ReceivingCenterTypeRowMapper;
import org.egov.pgr.web.contract.ReceivingCenterTypeGetReq;
import org.egov.pgr.web.contract.ReceivingCenterTypeReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ReceivingCenterTypeRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ReceivingCenterTypeQueryBuilder receivingCenterQueryBuilder;

    @Mock
    private ReceivingCenterTypeRowMapper receivingCenterRowMapper;

    @InjectMocks
    private ReceivingCenterTypeRepository receivingCenterRepository;

    @Test
    public void test_Should_Create_ReceivingCenter_Valid() {
        final ReceivingCenterTypeReq receivingCenterRequest = getReceivingCenterRequest();
        final ReceivingCenterType receivingCenter = receivingCenterRequest.getCenterType();
        receivingCenter.getCode();
        receivingCenter.getName();
        receivingCenter.getDescription();
        receivingCenter.getActive();
        Long.valueOf(receivingCenterRequest.getRequestInfo().getUserInfo().getId());
        Long.valueOf(receivingCenterRequest.getRequestInfo().getUserInfo().getId());
        new Date(new java.util.Date().getTime());
        new Date(new java.util.Date().getTime());
        receivingCenter.getTenantId();
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(receivingCenterRequest.equals(receivingCenterRepository.persistReceivingCenterType(receivingCenterRequest)));
    }

    @Test
    public void test_Should_Create_ReceivingCenter_Invalid() {
        final ReceivingCenterTypeReq receivingCenterReq = getReceivingCenterRequest();
        final ReceivingCenterType receivingCenter = receivingCenterReq.getCenterType();
       
        receivingCenter.getCode();
        receivingCenter.getName();
        receivingCenter.getDescription();
        receivingCenter.getActive();
        Long.valueOf(receivingCenterReq.getRequestInfo().getUserInfo().getId());
        Long.valueOf(receivingCenterReq.getRequestInfo().getUserInfo().getId());
        new Date(new java.util.Date().getTime());
        new Date(new java.util.Date().getTime());
        receivingCenter.getTenantId();
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(!receivingCenter.equals(receivingCenterRepository.persistReceivingCenterType(receivingCenterReq)));
    }

    @Test
    public void test_Should_Find_ReceivingCenter_Valid() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final ReceivingCenterTypeGetReq receivingCenterGetReq = Mockito.mock(ReceivingCenterTypeGetReq.class);
        final String queryString = "MyQuery";
        when(receivingCenterQueryBuilder.getQuery(receivingCenterGetReq, preparedStatementValues)).thenReturn(queryString);
        final List< ReceivingCenterType> connectionCategories = new ArrayList<>();
        when(jdbcTemplate.query(queryString, preparedStatementValues.toArray(), receivingCenterRowMapper))
                .thenReturn(connectionCategories);

        assertTrue(
                connectionCategories.equals(receivingCenterRepository.getAllReceivingCenterTypes(receivingCenterGetReq)));
    }

    private ReceivingCenterTypeReq getReceivingCenterRequest() {
        final ReceivingCenterTypeReq receivingCenterReq = new ReceivingCenterTypeReq();
        final ReceivingCenterType receivingCenter = new ReceivingCenterType();
        receivingCenter.setCode("23");
        receivingCenter.setName("New Category");
        receivingCenter.setDescription("New Category of Connection");
        receivingCenter.setActive(true);
        final RequestInfo requestInfo = new RequestInfo();
        final User newUser = new User();
        newUser.setId(2L);
        requestInfo.setUserInfo(newUser);
        receivingCenterReq.setRequestInfo(requestInfo);
        receivingCenterReq.setCenterType(receivingCenter);
        return receivingCenterReq;
    }
}
