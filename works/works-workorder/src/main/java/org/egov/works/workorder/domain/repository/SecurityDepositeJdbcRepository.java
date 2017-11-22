package org.egov.works.workorder.domain.repository;

import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.persistence.helper.LoaActivityHelper;
import org.egov.works.workorder.persistence.helper.SecurityDepositeHelper;
import org.egov.works.workorder.web.contract.LOAActivity;
import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.egov.works.workorder.web.contract.SecurityDeposit;
import org.egov.works.workorder.web.contract.SecurityDepositeSearchCriteria;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SecurityDepositeJdbcRepository extends JdbcRepository{

    public static final String TABLE_NAME = "egw_securitydeposit securitydeposite";

    public List<SecurityDeposit> searchSecurityDeposite(final SecurityDepositeSearchCriteria securityDepositeSearchCriteria) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (securityDepositeSearchCriteria.getSortBy() != null
                && !securityDepositeSearchCriteria.getSortBy().isEmpty()) {
            validateSortByOrder(securityDepositeSearchCriteria.getSortBy());
            validateEntityFieldName(securityDepositeSearchCriteria.getSortBy(), LetterOfAcceptance.class);
        }

        String orderBy = "order by id";
        if (securityDepositeSearchCriteria.getSortBy() != null
                && !securityDepositeSearchCriteria.getSortBy().isEmpty()) {
            orderBy = "order by " + securityDepositeSearchCriteria.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (securityDepositeSearchCriteria.getTenantId() != null) {
            addAnd(params);
            params.append("securitydeposite.tenantId =:tenantId");
            paramValues.put("tenantId", securityDepositeSearchCriteria.getTenantId());
        }
        if (securityDepositeSearchCriteria.getIds() != null) {
            addAnd(params);
            params.append("securitydeposite.id in(:ids) ");
            paramValues.put("ids", securityDepositeSearchCriteria.getIds());
        }


        if (securityDepositeSearchCriteria.getLetterOfAcceptanceIds() != null && !securityDepositeSearchCriteria.getLetterOfAcceptanceIds().isEmpty()) {
            addAnd(params);
            params.append("securitydeposite.letterofacceptance in(:letterofacceptance)");
            paramValues.put("letterofacceptance", securityDepositeSearchCriteria.getLetterOfAcceptanceIds());
        }

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(SecurityDepositeHelper.class);

        List<SecurityDepositeHelper> securityDepositeHelpers = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<SecurityDeposit> securityDeposits = new ArrayList<>();
        for (SecurityDepositeHelper securityDepositeHelper : securityDepositeHelpers) {
            SecurityDeposit securityDeposit = securityDepositeHelper.toDomain();
            securityDeposits.add(securityDeposit);

        }
        return securityDeposits;
    }
}
