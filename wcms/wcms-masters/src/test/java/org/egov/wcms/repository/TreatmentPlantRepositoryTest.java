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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.TreatmentPlant;
import org.egov.wcms.repository.builder.TreatmentPlantQueryBuilder;
import org.egov.wcms.repository.rowmapper.TreatmentPlantRowMapper;
import org.egov.wcms.service.RestWaterExternalMasterService;
import org.egov.wcms.web.contract.TreatmentPlantGetRequest;
import org.egov.wcms.web.contract.TreatmentPlantRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(TreatmentPlantRepository.class)
public class TreatmentPlantRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private TreatmentPlantRowMapper treatmentPlantRowMapper;

    @Mock
    private TreatmentPlantQueryBuilder treatmentPlantQueryBuilder;

    @InjectMocks
    private TreatmentPlantRepository treatmentPlantRepository;

    @Mock
    private RestWaterExternalMasterService restExternalMasterService;

    @SuppressWarnings("unchecked")
    @Test
    public void test_Should_Search_TreatmentPlant() {
        final List<TreatmentPlant> treatmentPlantList = new ArrayList<>();
        final TreatmentPlant treatmentPlant = getTreatmentPlant();
        treatmentPlantList.add(treatmentPlant);
        when(namedParameterJdbcTemplate.queryForObject(any(String.class), anyMap(), eq(Long.class))).thenReturn(2L);
        when(namedParameterJdbcTemplate.query(any(String.class), anyMap(), any(TreatmentPlantRowMapper.class)))
                .thenReturn(treatmentPlantList);
        when(namedParameterJdbcTemplate.queryForObject(any(String.class), anyMap(), eq(String.class)))
                .thenReturn("abcd");
        assertTrue(treatmentPlantRepository.findForCriteria(getTreatmentPlantGetRequest()).get(0)
                .getStorageReservoirName().equals("abcd"));
    }

    private TreatmentPlantGetRequest getTreatmentPlantGetRequest() {
        // TODO Auto-generated method stub
        return TreatmentPlantGetRequest.builder().tenantId("default").code("12").name("test").capacity(2d)
                .plantType("abcd").location("test").build();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_Should_Create_TreatmentPlant() {
        final TreatmentPlantRequest treatmentPlantRequest = new TreatmentPlantRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        treatmentPlantRequest.setRequestInfo(requestInfo);
        final List<TreatmentPlant> treatmentPlantList = new ArrayList<>();
        final TreatmentPlant treatmentPlant = getTreatmentPlant();
        treatmentPlantList.add(treatmentPlant);
        treatmentPlantRequest.setTreatmentPlants(treatmentPlantList);
        final TreatmentPlantRequest treatmentPlantReq = treatmentPlantRepository
                .create(treatmentPlantRequest);
        when(namedParameterJdbcTemplate.queryForObject(any(String.class), anyMap(), eq(Long.class))).thenReturn(2L);
        assertThat(treatmentPlantReq.getTreatmentPlants().size()).isEqualTo(1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_Should_Update_TreatmentPlant() {
        final TreatmentPlantRequest treatmentPlantRequest = new TreatmentPlantRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        treatmentPlantRequest.setRequestInfo(requestInfo);
        final List<TreatmentPlant> treatmentPlantList = new ArrayList<>();
        final TreatmentPlant treatmentPlant = getTreatmentPlant();
        treatmentPlantList.add(treatmentPlant);
        treatmentPlantRequest.setTreatmentPlants(treatmentPlantList);
        final TreatmentPlantRequest treatmentPlantReq = treatmentPlantRepository
                .update(treatmentPlantRequest);
        when(namedParameterJdbcTemplate.queryForObject(any(String.class), anyMap(), eq(Long.class))).thenReturn(2L);
        assertThat(treatmentPlantReq.getTreatmentPlants().size()).isEqualTo(1);
    }

    private TreatmentPlant getTreatmentPlant() {
        final TreatmentPlant treatmentPlant = new TreatmentPlant();
        treatmentPlant.setTenantId("default");
        treatmentPlant.setName("test");
        treatmentPlant.setCode("12");
        treatmentPlant.setLocation("test");
        treatmentPlant.setCapacity(2d);
        treatmentPlant.setPlantType("test");
        treatmentPlant.setStorageReservoirId(2L);
        treatmentPlant.setPlantType("abcd");
        return treatmentPlant;
    }

}