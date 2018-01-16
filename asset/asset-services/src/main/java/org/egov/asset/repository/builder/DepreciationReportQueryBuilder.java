package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.model.DepreciationReportCriteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DepreciationReportQueryBuilder {

    public static final String BASE_QUERY = "SELECT  depreciation.id as depreciationId,depreciation.tenantId as tenantId, depreciation.assetid as assetId,assetcategory.id as assetcategoryId,"
            + "asset.name as assetname,asset.code as assetcode,asset.department as department,depreciation.depreciationrate as depreciationrate,"
            + "depreciation.depreciationvalue as depreciationvalue,depreciation.valueafterdepreciation as valueafterdepreciation,asset.grossvalue as grossvalue, "
            + "assetcategory.name as assetcategoryname,assetcategory.code as assetcategorycode,assetcategory.assetcategorytype as assetcategorytype,assetcategory.parentid as parentid, "
            + "assetcategory.depreciationrate as assetcategory_depreciationrate,depreciation.financialyear as financialyear "
            + " FROM egasset_depreciation depreciation,egasset_asset asset, egasset_assetcategory assetcategory "
            +  "WHERE asset.assetcategory = assetcategory.id and "
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
        
        if (depreciationReportCriteria.getParent()!= null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" assetcategory.parentid =?");
            preparedStatementValues.add(depreciationReportCriteria.getParent());
        }
        

        if (depreciationReportCriteria.getAssetName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" asset.name ilike ?");
            preparedStatementValues.add("%" + depreciationReportCriteria.getAssetName() + "%");
        }
        
        if (depreciationReportCriteria.getAssetCode()!= null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" asset.code ilike ?");
            preparedStatementValues.add("%" + depreciationReportCriteria.getAssetCode() + "%");
        }
        
        if (depreciationReportCriteria.getFinancialYear()!= null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" depreciation.financialyear =?");
            preparedStatementValues.add(depreciationReportCriteria.getFinancialYear());
        }
         

    }

    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");
        return true;
    }
    
}
