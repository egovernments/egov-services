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

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCategoryResponse;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.model.enums.KafkaTopicName;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.repository.AssetCategoryRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetCategoryService {

    @Autowired
    private AssetCategoryRepository assetCategoryRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private AssetCommonService assetCommonService;

    public List<AssetCategory> search(final AssetCategoryCriteria assetCategoryCriteria) {
        return assetCategoryRepository.search(assetCategoryCriteria);
    }

    public void create(final AssetCategoryRequest assetCategoryRequest) {

        try {
            assetCategoryRepository.create(assetCategoryRequest);
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.info("AssetCategoryService create");
        }
    }

    public AssetCategoryResponse createAsync(final AssetCategoryRequest assetCategoryRequest) {

        final AssetCategory assetCategory = assetCategoryRequest.getAssetCategory();
        
        assetCategory.setDepreciationRate(assetCommonService.getDepreciationRate(assetCategory.getDepreciationRate()));
        
        assetCategory.setCode(assetCommonService.getCode("%03d", Sequence.ASSETCATEGORYCODESEQUENCE));
        
        assetCategory.setId(assetCommonService.getNextId(Sequence.ASSETCATEGORYSEQUENCE));
        
        log.debug("AssetCategoryService createAsync" + assetCategoryRequest);
        logAwareKafkaTemplate.send(applicationProperties.getCreateAssetCategoryTopicName(),
                KafkaTopicName.SAVEASSETCATEGORY.toString(), assetCategoryRequest);

        final List<AssetCategory> assetCategories = new ArrayList<AssetCategory>();
        assetCategories.add(assetCategory);

        return getAssetCategoryResponse(assetCategories, assetCategoryRequest.getRequestInfo());
    }

    public AssetCategoryResponse update(final AssetCategoryRequest assetCategoryRequest) {
        final AssetCategory assetCategory = assetCategoryRepository.update(assetCategoryRequest);
        final List<AssetCategory> assetCategories = new ArrayList<AssetCategory>();
        assetCategories.add(assetCategory);
        return getAssetCategoryResponse(assetCategories, new RequestInfo());
    }

    public AssetCategoryResponse updateAsync(final AssetCategoryRequest assetCategoryRequest) {
        final AssetCategory assetCategory = assetCategoryRequest.getAssetCategory();
        assetCategory.setDepreciationRate(assetCommonService.getDepreciationRate(assetCategory.getDepreciationRate()));
        log.info("AssetCategoryService updateAsync" + assetCategoryRequest);
        logAwareKafkaTemplate.send(applicationProperties.getUpdateAssetCategoryTopicName(),
                KafkaTopicName.UPDATEASSETCATEGORY.toString(), assetCategoryRequest);
        final List<AssetCategory> assetCategories = new ArrayList<AssetCategory>();
        assetCategories.add(assetCategory);

        return getAssetCategoryResponse(assetCategories, assetCategoryRequest.getRequestInfo());
    }

    private AssetCategoryResponse getAssetCategoryResponse(final List<AssetCategory> assetCategories,
            final RequestInfo requestInfo) {
        final AssetCategoryResponse assetCategoryResponse = new AssetCategoryResponse();
        assetCategoryResponse.setAssetCategory(assetCategories);
        assetCategoryResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo));
        return assetCategoryResponse;
    }
}
