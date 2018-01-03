package org.egov.works.masters.domain.repository;

import common.persistence.repository.JdbcRepository;
import org.apache.commons.lang3.StringUtils;
import org.egov.works.masters.web.contract.Remarks;
import org.egov.works.masters.web.contract.RemarksDetail;
import org.egov.works.masters.web.contract.RemarksDetailsSearchContract;
import org.egov.works.masters.web.contract.RemarksHelper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**parva
 * Created by ti on 2/1/18.
 */
@Repository
public class RemarksDetailsRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_remarks_detail";

    public List<RemarksDetail> search(final RemarksDetailsSearchContract remarksDetailsSearchContract) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        if (remarksDetailsSearchContract.getSortBy() != null
                && !remarksDetailsSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(remarksDetailsSearchContract.getSortBy());
            validateEntityFieldName(remarksDetailsSearchContract.getSortBy(), RemarksHelper.class);
        }

        String orderBy = "order by id";
        if (remarksDetailsSearchContract.getSortBy() != null
                && !remarksDetailsSearchContract.getSortBy().isEmpty()) {
            orderBy = "order by " + remarksDetailsSearchContract.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

        searchQuery = searchQuery.replace(":selectfields", " * ");


        if (StringUtils.isNotBlank(remarksDetailsSearchContract.getTenantId())) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", remarksDetailsSearchContract.getTenantId());
        }
        if (remarksDetailsSearchContract.getIds() != null) {
            addAnd(params);
            params.append("id in(:ids) ");
            paramValues.put("ids", remarksDetailsSearchContract.getIds());
        }

        if (remarksDetailsSearchContract.getRemarks() != null) {
            addAnd(params);
            params.append("remarks in(:remarks) ");
            paramValues.put("remarks", remarksDetailsSearchContract.getRemarks());
        }


        params.append(" and deleted = false");
        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(RemarksDetail.class);

        List<RemarksDetail> remarksDetails = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        return remarksDetails;

    }

}
