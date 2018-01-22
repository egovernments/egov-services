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

package org.egov.asset.web.validator;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.FunctionResponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.YearWiseDepreciation;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.AssetStatusObjectName;
import org.egov.asset.model.enums.Status;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.service.AssetCommonService;
import org.egov.asset.service.AssetConfigurationService;
import org.egov.asset.service.AssetMasterService;
import org.egov.asset.service.AssetService;
import org.egov.asset.service.CurrentValueService;
import org.egov.asset.util.ApplicationConstants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetValidator {

    @Autowired
    private AssetCategoryValidator assetCategoryValidator;

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetMasterService assetMasterService;

    @Autowired
    private CurrentValueService currentValueService;

    @Autowired
    private AssetConfigurationService assetConfigurationService;

    @Autowired
    private AssetCommonService assetCommonService;

    @Autowired
    private ApplicationConstants applicationConstants;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private RestTemplate restTemplate;

    public void validateAsset(final AssetRequest assetRequest) {
        final AssetCategory assetCategory = findAssetCategory(assetRequest);
        final Asset asset = assetRequest.getAsset();
        if (asset.getEnableYearWiseDepreciation() != null
                && validateAssetCategoryForLand(assetCategory.getAssetCategoryType()))
            asset.setDepreciationRate(null);
        else
            validateYearWiseDepreciationRate(asset);
        if (Status.CAPITALIZED.toString().equals(asset.getStatus())) {
            final BigDecimal grossValue = asset.getGrossValue();
            final BigDecimal accumulatedDepreciationValue = asset.getAccumulatedDepreciation();
            if (grossValue == null)
                throw new RuntimeException("Asset Gross Value can not be null.");
            if (grossValue.compareTo(BigDecimal.ONE) < 0)
                throw new RuntimeException("Asset Gross Value can not be less than 1.");
            if (accumulatedDepreciationValue != null && accumulatedDepreciationValue.compareTo(BigDecimal.ZERO) == -1)
                throw new RuntimeException("Asset Accumulated Depreciation Value can not be negative.");
            if (accumulatedDepreciationValue != null && (accumulatedDepreciationValue.compareTo(grossValue) == 0
                    || accumulatedDepreciationValue.compareTo(grossValue) == 1))
                throw new RuntimeException(
                        "Asset Accumulated Depreciation Value can not be equal to or greater than asset gross value.");
        }
    }

    public void validateYearWiseDepreciationRate(final Asset asset) {
        if (asset.getEnableYearWiseDepreciation() != null && asset.getEnableYearWiseDepreciation()) {
            final List<String> finacialYears = new ArrayList<String>();
            for (final YearWiseDepreciation ywd : asset.getYearWiseDepreciation()) {
                finacialYears.add(ywd.getFinancialYear());
                final Double depreciationRate = ywd.getDepreciationRate();
                assetCommonService.validateDepreciationRateValue(depreciationRate);
                ywd.setDepreciationRate(assetCommonService.getDepreciationRate(depreciationRate));
            }
            checkDuplicateFinancialYear(finacialYears);
        } else if (asset.getEnableYearWiseDepreciation() != null && !asset.getEnableYearWiseDepreciation()) {
            final Double depreciationRate = asset.getDepreciationRate();
            assetCommonService.validateDepreciationRateValue(asset.getDepreciationRate());
            asset.setDepreciationRate(assetCommonService.getDepreciationRate(depreciationRate));
        }

    }

    private void checkDuplicateFinancialYear(final List<String> finacialYears) {
        if (!finacialYears.isEmpty()) {
            final Iterator<String> itr = finacialYears.iterator();
            if (itr.hasNext()) {
                final String finYear = itr.next();
                while (itr.hasNext()) {
                    final String current = itr.next();
                    if (finYear.equalsIgnoreCase(current))
                        throw new RuntimeException("Can not contain duplicate financial years");
                }
            }
        }
    }

    public AssetCategory findAssetCategory(final AssetRequest assetRequest) {
        final AssetCategory assetCategory = assetRequest.getAsset().getAssetCategory();
        final List<AssetCategory> assetCategories = assetCategoryValidator.findByIdAndCode(assetCategory.getId(),
                assetCategory.getCode(), assetRequest.getAsset().getTenantId());

        if (assetCategories.isEmpty())
            throw new RuntimeException("Invalid asset category");
        else
            return assetCategories.get(0);
    }

    public void validateRevaluationCriteria(final RevaluationCriteria revaluationCriteria) {
        if (revaluationCriteria.getFromDate() == null && revaluationCriteria.getToDate() != null)
            throw new RuntimeException("Invalid Search! from date required");
        if (revaluationCriteria.getFromDate() != null && revaluationCriteria.getToDate() == null)
            throw new RuntimeException("Invalid Search! to date required");
        if (revaluationCriteria.getFromDate() != null && revaluationCriteria.getToDate() != null
                && revaluationCriteria.getToDate().compareTo(revaluationCriteria.getFromDate()) == -1)
            throw new RuntimeException("Invalid Search! to date should not be less than from date");
        if (revaluationCriteria.getFromDate() != null && revaluationCriteria.getToDate() != null
                && revaluationCriteria.getToDate().compareTo(revaluationCriteria.getFromDate()) == 0)
            throw new RuntimeException("Invalid Search! to date should not be equal to from date");
    }

    public void validateDisposalCriteria(final DisposalCriteria disposalCriteria) {
        if (disposalCriteria.getFromDate() == null && disposalCriteria.getToDate() != null)
            throw new RuntimeException("Invalid Search! from date required");
        if (disposalCriteria.getFromDate() != null && disposalCriteria.getToDate() == null)
            throw new RuntimeException("Invalid Search! to date required");

        if (disposalCriteria.getFromDate() != null && disposalCriteria.getToDate() != null
                && disposalCriteria.getToDate().compareTo(disposalCriteria.getFromDate()) == -1)
            throw new RuntimeException("Invalid Search! to date should not be less than from date");

        if (disposalCriteria.getFromDate() != null && disposalCriteria.getToDate() != null
                && disposalCriteria.getToDate().compareTo(disposalCriteria.getFromDate()) == 0)
            throw new RuntimeException("Invalid Search! to date should not be equal to from date");
    }

    public void validateDisposal(final DisposalRequest disposalRequest) {
        final Disposal disposal = disposalRequest.getDisposal();
        final String tenantId = disposal.getTenantId();
        final Asset asset = assetService.getAsset(tenantId, disposal.getAssetId(), disposalRequest.getRequestInfo());
        log.debug("Asset For Disposal :: " + asset);
        validateAssetForCapitalizedStatus(asset);
        if (StringUtils.isEmpty(disposal.getBuyerName()))
            throw new RuntimeException("Buyer Name should be present for disposing asset : " + asset.getName());

        if (StringUtils.isEmpty(disposal.getBuyerAddress()))
            throw new RuntimeException("Buyer Address should be present for disposing asset : " + asset.getName());

        if (StringUtils.isEmpty(disposal.getDisposalReason()))
            throw new RuntimeException("Disposal Reason should be present for disposing asset : " + asset.getName());

        if (disposal.getSaleValue() == null)
            throw new RuntimeException("Sale Value should be present for disposing asset : " + asset.getName());

        verifyPanCardAndAdhaarCardForAssetSale(disposal);
        if (getEnableYearWiseDepreciation(tenantId)) {
            validateAssetCategoryForVoucherGeneration(asset);

            if (asset.getAssetCategory() != null && asset.getAssetCategory().getAssetAccount() == null)
                throw new RuntimeException("Asset account should be present for disposing asset : " + asset.getName());

            if (disposal.getAssetSaleAccount() == null)
                throw new RuntimeException(
                        "Asset sale account should be present for asset disposal voucher generation");
        }
    }

    private void verifyPanCardAndAdhaarCardForAssetSale(final Disposal disposal) {
        final String adhaarcardNumber = disposal.getAadharCardNumber();
        final String pancardNumber = disposal.getPanCardNumber();
        final Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        final Matcher matcher = pattern.matcher(pancardNumber);

        final boolean transactionTypeSaleCheck = TransactionType.SALE.compareTo(disposal.getTransactionType()) == 0;

        if (transactionTypeSaleCheck && StringUtils.isEmpty(adhaarcardNumber))
            throw new RuntimeException("Aadhar Card Number is necessary for asset sale");

        if (transactionTypeSaleCheck && !StringUtils.isEmpty(adhaarcardNumber) && adhaarcardNumber.length() < 12
                && !adhaarcardNumber.matches("\\d+"))
            throw new RuntimeException("Aadhar Card Number should be numeric and should have length 12");

        if (transactionTypeSaleCheck && StringUtils.isEmpty(pancardNumber))
            throw new RuntimeException("PAN Card Number is necessary for asset sale");

        if (transactionTypeSaleCheck && !StringUtils.isEmpty(pancardNumber) && !matcher.matches())
            throw new RuntimeException("PAN Card Number Should be in Format : ABCDE1234F");
    }

    private void validateAssetForCapitalizedStatus(final Asset asset) {
        final List<AssetStatus> assetStatus = assetMasterService.getStatuses(AssetStatusObjectName.ASSETMASTER,
                Status.CAPITALIZED, asset.getTenantId());
        log.debug("asset status ::" + assetStatus);
        if (!assetStatus.isEmpty()) {
            final String status = assetStatus.get(0).getStatusValues().get(0).getCode();
            if (!status.equals(asset.getStatus()))
                throw new RuntimeException("Status of Asset " + asset.getName()
                        + " Should be CAPITALIZED for Revaluation, Depreciation and Disposal/sale");
        } else
            throw new RuntimeException(
                    "Status of asset :" + asset.getName() + "doesn't exists for tenant id : " + asset.getTenantId());
    }

    public void validateRevaluation(final RevaluationRequest revaluationRequest) {
        final Revaluation revaluation = revaluationRequest.getRevaluation();
        final String tenantId = revaluation.getTenantId();
        final RequestInfo requestInfo = revaluationRequest.getRequestInfo();
        final Asset asset = assetService.getAsset(tenantId, revaluation.getAssetId(), requestInfo);
        log.debug("Asset For Revaluation :: " + asset);
        validateAssetForCapitalizedStatus(asset);
        final boolean enableVoucherGeneration = getEnableYearWiseDepreciation(tenantId);
        if (enableVoucherGeneration) {
            validateAssetCategoryForVoucherGeneration(asset);
            if (revaluation.getFund() == null)
                throw new RuntimeException(
                        "Fund from financials is necessary for Asset Revaluation,Asset Depreciation and Asset Sale/Disposal");
            if (revaluation.getFunction() == null)
                throw new RuntimeException("Function from financials is necessary for asset revaluation");
        }

        final TypeOfChangeEnum typeOfChange = validateRevaluationForTypeOfChange(revaluation, asset);

        final BigDecimal revaluationAmount = revaluation.getRevaluationAmount();

        if (revaluationAmount == null)
            throw new RuntimeException(
                    "The amount by which the value is increased/decreased is necessary for revaluation");

        BigDecimal assetCurrentAmount = null;
        final Set<Long> ids = new HashSet<>();
        ids.add(revaluation.getAssetId());

        final List<AssetCurrentValue> assetCurrentValues = currentValueService
                .getCurrentValues(ids, tenantId, requestInfo).getAssetCurrentValues();
        if (assetCurrentValues != null && !assetCurrentValues.isEmpty())
            assetCurrentAmount = assetCurrentValues.get(0).getCurrentAmount();
        else if (asset.getAccumulatedDepreciation() != null)
            assetCurrentAmount = asset.getGrossValue().subtract(asset.getAccumulatedDepreciation());
        else
            assetCurrentAmount = asset.getGrossValue();

        log.debug("Asset Current Value :: " + assetCurrentAmount);

        revaluation.setCurrentCapitalizedValue(assetCurrentAmount);

        final BigDecimal valueAfterRevaluation = revaluation.getValueAfterRevaluation();

        if (valueAfterRevaluation == null)
            throw new RuntimeException("Value After Revaluation should have some value.");

        log.debug("Asset Value after Revaluation :: " + valueAfterRevaluation);

        if (typeOfChange != null && TypeOfChangeEnum.DECREASED.compareTo(typeOfChange) == 0
                && (valueAfterRevaluation.compareTo(assetCurrentAmount) == 0
                        || valueAfterRevaluation.compareTo(assetCurrentAmount) == 1))
            throw new RuntimeException(
                    "Decrease in amount should not be equal or greater than the current value of the asset. current value of asset is :: "
                            + assetCurrentAmount + " and value after revaluation is :: " + valueAfterRevaluation);

        validateRevaluationDate(revaluation);

        validateValueAfterRevaluation(assetCurrentAmount, revaluationAmount, valueAfterRevaluation, typeOfChange);

        // Setting Default Revaluation Status as APPROVED
        revaluation.setStatus(Status.APPROVED.toString());

    }

    private void validateValueAfterRevaluation(final BigDecimal assetCurrentAmount, final BigDecimal revaluationAmount,
            final BigDecimal valueAfterRevaluation, final TypeOfChangeEnum typeOfChange) {
        BigDecimal revaluatedValue = null;
        if (typeOfChange != null && TypeOfChangeEnum.INCREASED.compareTo(typeOfChange) == 0)
            revaluatedValue = assetCurrentAmount.add(revaluationAmount);
        else if (typeOfChange != null && TypeOfChangeEnum.DECREASED.compareTo(typeOfChange) == 0)
            revaluatedValue = assetCurrentAmount.subtract(revaluationAmount);

        if (revaluatedValue != null && revaluatedValue.compareTo(valueAfterRevaluation) != 0)
            throw new RuntimeException("Value after revaluation of asset should be :: " + revaluatedValue
                    + " but the value is :: " + valueAfterRevaluation);
    }

    private void validateRevaluationDate(final Revaluation revaluation) {
        final Long revaluationDate = revaluation.getRevaluationDate();
        if (revaluationDate == null)
            throw new RuntimeException("Revaluation Date is Required");

        if (revaluationDate > new Date().getTime())
            throw new RuntimeException("Revaluation Date should not be greater than current date.");
    }

    private TypeOfChangeEnum validateRevaluationForTypeOfChange(final Revaluation revaluation, final Asset asset) {
        final TypeOfChangeEnum typeOfChange = revaluation.getTypeOfChange();
        if (typeOfChange == null)
            throw new RuntimeException("Type Of Change is necessary for asset revaluation");

        if (getEnableYearWiseDepreciation(revaluation.getTenantId())) {
            if (typeOfChange != null && TypeOfChangeEnum.DECREASED.compareTo(typeOfChange) == 0
                    && revaluation.getFixedAssetsWrittenOffAccount() == null)
                throw new RuntimeException("Fixed Asset Written Off Account is necessary for asset " + asset.getName()
                        + " voucher generation for revaluation");

            final AssetCategory assetCategory = asset.getAssetCategory();

            if (typeOfChange != null
                    && (TypeOfChangeEnum.DECREASED.compareTo(typeOfChange) == 0
                            || TypeOfChangeEnum.INCREASED.compareTo(typeOfChange) == 0)
                    && assetCategory != null && assetCategory.getAssetAccount() == null)
                throw new RuntimeException("Asset Account is necessary for asset " + asset.getName()
                        + " voucher generation for revaluation");

            if (typeOfChange != null && TypeOfChangeEnum.INCREASED.compareTo(typeOfChange) == 0 && assetCategory != null
                    && assetCategory.getRevaluationReserveAccount() == null)
                throw new RuntimeException("Revaluation Reserve Account is necessary for asset " + asset.getName()
                        + " for voucher generation.");
        }
        return typeOfChange;
    }

    private boolean getEnableYearWiseDepreciation(final String tenantId) {
        return assetConfigurationService.getEnabledVoucherGeneration(AssetConfigurationKeys.ENABLEVOUCHERGENERATION,
                tenantId);
    }

    private void validateAssetCategoryForVoucherGeneration(final Asset asset) {
        if (asset.getAssetCategory() == null)
            throw new RuntimeException(
                    "Asset Category should be present for asset " + asset.getName() + " for voucher generation");
    }

    public void validateAssetForUpdate(final AssetRequest assetRequest) {
        final Asset assetFromReq = assetRequest.getAsset();
        final Asset asset = assetService.getAsset(assetFromReq.getTenantId(), assetFromReq.getId(),
                assetRequest.getRequestInfo());
        if (!assetFromReq.getCode().equalsIgnoreCase(asset.getCode()))
            throw new RuntimeException("Invalid Asset Code for Asset :: " + asset.getName());
        else
            validateYearWiseDepreciationRate(assetRequest.getAsset());
    }

    public void validateDepreciation(final DepreciationRequest depreciationRequest) {

        final DepreciationCriteria depreciationCriteria = depreciationRequest.getDepreciationCriteria();
        final String tenantId = depreciationCriteria.getTenantId();

        final Double depreciationMinimumValue = Double.valueOf(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.ASSETMINIMUMVALUE, tenantId));
        log.debug("Depreciation Minimum value :: " + depreciationMinimumValue);

        final Long fromDate = depreciationCriteria.getFromDate();
        log.debug("Depreciation Criteria From date :: " + fromDate);
        final Long toDate = depreciationCriteria.getToDate();
        log.debug("Depreciation Criteria To date :: " + toDate);
        final Set<Long> assetIds = depreciationCriteria.getAssetIds();
        log.debug("Asset IDs for Depreciation :: " + assetIds);
        final String status = Status.CAPITALIZED.toString();
        final String finacialYear = depreciationCriteria.getFinancialYear();
        log.debug("Depreciation Criteria Financial Year :: " + finacialYear);
        AssetCriteria assetCriteria = null;

        final boolean assetIdsCheck = assetIds != null && !assetIds.isEmpty();

        final boolean fromAndToDateCheck = fromDate != null && toDate != null;

        log.debug("From and To Date Check :: " + fromAndToDateCheck);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar cal = Calendar.getInstance();

        final boolean finYearCheck = validateFinYearFromDateAndToDate(fromDate, toDate, finacialYear, sdf, cal,
                fromAndToDateCheck);

        log.debug("Financial Year Check :: " + finYearCheck);

        if (!finYearCheck)
            throw new RuntimeException("From Date and To Date should belong to select Financial Year.");

        if (finacialYear == null && fromDate == null && toDate == null)
            throw new RuntimeException(
                    "financialyear and (time period)fromdate,todate both cannot be empty please provide atleast one value.");

        if (fromDate != null && toDate == null)
            throw new RuntimeException("If From Date is selected then To date is mandatory.");

        if (fromDate == null && toDate != null)
            throw new RuntimeException("Please select either financial year or from date in conjunction with to date.");

        if (fromAndToDateCheck && toDate < fromDate)
            throw new RuntimeException("To Date should not be less than From Date.");

        if (finacialYear == null && fromAndToDateCheck && !assetIdsCheck)
            throw new RuntimeException("Asset IDs are mandatory for custom time period.");

        if (finYearCheck && !assetIdsCheck)
            assetCriteria = AssetCriteria.builder().status(status).fromCapitalizedValue(depreciationMinimumValue)
                    .tenantId(tenantId).assetCreatedFrom(fromDate).assetCreatedTo(toDate).build();

        if (finacialYear != null && fromDate == null && toDate != null && assetIdsCheck) {
            final int[] yearRange = Stream.of(finacialYear.split("-")).mapToInt(Integer::parseInt).toArray();
            try {
                final Long finYearStart = getFinancialYearStartDate(sdf, yearRange).getTime();
                assetCriteria = AssetCriteria.builder().id(new ArrayList<Long>(assetIds)).status(status)
                        .fromCapitalizedValue(depreciationMinimumValue).tenantId(tenantId).assetCreatedFrom(finYearStart)
                        .assetCreatedTo(toDate).build();
            } catch (final ParseException e) {
                e.printStackTrace();
            }

        }

        if (finacialYear == null && fromAndToDateCheck && assetIdsCheck)
            assetCriteria = AssetCriteria.builder().id(new ArrayList<Long>(assetIds)).status(status)
                    .fromCapitalizedValue(depreciationMinimumValue).tenantId(tenantId).assetCreatedFrom(fromDate)
                    .assetCreatedTo(toDate)
                    .build();

        if (finacialYear != null && fromDate == null && toDate == null)
            assetCriteria = AssetCriteria.builder().id(new ArrayList<Long>(assetIds)).status(status)
                    .fromCapitalizedValue(depreciationMinimumValue).tenantId(tenantId).build();

        log.debug("Asset Criteria for Asset Search for Depreciation :: " + assetCriteria);

        final List<Asset> assets = assetService.getAssets(assetCriteria, depreciationRequest.getRequestInfo())
                .getAssets();
        log.debug("Assets For Depreciation :: " + assets);

        if (getEnableYearWiseDepreciation(tenantId) && assets != null && !assets.isEmpty())
            for (final Asset asset : assets) {
                final String assetName = asset.getName();
                final AssetCategory assetCategory = asset.getAssetCategory();
                final String assetCategoryName = assetCategory.getName();

                if (validateAssetCategoryForLand(assetCategory.getAssetCategoryType()))
                    throw new RuntimeException("Asset category type is LAND for asset :: " + assetName);

                if (assetCategory.getAccumulatedDepreciationAccount() == null)
                    throw new RuntimeException(
                            "Accumulated Depreciation Account should be present for voucher generation for asset :: "
                                    + assetName + " for asset Category :: " + assetCategoryName);

                if (assetCategory.getDepreciationExpenseAccount() == null)
                    throw new RuntimeException(
                            "Depreciation Expense Account should be present for voucher generation for asset :: "
                                    + assetName + " for asset Category :: " + assetCategoryName);
            }
        else
            throw new RuntimeException("There is no Asset For Depreciation.");

    }

    private boolean validateFinYearFromDateAndToDate(final Long fromDate, final Long toDate, final String finacialYear,
            final SimpleDateFormat sdf, final Calendar cal, final boolean fromAndToDateCheck) {
        if (finacialYear != null && fromAndToDateCheck) {
            final int[] yearRange = Stream.of(finacialYear.split("-")).mapToInt(Integer::parseInt).toArray();
            try {
                final Date finYearStart = getFinancialYearStartDate(sdf, yearRange);
                final Date finYearEnd = sdf.parse(yearRange[0] + 1 + "-03-31");

                cal.setTimeInMillis(fromDate);
                final Date fd = cal.getTime();

                if (!(finYearStart.compareTo(fd) * fd.compareTo(finYearEnd) >= 0))
                    return false;

                cal.setTimeInMillis(toDate);
                final Date td = cal.getTime();

                if (!(finYearStart.compareTo(td) * td.compareTo(finYearEnd) >= 0))
                    return false;

            } catch (final ParseException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    private Date getFinancialYearStartDate(final SimpleDateFormat sdf, final int[] yearRange) throws ParseException {
        return sdf.parse(yearRange[0] + "-04-01");
    }

    private boolean validateAssetCategoryForLand(final AssetCategoryType assetCategoryType) {
        if (assetCategoryType != null && AssetCategoryType.LAND.compareTo(assetCategoryType) == 0)
            return true;
        else
            return false;
    }

    public List<ErrorResponse> validateSearchAssetDepreciation(final AssetCriteria assetCriteria) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        boolean isDateOfDepreciatioInFutureDate = false;
        boolean isDateOfDepreciationManadatoryFeilds = false;

        if (assetCriteria.getDateOfDepreciation() == null)
            isDateOfDepreciationManadatoryFeilds = true;
        else if (assetCriteria.getDateOfDepreciation() > new Date().getTime())
            isDateOfDepreciatioInFutureDate = true;

        if (isDateOfDepreciationManadatoryFeilds) {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription(
                    applicationConstants
                            .getErrorMessage(ApplicationConstants.MSG_DEPRECIATION_DATE));
            errorResponse.setError(error);
            errorResponses.add(errorResponse);
        }

        if (isDateOfDepreciatioInFutureDate) {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription(
                    applicationConstants
                            .getErrorMessage(ApplicationConstants.MSG_DEPRECIATION_FUTURE_DATE));
            errorResponse.setError(error);
            errorResponses.add(errorResponse);
        }

        return errorResponses;
    }

    public List<ErrorResponse> validateAssetRequest(final AssetRequest assetRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Asset asset = assetRequest.getAsset();
        final Error error = getError(asset);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }

    public Error getError(final Asset asset) {
        final List<ErrorField> errorFields = new ArrayList<>();

        final List<ErrorField> errorList = validateAssetRequest(asset);
        errorFields.addAll(errorList);

        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(ApplicationConstants.INVALID_REQUEST_MESSAGE).errorFields(errorFields).build();
    }

    public List<ErrorField> validateAssetRequest(final Asset asset) {
        final List<ErrorField> errorFields = new ArrayList<>();

        if (!getFunctionByCode(asset))
            errorFields.add(buildErrorField(ApplicationConstants.ASSET_FUNCTION_CODE_INVALID_CODE,
                    ApplicationConstants.ASSET_FUNCTION_CODE_INVALID_ERROR_MESSAGE,
                    ApplicationConstants.ASSET_FUNCTION_CODE_INVALID_NAME));

        return errorFields;
    }

    private ErrorField buildErrorField(final String code, final String message, final String field) {
        return ErrorField.builder().code(code).message(message).field(field).build();

    }

    private boolean getFunctionByCode(final Asset asset) {

        final StringBuilder url = new StringBuilder();
        Boolean isValidFunctionCode = Boolean.FALSE;
        url.append(applicationProperties.getEgfMastersHost())
                .append(applicationProperties.getEgfServiceFunctionsSearchPath())
                .append("?code=").append(asset.getFunction())
                .append(
                        "&tenantId=")
                .append(asset.getTenantId());

        final RequestInfo requestInfo = RequestInfo.builder().build();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final FunctionResponse functionRes = new RestTemplate().postForObject(url.toString(), wrapper, FunctionResponse.class);
        if (functionRes != null && !functionRes.getFunctions().isEmpty()) {
            asset.setFunction(functionRes.getFunctions() != null
                    && functionRes.getFunctions().get(0) != null ? String.valueOf(functionRes.getFunctions().get(0).getCode())
                            : "");
            isValidFunctionCode = Boolean.TRUE;
        }
        return isValidFunctionCode;

    }

}
