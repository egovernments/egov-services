package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.model.AssetConfigurationCriteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetConfigurationQueryBuilder {

    private static final String BASE_QUERY = "SELECT ck.keyName as key, cv.value as value"
            + " FROM egasset_assetconfiguration ck JOIN egasset_assetconfigurationvalues cv ON ck.id = cv.keyId "
            + "AND ck.tenantId = cv.tenantId";

    @SuppressWarnings("rawtypes")
    public String getQuery(final AssetConfigurationCriteria assetConfigurationCriteria,
            final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, assetConfigurationCriteria);
        log.debug("Asset Configuration Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final AssetConfigurationCriteria assetConfigurationCriteria) {

        if (assetConfigurationCriteria.getId() == null && assetConfigurationCriteria.getEffectiveFrom() == null
                && assetConfigurationCriteria.getName() == null && assetConfigurationCriteria.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (assetConfigurationCriteria.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" ck.tenantId = ?");
            preparedStatementValues.add(assetConfigurationCriteria.getTenantId());
        }

        if (assetConfigurationCriteria.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ck.id IN " + getIdQuery(assetConfigurationCriteria.getId()));
        }

        if (assetConfigurationCriteria.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ck.keyName = ?");
            preparedStatementValues.add(assetConfigurationCriteria.getName());
        }

        if (assetConfigurationCriteria.getEffectiveFrom() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" cv.effectiveFrom = ?");
            preparedStatementValues.add(assetConfigurationCriteria.getEffectiveFrom());
        }
    }

    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");
        return true;
    }

    private static String getIdQuery(final List<Long> idList) {
        final StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++)
                query.append(", " + idList.get(i));
        }
        return query.append(")").toString();
    }

}
