package org.egov.asset.repository.builder;

import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.model.AssetConfigurationCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssetConfigurationQueryBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetConfigurationQueryBuilder.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "SELECT ck.keyName as key, cv.value as value"
            + " FROM egasset_assetconfiguration ck JOIN egasset_assetconfigurationvalues cv ON ck.id = cv.keyId "
            + "AND ck.tenantId = cv.tenantId";

    @SuppressWarnings("rawtypes")
    public String getQuery(final AssetConfigurationCriteria assetConfigurationCriteria,
            final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, assetConfigurationCriteria);
        addOrderByClause(selectQuery, assetConfigurationCriteria);
        addPagingClause(selectQuery, preparedStatementValues, assetConfigurationCriteria);

        LOGGER.debug("Asset Configuration Query : " + selectQuery);
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

    private void addOrderByClause(final StringBuilder selectQuery,
            final AssetConfigurationCriteria assetConfigurationCriteria) {
        final String sortBy = assetConfigurationCriteria.getSortBy() == null ? "keyName"
                : assetConfigurationCriteria.getSortBy();
        final String sortOrder = assetConfigurationCriteria.getSortOrder() == null ? "ASC"
                : assetConfigurationCriteria.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final AssetConfigurationCriteria hrConfigurationGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.commonsSearchPageSizeDefault());
        if (hrConfigurationGetRequest.getPageSize() != null)
            pageSize = hrConfigurationGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (hrConfigurationGetRequest.getPageNumber() != null)
            pageNumber = hrConfigurationGetRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); // Set offset to
                                                            // pageNo * pageSize
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
