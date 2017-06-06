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
import org.egov.wcms.model.WaterSourceType;
import org.egov.wcms.repository.builder.WaterSourceTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.WaterSourceTypeRowMapper;
import org.egov.wcms.service.WaterSourceTypeService;
import org.egov.wcms.web.contract.WaterSourceTypeGetRequest;
import org.egov.wcms.web.contract.WaterSourceTypeRequest;
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
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(WaterSourceTypeRepository.class)
@WebAppConfiguration
public class WaterSourceTypeRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private WaterSourceTypeQueryBuilder waterSourceTypeQueryBuilder;

    @Mock
    private WaterSourceTypeRowMapper waterSourceTypeRowMapper;

    @MockBean
    private WaterSourceTypeService waterSourceTypeService;

    @MockBean
    private ErrorHandler errHandler;

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @InjectMocks
    private WaterSourceTypeRepository waterSourceTypeRepository;

    @Test
    public void test_Should_Create_WaterSource() {

        final WaterSourceTypeRequest waterSourceTypeRequest = new WaterSourceTypeRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        waterSourceTypeRequest.setRequestInfo(requestInfo);
        final WaterSourceType waterSource = getWaterSourceType();
        waterSourceTypeRequest.setWaterSourceType(waterSource);

        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(waterSourceTypeRequest
                .equals(waterSourceTypeRepository.persistCreateWaterSourceType(waterSourceTypeRequest)));
    }

    @Test
    public void test_Should_Update_WaterSource() {

        final WaterSourceTypeRequest waterSourceTypeRequest = new WaterSourceTypeRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        waterSourceTypeRequest.setRequestInfo(requestInfo);
        final WaterSourceType waterSource = getWaterSourceType();
        waterSourceTypeRequest.setWaterSourceType(waterSource);

        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        assertTrue(waterSourceTypeRequest
                .equals(waterSourceTypeRepository.persistModifyWaterSourceType(waterSourceTypeRequest)));
    }

    @Test
    public void test_Should_Search_WaterSource() {

        final List<WaterSourceType> waterSourceTypes = new ArrayList<>();
        final WaterSourceType waterSource = getWaterSourceType();
        waterSourceTypes.add(waterSource);

        when(waterSourceTypeQueryBuilder.getQuery(any(WaterSourceTypeGetRequest.class), any(List.class))).thenReturn("");
        when(jdbcTemplate.query(any(String.class), any(Object[].class), any(WaterSourceTypeRowMapper.class)))
                .thenReturn(waterSourceTypes);

        assertTrue(waterSourceTypes.equals(waterSourceTypeRepository.findForCriteria(new WaterSourceTypeGetRequest())));
    }

    @Test
    public void test_Inavalid_Find_WaterSource() throws Exception {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final List<WaterSourceType> waterSourceTypes = new ArrayList<>();
        final WaterSourceType waterSource = getWaterSourceType();
        waterSourceTypes.add(waterSource);
        final WaterSourceTypeGetRequest waterSourceGetRequest = Mockito.mock(WaterSourceTypeGetRequest.class);
        when(waterSourceTypeQueryBuilder.getQuery(waterSourceGetRequest, preparedStatementValues)).thenReturn(null);
        when(jdbcTemplate.query("query", preparedStatementValues.toArray(), waterSourceTypeRowMapper))
                .thenReturn(waterSourceTypes);

        assertTrue(!waterSourceTypes.equals(waterSourceTypeRepository.findForCriteria(waterSourceGetRequest)));
    }

    private WaterSourceType getWaterSourceType() {
        final WaterSourceType waterSource = new WaterSourceType();
        waterSource.setTenantId("default");
        waterSource.setCode("10");
        waterSource.setName("water source");
        waterSource.setActive(true);
        waterSource.setDescription("water soucre type ");
        return waterSource;
    }
}
