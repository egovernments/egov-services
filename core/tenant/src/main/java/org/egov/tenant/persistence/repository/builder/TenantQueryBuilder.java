package org.egov.tenant.persistence.repository.builder;

import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TenantQueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(TenantQueryBuilder.class);


    private static final String TENANT_BASE_QUERY = "SELECT distinct id, code, description, domainurl, logoid, imageid, createdby, createddate, lastmodifiedby, lastmodifieddate from tenant";

    public String getInsertQuery() {
        return "INSERT INTO tenant (id, code, description, domainurl, logoid, imageid, createdby, createddate, lastmodifiedby, lastmodifieddate) " +
                "values ( nextval('seq_tenant'), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }


    public String getSearchQuery(TenantSearchCriteria tenantSearchCriteria) {

        final StringBuilder selectQuery = new StringBuilder(TENANT_BASE_QUERY);
        addWhereClause(selectQuery,tenantSearchCriteria);
        selectQuery.append(" order by id");

        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    private void addWhereClause(final StringBuilder selectQuery,
                                final TenantSearchCriteria tenantSearchCriteria) {

        if (tenantSearchCriteria != null && tenantSearchCriteria.getTenantCodes() == null && tenantSearchCriteria.getTenantCodes().isEmpty())
            return;

        selectQuery.append(" WHERE ");

        if (tenantSearchCriteria.getTenantCodes() != null) {
            selectQuery.append("code in " + getInOperatorQuery(tenantSearchCriteria.getTenantCodes()));
        }
    }



    private String getInOperatorQuery(List<String> codeList) {
        final String COMMA = ",";
        String commaSeparatedCodes = codeList.stream().map(putSingleQuotesAroundWords).collect(Collectors.joining(COMMA));
        return String.format("(%s)", commaSeparatedCodes);
    }

    private Function<String, String> putSingleQuotesAroundWords = (code) -> String.format("'%s'", code);
}
