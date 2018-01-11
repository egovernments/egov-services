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

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.DepreciationReportResponse;
import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.contract.DepreciationResponse;
import org.egov.asset.contract.FinancialYearContract;
import org.egov.asset.contract.FinancialYearContractResponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.contract.VoucherRequest;
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
        getFinancialYearData(depreciationRequest, requestInfo);

        final Depreciation depreciation = depreciateAssets(depreciationRequest, headers);

        depreciation.setAuditDetails(assetCommonService.getAuditDetails(requestInfo));

        kafkaTemplate.send(applicationProperties.getSaveDepreciationTopic(), depreciation);

        return DepreciationResponse.builder().depreciation(depreciation).responseInfo(null).build();
    }

    /***
     * Calculates the Depreciation and returns , Calculates the CurrentValue request and sends it to currentValue Service
     *
     * @param depreciationCriteria
     * @param requestInfo
     * @return
     */
    public Depreciation depreciateAssets(final DepreciationRequest depreciationRequest, final HttpHeaders headers) {

        final DepreciationCriteria depreciationCriteria = depreciationRequest.getDepreciationCriteria();
        final RequestInfo requestInfo = depreciationRequest.getRequestInfo();

        final List<DepreciationInputs> depreciationInputsList = depreciationRepository
                .getDepreciationInputs(depreciationCriteria);

        log.info("the list of assets fetched : " + depreciationInputsList);

        final List<DepreciationDetail> depreciationDetailsList = new ArrayList<>();
        final List<AssetCurrentValue> currentValues = new ArrayList<>();
        Depreciation depreciation = null;

        // calculating the depreciation and adding the currenVal and DepDetail to the
        // lists
        calculateDepreciationAndCurrentValue(depreciationInputsList, depreciationDetailsList, currentValues,
                depreciationCriteria.getFromDate(), depreciationCriteria.getToDate());

        getDepreciationdetailsId(depreciationDetailsList);
        // sending dep/currval objects to respective create async methods
        depreciation = Depreciation.builder().depreciationCriteria(depreciationCriteria)
                .depreciationDetails(depreciationDetailsList).build();
        depreciation.setTenantId(depreciationCriteria.getTenantId());
        final String tenantId = depreciationCriteria.getTenantId();
        if (currentValues != null && !currentValues.isEmpty())
            currentValueService.createCurrentValueAsync(
                    AssetCurrentValueRequest.builder().assetCurrentValues(currentValues).requestInfo(requestInfo).build());
        // voucher creation
        validationAndGenerationDepreciationVoucherDetails(depreciationInputsList, depreciationDetailsList, currentValues,
                tenantId, headers, requestInfo);

        return depreciation;
    }

    public void validationAndGenerationDepreciationVoucherDetails(final List<DepreciationInputs> depreciationInputsList,
            final List<DepreciationDetail> depreciationDetailsList, final List<AssetCurrentValue> currValList, final String tenantId,
            final HttpHeaders headers, final RequestInfo requestInfo) {
        if (assetConfigurationService.getEnabledVoucherGeneration(AssetConfigurationKeys.ENABLEVOUCHERGENERATION,
                tenantId))
            depreciationInputsList.forEach(depreciation -> {

                log.info("Commencing voucher generation for depreciation");
                final Map<Long, VoucherAccountCodeDetails> ledgerMap = new HashMap<>();
                final List<VoucherAccountCodeDetails> accountCodeDetails = new ArrayList<>();

                final Long departmentId = depreciation.getDepartment();

                log.debug("Asset Department ID :: " + departmentId);
                for (final DepreciationDetail depreciationDetail : depreciationDetailsList)
                    if (depreciationDetail!=null && depreciationDetail.getStatus().equals(DepreciationStatus.SUCCESS)) {

                        final BigDecimal amount = depreciationDetail.getDepreciationValue();
                        log.debug("Depreciation Amount :: " + amount);
                        final Long aDAccount = depreciation.getAccumulatedDepreciationAccount();
                        log.debug("Accumulated Depreciation Account :: " + aDAccount);
                        final Long dEAccount = depreciation.getDepreciationExpenseAccount();
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

                        log.debug("Ledger Map :: " + ledgerMap);
                        accountCodeDetails.addAll(ledgerMap.values());
                        log.debug("Depreciation Account Code Details :: " + accountCodeDetails);
                        validateDepreciationSubledgerDetails(requestInfo, tenantId, ledgerMap.keySet());
                        if (!accountCodeDetails.isEmpty()) {
                            final VoucherRequest voucherRequest = voucherService.createDepreciationVoucherRequest(
                                    depreciationInputsList, departmentId, accountCodeDetails, tenantId, headers);
                            log.debug("Voucher Request for Depreciation :: " + voucherRequest);

                            final String voucherNumber = voucherService.createVoucher(voucherRequest, tenantId, headers);
                            log.debug("Voucher Number for Depreciation :: " + voucherNumber);
                            setVoucherIdToDepreciaitionDetails(voucherNumber, depreciationInputsList, depreciationDetailsList);
                        }
                    }

            });

    }

    private void setVoucherIdToDepreciaitionDetails(final String voucherNumber,
            final List<DepreciationInputs> depreciationInputsList,
            final List<DepreciationDetail> depreciationDetailsList) {
        for (final DepreciationDetail depreciationDetail : depreciationDetailsList) {

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

    private void getFinancialYearData(final DepreciationRequest depreciationRequest, final RequestInfo requestInfo) {

        final DepreciationCriteria criteria = depreciationRequest.getDepreciationCriteria();
        final Long todate = criteria.getToDate();
        final DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        final Date depreciationDate = new Date(todate);

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
     * Calculate the Depreciation value and the current and populate the respective lists for the values
     *
     * @param depreciationInputsList
     * @param depDetList
     * @param currValList
     * @param fromDate
     * @param toDate
     */

    private void calculateDepreciationAndCurrentValue(final List<DepreciationInputs> depreciationInputsList,
            final List<DepreciationDetail> depDetList, final List<AssetCurrentValue> currValList, final Long fromDate,
            final Long toDate) {
        log.info("depreciationInputsList.size()" + depreciationInputsList.size());

        depreciationInputsList.forEach(depreciation -> {

            log.info("the current depreciation input object : " + depreciation);
            final BigDecimal minValue = BigDecimal.ONE;
            ReasonForFailure reason = null;
            DepreciationStatus status = DepreciationStatus.FAIL;
            BigDecimal amtToBeDepreciated = BigDecimal.ZERO;
            BigDecimal valueAfterDep = BigDecimal.ZERO;

            BigDecimal valueAfterDepRounded = BigDecimal.ZERO;
            BigDecimal amtToBeDepreciatedRounded = BigDecimal.ZERO;
            Double depreciationRate;

            // getting the indvidual fromDate
            final Long invidualFromDate = getFromDateForIndvidualAsset(depreciation, fromDate);
            // checking if its year wise depreciation
            if (depreciation.getEnableYearwiseDepreciation())
                depreciationRate = depreciation.getYearwiseDepreciationRate();
            else
                depreciationRate = depreciation.getDepreciationRate();

            if (depreciation.getCurrentValue().compareTo(BigDecimal.ZERO) != 0)
                if (depreciationRate==0.0)
                    reason = ReasonForFailure.DEPRECIATION_RATE_NOT_FOUND;
                else if (depreciation.getCurrentValue().compareTo(minValue) <= 0)
                    reason = ReasonForFailure.ASSET_IS_FULLY_DEPRECIATED_TO_MINIMUN_VALUE;
                else if (depreciation.getCurrentValue().compareTo(new BigDecimal(5000)) < 0)
                    reason = ReasonForFailure.ASSET_IS_FULLY_DEPRECIATED_TO_MINIMUN_VALUE;
                else {

                    status = DepreciationStatus.SUCCESS;

                    // getting the amt to be depreciated
                    amtToBeDepreciated = getAmountToBeDepreciated(depreciation, invidualFromDate, toDate);

                    amtToBeDepreciatedRounded = new BigDecimal(
                            amtToBeDepreciated.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    log.info("amtToBeDepreciatedRounded------------" + amtToBeDepreciatedRounded);

                    // calculating the valueAfterDepreciation
                    valueAfterDep = depreciation.getCurrentValue().subtract(amtToBeDepreciated);

                    valueAfterDepRounded = new BigDecimal(valueAfterDep.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    log.info("valueAfterDepRounded------------" + valueAfterDepRounded);

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

    private Long getFromDateForIndvidualAsset(final DepreciationInputs depInputs, Long fromDate) {

        // deciding the from date from the last depreciation date
        if (depInputs.getLastDepreciationDate() != null && depInputs.getLastDepreciationDate().compareTo(fromDate) >= 0) {
            fromDate = depInputs.getLastDepreciationDate();
            fromDate += 86400000l; // adding one day in milli seconds to start depreciation from next day
        } else if (depInputs.getDateOfCreation() > fromDate)
            fromDate = depInputs.getDateOfCreation();
        // fromDate += 86400000l;
        return fromDate;
    }

    /***
     * to find the Amount to be depreciated for every Asset from the DepreciationInput Object
     *
     * @param depInputs
     * @param indvidualFromDate
     * @param toDate
     * @return
     */
    private BigDecimal getAmountToBeDepreciated(final DepreciationInputs depInputs, final Long indvidualFromDate,
            final Long toDate) {

        log.info("depInputs" + depInputs);

        // getting the no of days betweeen the from and todate (including both from and
        // to date)
        final Long noOfDays = (toDate - indvidualFromDate) / 1000 / 60 / 60 / 24 + 1;
        log.info("no of days between fromdate : " + indvidualFromDate + " and todate : " + toDate + " is : " + noOfDays);

        // deprate for the no of days = no of days * calculated dep rate per day
        final Double depRateForGivenPeriod = noOfDays * depInputs.getDepreciationRate() / 365;
        log.info("dep rate for given period is : " + depRateForGivenPeriod);

        // returning the calculated amt to be depreciated using the grossvalue from
        // dep inputs and depreciation rate for given period ,straight line method
        if (depInputs.getDepreciationSum() != null
                && depInputs.getDepreciationMethod().equals(DepreciationMethod.STRAIGHT_LINE_METHOD))
            return BigDecimal.valueOf(depInputs.getCurrentValue().add(depInputs.getDepreciationSum()).doubleValue()
                    * (depRateForGivenPeriod / 100));
        else
            // written down value method
            return BigDecimal.valueOf(depInputs.getCurrentValue().doubleValue() * (depRateForGivenPeriod / 100));

    }

    private void getDepreciationdetailsId(final List<DepreciationDetail> depreciationDetailsList) {

        final List<Long> idList = sequenceGenService.getIds(depreciationDetailsList.size(),
                Sequence.DEPRECIATIONSEQUENCE.toString());
        int i = 0;
        for (final DepreciationDetail depreciationDetail : depreciationDetailsList)
            depreciationDetail.setId(idList.get(i++));
    }

}
