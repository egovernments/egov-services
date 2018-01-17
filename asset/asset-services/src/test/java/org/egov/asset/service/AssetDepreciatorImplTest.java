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

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.domain.CalculationCurrentValue;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.DepreciationCriteria;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.DepreciationStatus;
import org.egov.asset.model.enums.TransactionType;
import org.egov.common.contract.request.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssetDepreciatorImplTest {

    @Mock
    private AssetConfigurationService assetConfigurationService;

    @InjectMocks
    private AssetDepreciatorImpl assetDepreciatorImpl;

    @Test
    public void depreciateAssetTest() {
        final DepreciationRequest depreciationRequest = getDepreciationRequest();
        final List<CalculationAssetDetails> calculationAssetDetailList = getCalculationAssetDetailList();
        final List<AssetCurrentValue> assetCurrentValues = getAssetCurrentValues();
        final Map<Long, CalculationCurrentValue> calculationCurrentValues = getCalculationCurrentValueMap();
        final Map<Long, BigDecimal> depreciationSumMap = getDepreciationSumMap();
        final Map<Long, DepreciationDetail> depreciationDetailsMap = getDepreciationDetailsMap();

        final String depreciationMinimumValue = "5000";
        final String depreciationCapiatalizedValue = "1";
        final String depreciationCFactor = "0.5";

        when(assetConfigurationService.getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.ASSETMINIMUMVALUE,
                "ap.kurnool")).thenReturn(depreciationMinimumValue);
        when(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.ASSETDEFAULTCAPITALIZEDVALUE, "ap.kurnool"))
                        .thenReturn(depreciationCapiatalizedValue);
        when(assetConfigurationService.getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DEPRECIATIONCFACTOR,
                "ap.kurnool")).thenReturn(depreciationCFactor);

        assetDepreciatorImpl.depreciateAsset(depreciationRequest, calculationAssetDetailList, calculationCurrentValues,
                depreciationSumMap, assetCurrentValues, depreciationDetailsMap);
    }

    private List<AssetCurrentValue> getAssetCurrentValues() {
        final List<AssetCurrentValue> assetCurrentValues = new ArrayList<AssetCurrentValue>();
        final AssetCurrentValue assetCurrentValue = new AssetCurrentValue();
        assetCurrentValue.setAssetId(Long.valueOf("298"));
        assetCurrentValue.setAssetTranType(TransactionType.DEPRECIATION);
        assetCurrentValue.setCurrentAmount(new BigDecimal("15000"));
        assetCurrentValue.setId(Long.valueOf("1"));
        assetCurrentValue.setTenantId("ap.kurnool");
        assetCurrentValue.setAuditDetails(getAuditDetails());
        assetCurrentValues.add(assetCurrentValue);
        return assetCurrentValues;
    }

    private List<CalculationAssetDetails> getCalculationAssetDetailList() {
        final List<CalculationAssetDetails> calculationAssetDetails = new ArrayList<CalculationAssetDetails>();
        final CalculationAssetDetails calculationAssetDetail = new CalculationAssetDetails();
        calculationAssetDetail.setAccumulatedDepreciation(BigDecimal.ZERO);
        calculationAssetDetail.setAccumulatedDepreciationAccount(Long.valueOf("1947"));
        calculationAssetDetail.setAssetCategoryDepreciationRate(null);
        calculationAssetDetail.setAssetCategoryId(Long.valueOf("196"));
        calculationAssetDetail.setAssetCategoryName("Kalyana Mandapam");
        calculationAssetDetail.setAssetDepreciationRate(Double.valueOf("16.53"));
        calculationAssetDetail.setAssetId(Long.valueOf("552"));
        calculationAssetDetail.setAssetReference(null);
        calculationAssetDetail.setDepartmentId(Long.valueOf("5"));
        calculationAssetDetail.setDepreciationExpenseAccount(Long.valueOf("1906"));
        calculationAssetDetail.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
        calculationAssetDetail.setEnableYearWiseDepreciation(true);
        calculationAssetDetail.setFinancialyear("2017-18");
        calculationAssetDetail.setGrossValue(new BigDecimal("15000"));
        calculationAssetDetail.setYearwisedepreciationrate(Double.valueOf("15"));
        calculationAssetDetails.add(calculationAssetDetail);
        return calculationAssetDetails;
    }

    private Map<Long, DepreciationDetail> getDepreciationDetailsMap() {
        final Map<Long, DepreciationDetail> depreciationDetailsMap = new HashMap<Long, DepreciationDetail>();
        depreciationDetailsMap.put(Long.valueOf("552"), getDepreciationDetail());
        return depreciationDetailsMap;
    }

    private DepreciationDetail getDepreciationDetail() {
        final DepreciationDetail depreciationDetail = DepreciationDetail.builder().assetId(Long.valueOf("552"))
                .depreciationRate(Double.valueOf("20")).depreciationValue(new BigDecimal("3200"))
                .status(DepreciationStatus.SUCCESS).reasonForFailure(null)
                .valueBeforeDepreciation(new BigDecimal("16000")).valueAfterDepreciation(new BigDecimal("12800"))
                .voucherReference("1/GJV/00000219/08/2017-18").build();
        return depreciationDetail;
    }

    private Map<Long, BigDecimal> getDepreciationSumMap() {
        final Map<Long, BigDecimal> depreciationSumMap = new HashMap<Long, BigDecimal>();
        depreciationSumMap.put(Long.valueOf("552"), new BigDecimal("3200"));
        return depreciationSumMap;
    }

    private Map<Long, CalculationCurrentValue> getCalculationCurrentValueMap() {
        final Map<Long, CalculationCurrentValue> calculationCurrentValueMap = new HashMap<Long, CalculationCurrentValue>();
        calculationCurrentValueMap.put(Long.valueOf("552"), getCalculationCurrentValue());
        return calculationCurrentValueMap;
    }

    private CalculationCurrentValue getCalculationCurrentValue() {
        final CalculationCurrentValue calculationCurrentValue = new CalculationCurrentValue();
        calculationCurrentValue.setAssetId(Long.valueOf("552"));
        calculationCurrentValue.setCurrentAmountAfterSeptember(null);
        calculationCurrentValue.setCurrentAmountBeforeSeptember(new BigDecimal("11000"));
        calculationCurrentValue.setId(Long.valueOf("1"));
        calculationCurrentValue.setTenantId("ap.kurnool");
        return calculationCurrentValue;
    }

    private DepreciationRequest getDepreciationRequest() {
        final DepreciationRequest depreciationRequest = new DepreciationRequest();
        final DepreciationCriteria depreciationCriteria = new DepreciationCriteria();
        final Set<Long> assetIds = new HashSet<Long>();
        assetIds.add(Long.valueOf("552"));
        depreciationCriteria.setAssetIds(assetIds);
        depreciationCriteria.setFinancialYear("2017-18");
        depreciationCriteria.setFromDate(null);
        depreciationCriteria.setToDate(null);
        depreciationCriteria.setTenantId("ap.kurnool");
        depreciationRequest.setDepreciationCriteria(depreciationCriteria);
        depreciationRequest.setRequestInfo(new RequestInfo());
        return depreciationRequest;
    }

    private AuditDetails getAuditDetails() {
        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy("5");
        auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
        auditDetails.setLastModifiedBy("5");
        auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
        return auditDetails;
    }

}
