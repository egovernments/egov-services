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

package org.egov.asset.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.repository.builder.AssetCategoryQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetCategoryRowMapper;
import org.egov.asset.service.AssetCommonService;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AssetCategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AssetCategoryRowMapper assetCategoryRowMapper;

    @Autowired
    private AssetCategoryQueryBuilder assetCategoryQueryBuilder;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AssetCommonService assetCommonService;

    public List<AssetCategory> search(final AssetCategoryCriteria assetCategoryCriteria) {

        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = assetCategoryQueryBuilder.getQuery(assetCategoryCriteria, preparedStatementValues);

        List<AssetCategory> assetCategory = new ArrayList<>();
        try {
            assetCategory = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), assetCategoryRowMapper);
        } catch (final Exception exception) {
            log.info("the exception in assetcategory search :" + exception);
            throw new RuntimeException("the exception in assetcategory search");
        }
        return assetCategory;
    }

    public AssetCategory create(final AssetCategoryRequest assetCategoryRequest) {

        final RequestInfo requestInfo = assetCategoryRequest.getRequestInfo();
        final AssetCategory assetCategory = assetCategoryRequest.getAssetCategory();
        final String queryStr = assetCategoryQueryBuilder.getInsertQuery();

        mapper.setSerializationInclusion(Include.NON_NULL);

        final AssetCategory assetCategory2 = new AssetCategory();
        assetCategory2.setAssetFieldsDefination(assetCategory.getAssetFieldsDefination());

        String customFields = null;
        String assetCategoryType = null;
        String depreciationMethod = null;

        if (assetCategory.getAssetCategoryType() != null)
            assetCategoryType = assetCategory.getAssetCategoryType().toString();

        if (assetCategory.getDepreciationMethod() != null)
            depreciationMethod = assetCategory.getDepreciationMethod().toString();

        try {
            customFields = mapper.writeValueAsString(assetCategory2);
            log.debug("customFields:::" + customFields);
        } catch (final JsonProcessingException e) {
            log.debug("the exception in assetcategory customfileds mapping :" + e);
            throw new RuntimeException("the exception in assetcategory customfileds mapping");
        }

        final Object[] obj = new Object[] { assetCategory.getId(), assetCategory.getName(), assetCategory.getCode(),
                assetCategory.getParent(), assetCategoryType, depreciationMethod,
                assetCommonService.getDepreciationRate(assetCategory.getDepreciationRate()),
                assetCategory.getAssetAccount(), assetCategory.getAccumulatedDepreciationAccount(),
                assetCategory.getRevaluationReserveAccount(), assetCategory.getDepreciationExpenseAccount(),
                assetCategory.getUnitOfMeasurement(), customFields, assetCategory.getTenantId(),
                requestInfo.getUserInfo().getId(), new Date().getTime(), requestInfo.getUserInfo().getId(),
                new Date().getTime(), assetCategory.getIsAssetAllow(), assetCategory.getVersion(),
                assetCategory.getUsedForLease() };

        try {
            jdbcTemplate.update(queryStr, obj);
        } catch (final Exception ex) {
            log.debug("the exception in assetcategory insert :" + ex);
            throw new RuntimeException("exceptions occured while persisting the assetcategory insert : " + ex.getMessage());
        }

        return assetCategory;
    }

    public AssetCategory update(final AssetCategoryRequest assetCategoryRequest) {

        final RequestInfo requestInfo = assetCategoryRequest.getRequestInfo();
        final AssetCategory assetCategory = assetCategoryRequest.getAssetCategory();
        final String queryStr = assetCategoryQueryBuilder.getUpdateQuery();

        mapper.setSerializationInclusion(Include.NON_NULL);

        final AssetCategory assetCategory2 = new AssetCategory();
        assetCategory2.setAssetFieldsDefination(assetCategory.getAssetFieldsDefination());

        String customFields = null;
        String assetCategoryType = null;
        String depreciationMethod = null;

        if (assetCategory.getAssetCategoryType() != null)
            assetCategoryType = assetCategory.getAssetCategoryType().toString();

        if (assetCategory.getDepreciationMethod() != null)
            depreciationMethod = assetCategory.getDepreciationMethod().toString();

        try {
            customFields = mapper.writeValueAsString(assetCategory2);
            log.info("customFields:::" + customFields);
        } catch (final JsonProcessingException e) {
            log.info("the exception in assetcategory customfileds mapping :" + e);
            throw new RuntimeException("the exception in assetcategory customfileds mapping");
        }

        final Object[] obj = new Object[] { assetCategory.getParent(), assetCategoryType, depreciationMethod,
                assetCommonService.getDepreciationRate(assetCategory.getDepreciationRate()),
                assetCategory.getAssetAccount(), assetCategory.getAccumulatedDepreciationAccount(),
                assetCategory.getRevaluationReserveAccount(), assetCategory.getDepreciationExpenseAccount(),
                assetCategory.getUnitOfMeasurement(), customFields, requestInfo.getUserInfo().getId(),
                new Date().getTime(), assetCategory.getIsAssetAllow(), assetCategory.getVersion(),
                assetCategory.getUsedForLease(),
                assetCategory.getCode(), assetCategory.getTenantId() };

        try {
            log.info("asset category update query::" + queryStr + "," + Arrays.toString(obj));
            final int i = jdbcTemplate.update(queryStr, obj);
            log.info("output of update asset category query : " + i);
        } catch (final Exception exception) {
            log.info("the exception in assetcategory update :" + exception);
            throw new RuntimeException("exceptions occured while persisting the asset category update ");
        }

        return assetCategory;
    }
}
