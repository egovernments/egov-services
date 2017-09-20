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
package org.egov.wcms.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.MeterWaterRates;
import org.egov.wcms.model.Slab;
import org.egov.wcms.repository.MeterWaterRatesRepository;
import org.egov.wcms.web.contract.MeterWaterRatesGetRequest;
import org.egov.wcms.web.contract.MeterWaterRatesRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(MeterWaterRatesService.class)
public class MeterWaterRatesServiceTest {

    @Mock
    private MeterWaterRatesRepository meterWaterRatesRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private CodeGeneratorService codeGeneratorService;

    @InjectMocks
    private MeterWaterRatesService meterWaterRatesService;

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_Should_Search_MeterWaterRates() {
        final List<MeterWaterRates> meterWaterRatesList = new ArrayList<>();
        meterWaterRatesList.add(getMeterWaterRates());
        final MeterWaterRatesGetRequest meterWaterRatesRequest = Mockito.mock(MeterWaterRatesGetRequest.class);
        when(meterWaterRatesRepository.findForCriteria(meterWaterRatesRequest)).thenThrow(Exception.class);
        assertTrue(meterWaterRatesList
                .equals(meterWaterRatesService.getMeterWaterRates(meterWaterRatesRequest)));
    }

    @Test
    public void test_throwException_Push_To_Producer_MeterWaterRates() {
        final List<MeterWaterRates> meterWaterRatesList = new ArrayList<>();
        meterWaterRatesList.add(getMeterWaterRates());
        final MeterWaterRatesRequest meterWaterRatesRequest = new MeterWaterRatesRequest();
        meterWaterRatesRequest.setMeterWaterRates(meterWaterRatesList);
        assertTrue(meterWaterRatesList.equals(meterWaterRatesService.pushCreateToQueue("", "", meterWaterRatesRequest)));
    }

    @Test
    public void test_throwException_Create_MeterWaterRates() {

        final List<MeterWaterRates> meterWaterRatesList = new ArrayList<>();
        meterWaterRatesList.add(getMeterWaterRates());
        final MeterWaterRatesRequest meterWaterRatesRequest = new MeterWaterRatesRequest();
        meterWaterRatesRequest.setMeterWaterRates(meterWaterRatesList);
        when(meterWaterRatesRepository.create(any(MeterWaterRatesRequest.class)))
                .thenReturn(meterWaterRatesRequest);
        assertTrue(meterWaterRatesRequest.equals(meterWaterRatesService.create(meterWaterRatesRequest)));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void test_throwException_Update_MeterWaterRates() throws Exception {

        final MeterWaterRatesRequest meterWaterRatesRequest = Mockito.mock(MeterWaterRatesRequest.class);
        when(meterWaterRatesRepository.update(meterWaterRatesRequest)).thenThrow(Exception.class);

        assertTrue(meterWaterRatesRequest.equals(meterWaterRatesService.update(meterWaterRatesRequest)));
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
        meterWaterRates.setFromDate(12245666l);
        meterWaterRates.setToDate(1234568l);
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
