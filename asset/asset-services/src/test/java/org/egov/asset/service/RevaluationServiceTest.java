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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.RevaluationResponse;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.Department;
import org.egov.asset.model.Function;
import org.egov.asset.model.Fund;
import org.egov.asset.model.Location;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.Voucher;
import org.egov.asset.model.VoucherAccountCodeDetails;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.model.enums.Status;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.model.enums.VoucherType;
import org.egov.asset.repository.RevaluationRepository;
import org.egov.asset.util.FileUtils;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class RevaluationServiceTest {

    @Mock
    private RevaluationRepository revaluationRepository;

    @Mock
    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private RevaluationCriteria revaluationCriteria;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AssetService assetService;

    @Mock
    private RevaluationRequest revaluationRequest;

    @Mock
    private VoucherService voucherService;

    @InjectMocks
    private RevaluationService revaluationService;

    @Mock
    private ChartOfAccountDetailContract chartOfAccountDetailContract;

    @Mock
    private CurrentValueService currentValueService;

    @Mock
    private AssetConfigurationService assetConfigurationService;

    @Mock
    private AssetCommonService assetCommonService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @Test
    public void testCreate() {
        RevaluationResponse revaluationResponse = null;
        try {
            revaluationResponse = getRevaluation("revaluation/revaluationServiceResponse.revaluation1.json");
        } catch (final Exception e) {
            e.printStackTrace();
            fail();
        }
        final RevaluationRequest revaluationRequest = new RevaluationRequest();
        revaluationRequest.setRevaluation(getRevaluationForCreateAsync());
        revaluationRequest.getRevaluation().setId(Long.valueOf("15"));

        doNothing().when(revaluationRepository).create(revaluationRequest);
        revaluationService.create(revaluationRequest);
        revaluationService.saveRevaluationAmountToCurrentAmount(revaluationRequest);
        assertEquals(revaluationResponse.getRevaluations().get(0).toString(),
                revaluationRequest.getRevaluation().toString());
    }

    @Test
    public void testCreateAsync() throws Exception {
        final RevaluationRequest revaluationRequest = getRevaluationRequest();
        final HttpHeaders headers = getHttpHeaders();
        final RevaluationResponse expectedRevaluationResponse = getRevaluationResponse();

        when(assetCommonService.getNextId(any(Sequence.class))).thenReturn(Long.valueOf("1"));
        final RevaluationResponse actualRevaluationResponse = revaluationService.createAsync(revaluationRequest,
                headers);
        assertEquals(expectedRevaluationResponse.toString(), actualRevaluationResponse.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createVoucherForRevaluationTest() throws Exception {
        final RevaluationRequest revaluationRequest = getRevaluationRequest();
        final HttpHeaders headers = getHttpHeaders();
        final Asset asset = getAsset();

        final VoucherRequest voucherRequest = getVoucherRequest();

        when(assetService.getAsset(any(String.class), any(Long.class), any(RequestInfo.class))).thenReturn(asset);
        when(voucherService.createRevaluationVoucherRequest(any(Revaluation.class), any(List.class), any(Long.class),
                any(Long.class), any(HttpHeaders.class))).thenReturn(voucherRequest);
        revaluationService.createVoucherForRevaluation(revaluationRequest, headers);
    }

    @Test
    public void testSearch() {
        final RevaluationCriteria revaluationCriteria = getRevaluationCriteria();
        final List<Revaluation> revaluation = new ArrayList<>();
        revaluation.add(getRevaluationForSearch());

        final RevaluationResponse revaluationResponse = new RevaluationResponse();
        revaluationResponse.setRevaluations(revaluation);

        when(revaluationRepository.search(any(RevaluationCriteria.class))).thenReturn(revaluation);
        final RevaluationResponse expectedRevaluationResponse = revaluationService.search(revaluationCriteria,
                new RequestInfo());

        assertEquals(revaluationResponse.toString(), expectedRevaluationResponse.toString());
    }

    private RevaluationCriteria getRevaluationCriteria() {
        final RevaluationCriteria revaluationCriteria = new RevaluationCriteria();
        final List<Long> ids = new ArrayList<Long>();
        ids.add(Long.valueOf("1"));
        revaluationCriteria.setId(ids);
        revaluationCriteria.setStatus(Status.APPROVED.toString());
        revaluationCriteria.setTenantId("ap.kurnool");
        return null;
    }

    private VoucherRequest getVoucherRequest() {
        final VoucherRequest voucherRequest = new VoucherRequest();
        final List<Voucher> vouchers = new ArrayList<>();
        final Voucher voucher = getVoucher();
        vouchers.add(voucher);
        voucherRequest.setRequestInfo(null);
        voucherRequest.setVouchers(vouchers);
        return voucherRequest;
    }

    private Voucher getVoucher() {
        final Voucher voucher = new Voucher();
        voucher.setCgvn(null);
        voucher.setDepartment(Long.valueOf("5"));
        voucher.setDescription("Asset Revaluation Jouranl Voucher");
        voucher.setFiscalPeriod(null);
        voucher.setFund(getFund());
        voucher.setId(Long.valueOf("50"));
        voucher.setLedgers(getLedgers());
        voucher.setModuleId(null);
        voucher.setName("Asset Revaluation");
        voucher.setOriginalVhId(null);
        voucher.setRefVhId(null);
        voucher.setStatus(Status.CREATED.toString());
        voucher.setType(VoucherType.JOURNALVOUCHER.toString());
        voucher.setVoucherNumber("1/GJV/00000197/08/2017-18");
        return null;
    }

    private List<VoucherAccountCodeDetails> getLedgers() {
        final List<VoucherAccountCodeDetails> ledgers = new ArrayList<>();
        final VoucherAccountCodeDetails creditAccountDetails = new VoucherAccountCodeDetails();
        creditAccountDetails.setCreditAmount(new BigDecimal("500"));
        creditAccountDetails.setDebitAmount(BigDecimal.ZERO);
        creditAccountDetails.setFunction(getFunction());
        creditAccountDetails.setGlcode("144005");
        creditAccountDetails.setSubledgerDetails(null);
        final VoucherAccountCodeDetails debitAccountDetails = new VoucherAccountCodeDetails();
        debitAccountDetails.setCreditAmount(BigDecimal.ZERO);
        debitAccountDetails.setDebitAmount(new BigDecimal("500"));
        debitAccountDetails.setFunction(getFunction());
        debitAccountDetails.setGlcode("122365");
        debitAccountDetails.setSubledgerDetails(null);
        ledgers.add(creditAccountDetails);
        ledgers.add(debitAccountDetails);
        return ledgers;
    }

    private Fund getFund() {
        final Fund fund = new Fund();
        fund.setActive(true);
        fund.setCode("01");
        fund.setName("Municipal Fund");
        fund.setId(Long.valueOf("1"));
        fund.setIdentifier(null);
        fund.setLevel(null);
        fund.setParentId(null);
        return fund;
    }

    private Function getFunction() {
        final Function function = new Function();
        function.setId(Long.valueOf("5"));
        function.setCode("0600");
        function.setActive(true);
        function.setName("Estate");
        function.setParentId(null);
        function.setLevel(null);
        return function;
    }

    private Asset getAsset() {
        final Asset asset = new Asset();
        asset.setTenantId("ap.kurnool");
        asset.setId(Long.valueOf("1"));
        asset.setName("asset name");
        asset.setStatus(Status.CREATED.toString());
        asset.setModeOfAcquisition(ModeOfAcquisition.ACQUIRED);
        asset.setEnableYearWiseDepreciation(true);

        final Location location = new Location();
        location.setLocality(4l);
        location.setDoorNo("door no");

        final AssetCategory assetCategory = new AssetCategory();
        assetCategory.setId(1l);
        assetCategory.setName("category name");

        asset.setLocationDetails(location);
        asset.setAssetCategory(assetCategory);

        final Department department = new Department();
        department.setId(Long.valueOf("5"));
        department.setCode("ENG");
        department.setName("ENGINEERING");

        asset.setDepartment(department);
        return asset;
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

    private RevaluationRequest getRevaluationRequest() {
        final RevaluationRequest revaluationRequest = new RevaluationRequest();
        revaluationRequest.setRequestInfo(new RequestInfo());
        revaluationRequest.setRevaluation(getRevaluationForCreateAsync());
        return revaluationRequest;
    }

    private RevaluationResponse getRevaluationResponse() {
        final RevaluationResponse revaluationResponse = new RevaluationResponse();
        final List<Revaluation> revaluations = new ArrayList<>();
        revaluations.add(getRevaluationForCreateAsync());
        revaluationResponse.setResposneInfo(null);
        revaluationResponse.setRevaluations(revaluations);
        return revaluationResponse;
    }

    private Revaluation getRevaluationForCreateAsync() {

        final Revaluation revaluation = new Revaluation();
        revaluation.setId(Long.valueOf("1"));
        revaluation.setTenantId("ap.kurnool");
        revaluation.setAssetId(Long.valueOf("31"));
        revaluation.setCurrentCapitalizedValue(new BigDecimal("100.68"));
        revaluation.setTypeOfChange(TypeOfChangeEnum.DECREASED);
        revaluation.setRevaluationAmount(new BigDecimal("10.0"));
        revaluation.setValueAfterRevaluation(new BigDecimal("90.68"));
        revaluation.setRevaluationDate(Long.valueOf("1496430744825"));
        revaluation.setReevaluatedBy("5");
        revaluation.setReasonForRevaluation("reasonForRevaluation");
        revaluation.setFixedAssetsWrittenOffAccount(Long.valueOf("1"));
        revaluation.setFunction(Long.valueOf("2"));
        revaluation.setFund(Long.valueOf("3"));
        revaluation.setRevaluationOrderDate(Long.valueOf("1496430744825"));;
        revaluation.setRevaluationOrderNo("12");
        revaluation.setComments("coments");
        revaluation.setStatus(Status.APPROVED.toString());
        revaluation.setAuditDetails(getAuditDetails());
        revaluation.setVoucherReference("42");
        return revaluation;
    }

    private AuditDetails getAuditDetails() {
        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("5");
        auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
        auditDetails.setLastModifiedBy("5");
        auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
        return auditDetails;
    }

    private Revaluation getRevaluationForSearch() {

        final Revaluation revaluation = new Revaluation();
        revaluation.setId(Long.valueOf("1"));
        revaluation.setTenantId("ap.kurnool");
        revaluation.setAssetId(Long.valueOf("31"));
        revaluation.setCurrentCapitalizedValue(new BigDecimal("100.68"));
        revaluation.setTypeOfChange(TypeOfChangeEnum.DECREASED);
        revaluation.setRevaluationAmount(new BigDecimal("10"));
        revaluation.setValueAfterRevaluation(new BigDecimal("90.68"));
        revaluation.setRevaluationDate(Long.valueOf("1496430744825"));
        revaluation.setReevaluatedBy("5");
        revaluation.setReasonForRevaluation("reasonForRevaluation");
        revaluation.setFixedAssetsWrittenOffAccount(Long.valueOf("1"));
        revaluation.setFunction(Long.valueOf("2"));
        revaluation.setFund(Long.valueOf("3"));
        revaluation.setRevaluationOrderDate(Long.valueOf("1496430744825"));;
        revaluation.setRevaluationOrderNo("12");
        revaluation.setComments("coments");
        revaluation.setStatus(Status.APPROVED.toString());
        revaluation.setAuditDetails(getAuditDetails());
        revaluation.setVoucherReference("1/GJV/00000214/08/2017-18");

        return revaluation;
    }

    private RevaluationResponse getRevaluation(final String filePath) throws IOException {
        final String empJson = new FileUtils().getFileContents(filePath);
        return new ObjectMapper().readValue(empJson, RevaluationResponse.class);
    }
}
