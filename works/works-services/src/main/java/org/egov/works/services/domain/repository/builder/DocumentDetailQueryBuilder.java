package org.egov.works.services.domain.repository.builder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.services.config.PropertiesManager;
import org.egov.works.services.web.contract.DocumentDetailSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class DocumentDetailQueryBuilder {

    @Autowired
    private PropertiesManager propertiesManager;

    public String searchDocumentsQuery(final DocumentDetailSearchCriteria documentDetailSearchCriteria,
                                       final MapSqlParameterSource preparedStatementValues) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("select * from egw_documentdetail where id is not null");
        addWhereClause(queryString,documentDetailSearchCriteria,preparedStatementValues);
        addOrderByClause(queryString,documentDetailSearchCriteria,preparedStatementValues);
        addPagingClause(queryString, documentDetailSearchCriteria, preparedStatementValues);
        return queryString.toString();
    }

    private void addOrderByClause(StringBuilder queryString, DocumentDetailSearchCriteria documentDetailSearchCriteria, MapSqlParameterSource preparedStatementValues) {

        final String sortBy = StringUtils.isBlank(documentDetailSearchCriteria.getSortProperty()) ? "objectid"
                : documentDetailSearchCriteria.getSortProperty();
        queryString.append(" ORDER BY " + sortBy );
    }

    private void addWhereClause(final StringBuilder queryString,final DocumentDetailSearchCriteria documentDetailSearchCriteria,
                                MapSqlParameterSource preparedStatementValues) {
        if(StringUtils.isNotBlank(documentDetailSearchCriteria.getTenantId())) {
            queryString.append(" and tenantId=:tenantId");
            preparedStatementValues.addValue("tenantId",documentDetailSearchCriteria.getTenantId());
        }
        if(documentDetailSearchCriteria.getIds() != null && !documentDetailSearchCriteria.getIds().isEmpty()) {
            queryString.append(" and id IN " + getIdQuery(documentDetailSearchCriteria.getIds()));
        }

        if(documentDetailSearchCriteria.getObjectIds() != null && !documentDetailSearchCriteria.getObjectIds().isEmpty()) {
            queryString.append(" and objectid IN " + getIdQuery(documentDetailSearchCriteria.getObjectIds()));
        }

        queryString.append(" and deleted=false ");
    }

    private void addPagingClause(final StringBuilder selectQuery,final DocumentDetailSearchCriteria documentDetailSearchCriteria,
                                 final MapSqlParameterSource preparedStatementValues) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT :limit");
        long pageSize = Integer.parseInt(propertiesManager.getWorksSearchPageSizeDefault());
        if (documentDetailSearchCriteria.getPageSize() != null)
            pageSize = documentDetailSearchCriteria.getPageSize();
        preparedStatementValues.addValue("limit", pageSize); // Set limit to
        // pageSize

        // handle offset here
        selectQuery.append(" OFFSET :offset");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (documentDetailSearchCriteria.getPageNumber() != null)
            pageNumber = documentDetailSearchCriteria.getPageNumber() - 1;
        preparedStatementValues.addValue("offset", pageNumber * pageSize); // Set
        // offset
        // to
        // pageNo * pageSize
    }

    private static String getIdQuery(List<String> instrumentIdList) {
        StringBuilder query = new StringBuilder("(");
        if (instrumentIdList.size() >= 1) {
            query.append("'" + instrumentIdList.get(0).toString() + "'");
            for (int i = 1; i < instrumentIdList.size(); i++) {
                query.append(", '" + instrumentIdList.get(i) + "'");
            }
        }
        return query.append(")").toString();
    }
}
