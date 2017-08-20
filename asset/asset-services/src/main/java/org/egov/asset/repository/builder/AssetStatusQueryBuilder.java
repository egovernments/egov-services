package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.model.AssetStatusCriteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetStatusQueryBuilder {

    public String BASE_QUERY = "SELECT objectname,code,name,description,tenantid,createdby,createddate,lastmodifiedby,lastmodifieddate FROM"
            + " egasset_statuses as status";

    public String getQuery(final AssetStatusCriteria assetStatusCriteria, final List<Object> preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
        log.info("get query");
        addWhereClause(selectQuery, preparedStatementValues, assetStatusCriteria);
        log.info("Query from asset status querybuilder for search : " + selectQuery);
        return selectQuery.toString();
    }

    private void addWhereClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
            final AssetStatusCriteria assetStatusCriteria) {
        System.out.println("assetStatusCriteria>>" + assetStatusCriteria);
        boolean isAppendAndClause = false;

        selectQuery.append(" WHERE");
        if (assetStatusCriteria.getObjectName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" status.objectname = ?");
            preparedStatementValues.add(assetStatusCriteria.getObjectName());
        }

        if (assetStatusCriteria.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" status.code = ?");
            preparedStatementValues.add(assetStatusCriteria.getCode());
        }

        if (assetStatusCriteria.getTenantId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" status.tenantId = ?");
            preparedStatementValues.add(assetStatusCriteria.getTenantId());
        }

    }

    private boolean addAndClauseIfRequired(final boolean isAppendAndClause, final StringBuilder selectQuery) {
        if (isAppendAndClause)
            selectQuery.append(" AND");
        return true;
    }

}
