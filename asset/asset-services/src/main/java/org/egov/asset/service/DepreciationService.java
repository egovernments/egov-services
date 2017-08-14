package org.egov.asset.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.contract.DepreciationResponse;
import org.egov.asset.contract.FinancialYearContract;
import org.egov.asset.contract.FinancialYearContractResponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.domain.CalculationCurrentValue;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.Depreciation;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.model.VouchercreateAccountCodeDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.repository.AssetRepository;
import org.egov.asset.repository.DepreciationRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepreciationService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DepreciationRepository depreciationRepository;

    @Autowired
    private CurrentValueService currentValueService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private AssetDepreciator assetDepreciator;

    @Autowired
    private SequenceGenService sequenceGenService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private AssetCommonService assetCommonService;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private AssetConfigurationService assetConfigurationService;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private VoucherService voucherService;

    /*
     * Pre-requisite to understanding the logic:
     * https://issues.egovernments.org/browse/EGSVC-271
     */
    public DepreciationResponse depreciateAsset(final DepreciationRequest depreciationRequest,
            final HttpHeaders headers) {

        final RequestInfo requestInfo = depreciationRequest.getRequestInfo();
        final DepreciationCriteria depreciationCriteria = depreciationRequest.getDepreciationCriteria();
        final String tenantId = depreciationCriteria.getTenantId();
        setDefaultsInDepreciationCriteria(depreciationCriteria, requestInfo);

        final Map<Long, DepreciationDetail> depreciationDetailsMap = new HashMap<>();
        final List<AssetCurrentValue> assetCurrentValues = new ArrayList<>();
        getDepreciationDetailsAndCurrentValues(depreciationRequest, depreciationDetailsMap, assetCurrentValues,
                headers);

        final AuditDetails auditDetails = assetCommonService.getAuditDetails(requestInfo);
        // TODO remove voucher reference from depreciation to dep details FIXME
        final Depreciation depreciation = Depreciation.builder().depreciationCriteria(depreciationCriteria)
                .depreciationDetails(new ArrayList<>(depreciationDetailsMap.values())).auditDetails(auditDetails)
                .build();
        saveAsync(depreciation);
        // FIXME persist only success depreciaition is success
        for (final AssetCurrentValue assetCurrentValue : assetCurrentValues)
            assetCurrentValue.setTenantId(tenantId);
        currentValueService.createCurrentValueAsync(AssetCurrentValueRequest.builder()
                .assetCurrentValues(assetCurrentValues).requestInfo(requestInfo).build());

        return new DepreciationResponse(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo),
                depreciation);
    }

    /**
     * This method returns two values namely assetCurrentValues and
     * depreciationDetails and hence they are part of arguments to this method.
     *
     * @param depreciationRequest
     * @param depreciation
     * @param assetCurrentValues
     */
    private void getDepreciationDetailsAndCurrentValues(final DepreciationRequest depreciationRequest,
            final Map<Long, DepreciationDetail> depreciationDetailsMap,
            final List<AssetCurrentValue> assetCurrentValues, final HttpHeaders headers) {

        final DepreciationCriteria depreciationCriteria = depreciationRequest.getDepreciationCriteria();
        final List<CalculationAssetDetails> calculationAssetDetailList = depreciationRepository
                .getCalculationAssetDetails(depreciationCriteria);
        final Map<Long, CalculationCurrentValue> calculationCurrentValues = depreciationRepository
                .getCalculationCurrentvalue(depreciationCriteria).stream()
                .collect(Collectors.toMap(CalculationCurrentValue::getAssetId, Function.identity()));
        final Map<Long, BigDecimal> depreciationSumMap = depreciationRepository
                .getdepreciationSum(depreciationCriteria.getTenantId());

        assetDepreciator.depreciateAsset(depreciationRequest, calculationAssetDetailList, calculationCurrentValues,
                depreciationSumMap, assetCurrentValues, depreciationDetailsMap);

        if (assetConfigurationService.getEnabledVoucherGeneration(AssetConfigurationKeys.ENABLEVOUCHERGENERATION,
                depreciationCriteria.getTenantId())) {
            final Map<String, List<CalculationAssetDetails>> voucherMap = new HashMap<>();
            for (final CalculationAssetDetails cad : calculationAssetDetailList) {
                final String key = cad.getAccumulatedDepreciationAccount().toString()
                        + cad.getDepreciationExpenseAccount();
                final List<CalculationAssetDetails> calculationAssetDetails = voucherMap.get(key);
                if (calculationAssetDetails != null)
                    calculationAssetDetails.add(cad);
                else {
                    final List<CalculationAssetDetails> newCalcAssetDetails = new ArrayList<>();
                    newCalcAssetDetails.add(cad);
                    voucherMap.put(key, newCalcAssetDetails);
                }
            }
            for (final Map.Entry<String, List<CalculationAssetDetails>> entry : voucherMap.entrySet()) {
                final BigDecimal amt = BigDecimal.ZERO;
                final List<CalculationAssetDetails> assetDetails = entry.getValue();
                final CalculationAssetDetails assetDetail = assetDetails.get(0);
                final Long depExpenxeAcc = assetDetail.getDepreciationExpenseAccount();
                final Long accumulatedDepAcc = assetDetail.getAccumulatedDepreciationAccount();

                for (final CalculationAssetDetails calculationAssetDetail : assetDetails)
                    amt.add(depreciationDetailsMap.get(calculationAssetDetail.getAssetId()).getDepreciationValue());

                createVoucherForDepreciation(assetDetail, depreciationRequest.getRequestInfo(), accumulatedDepAcc,
                        depExpenxeAcc, amt, depreciationCriteria.getTenantId(), headers);

                // TODO call method with two acc codes

            }
        }

        // final Long voucherReference = null;
        /*
         * TODO get voucherreference do integration if
         * (assetConfigurationService.getEnabledVoucherGeneration(
         * AssetConfigurationKeys.ENABLEVOUCHERGENERATION,
         * depreciationCriteria.getTenantId())) try { // TODO VOUCHER GEN
         * voucherReference = //
         * createVoucherForRevaluation(revaluationRequest); } catch (final
         * Exception e) { throw new RuntimeException(
         * "Voucher Generation is failed due to :" + e.getMessage()); }
         */
    }

    private void createVoucherForDepreciation(final CalculationAssetDetails cad, final RequestInfo requestInfo,
            final Long aDAccount, final Long dEAccount, final BigDecimal amount, final String tenantId,
            final HttpHeaders headers) {
        final List<ChartOfAccountDetailContract> subledgerDetailsForAD = voucherService.getSubledgerDetails(requestInfo,
                tenantId, aDAccount);
        final List<ChartOfAccountDetailContract> subledgerDetailsForDE = voucherService.getSubledgerDetails(requestInfo,
                tenantId, dEAccount);

        if (subledgerDetailsForAD != null && subledgerDetailsForDE != null && !subledgerDetailsForAD.isEmpty()
                && !subledgerDetailsForDE.isEmpty())
            throw new RuntimeException("Subledger Details Should not be present for Chart Of Accounts");

        final List<VouchercreateAccountCodeDetails> accountCodeDetails = new ArrayList<VouchercreateAccountCodeDetails>();
        accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId, aDAccount, amount, 94l, false, true));
        accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId, dEAccount, amount, 94l, true, false));

        log.debug("Voucher Create Account Code Details :: " + accountCodeDetails);

        final List<Long> assetIds = new ArrayList<>();
        assetIds.add(cad.getAssetId());

        final Asset asset = assetRepository
                .findForCriteria(AssetCriteria.builder().tenantId(tenantId).id(assetIds).build()).get(0);
        
        log.debug("Depreciation Asset :: " + asset);

        final VoucherRequest voucherRequest = voucherService.createVoucherRequest(cad, 1l,
                asset.getDepartment().getId(), accountCodeDetails, requestInfo, tenantId);
        log.debug("Voucher Request for Depreciation :: " + voucherRequest);

        voucherService.createVoucher(voucherRequest, tenantId, headers);

    }

    /**
     * To set missing criteria feilds for depreciaition in case 1 throw
     * exception if all of the expected feilds are null In case 2, financialYear
     * is set to 2017-18 where "year of fromdate" = 2017 and "year of todate"
     * =2018 depreciationCriteria.setFinancialYear(getFinancialYear(fromDate,
     * toDate)); In case 3 Here, get the financial year contract object from
     * finance service for the financial year Now, we get fromDate and toDate
     * from this contract.
     *
     * @param depreciationCriteria
     * @param requestInfo
     */
    private void setDefaultsInDepreciationCriteria(final DepreciationCriteria depreciationCriteria,
            final RequestInfo requestInfo) {

        if (depreciationCriteria.getFinancialYear() == null
                && (depreciationCriteria.getFromDate() == null || depreciationCriteria.getToDate() == null))
            throw new RuntimeException("financialyear and (time period)fromdate,todate both "
                    + "cannot be null please provide atleast one value");
        else if (depreciationCriteria.getFinancialYear() == null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(depreciationCriteria.getFromDate());
            final int from = calendar.get(Calendar.YEAR);
            calendar.setTimeInMillis(depreciationCriteria.getToDate());
            final int to = calendar.get(Calendar.YEAR);
            depreciationCriteria.setFinancialYear(from + "-" + Integer.toString(to).substring(2, 4));
            log.info("financial year value -- " + depreciationCriteria.getFinancialYear());
        } else if (depreciationCriteria.getFromDate() == null && depreciationCriteria.getToDate() == null) {

            final String url = applicationProperties.getEgfServiceHostName()
                    + applicationProperties.getEgfFinancialYearSearchPath() + "?tenantId ="
                    + depreciationCriteria.getTenantId() + "&finYearRange=" + depreciationCriteria.getFinancialYear();

            final FinancialYearContract financialYearContract = restTemplate
                    .postForObject(url, new RequestInfoWrapper(requestInfo), FinancialYearContractResponse.class)
                    .getFinancialYears().get(0);
            depreciationCriteria.setToDate(financialYearContract.getEndingDate().getTime());
            depreciationCriteria.setFromDate(financialYearContract.getStartingDate().getTime());
        }
    }

    public void saveAsync(final Depreciation depreciation) {

        final List<DepreciationDetail> depreciationDetails = depreciation.getDepreciationDetails();
        final List<Long> depreciationDetailsId = sequenceGenService.getIds(depreciationDetails.size(),
                Sequence.DEPRECIATIONSEQUENCE.toString());
        int depreciationCount = 0;
        for (final DepreciationDetail depreciationDetail : depreciationDetails)
            depreciationDetail.setId(depreciationDetailsId.get(depreciationCount++));
        kafkaTemplate.send(applicationProperties.getSaveDepreciationTopic(), depreciation);
    }

    public void save(final Depreciation depreciation) {
        depreciationRepository.saveDepreciation(depreciation);
    }
}
