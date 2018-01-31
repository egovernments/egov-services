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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AgreementResponse;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.model.Agreement;
import org.egov.asset.model.AgreementCriteria;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.service.AssetCategoryService;
import org.egov.asset.service.AssetCommonService;
import org.egov.asset.service.AssetService;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetCategoryValidator {

    @Autowired
    private AssetCategoryService assetCategoryService;

    @Autowired
    private AssetCommonService assetCommonService;

    @Autowired
    private AssetService assetService;
    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private RestTemplate restTemplate;

    public void validateAssetCategory(final AssetCategoryRequest assetCategoryRequest) {

        final AssetCategory assetCategory = assetCategoryRequest.getAssetCategory();
        final List<AssetCategory> assetCategories = findByName(assetCategory.getName(), assetCategory.getTenantId());

        if (!assetCategories.isEmpty())
            throw new RuntimeException("Duplicate asset category name");

        validateDepreciationRate(assetCategory);

    }

    private void validateDepreciationRate(final AssetCategory assetCategory) {
        final Double depreciationRate = assetCategory.getDepreciationRate();
        assetCommonService.validateDepreciationRateValue(depreciationRate);
        assetCategory.setDepreciationRate(assetCommonService.getDepreciationRate(depreciationRate));
    }

    public List<AssetCategory> findByName(final String name, final String tenantId) {

        final AssetCategoryCriteria categoryCriteria = new AssetCategoryCriteria();
        categoryCriteria.setName(name);
        categoryCriteria.setTenantId(tenantId);
        List<AssetCategory> assetCategories = new ArrayList<AssetCategory>();

        try {
            assetCategories = assetCategoryService.search(categoryCriteria);
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.info("findByName assetCategories:" + assetCategories);
        }
        return assetCategories;
    }

    public List<AssetCategory> findByIdAndCode(final Long id, final String code, final String tenantId) {

        final AssetCategoryCriteria categoryCriteria = new AssetCategoryCriteria();

        categoryCriteria.setTenantId(tenantId);
        categoryCriteria.setId(id);
        categoryCriteria.setCode(code);
        List<AssetCategory> assetCategories = new ArrayList<AssetCategory>();

        try {
            assetCategories = assetCategoryService.search(categoryCriteria);
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.info("findById assetCategories:" + assetCategories);
        }

        return assetCategories;
    }

    public void validateAssetCategoryForUpdate(final AssetCategoryRequest assetCategoryRequest) {
        final Set<Long> assetIds = new HashSet<>();
        final RequestInfo requestInfo = assetCategoryRequest.getRequestInfo();
        final AssetCategory assetCategory = assetCategoryRequest.getAssetCategory();
        final List<AssetCategory> assetCategories = findByIdAndCode(assetCategory.getId(), assetCategory.getCode(),
                assetCategory.getTenantId());

        if (assetCategories.isEmpty())
            throw new RuntimeException("Invalid Asset Category Code for Asset :: " + assetCategory.getName());

        if (!assetCategories.isEmpty()) {
            final AssetCriteria assetCriteria = new AssetCriteria();
            assetCriteria.setAssetCategory(assetCategories.get(0).getId());
            assetCriteria.setTenantId(assetCategories.get(0).getTenantId());
            final List<Asset> assets = assetService.getAssets(assetCriteria, requestInfo).getAssets();
            if (assets != null && !assets.isEmpty()) {
                assetIds.addAll(assets.stream().map(asset -> asset.getId()).collect(Collectors.toList()));
                getAggrementsByAsset(assetIds, assets.get(0).getTenantId(), requestInfo);
            }
        }
        validateDepreciationRate(assetCategory);
    }

    private void getAggrementsByAsset(final Set<Long> assetIds, final String tenantId, final RequestInfo requestInfo) {

        final String url = applicationProperties.getLamsServiceHost()
                + applicationProperties.getLamsServiceAgreementsSearchPath() + "?tenantId=" + tenantId;
        final AgreementCriteria criteria = new AgreementCriteria();
        criteria.setStatus("ACTIVE");
        criteria.setAsset(assetIds);
        criteria.setTenantId(tenantId);
        criteria.setRequestInfo(requestInfo);
        log.debug("Agreement Search URL :: " + url);
        final List<Agreement> agreements = restTemplate
                .postForObject(url, criteria, AgreementResponse.class)
                .getAgreement();
        log.debug("Agreement Response Response :: " + agreements);
        if (agreements != null && !agreements.isEmpty())
            throw new RuntimeException("This category has already been used for agreements,hence can not be modified .");

    }

}
