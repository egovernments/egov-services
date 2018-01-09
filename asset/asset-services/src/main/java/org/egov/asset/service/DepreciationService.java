package org.egov.asset.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.Depreciation;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.model.DepreciationInputs;
import org.egov.asset.model.DepreciationReportCriteria;
import org.egov.asset.model.VoucherAccountCodeDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.DepreciationStatus;
import org.egov.asset.model.enums.ReasonForFailure;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.model.enums.TransactionType;
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
   
    public DepreciationResponse depreciateAsset(final DepreciationRequest depreciationRequest,
            final HttpHeaders headers) {

        final RequestInfo requestInfo = depreciationRequest.getRequestInfo();
        getFinancialYearData(depreciationRequest,requestInfo);
        
        Depreciation depreciation = depreciateAssets(depreciationRequest);

        depreciation.setAuditDetails(assetCommonService.getAuditDetails(requestInfo));

        kafkaTemplate.send(applicationProperties.getSaveDepreciationTopic(), depreciation);

        return DepreciationResponse.builder().depreciation(depreciation).responseInfo(null).build();
}
    
    /***
     * Calculates the Depreciation and returns , Calculates the CurrentValue request
     * and sends it to currentValue Service
     * 
     * @param depreciationCriteria
     * @param requestInfo
     * @return
     */
    public Depreciation depreciateAssets(DepreciationRequest depreciationRequest) {

        DepreciationCriteria depreciationCriteria = depreciationRequest.getDepreciationCriteria();
        RequestInfo requestInfo = depreciationRequest.getRequestInfo();

        List<DepreciationInputs> depreciationInputsList = depreciationRepository
                .getDepreciationInputs(depreciationCriteria);

        log.info("the list of assets fetched : " + depreciationInputsList);

        List<DepreciationDetail> depreciationDetailsList = new ArrayList<>();
        List<AssetCurrentValue> currentValues = new ArrayList<>();
        Depreciation depreciation = null;

        // calculating the depreciation and adding the currenVal and DepDetail to the
        // lists
        calculateDepreciationAndCurrentValue(depreciationInputsList, depreciationDetailsList, currentValues,
                depreciationCriteria.getFromDate(), depreciationCriteria.getToDate());

        // FIXME TODO voucher integration

        getDepreciationdetailsId(depreciationDetailsList);
        // sending dep/currval objects to respective create async methods
        depreciation = Depreciation.builder().depreciationCriteria(depreciationCriteria)
                .depreciationDetails(depreciationDetailsList).build();
        depreciation.setTenantId(depreciationCriteria.getTenantId());

        currentValueService.createCurrentValueAsync(
                AssetCurrentValueRequest.builder().assetCurrentValues(currentValues).requestInfo(requestInfo).build());

        return depreciation;
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

    private void getFinancialYearData(DepreciationRequest depreciationRequest, final RequestInfo requestInfo) {

        DepreciationCriteria criteria = depreciationRequest.getDepreciationCriteria();
        final Long todate = criteria.getToDate();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date depreciationDate = new Date(todate);

        final String url = applicationProperties.getEgfMastersHost()
                + applicationProperties.getEgfFinancialYearSearchPath() + "?tenantId ="
                + criteria.getTenantId() + "&asOnDate=" + format.format(depreciationDate);

        log.debug("Financial Year Search URL :: " + url);
        final List<FinancialYearContract> financialYearContracts = restTemplate
                .postForObject(url, new RequestInfoWrapper(requestInfo), FinancialYearContractResponse.class)
                .getFinancialYears();
        log.debug("Financial Year Response :: " + financialYearContracts);
        log.debug("Financial Year Response :: " + financialYearContracts);
        if (financialYearContracts != null && !financialYearContracts.isEmpty()) {
            final FinancialYearContract financialYearContract = financialYearContracts.get(0);
            criteria.setFromDate(financialYearContract.getStartingDate().getTime());
            criteria.setFinancialYear(financialYearContract.getFinYearRange());
        } else

            throw new RuntimeException(" No Financial Found For The Given ToDate:: " + depreciationDate);
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

    /***
     * Calculate the Depreciation value and the current and populate the respective
     * lists for the values
     * 
     * @param depreciationInputsList
     * @param depDetList
     * @param currValList
     * @param fromDate
     * @param toDate
     */

    private void calculateDepreciationAndCurrentValue(List<DepreciationInputs> depreciationInputsList,
            List<DepreciationDetail> depDetList, List<AssetCurrentValue> currValList, Long fromDate, Long toDate) {
        log.info("depreciationInputsList.size()" + depreciationInputsList.size());

        depreciationInputsList.forEach(depreciation -> {

            log.info("the current depreciation input object : " + depreciation);
            BigDecimal minValue = BigDecimal.ONE;
            ReasonForFailure reason = null;
            DepreciationStatus status = DepreciationStatus.FAIL;
            BigDecimal amtToBeDepreciated = null;
            BigDecimal valueAfterDep = null;

            BigDecimal valueAfterDepRounded = null;
            BigDecimal amtToBeDepreciatedRounded = null;
            Double depreciationRate = null;

            // getting the indvidual fromDate
            Long invidualFromDate = getFromDateForIndvidualAsset(depreciation, fromDate);
            // checking if its year wise depreciation
            if (depreciation.getEnableYearwiseDepreciation()) {
                depreciationRate = depreciation.getYearwiseDepreciationRate();
            } else
                depreciationRate = depreciation.getDepreciationRate();

            if (depreciation.getCurrentValue() != null)
                if (depreciation.getCurrentValue().compareTo(minValue) <= 0) {

                    reason = ReasonForFailure.ASSET_IS_FULLY_DEPRECIATED_TO_MINIMUN_VALUE;
                } else if (null == depreciationRate) {

                    reason = ReasonForFailure.DEPRECIATION_RATE_NOT_FOUND;
                }

                else {

                    status = DepreciationStatus.SUCCESS;

                    // getting the amt to be depreciated
                    amtToBeDepreciated = getAmountToBeDepreciated(depreciation, invidualFromDate, toDate);

                    amtToBeDepreciatedRounded = new BigDecimal(
                            amtToBeDepreciated.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    System.err.println("amtToBeDepreciatedRounded------------" + amtToBeDepreciatedRounded);

                    // calculating the valueAfterDepreciation
                    valueAfterDep = depreciation.getCurrentValue().subtract(amtToBeDepreciated);

                    valueAfterDepRounded = new BigDecimal(valueAfterDep.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    System.err.println("valueAfterDepRounded------------" + valueAfterDepRounded);

                    if (valueAfterDepRounded.doubleValue() < 1)
                        valueAfterDepRounded = BigDecimal.ONE;

                    // adding currval to the currval list
                    currValList.add(AssetCurrentValue.builder().assetId(depreciation.getAssetId())
                            .assetTranType(TransactionType.DEPRECIATION).currentAmount(valueAfterDepRounded)
                            .tenantId(depreciation.getTenantId()).build());
                }

            // adding the depreciation detail object to list
            depDetList.add(DepreciationDetail.builder().assetId(depreciation.getAssetId()).reasonForFailure(reason)
                    .assetCode(depreciation.getAssetCode()).assetName(depreciation.getAssetName())
                    .assetCategoryName(depreciation.getAssetCategoryName()).department(depreciation.getDepartment())
                    .depreciationRate(depreciationRate).depreciationValue(amtToBeDepreciatedRounded)
                    .fromDate(invidualFromDate)
                    .valueAfterDepreciation(valueAfterDepRounded).valueBeforeDepreciation(depreciation.getCurrentValue())
                    .status(status)
                    .build());
        });
    }

    private Long getFromDateForIndvidualAsset(DepreciationInputs depInputs, Long fromDate) {

        // deciding the from date from the last depreciation date
        if (depInputs.getLastDepreciationDate() != null && depInputs.getLastDepreciationDate().compareTo(fromDate) >= 0) {
            fromDate = depInputs.getLastDepreciationDate();
            fromDate += 86400000l; // adding one day in milli seconds to start depreciation from next day
        } else if (depInputs.getDateOfCreation() > fromDate) {
            fromDate = depInputs.getDateOfCreation();
            // fromDate += 86400000l;
        }
        return fromDate;
    }
    
    /***
     * to find the Amount to be depreciated for every Asset from the
     * DepreciationInput Object
     * 
     * @param depInputs
     * @param indvidualFromDate
     * @param toDate
     * @return
     */
    private BigDecimal getAmountToBeDepreciated(DepreciationInputs depInputs, Long indvidualFromDate, Long toDate) {

        System.err.println("depInputs" + depInputs);

        // getting the no of days betweeen the from and todate (including both from and
        // to date)
        Long noOfDays = ((toDate - indvidualFromDate) / 1000 / 60 / 60 / 24) + 1;
        log.info("no of days between fromdate : " + indvidualFromDate + " and todate : " + toDate + " is : " + noOfDays);

        // deprate for the no of days = no of days * calculated dep rate per day
        Double depRateForGivenPeriod = noOfDays * depInputs.getDepreciationRate() / 365;
        log.info("dep rate for given period is : " + depRateForGivenPeriod);

        // returning the calculated amt to be depreciated using the grossvalue from
        // dep inputs and depreciation rate for given period ,straight line method 
        if (depInputs.getDepreciationSum() != null && depInputs.getDepreciationMethod().equals(DepreciationMethod.STRAIGHT_LINE_METHOD))
            return BigDecimal.valueOf((depInputs.getCurrentValue()).add(depInputs.getDepreciationSum()).doubleValue()
                    * (depRateForGivenPeriod / 100));
        else
            //written down value method
            return BigDecimal.valueOf((depInputs.getCurrentValue()).doubleValue() * (depRateForGivenPeriod / 100));

    }
    
    private void getDepreciationdetailsId(List<DepreciationDetail> depreciationDetailsList) {

        final List<Long> idList = sequenceGenService.getIds(depreciationDetailsList.size(),
                        Sequence.DEPRECIATIONSEQUENCE.toString());
        int i = 0;
        for (DepreciationDetail depreciationDetail : depreciationDetailsList)
                depreciationDetail.setId(idList.get(i++));
}
    


}
