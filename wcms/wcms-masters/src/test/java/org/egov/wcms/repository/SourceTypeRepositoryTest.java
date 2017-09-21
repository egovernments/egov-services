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
package org.egov.wcms.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.SourceType;
import org.egov.wcms.repository.builder.SourceTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.SourceTypeRowMapper;
import org.egov.wcms.service.SourceTypeService;
import org.egov.wcms.web.contract.SourceTypeGetRequest;
import org.egov.wcms.web.contract.SourceTypeRequest;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(SourceTypeRepository.class)
@WebAppConfiguration
public class SourceTypeRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SourceTypeQueryBuilder waterSourceTypeQueryBuilder;

    @Mock
    private SourceTypeRowMapper waterSourceTypeRowMapper;

    @MockBean
    private SourceTypeService waterSourceTypeService;

    @MockBean
    private ErrorHandler errHandler;

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @InjectMocks
    private SourceTypeRepository waterSourceTypeRepository;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    public void test_Should_Create_WaterSource() {

        final SourceTypeRequest waterSourceTypeRequest = new SourceTypeRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        waterSourceTypeRequest.setRequestInfo(requestInfo);
        final List<SourceType> waterSourceList = new ArrayList<>();
        waterSourceList.add(getWaterSourceType());
        waterSourceTypeRequest.setSourceTypes(waterSourceList);

        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(waterSourceTypeRequest
                .equals(waterSourceTypeRepository.create(waterSourceTypeRequest)));
    }

    @Test
    public void test_Should_Update_WaterSource() {

        final SourceTypeRequest waterSourceTypeRequest = new SourceTypeRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        waterSourceTypeRequest.setRequestInfo(requestInfo);
        final List<SourceType> waterSourceList = new ArrayList<>();
        waterSourceList.add(getWaterSourceType());
        waterSourceTypeRequest.setSourceTypes(waterSourceList);

        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(waterSourceTypeRequest
                .equals(waterSourceTypeRepository.update(waterSourceTypeRequest)));
    }

    @Test
    public void test_Should_Search_WaterSource() {

        final List<SourceType> waterSourceTypes = new ArrayList<>();
        final SourceType waterSource = getWaterSourceType();
        waterSourceTypes.add(waterSource);

        when(waterSourceTypeQueryBuilder.getQuery(any(SourceTypeGetRequest.class), any(List.class))).thenReturn("");
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(SourceTypeRowMapper.class)))
                .thenReturn(waterSourceTypes);

        assertTrue(waterSourceTypes.equals(waterSourceTypeRepository.findForCriteria(new SourceTypeGetRequest())));
    }

    @Test
    public void test_Inavalid_Find_WaterSource() throws Exception {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final List<SourceType> waterSourceTypes = new ArrayList<>();
        final SourceType waterSource = getWaterSourceType();
        waterSourceTypes.add(waterSource);
        final SourceTypeGetRequest waterSourceGetRequest = Mockito.mock(SourceTypeGetRequest.class);
        when(waterSourceTypeQueryBuilder.getQuery(waterSourceGetRequest, preparedStatementValues)).thenReturn(null);
        when(jdbcTemplate.query("query", preparedStatementValues.toArray(), waterSourceTypeRowMapper))
                .thenReturn(waterSourceTypes);

        assertTrue(!waterSourceTypes.equals(waterSourceTypeRepository.findForCriteria(waterSourceGetRequest)));
    }

    private SourceType getWaterSourceType() {
        final SourceType waterSource = new SourceType();
        waterSource.setTenantId("default");
        waterSource.setCode("10");
        waterSource.setName("water source");
        waterSource.setActive(true);
        waterSource.setDescription("water soucre type ");
        return waterSource;
    }
}
