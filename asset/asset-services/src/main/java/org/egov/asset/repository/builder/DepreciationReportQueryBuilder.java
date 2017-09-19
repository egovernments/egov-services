package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.model.DepreciationReportCriteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DepreciationReportQueryBuilder {

    public static final String BASE_QUERY = "SELECT *,asset.id AS assetId,assetcategory.id AS assetcategoryId,"
            + "asset.name as assetname,asset.code as assetcode,"
            + "assetcategory.name AS assetcategoryname,assetcategory.code AS assetcategorycode,ywd.id as ywd_id,ywd.depreciationrate as "
            + "ywd_depreciationrate,assetcategory.depreciationrate as assetcategory_depreciationrate"
            + " FROM egasset_asset asset " + "INNER JOIN egasset_assetcategory assetcategory "
            + "ON asset.assetcategory = assetcategory.id LEFT OUTER JOIN egasset_yearwisedepreciation ywd "
            + "ON asset.id = ywd.assetid WHERE "
            + "asset.id in (SELECT depreciation.assetid FROM egasset_depreciation depreciation WHERE "
            + "depreciation.tenantId = ?)";

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
            selectQuery.append(" asset.tenantId = ?");
            preparedStatementValues.add(depreciationTenantId);
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

        if (depreciationReportCriteria.getAssetName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" asset.name ilike ?");
            preparedStatementValues.add("%" + depreciationReportCriteria.getAssetName() + "%");
        }

    }

    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");
        return true;
    }

}
