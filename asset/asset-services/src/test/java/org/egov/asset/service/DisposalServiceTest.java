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
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.DisposalResponse;
import org.egov.asset.contract.FunctionResponse;
import org.egov.asset.contract.FundResponse;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.Department;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.model.Function;
import org.egov.asset.model.Fund;
import org.egov.asset.model.Location;
import org.egov.asset.model.StatusValue;
import org.egov.asset.model.Voucher;
import org.egov.asset.model.VoucherAccountCodeDetails;
import org.egov.asset.model.enums.AssetStatusObjectName;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.model.enums.Status;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.model.enums.VoucherType;
import org.egov.asset.repository.AssetRepository;
import org.egov.asset.repository.DisposalRepository;
import org.egov.asset.util.FileUtils;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class DisposalServiceTest {

    @Mock
    private DisposalRepository disposalRepository;

    @Mock
    private AssetRepository assetRepository;


    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private DisposalCriteria disposalCriteria;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private VoucherService voucherService;

    @Mock
    private ChartOfAccountDetailContract chartOfAccountDetailContract;

    @InjectMocks
    private DisposalService disposalService;

    @Mock
    private AssetConfigurationService assetConfigurationService;

    @Mock
    private AssetMasterService assetMasterService;

    @Mock
    private AssetService assetService;

    @Mock
    private AssetCommonService assetCommonService;

    @Mock
    private ResponseInfoFactory responseInfoFactory;
    
    @Mock
    private CurrentValueService currentValueService;

    @Test
    public void testSearch() {
        final List<Disposal> disposal = new ArrayList<>();
        disposal.add(getDisposalForSearch());

        final DisposalResponse disposalResponse = new DisposalResponse();
        disposalResponse.setDisposals(disposal);

        when(disposalRepository.search(any(DisposalCriteria.class))).thenReturn(disposal);
        final List<Disposal> expectedDisposalResponse = disposalService.search(Matchers.any(DisposalCriteria.class),
                new RequestInfo());

        assertEquals(disposal.toString(), expectedDisposalResponse.toString());
    }


    @SuppressWarnings("unchecked")
    @Test
    public void createVoucherForDisposalTest() throws Exception {
        final DisposalRequest disposalRequest = getDisposalRequest();
        final HttpHeaders headers = getHttpHeaders();
        final Asset asset = getAsset();
        final List<Asset> assets = new ArrayList<>();
        assets.add(asset);
        getFunctionResponse();
        getFundResponse();
        final VoucherRequest voucherRequest = getVoucherRequest();
        when(assetRepository.findForCriteria(any(AssetCriteria.class))).thenReturn(assets);
        when(voucherService.createDisposalVoucherRequest(any(Disposal.class), any(Long.class), any(Long.class),
                any(List.class),any(String.class) ,any(HttpHeaders.class))).thenReturn(voucherRequest);
        disposalService.createVoucherForDisposal(disposalRequest, headers);
    }

    @Test
    public void testCreate() {
        DisposalResponse disposalResponse = null;
        try {
            disposalResponse = getDisposalResponse("disposal/disposalServiceResponse.disposal1.json");
        } catch (final Exception e) {
            e.printStackTrace();
            fail();
        }
        final Asset asset = getAsset();
        final AssetStatus assetStatus = getAssetStatus();
        final List<AssetStatus> assetStatuses = new ArrayList<>();
        assetStatuses.add(assetStatus);
        final DisposalRequest disposalRequest = new DisposalRequest();
        disposalRequest.setDisposal(getDisposalForCreateAsync());

        doNothing().when(disposalRepository).create(any(DisposalRequest.class));
        assertEquals(disposalResponse.getDisposals().get(0).toString(), disposalRequest.getDisposal().toString());
        when(assetService.getAsset(any(String.class), any(Long.class), any(RequestInfo.class))).thenReturn(asset);
        when(assetMasterService.getStatuses(any(AssetStatusObjectName.class), any(Status.class), any(String.class)))
                .thenReturn(assetStatuses);
        disposalService.setStatusOfAssetToDisposed(disposalRequest);
        disposalService.create(disposalRequest);
    }

    private AssetStatus getAssetStatus() {
        final AssetStatus assetStatus = new AssetStatus();
        final StatusValue statusValue = new StatusValue();
        statusValue.setCode(Status.DISPOSED.toString());
        statusValue.setName(Status.DISPOSED.toString());
        statusValue.setDescription("Asset status is Disposed");
        final List<StatusValue> statusValues = new ArrayList<>();
        statusValues.add(statusValue);
        assetStatus.setObjectName(AssetStatusObjectName.DISPOSAL.toString());
        assetStatus.setStatusValues(statusValues);
        assetStatus.setAuditDetails(getAuditDetails());
        return assetStatus;
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

    private FundResponse getFundResponse() {
        final FundResponse fundResponse = new FundResponse();
        final List<Fund> funds = new ArrayList<>();
        final Fund fund = getFund();
        funds.add(fund);
        fundResponse.setFunds(funds);
        fundResponse.setFund(null);
        fundResponse.setPage(null);
        fundResponse.setResponseInfo(null);
        return fundResponse;
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

    private FunctionResponse getFunctionResponse() {
        final FunctionResponse functionResponse = new FunctionResponse();
        final List<Function> functions = new ArrayList<>();
        final Function function = getFunction();
        functions.add(function);
        functionResponse.setFunctions(functions);
        functionResponse.setFunction(null);
        functionResponse.setPage(null);
        functionResponse.setResponseInfo(null);
        return functionResponse;
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

    private DisposalRequest getDisposalRequest() {
        final DisposalRequest disposalRequest = new DisposalRequest();
        disposalRequest.setRequestInfo(new RequestInfo());
        disposalRequest.setDisposal(getDisposalForCreateAsync());
        return disposalRequest;
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

    private Disposal getDisposalForSearch() {

        final Disposal disposal = new Disposal();
        disposal.setTenantId("ap.kurnool");
        disposal.setId(Long.valueOf("15"));
        disposal.setAssetId(Long.valueOf("31"));
        disposal.setBuyerName("Abhi");
        disposal.setBuyerAddress("Bangalore");
        disposal.setDisposalDate(Long.valueOf("1496564536178"));
        disposal.setDisposalReason("disposalReason");
        disposal.setPanCardNumber("baq1234567");
        disposal.setAadharCardNumber("12345678123456");
        disposal.setAssetCurrentValue(new BigDecimal("100.0"));
        disposal.setSaleValue(new BigDecimal("200.0"));
        disposal.setTransactionType(TransactionType.SALE);
        disposal.setAssetSaleAccount(Long.valueOf("15"));
        disposal.setAuditDetails(getAuditDetails());

        return disposal;
    }

    private AuditDetails getAuditDetails() {
        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("2");
        auditDetails.setCreatedDate(Long.valueOf("1496746205544"));
        auditDetails.setLastModifiedBy("2");
        auditDetails.setLastModifiedDate(Long.valueOf("1496746205544"));
        return auditDetails;
    }

    private Disposal getDisposalForCreateAsync() {

        final Disposal disposal = new Disposal();
        disposal.setId(Long.valueOf("15"));
        disposal.setTenantId("ap.kurnool");
        disposal.setAssetId(Long.valueOf("31"));
        disposal.setBuyerName("Abhi");
        disposal.setBuyerAddress("Bangalore");
        disposal.setDisposalDate(Long.valueOf("1496564536178"));
        disposal.setDisposalReason("disposalReason");
        disposal.setPanCardNumber("baq1234567");
        disposal.setAadharCardNumber("12345678123456");
        disposal.setAssetCurrentValue(new BigDecimal("100.0"));
        disposal.setSaleValue(new BigDecimal("200.0"));
        disposal.setTransactionType(TransactionType.SALE);
        disposal.setAssetSaleAccount(Long.valueOf("15"));
        disposal.setProfitLossVoucherReference("6");
        disposal.setAuditDetails(getAuditDetails());
        disposal.setProfitLossVoucherReference("6");

        return disposal;
    }

    private DisposalResponse getDisposalResponse(final String filePath) throws IOException {
        final String empJson = new FileUtils().getFileContents(filePath);
        return new ObjectMapper().readValue(empJson, DisposalResponse.class);
    }
}
