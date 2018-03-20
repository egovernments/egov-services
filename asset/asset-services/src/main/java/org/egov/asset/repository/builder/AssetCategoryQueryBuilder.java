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

import org.egov.asset.model.AssetCategoryCriteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetCategoryQueryBuilder {

    private static final String SELECT_BASE_QUERY = "SELECT *" + " FROM egasset_assetcategory assetcategory ";

    public String getQuery(final AssetCategoryCriteria assetCategoryCriteria,
            final List<Object> preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(SELECT_BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, assetCategoryCriteria);
        addOrderByClause(selectQuery, assetCategoryCriteria);
        addPagingClause(selectQuery, preparedStatementValues, assetCategoryCriteria);
        log.info("selectQuery::" + selectQuery);
        log.info("preparedstmt values : " + preparedStatementValues);
        return selectQuery.toString();
    }

    private void addWhereClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
            final AssetCategoryCriteria assetCategoryCriteria) {

        if (assetCategoryCriteria.getId() == null && assetCategoryCriteria.getName() == null
                && assetCategoryCriteria.getCode() == null && assetCategoryCriteria.getTenantId() == null
                && assetCategoryCriteria.getAssetCategoryType().isEmpty())
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (assetCategoryCriteria.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" assetcategory.tenantId = ?");
            preparedStatementValues.add(assetCategoryCriteria.getTenantId());
        }

        if (assetCategoryCriteria.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.id = ?");
            preparedStatementValues.add(assetCategoryCriteria.getId());
        }

        if (assetCategoryCriteria.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.name = ?");
            preparedStatementValues.add(assetCategoryCriteria.getName());
        }

        if (assetCategoryCriteria.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.code = ?");
            preparedStatementValues.add(assetCategoryCriteria.getCode());
        }

        if (assetCategoryCriteria.getAssetCategoryType() != null
                && assetCategoryCriteria.getAssetCategoryType().size() != 0) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.assetcategorytype IN ("
                    + getAssetCategoryTypeQuery(assetCategoryCriteria.getAssetCategoryType()));
        }
        if (assetCategoryCriteria.getIsChildCategory() != null) {
            selectQuery.append(
                    " AND NOT EXISTS (select category.parentid from egasset_assetcategory category where assetcategory.id= category.parentid)");
            }

        if (assetCategoryCriteria.getParent() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.parentid = ?");
            preparedStatementValues.add(assetCategoryCriteria.getParent());
        }

        if (assetCategoryCriteria.getUsedForLease() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.usedforlease = ?");
            preparedStatementValues.add(assetCategoryCriteria.getUsedForLease());
        }

    }

    /**
     * This method is always called at the beginning of the method so that and is prepended before the field's predicate is
     * handled.
     *
     * @param appendAndClauseFlag
     * @param queryString
     * @return boolean indicates if the next predicate should append an "AND"
     */
    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");
        return true;
    }

    private static String getAssetCategoryTypeQuery(final List<String> categoryTypes) {
        StringBuilder query = null;
        if (categoryTypes.size() >= 1) {
            query = new StringBuilder("'" + categoryTypes.get(0).toString() + "'");
            for (int i = 1; i < categoryTypes.size(); i++)
                query.append(",'" + categoryTypes.get(i) + "'");
        }
        return query.append(")").toString();
    }

    private void addPagingClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
            final AssetCategoryCriteria assetCategoryCriteria) {

        selectQuery.append(" LIMIT ?");
        preparedStatementValues.add(500); // Set limit to pageSize

        selectQuery.append(" OFFSET ?");
        final long pageNumber = 0; // Default pageNo is zero meaning first page
        preparedStatementValues.add(pageNumber);
    }

    private void addOrderByClause(final StringBuilder selectQuery, final AssetCategoryCriteria assetCategoryCriteria) {
        selectQuery.append(" ORDER BY assetcategory.name");
    }

    public String getInsertQuery() {
        return "INSERT into egasset_assetcategory "
                + "(id,name,code,parentid,assetcategorytype,depreciationmethod,depreciationrate,assetaccount,accumulateddepreciationaccount,"
                + "revaluationreserveaccount,depreciationexpenseaccount,unitofmeasurement,customfields,tenantid,createdby,createddate,"
                + "lastmodifiedby,lastmodifieddate,isassetallow,version,usedforlease)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public String getUpdateQuery() {
        return "UPDATE egasset_assetcategory SET "
                + "parentid=?,assetcategorytype=?,depreciationmethod=?,depreciationrate=?,assetaccount=?,accumulateddepreciationaccount=?,"
                + "revaluationreserveaccount=?,depreciationexpenseaccount=?,unitofmeasurement=?,customfields=?,"
                + "lastmodifiedby=?,lastmodifieddate=?,isassetallow=?,version=?,usedforlease=? WHERE code=? and tenantid=?";
    }
}
