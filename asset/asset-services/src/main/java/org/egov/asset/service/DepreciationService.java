package org.egov.asset.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.DepreciationReportResponse;
import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.contract.DepreciationResponse;
import org.egov.asset.contract.FinancialYearContract;
import org.egov.asset.contract.FinancialYearContractResponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.domain.CalculationCurrentValue;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.Depreciation;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.model.DepreciationReportCriteria;
import org.egov.asset.model.VoucherAccountCodeDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.Sequence;
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
    private VoucherService voucherService;

    /*
     * Pre-requisite to understanding the logic: https://issues.egovernments.org/browse/EGSVC-271
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

        final List<DepreciationDetail> depreciationListToBeSaved = depreciationDetailsMap.values().stream()
                .filter(DepreciationDetail -> DepreciationDetail.getVoucherReference() != null)
                .collect(Collectors.toList());
        log.debug("Depreciation List TO be Saved :: " + depreciationListToBeSaved);
        final List<AssetCurrentValue> currentValueListToBeSaved = new ArrayList<>();
        for (final AssetCurrentValue assetCurrentValue : assetCurrentValues) {

            final DepreciationDetail depreciationDetail = depreciationDetailsMap.get(assetCurrentValue.getAssetId());
            if (depreciationDetail != null) {
                assetCurrentValue.setTenantId(tenantId);
                currentValueListToBeSaved.add(assetCurrentValue);
            }
        }

        final AuditDetails auditDetails = assetCommonService.getAuditDetails(requestInfo);
        final Depreciation depreciation = Depreciation.builder().depreciationCriteria(depreciationCriteria)
                .depreciationDetails(depreciationListToBeSaved).auditDetails(auditDetails).build();
        final AssetCurrentValueRequest currentValueRequest = AssetCurrentValueRequest.builder()
                .assetCurrentValues(assetCurrentValues).requestInfo(requestInfo).build();
        depreciation.setTenantId(tenantId);
        saveAsync(depreciation);
        currentValueService.createCurrentValueAsync(currentValueRequest);
        return new DepreciationResponse(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo),
                depreciation);
    }

    /**
     * This method returns two values namely assetCurrentValues and depreciationDetails and hence they are part of arguments to
     * this method.
     *
     * @param depreciationRequest
     * @param depreciation
     * @param assetCurrentValues
     */
    private void getDepreciationDetailsAndCurrentValues(final DepreciationRequest depreciationRequest,
            final Map<Long, DepreciationDetail> depreciationDetailsMap,
            final List<AssetCurrentValue> assetCurrentValues, final HttpHeaders headers) {

        final RequestInfo requestInfo = depreciationRequest.getRequestInfo();
        final DepreciationCriteria depreciationCriteria = depreciationRequest.getDepreciationCriteria();
        final String tenantId = depreciationCriteria.getTenantId();
        final List<CalculationAssetDetails> calculationAssetDetailList = depreciationRepository
                .getCalculationAssetDetails(depreciationCriteria);
        log.debug("Calculation Asset Detail List::" + calculationAssetDetailList);
        final Map<Long, CalculationCurrentValue> calculationCurrentValues = depreciationRepository
                .getCalculationCurrentvalue(depreciationCriteria).stream()
                .collect(Collectors.toMap(CalculationCurrentValue::getAssetId, Function.identity()));
        log.debug("Calculation Current Values Map :: " + calculationCurrentValues);
        final Map<Long, List<CalculationAssetDetails>> cadMap = calculationAssetDetailList.stream()
                .collect(Collectors.groupingBy(CalculationAssetDetails::getDepartmentId));
        log.debug("Calculation Asset Details Map :: " + cadMap);
        final Map<Long, BigDecimal> depreciationSumMap = depreciationRepository.getdepreciationSum(tenantId);
        log.debug("Depreciation Sum Map :: " + depreciationSumMap);

        assetDepreciator.depreciateAsset(depreciationRequest, calculationAssetDetailList, calculationCurrentValues,
                depreciationSumMap, assetCurrentValues, depreciationDetailsMap);

        validationAndGenerationDepreciationVoucher(depreciationDetailsMap, headers, requestInfo, tenantId,
                calculationAssetDetailList, cadMap);

    }

    public void validationAndGenerationDepreciationVoucher(final Map<Long, DepreciationDetail> depreciationDetailsMap,
            final HttpHeaders headers, final RequestInfo requestInfo, final String tenantId,
            final List<CalculationAssetDetails> calculationAssetDetailList,
            final Map<Long, List<CalculationAssetDetails>> cadMap) {
        if (assetConfigurationService.getEnabledVoucherGeneration(AssetConfigurationKeys.ENABLEVOUCHERGENERATION,
                tenantId)) {
            log.info("Commencing voucher generation for depreciation");
            final Map<Long, VoucherAccountCodeDetails> ledgerMap = new HashMap<>();
            final List<VoucherAccountCodeDetails> accountCodeDetails = new ArrayList<VoucherAccountCodeDetails>();

            for (final Map.Entry<Long, List<CalculationAssetDetails>> entry : cadMap.entrySet()) {
                final Long departmentId = entry.getKey();
                log.debug("Asset Department ID :: " + departmentId);
                final List<CalculationAssetDetails> entryValue = entry.getValue();
                for (final CalculationAssetDetails cad : entryValue) {
                    final DepreciationDetail depreciationDetail = depreciationDetailsMap.get(cad.getAssetId());
                    log.debug("Depreciation Detail :: " + depreciationDetail);
                    if (depreciationDetail != null) {
                        final BigDecimal amount = depreciationDetail.getDepreciationValue();
                        log.debug("Depreciation Amount :: " + amount);
                        final Long aDAccount = cad.getAccumulatedDepreciationAccount();
                        log.debug("Accumulated Depreciation Account :: " + aDAccount);
                        final Long dEAccount = cad.getDepreciationExpenseAccount();
                        log.debug("Depreciation Expense Account :: " + dEAccount);
                        VoucherAccountCodeDetails adAccountCodeDetails = ledgerMap.get(aDAccount);
                        log.debug("Accumulated Depreciation Account Code Details :: " + adAccountCodeDetails);
                        if (adAccountCodeDetails != null)
                            adAccountCodeDetails.setDebitAmount(adAccountCodeDetails.getDebitAmount().add(amount));
                        else {
                            adAccountCodeDetails = voucherService.getGlCodes(requestInfo, tenantId, aDAccount, amount,
                                    false, true);
                            ledgerMap.put(aDAccount, adAccountCodeDetails);
                        }

                        VoucherAccountCodeDetails deAccountCodeDetails = ledgerMap.get(dEAccount);
                        log.debug("Depreciation Expense Account Code Details :: " + deAccountCodeDetails);
                        if (deAccountCodeDetails != null)
                            deAccountCodeDetails.setCreditAmount(deAccountCodeDetails.getCreditAmount().add(amount));
                        else {
                            deAccountCodeDetails = voucherService.getGlCodes(requestInfo, tenantId, dEAccount, amount,
                                    true, false);
                            ledgerMap.put(dEAccount, deAccountCodeDetails);
                        }
                    }
                }
                log.debug("Ledger Map :: " + ledgerMap);
                accountCodeDetails.addAll(ledgerMap.values());
                log.debug("Depreciation Account Code Details :: " + accountCodeDetails);
                validateDepreciationSubledgerDetails(requestInfo, tenantId, ledgerMap.keySet());
                if (!accountCodeDetails.isEmpty()) {
                    final VoucherRequest voucherRequest = voucherService.createDepreciationVoucherRequest(
                            calculationAssetDetailList, departmentId, accountCodeDetails, tenantId, headers);
                    log.debug("Voucher Request for Depreciation :: " + voucherRequest);

                    final String voucherNumber = voucherService.createVoucher(voucherRequest, tenantId, headers);
                    log.debug("Voucher Number for Depreciation :: " + voucherNumber);
                    setVoucherIdToDepreciaitionDetails(voucherNumber, entryValue, depreciationDetailsMap);
                }

            }
        }
    }

    private void setVoucherIdToDepreciaitionDetails(final String voucherNumber,
            final List<CalculationAssetDetails> entryValue,
            final Map<Long, DepreciationDetail> depreciationDetailsMap) {

        for (final CalculationAssetDetails calculationAssetDetails : entryValue) {
            final DepreciationDetail depreciationDetail = depreciationDetailsMap
                    .get(calculationAssetDetails.getAssetId());
            if (depreciationDetail != null)
                depreciationDetail.setVoucherReference(voucherNumber);
            log.debug("Depreciation Details having voucher reference :: " + depreciationDetail);
        }
    }

    private void validateDepreciationSubledgerDetails(final RequestInfo requestInfo, final String tenantId,
            final Set<Long> keySet) {
        for (final Long coa : keySet) {
            final List<ChartOfAccountDetailContract> subledgerDetails = voucherService.getSubledgerDetails(requestInfo,
                    tenantId, coa);
            if (subledgerDetails != null && !subledgerDetails.isEmpty())
                throw new RuntimeException("Subledger Details Should not be present for Chart Of Accounts");
        }

    }

    /**
     * To set missing criteria feilds for depreciaition in case 1 throw exception if all of the expected feilds are null In case
     * 2, financialYear is set to 2017-18 where "year of fromdate" = 2017 and "year of todate" =2018
     * depreciationCriteria.setFinancialYear(getFinancialYear(fromDate, toDate)); In case 3 Here, get the financial year contract
     * object from finance service for the financial year Now, we get fromDate and toDate from this contract.
     *
     * @param depreciationCriteria
     * @param requestInfo
     */
    public void setDefaultsInDepreciationCriteria(final DepreciationCriteria depreciationCriteria,
            final RequestInfo requestInfo) {
        final String financialYear = depreciationCriteria.getFinancialYear();
        log.debug("financial year value -- " + financialYear);
        if (financialYear == null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(depreciationCriteria.getFromDate());
            final int from = calendar.get(Calendar.YEAR);
            calendar.setTimeInMillis(depreciationCriteria.getToDate());
            final int to = calendar.get(Calendar.YEAR);
            log.debug("Financial Year From :: " + from);
            log.debug("Financial Year To :: " + to);
            if (from != to)
                depreciationCriteria.setFinancialYear(from + "-" + Integer.toString(to).substring(2, 4));
            else
                depreciationCriteria.setFinancialYear(from + "-" + Integer.toString(to + 1).substring(2, 4));
        } else if (depreciationCriteria.getFromDate() == null && depreciationCriteria.getToDate() == null) {

            final String url = applicationProperties.getEgfMastersHost()
                    + applicationProperties.getEgfFinancialYearSearchPath() + "?tenantId ="
                    + depreciationCriteria.getTenantId() + "&finYearRange=" + financialYear;

            log.debug("Financial Year Search URL :: " + url);
            final List<FinancialYearContract> financialYearContracts = restTemplate
                    .postForObject(url, new RequestInfoWrapper(requestInfo), FinancialYearContractResponse.class)
                    .getFinancialYears();
            log.debug("Financial Year Response :: " + financialYearContracts);
            if (financialYearContracts != null && !financialYearContracts.isEmpty()) {
                final FinancialYearContract financialYearContract = financialYearContracts.get(0);
                depreciationCriteria.setToDate(financialYearContract.getEndingDate().getTime());
                depreciationCriteria.setFromDate(financialYearContract.getStartingDate().getTime());
            } else
                throw new RuntimeException("There is no data present for financial year :: " + financialYear);
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

    public DepreciationReportResponse getDepreciationReport(final RequestInfo requestInfo,
            final DepreciationReportCriteria depreciationReportCriteria) {
        final List<DepreciationReportCriteria> assets = depreciationRepository.getDepreciatedAsset(depreciationReportCriteria);
        final DepreciationReportResponse assetResponse = new DepreciationReportResponse();
        assetResponse.setDepreciationReportCriteria(assets);
        assetResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo));
        return assetResponse;
    }
}
