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

package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.model.DepreciationReportCriteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DepreciationReportQueryBuilder {

    public static final String BASE_QUERY = "SELECT * ,depreciation.id as depreciationId,depreciation.tenantId as tenantId, depreciation.assetid as assetId,assetcategory.id as assetcategoryId,"
            + "asset.name as assetname,asset.code as assetcode,asset.department as department,depreciation.depreciationrate as depreciationrate,"
            + "depreciation.depreciationvalue as depreciationvalue,depreciation.valueafterdepreciation as valueafterdepreciation,asset.grossvalue as grossvalue, "
            + "assetcategory.name as assetcategoryname,assetcategory.code as assetcategorycode,assetcategory.assetcategorytype as assetcategorytype,assetcategory.parentid as parentid, "
            + "assetcategory.depreciationrate as assetcategory_depreciationrate,depreciation.financialyear as financialyear,depreciation.voucherreference as voucherreference"
            + " FROM egasset_depreciation depreciation,egasset_asset asset, egasset_assetcategory assetcategory "
            + " WHERE asset.assetcategory = assetcategory.id and "
            + " depreciation.assetid = asset.id and depreciation.tenantId =asset.tenantId "
            + " and assetcategory.id= asset.assetcategory and assetcategory.tenantId = asset.tenantId ";

    public String getQuery(final DepreciationReportCriteria depreciationReportCriteria,
            final List<Object> preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        log.info("get depreciationReportCriteria query");
        addWhereClause(selectQuery, preparedStatementValues, depreciationReportCriteria);
        log.debug("Query from depreciationReportCriteria querybuilder for asset search : " + selectQuery);
        return selectQuery.toString();
    }

    private void addWhereClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
            final DepreciationReportCriteria depreciationReportCriteria) {

        boolean isAppendAndClause = true;

        final String depreciationTenantId = depreciationReportCriteria.getTenantId();

        log.debug("Depreciation Tenant ID :: " + depreciationTenantId);

        if (depreciationTenantId != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" depreciation.tenantId = ?");
            preparedStatementValues.add(depreciationTenantId);

        }

        if (depreciationReportCriteria.getAssetCategoryName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.name ilike ?");
            preparedStatementValues.add("%" + depreciationReportCriteria.getAssetCategoryName() + "%");
        }

        if (depreciationReportCriteria.getAssetCategoryType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.assetcategorytype =?");
            preparedStatementValues.add(depreciationReportCriteria.getAssetCategoryType());
        }

        if (depreciationReportCriteria.getParent() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.parentid =?");
            preparedStatementValues.add(depreciationReportCriteria.getParent());
        }

        if (depreciationReportCriteria.getAssetName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" asset.name ilike ?");
            preparedStatementValues.add("%" + depreciationReportCriteria.getAssetName() + "%");
        }

        if (depreciationReportCriteria.getAssetCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" asset.code ilike ?");
            preparedStatementValues.add("%" + depreciationReportCriteria.getAssetCode() + "%");
        }

        if (depreciationReportCriteria.getFinancialYear() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" depreciation.financialyear =?");
            preparedStatementValues.add(depreciationReportCriteria.getFinancialYear());
        }
        
        if (depreciationReportCriteria.getAssetId() != null ) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" depreciation.assetid =?");
            preparedStatementValues.add(depreciationReportCriteria.getAssetId());
        }

    }

    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");
        return true;
    }

}
