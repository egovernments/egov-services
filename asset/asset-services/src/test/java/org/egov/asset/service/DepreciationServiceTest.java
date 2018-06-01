/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.asset.service;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.contract.DepreciationResponse;
import org.egov.asset.contract.FinancialYearContract;
import org.egov.asset.contract.FinancialYearContractResponse;
import org.egov.asset.model.Depreciation;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.repository.DepreciationRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(DepreciationService.class)
@WebAppConfiguration
public class DepreciationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AssetConfigurationService assetConfigurationService;

    @Mock
    private DepreciationRepository depreciationRepository;

    @Mock
    private CurrentValueService currentValueService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @Mock
    private AssetDepreciator assetDepreciator;

    @Mock
    private SequenceGenService sequenceGenService;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private AssetCommonService assetCommonService;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private DepreciationService depreciationService;

    @Mock
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private VoucherService voucherService;

    @SuppressWarnings("unchecked")
    @Test
    public void test_Depreciate_Asset() {
        final DepreciationResponse depreciationResponse = getDepreciationReponse();
        final DepreciationRequest depreciationRequest = DepreciationRequest.builder().requestInfo(new RequestInfo())
                .depreciationCriteria(getDepreciationCriteria()).build();
        final HttpHeaders headers = getHttpHeaders();
        final FinancialYearContractResponse financialYearContractResponse = getFinancialYearContractResponse();
        when(restTemplate.postForObject(any(String.class), any(Object.class), any(Class.class)))
                .thenReturn(financialYearContractResponse);
        assertNotEquals(depreciationResponse.toString(),
                depreciationService.saveDepreciateAsset(depreciationRequest, headers).toString());
    }

    /*
     * @Test public void test_ValidationAndGenerationDepreciationVoucher() { final Map<Long, DepreciationDetail>
     * depreciationDetailsMap = getDepreciationDetailsMap(); final HttpHeaders headers = getHttpHeaders(); final
     * List<CalculationAssetDetails> calculationAssetDetailList = getCalculationAssetDetailList(); final Map<Long,
     * List<CalculationAssetDetails>> cadMap = getCalculationAssetDetailMap(); final RequestInfo requestInfo = getRequestInfo();
     * when(assetConfigurationService.getEnabledVoucherGeneration(AssetConfigurationKeys.ENABLEVOUCHERGENERATION,
     * "ap.kurnool")).thenReturn(true); depreciationService.validationAndGenerationDepreciationVoucher(depreciationDetailsMap,
     * headers, requestInfo, "ap.kurnool", calculationAssetDetailList, cadMap); }
     */

    /*
     * @SuppressWarnings("unchecked")
     * @Test public void test_SetDefaultsInDepreciationCriteria() { final DepreciationCriteria depreciationCriteria =
     * getDepreciationCriteria(); final RequestInfo requestInfo = getRequestInfo(); final FinancialYearContractResponse
     * financialYearContractResponse = getFinancialYearContractResponse(); when(restTemplate.postForObject(any(String.class),
     * any(Object.class), any(Class.class))) .thenReturn(financialYearContractResponse);
     * depreciationService.setDefaultsInDepreciationCriteria(depreciationCriteria, requestInfo); }
     */
    private FinancialYearContractResponse getFinancialYearContractResponse() {
        final FinancialYearContractResponse financialYearContractResponse = new FinancialYearContractResponse();
        final List<FinancialYearContract> financialYearContracts = new ArrayList<FinancialYearContract>();
        final FinancialYearContract financialYearContract = new FinancialYearContract();
        financialYearContract.setFinYearRange("2017-18");
        financialYearContract.setActive(true);
        financialYearContract.setStartingDate(java.sql.Date.valueOf(LocalDate.of(2017, 04, 1)));
        financialYearContract.setEndingDate(java.sql.Date.valueOf(LocalDate.of(2018, 03, 31)));
        financialYearContract.setIsActiveForPosting(true);
        financialYearContracts.add(financialYearContract);
        financialYearContractResponse.setFinancialYears(financialYearContracts);
        financialYearContractResponse.setPage(null);
        financialYearContractResponse.setFinancialYear(null);
        financialYearContractResponse.setResponseInfo(null);
        return financialYearContractResponse;
    }

    private HttpHeaders getHttpHeaders() {
        final List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.ALL);
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set(HttpHeaders.COOKIE, "SESSIONID=123");
        requestHeaders.setPragma("no-cache");
        requestHeaders.setConnection("keep-alive");
        requestHeaders.setCacheControl("no-cache");
        requestHeaders.setAccept(mediaTypes);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return requestHeaders;
    }

    private DepreciationResponse getDepreciationReponse() {
        final DepreciationResponse depreciationResponse = new DepreciationResponse();
        final DepreciationCriteria depreciationCriteria = getDepreciationCriteria();
        final Depreciation depreciation = Depreciation.builder().depreciationCriteria(depreciationCriteria)
                .tenantId("ap.kurnool").depreciationDetails(new ArrayList<DepreciationDetail>()).build();
        depreciationResponse.setDepreciation(depreciation);
        depreciationResponse.setResponseInfo(null);
        return depreciationResponse;
    }

    private DepreciationCriteria getDepreciationCriteria() {
        final DepreciationCriteria depreciationCriteria = new DepreciationCriteria();
        depreciationCriteria.setAssetIds(null);
        depreciationCriteria.setFinancialYear("2017-18");
        depreciationCriteria.setFromDate(null);
        depreciationCriteria.setToDate(1514965363105l);
        depreciationCriteria.setTenantId("ap.kurnool");
        return depreciationCriteria;
    }

}
