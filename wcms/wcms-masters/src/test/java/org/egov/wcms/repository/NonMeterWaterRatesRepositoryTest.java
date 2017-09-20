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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.NonMeterWaterRates;
import org.egov.wcms.repository.builder.NonMeterWaterRatesQueryBuilder;
import org.egov.wcms.repository.rowmapper.NonMeterWaterRatesRowMapper;
import org.egov.wcms.service.RestWaterExternalMasterService;
import org.egov.wcms.web.contract.NonMeterWaterRatesReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(NonMeterWaterRatesRepository.class)
public class NonMeterWaterRatesRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private NonMeterWaterRatesRowMapper nonMeterWaterRatesRowMapper;

    @Mock
    private NonMeterWaterRatesQueryBuilder nonMeterWaterRatesQueryBuilder;

    @InjectMocks
    private NonMeterWaterRatesRepository nonMeterWaterRatesRepository;

    @Mock
    private RestWaterExternalMasterService restExternalMasterService;

    /*
     * @Test public void test_Should_Search_NonMeterWaterRates() { new ArrayList<>(); final List<NonMeterWaterRates>
     * nonMeterWaterRatesList = new ArrayList<>(); final NonMeterWaterRates nonMeterWaterRates = getNonMeterWaterRates();
     * nonMeterWaterRatesList.add(nonMeterWaterRates); final NonMeterWaterRatesGetReq nonMeterWaterRatesGetRequest =
     * getNonMeterWaterRatesGetReq(); when(namedParameterJdbcTemplate.query(any(String.class), anyMap(),
     * any(NonMeterWaterRatesRowMapper.class))) .thenReturn(nonMeterWaterRatesList);
     * assertTrue(nonMeterWaterRatesRepository.findForCriteria(nonMeterWaterRatesGetRequest).get(0).getCode()
     * .equals(nonMeterWaterRatesList.get(0).getCode())); } private NonMeterWaterRatesGetReq getNonMeterWaterRatesGetReq() {
     * return NonMeterWaterRatesGetReq.builder().code("12").sourceTypeId(2l).pipeSizeId(2l) .tenantId("default").build(); }
     */
    @Test
    public void test_Should_Create_NonMeterWaterRates() {

        final NonMeterWaterRatesReq nonMeterWaterRatesReq = new NonMeterWaterRatesReq();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        nonMeterWaterRatesReq.setRequestInfo(requestInfo);
        final List<NonMeterWaterRates> nonMeterWaterRatesList = new ArrayList<>();
        final NonMeterWaterRates nonMeterWaterRates = getNonMeterWaterRates();
        nonMeterWaterRatesList.add(nonMeterWaterRates);
        nonMeterWaterRatesReq.setNonMeterWaterRates(nonMeterWaterRatesList);
        final NonMeterWaterRatesReq nonMeterWaterRatesRequest = nonMeterWaterRatesRepository
                .create(nonMeterWaterRatesReq);
        when(namedParameterJdbcTemplate.queryForObject(any(String.class), anyMap(), eq(Long.class))).thenReturn(2L);
        assertThat(nonMeterWaterRatesRequest.getNonMeterWaterRates().size()).isEqualTo(1);

    }

    @Test
    public void test_Should_Update_NonMeterWaterRates() {

        final NonMeterWaterRatesReq nonMeterWaterRatesReq = new NonMeterWaterRatesReq();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        nonMeterWaterRatesReq.setRequestInfo(requestInfo);
        final List<NonMeterWaterRates> nonMeterWaterRatesList = new ArrayList<>();
        final NonMeterWaterRates nonMeterWaterRates = getNonMeterWaterRates();
        nonMeterWaterRatesList.add(nonMeterWaterRates);
        nonMeterWaterRatesReq.setNonMeterWaterRates(nonMeterWaterRatesList);
        final NonMeterWaterRatesReq nonMeterWaterRatesRequest = nonMeterWaterRatesRepository
                .update(nonMeterWaterRatesReq);
        when(namedParameterJdbcTemplate.queryForObject(any(String.class), anyMap(), eq(Long.class))).thenReturn(2L);
        assertThat(nonMeterWaterRatesRequest.getNonMeterWaterRates().size()).isEqualTo(1);
    }

    private NonMeterWaterRates getNonMeterWaterRates() {
        final NonMeterWaterRates nonMeterWaterRates = new NonMeterWaterRates();
        nonMeterWaterRates.setTenantId("default");
        nonMeterWaterRates.setBillingType("METERED");
        nonMeterWaterRates.setConnectionType("PERMENT");
        nonMeterWaterRates.setCode("12");
        nonMeterWaterRates.setSourceTypeId(2l);
        nonMeterWaterRates.setUsageTypeId(1l);
        nonMeterWaterRates.setPipeSizeId(2l);
        nonMeterWaterRates.setFromDate(123456654l);
        nonMeterWaterRates.setAmount(200D);
        nonMeterWaterRates.setNoOfTaps(2l);
        nonMeterWaterRates.setActive(true);
        return nonMeterWaterRates;
    }

}
