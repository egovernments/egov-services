package org.egov.asset.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Department;
import org.egov.asset.model.Function;
import org.egov.asset.model.Fund;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.Voucher;
import org.egov.asset.model.VouchercreateAccountCodeDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.Status;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.model.enums.VoucherType;
import org.egov.common.contract.request.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class VoucherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private VouchercreateAccountCodeDetails vouchercreateAccountCodeDetails;

    @InjectMocks
    private VoucherService voucherService;

    @Mock
    private AssetConfigurationService assetConfigurationService;

    @Test
    public void test_shuould_create_VoucherRequest_For_Reevalaution() {

        final RevaluationRequest revaluationRequest = getRevaluationRequest();
        final Revaluation revaluation = revaluationRequest.getRevaluation();
        revaluation.setFund(Long.valueOf("3"));

        final Asset asset = get_Asset();
        final List<VouchercreateAccountCodeDetails> accountCodeDetails = getVouchercreateAccountCodeDetails();

        final String tenantId = revaluation.getTenantId();

        when(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.REVALUATIONVOUCHERNAME, tenantId))
                        .thenReturn("Asset Revaluation Voucher");
        when(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.REVALUATIONVOUCHERDESCRIPTION, tenantId))
                        .thenReturn("Creating Voucher for Asset Revaluation");

        final VoucherRequest generatedVoucherRequest = voucherService.createVoucherRequest(revaluation,
                revaluation.getFund(), asset.getDepartment().getId(), accountCodeDetails, tenantId);

        final Fund fund = get_Fund(revaluationRequest);
        final Voucher voucher = getVoucher(asset, fund, tenantId);
        final List<Voucher> vouchers = new ArrayList<>();
        vouchers.add(voucher);

        final VoucherRequest expectedVoucherRequest = new VoucherRequest();
        expectedVoucherRequest.setRequestInfo(new RequestInfo());
        expectedVoucherRequest.setVouchers(vouchers);

        System.out.println(generatedVoucherRequest + "\n");
        System.err.println(expectedVoucherRequest);

        assertEquals(expectedVoucherRequest.getVouchers(), generatedVoucherRequest.getVouchers());

    }

    private Voucher getVoucher(final Asset asset, final Fund fund, final String tenantId) {

        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        final Voucher voucher = new Voucher();
        voucher.setType(VoucherType.JOURNALVOUCHER.toString());
        voucher.setVoucherDate(sdf.format(new Date()));
        voucher.setLedgers(getVouchercreateAccountCodeDetails());
        voucher.setDepartment(asset.getDepartment().getId());
        voucher.setName(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.REVALUATIONVOUCHERNAME, tenantId));
        voucher.setDescription(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.REVALUATIONVOUCHERDESCRIPTION, tenantId));
        voucher.setFund(fund);

        return voucher;
    }

    private List<VouchercreateAccountCodeDetails> getVouchercreateAccountCodeDetails() {
        final VouchercreateAccountCodeDetails vouchercreateAccountCodeDetails = new VouchercreateAccountCodeDetails();
        vouchercreateAccountCodeDetails.setCreditAmount(new BigDecimal("0"));
        vouchercreateAccountCodeDetails.setDebitAmount(new BigDecimal("10"));
        vouchercreateAccountCodeDetails.setGlcode("generalLedger6Code");
        final Function function = new Function();
        function.setId(Long.valueOf("124"));

        vouchercreateAccountCodeDetails.setFunction(function);

        final List<VouchercreateAccountCodeDetails> accountCodeDetails = new ArrayList<>();
        accountCodeDetails.add(vouchercreateAccountCodeDetails);

        return accountCodeDetails;
    }

    private Fund get_Fund(final RevaluationRequest revaluationRequest) {
        final Fund fund = new Fund();
        fund.setId(revaluationRequest.getRevaluation().getFund());
        return fund;
    }

    private Asset get_Asset() {
        final Asset asset = new Asset();
        asset.setId(Long.valueOf("12"));
        final Department department = new Department();
        department.setId(Long.valueOf("2"));
        asset.setDepartment(department);
        return asset;
    }

    private RevaluationRequest getRevaluationRequest() {
        final RevaluationRequest revaluationRequest = new RevaluationRequest();
        revaluationRequest.setRequestInfo(new RequestInfo());
        final Revaluation revaluation = new Revaluation();
        revaluation.setTenantId("ap.kurnool");
        revaluation.setAssetId(Long.valueOf("12"));
        revaluation.setCurrentCapitalizedValue(new BigDecimal("100.58"));
        revaluation.setTypeOfChange(TypeOfChangeEnum.INCREASED);
        revaluation.setRevaluationAmount(new BigDecimal("10"));
        revaluation.setValueAfterRevaluation(new BigDecimal("90.68"));
        revaluation.setRevaluationDate(Long.valueOf("1496430744825"));
        revaluation.setReevaluatedBy("5");
        revaluation.setReasonForRevaluation("reasonForRevaluation");
        revaluation.setFixedAssetsWrittenOffAccount(Long.valueOf("1"));
        revaluation.setFunction(Long.valueOf("124"));
        revaluation.setFund(Long.valueOf("3"));
        revaluation.setScheme(Long.valueOf("4"));
        revaluation.setSubScheme(Long.valueOf("5"));
        revaluation.setComments("coments");
        revaluation.setStatus(Status.APPROVED.toString());

        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("5");
        auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
        auditDetails.setLastModifiedBy("5");
        auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
        revaluation.setAuditDetails(auditDetails);

        revaluationRequest.setRevaluation(revaluation);
        return revaluationRequest;

    }

}
