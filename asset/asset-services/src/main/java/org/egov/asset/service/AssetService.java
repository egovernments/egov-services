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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.Document;
import org.egov.asset.model.TransactionHistory;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.repository.AssetDocumentsRepository;
import org.egov.asset.repository.AssetRepository;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetCommonService assetCommonService;

    @Autowired
    private CurrentValueService currentValueService;

    @Autowired
    private AssetDocumentsRepository assetDocumentsRepository;

    public List<Asset> getAssets(final AssetCriteria searchAsset, final RequestInfo requestInfo) {
        log.info("AssetService getAssets");
        final List<Asset> assets = assetRepository.findForCriteria(searchAsset);

        for (final Asset asset : assets) {
            final List<Document> documents = new ArrayList<>();
            final List<Document> assetDocuments = assetDocumentsRepository.findByAssetId(asset.getId(),
                    asset.getTenantId());
            if (!assetDocuments.isEmpty()) {
                for (final Document document : assetDocuments) {
                    final Document assetDocs = new Document();
                    assetDocs.setId(document.getId());
                    assetDocs.setAsset(asset.getId());
                    assetDocs.setFileStore(document.getFileStore());
                    assetDocs.setTenantId(document.getTenantId());
                    documents.add(assetDocs);
                }
                asset.getDocuments().addAll(documents);

            }
        }
        if (searchAsset.getIsTransactionHistoryRequired() == null)
            searchAsset.setIsTransactionHistoryRequired(false);
        else if (searchAsset.getIsTransactionHistoryRequired().equals(true) && !assets.isEmpty())
            getAssetTransactionHistory(assets);
        return assets;
    }

    public Asset create(final AssetRequest assetRequest) {
        return assetRepository.create(assetRequest);
    }

    public Asset createAsset(final AssetRequest assetRequest) {
        final Asset asset = assetRequest.getAsset();

        asset.setCode(assetCommonService.getCode("%06d", Sequence.ASSETCODESEQUENCE));

        asset.setId(assetCommonService.getNextId(Sequence.ASSETSEQUENCE));

        // setDepriciationRateAndEnableYearWiseDepreciation(asset);

        log.debug("assetRequest create Asset Request::" + assetRequest);

        final AssetCurrentValue currentValue = AssetCurrentValue.builder().assetId(asset.getId()).tenantId(asset.getTenantId())
                .assetTranType(TransactionType.CREATE).build();

        final BigDecimal grossValue = asset.getGrossValue();
        final BigDecimal accumulatedDepreciation = asset.getAccumulatedDepreciation();

        if (grossValue != null && accumulatedDepreciation != null)
            currentValue.setCurrentAmount(grossValue.subtract(accumulatedDepreciation));
        else if (grossValue != null)
            currentValue.setCurrentAmount(grossValue);

        if (grossValue != null || accumulatedDepreciation != null) {
            final List<AssetCurrentValue> assetCurrentValueList = new ArrayList<>();
            assetCurrentValueList.add(currentValue);
            currentValueService.createCurrentValue(AssetCurrentValueRequest.builder()
                    .assetCurrentValues(assetCurrentValueList).requestInfo(assetRequest.getRequestInfo()).build());
        }

        create(assetRequest);
        return asset;
    }

    public Asset update(final AssetRequest assetRequest) {
        return assetRepository.update(assetRequest);

    }

    public Asset updateAsset(final AssetRequest assetRequest) {
        final Asset asset = assetRequest.getAsset();
        // setDepriciationRateAndEnableYearWiseDepreciation(asset);

        log.debug("assetRequest update Asset::" + assetRequest);
        final Set<Long> assetIds = new HashSet<>();
        assetIds.add(asset.getId());

        final List<AssetCurrentValue> assetCurrentValues = currentValueService
                .getCurrentValues(assetIds, asset.getTenantId(), assetRequest.getRequestInfo()).getAssetCurrentValues();
        if (assetCurrentValues != null && !assetCurrentValues.isEmpty()) {
            final BigDecimal grossValue = asset.getGrossValue();
            final BigDecimal accumulatedDepreciation = asset.getAccumulatedDepreciation();

            if (grossValue != null && accumulatedDepreciation != null)
                assetCurrentValues.get(0).setCurrentAmount(grossValue.subtract(accumulatedDepreciation));
            else if (grossValue != null)
                assetCurrentValues.get(0).setCurrentAmount(grossValue);

            if (grossValue != null || accumulatedDepreciation != null)
                currentValueService.saveUpdatedCurrentValue(AssetCurrentValueRequest.builder()
                        .assetCurrentValues(assetCurrentValues).requestInfo(assetRequest.getRequestInfo()).build());

        }
        update(assetRequest);
        return asset;
    }

    /*
     * private void setDepriciationRateAndEnableYearWiseDepreciation(final Asset asset) { final List<YearWiseDepreciation>
     * yearWiseDepreciation = asset.getYearWiseDepreciation(); log.debug("Year wise depreciations from Request :: " +
     * yearWiseDepreciation); final Boolean enableYearWiseDepreciation = asset.getEnableYearWiseDepreciation(); log.debug(
     * "Enable year wise depreciaition from Request :: " + enableYearWiseDepreciation); log.debug("Asset ID from Request :: " +
     * asset.getId()); if (enableYearWiseDepreciation != null && enableYearWiseDepreciation && yearWiseDepreciation != null &&
     * !yearWiseDepreciation.isEmpty()) for (final YearWiseDepreciation depreciationRate : yearWiseDepreciation)
     * depreciationRate.setAssetId(asset.getId()); else if (enableYearWiseDepreciation != null && !enableYearWiseDepreciation) {
     * asset.setEnableYearWiseDepreciation(false); final Double depreciationRate =
     * assetCommonService.getDepreciationRate(asset.getDepreciationRate()); log.debug("Depreciation rate for asset create :: " +
     * depreciationRate); asset.setDepreciationRate(depreciationRate); } }
     */
    public Asset getAsset(final String tenantId, final Long assetId, final RequestInfo requestInfo) {
        final List<Long> assetIds = new ArrayList<>();
        assetIds.add(assetId);
        final AssetCriteria assetCriteria = AssetCriteria.builder().tenantId(tenantId).id(assetIds).build();
        final List<Asset> assets = getAssets(assetCriteria, requestInfo);
        if (assets != null && !assets.isEmpty())
            return assets.get(0);
        else
            throw new RuntimeException(
                    "There is no asset exists for id ::" + assetId + " for tenant id :: " + tenantId);
    }

    private void getAssetTransactionHistory(final List<Asset> assets) {
        final List<Long> assetIds = assets.stream().map(assetsList -> assetsList.getId()).collect(Collectors.toList());
        final Map<Long, List<TransactionHistory>> transactionhistoryMap = assetRepository.getTransactionHistory(assetIds,
                assets.get(0).getTenantId());
        assets.forEach(assetsList -> assetsList.setTransactionHistory(transactionhistoryMap.get(assetsList.getId())));
    }

}