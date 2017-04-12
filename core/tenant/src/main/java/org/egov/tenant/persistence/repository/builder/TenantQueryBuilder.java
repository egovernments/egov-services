package org.egov.tenant.persistence.repository.builder;

import org.egov.tenant.domain.model.TenantSearchCriteria;
import org.egov.tenant.persistence.repository.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by parvati on 11/4/17.
 */
@Component
public class TenantQueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(TenantQueryBuilder.class);


    private static final String TENANT_BASE_QUERY = "SELECT distinct t.id AS t_id, t.name AS t_name, t.code as t_code,t.description as t_description, "
            +" t.longitude as t_longitude,t.latitude as t_latitude,t.domainurl as t_domainurl,t.regionName as t_regionName,t.districtCode as t_districtCode, " +
            " t.districtName as t_districtName ,t.grade as t_grade ,t.active as t_active,t.localname as t_localname from tenant t";

    public String insertTenantQuery() {
        return "INSERT INTO tenant values "
                + "(nextval('seq_tenant'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }


    public String getQuery(TenantSearchCriteria tenantSearchCriteria) {

        final StringBuilder selectQuery = new StringBuilder(TENANT_BASE_QUERY);
        addWhereClause(selectQuery,tenantSearchCriteria);
        selectQuery.append(" order by t.name");

        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    private void addWhereClause(final StringBuilder selectQuery,
                                final TenantSearchCriteria tenantSearchCriteria) {

        if (tenantSearchCriteria != null && tenantSearchCriteria.getTenantCodes() == null && tenantSearchCriteria.getTenantCodes().isEmpty())
            return;

        selectQuery.append(" WHERE ");

        if (tenantSearchCriteria.getTenantCodes() != null) {
            selectQuery.append(" t.code in "+getIdQuery(tenantSearchCriteria.getTenantCodes()));
        }
    }



    private static String getIdQuery(List<String> codeList) {
        StringBuilder query = new StringBuilder("(");
        if (codeList.size() >= 1) {
            query.append("'").append(codeList.get(0).toString()).append("'");
            for (int i = 1; i < codeList.size(); i++) {
                query.append(", ").append("'").append(codeList.get(i)).append("'");
            }
        }
        return query.append(")").toString();
    }


}
