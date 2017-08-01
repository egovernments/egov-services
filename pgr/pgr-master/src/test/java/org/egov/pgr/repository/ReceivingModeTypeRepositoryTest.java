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
import org.egov.pgr.domain.model.ReceivingModeType;
import org.egov.pgr.repository.builder.ReceivingModeTypeQueryBuilder;
import org.egov.pgr.repository.rowmapper.ReceivingModeTypeRowMapper;
import org.egov.pgr.web.contract.ReceivingModeTypeGetReq;
import org.egov.pgr.web.contract.ReceivingModeTypeReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ReceivingModeTypeRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ReceivingModeTypeQueryBuilder receivingModeQueryBuilder;

    @Mock
    private ReceivingModeTypeRowMapper receivingModeRowMapper;

    @InjectMocks
    private ReceivingModeTypeRepository receivingModeRepository;

    @Test
    public void test_Should_Create_ReceivingMode_Valid() {
        final ReceivingModeTypeReq receivingModeRequest = getReceivingModeRequest();
        final ReceivingModeType receivingMode = receivingModeRequest.getModeType();
        receivingMode.getCode();
        receivingMode.getName();
        receivingMode.getDescription();
        receivingMode.getActive();
        Long.valueOf(receivingModeRequest.getRequestInfo().getUserInfo().getId());
        Long.valueOf(receivingModeRequest.getRequestInfo().getUserInfo().getId());
        new Date(new java.util.Date().getTime());
        new Date(new java.util.Date().getTime());
        receivingMode.getTenantId();
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(receivingModeRequest.equals(receivingModeRepository.persistReceivingModeType(receivingModeRequest)));
    }

    @Test
    public void test_Should_Create_ReceivingMode_Invalid() {
        final ReceivingModeTypeReq receivingModeReq = getReceivingModeRequest();
        final ReceivingModeType receivingMode = receivingModeReq.getModeType();
       
        receivingMode.getCode();
        receivingMode.getName();
        receivingMode.getDescription();
        receivingMode.getActive();
        Long.valueOf(receivingModeReq.getRequestInfo().getUserInfo().getId());
        Long.valueOf(receivingModeReq.getRequestInfo().getUserInfo().getId());
        new Date(new java.util.Date().getTime());
        new Date(new java.util.Date().getTime());
        receivingMode.getTenantId();
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(!receivingMode.equals(receivingModeRepository.persistReceivingModeType(receivingModeReq)));
    }

    @Test
    public void test_Should_Find_ReceivingMode_Valid() {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final ReceivingModeTypeGetReq receivingModeGetReq = Mockito.mock(ReceivingModeTypeGetReq.class);
        final String queryString = "MyQuery";
        when(receivingModeQueryBuilder.getQuery(receivingModeGetReq, preparedStatementValues)).thenReturn(queryString);
        final List< ReceivingModeType> connectionCategories = new ArrayList<>();
        when(jdbcTemplate.query(queryString, preparedStatementValues.toArray(), receivingModeRowMapper))
                .thenReturn(connectionCategories);

        assertTrue(
                connectionCategories.equals(receivingModeRepository.getAllReceivingModeTypes(receivingModeGetReq)));
    }

    private ReceivingModeTypeReq getReceivingModeRequest() {
        final ReceivingModeTypeReq receivingModeReq = new ReceivingModeTypeReq();
        final ReceivingModeType receivingMode = new ReceivingModeType();
        receivingMode.setCode("23");
        receivingMode.setName("New Category");
        receivingMode.setDescription("New Category of Connection");
        receivingMode.setActive(true);
        final RequestInfo requestInfo = new RequestInfo();
        final User newUser = new User();
        newUser.setId(2L);
        requestInfo.setUserInfo(newUser);
        receivingModeReq.setRequestInfo(requestInfo);
        receivingModeReq.setModeType(receivingMode);
        return receivingModeReq;
    }
}
