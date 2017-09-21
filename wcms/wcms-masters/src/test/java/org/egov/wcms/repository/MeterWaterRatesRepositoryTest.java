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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.model.MeterWaterRates;
import org.egov.wcms.model.Slab;
import org.egov.wcms.repository.builder.MeterWaterRatesQueryBuilder;
import org.egov.wcms.repository.rowmapper.MeterWaterRatesRowMapper;
import org.egov.wcms.service.RestWaterExternalMasterService;
import org.egov.wcms.web.contract.MeterWaterRatesGetRequest;
import org.egov.wcms.web.contract.MeterWaterRatesRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(MeterWaterRatesRepository.class)
public class MeterWaterRatesRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private MeterWaterRatesRowMapper meterWaterRatesRowMapper;

    @Mock
    private MeterWaterRatesQueryBuilder meterWaterRatesQueryBuilder;

    @InjectMocks
    private MeterWaterRatesRepository meterWaterRatesRepository;

    @Mock
    private RestWaterExternalMasterService restExternalMasterService;

    @Test
    public void test_Should_Search_MeterWaterRates() {
        final Map<String, Object> preparedStatementValues = new HashMap<>();

        final MeterWaterRatesGetRequest meterWaterRatesGetRequest = Mockito.mock(MeterWaterRatesGetRequest.class);
        final String queryString = "MyQuery";
        when(meterWaterRatesQueryBuilder.getQuery(meterWaterRatesGetRequest, preparedStatementValues))
                .thenReturn(queryString);
        final List<MeterWaterRates> meterWaterRates = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(queryString, preparedStatementValues, meterWaterRatesRowMapper))
                .thenReturn(meterWaterRates);

        assertTrue(meterWaterRates.equals(meterWaterRatesRepository.findForCriteria(meterWaterRatesGetRequest)));

    }

    @Test
    public void test_Inavalid_Find_MeterWaterRates() throws Exception {
        final Map<String, Object> preparedStatementValues = new HashMap<>();
        final MeterWaterRatesGetRequest meterWaterRatesGetRequest = Mockito.mock(MeterWaterRatesGetRequest.class);
        final String queryString = "MyQuery";
        when(meterWaterRatesQueryBuilder.getQuery(meterWaterRatesGetRequest, preparedStatementValues)).thenReturn(null);
        final List<MeterWaterRates> meterWaterRates = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(queryString, preparedStatementValues, meterWaterRatesRowMapper))
                .thenReturn(null);

        assertTrue(meterWaterRates.equals(meterWaterRatesRepository.findForCriteria(meterWaterRatesGetRequest)));
    }

    @Test
    public void test_Should_Create_MeterWaterRates() {

        final MeterWaterRatesRequest meterWaterRatesRequest = new MeterWaterRatesRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        meterWaterRatesRequest.setRequestInfo(requestInfo);
        final List<MeterWaterRates> meterWaterRatesList = new ArrayList<>();
        final MeterWaterRates meterWaterRates = getMeterWaterRates();
        meterWaterRatesList.add(meterWaterRates);
        meterWaterRatesRequest.setMeterWaterRates(meterWaterRatesList);

        final MeterWaterRatesRequest meterWaterRatesReq = meterWaterRatesRepository
                .create(meterWaterRatesRequest);
        when(namedParameterJdbcTemplate.queryForObject(any(String.class), anyMap(), eq(Long.class))).thenReturn(2L);
        assertThat(meterWaterRatesReq.getMeterWaterRates().size()).isEqualTo(1);
    }

    @Test
    public void test_Should_Update_MeterWaterRates() {

        final MeterWaterRatesRequest meterWaterRatesRequest = new MeterWaterRatesRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final User user = new User();
        user.setId(1l);
        requestInfo.setUserInfo(user);
        meterWaterRatesRequest.setRequestInfo(requestInfo);
        final List<MeterWaterRates> meterWaterRatesList = new ArrayList<>();
        final MeterWaterRates meterWaterRates = getMeterWaterRates();
        meterWaterRatesList.add(meterWaterRates);
        meterWaterRatesRequest.setMeterWaterRates(meterWaterRatesList);

        final MeterWaterRatesRequest meterWaterRatesReq = meterWaterRatesRepository
                .update(meterWaterRatesRequest);
        when(namedParameterJdbcTemplate.queryForObject(any(String.class), anyMap(), eq(Long.class))).thenReturn(2L);
        assertThat(meterWaterRatesReq.getMeterWaterRates().size()).isEqualTo(1);
    }

    private MeterWaterRates getMeterWaterRates() {
        final MeterWaterRates meterWaterRates = new MeterWaterRates();
        meterWaterRates.setTenantId("default");
        meterWaterRates.setBillingType("METERED");
        meterWaterRates.setCode("12");
        meterWaterRates.setSourceTypeId(2l);
        meterWaterRates.setUsageTypeId(1l);
        meterWaterRates.setPipeSizeId(2l);
        final List<Slab> slabList = new ArrayList<>();
        final Slab slab = getSlabDetails();
        slabList.add(slab);
        meterWaterRates.setSlab(slabList);
        meterWaterRates.setFromDate(123456654l);
        meterWaterRates.setToDate(52368899l);
        meterWaterRates.setActive(true);
        return meterWaterRates;
    }

    private Slab getSlabDetails() {
        final Slab slab = new Slab();
        slab.setTenantId("default");
        slab.setFromUnit(200l);
        slab.setToUnit(400l);
        slab.setUnitRate(600l);
        return slab;
    }

}
