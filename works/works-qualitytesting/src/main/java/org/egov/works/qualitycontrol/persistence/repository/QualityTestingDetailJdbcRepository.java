package org.egov.works.qualitycontrol.persistence.repository;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.qualitycontrol.persistence.helper.QualityTestingHelper;
import org.egov.works.qualitycontrol.web.contract.QualityTesting;
import org.egov.works.qualitycontrol.web.contract.QualityTestingDetail;
import org.egov.works.qualitycontrol.web.contract.QualityTestingDetailSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QualityTestingDetailJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_qualitytesting_detail";

    public List<QualityTestingDetail> search(final QualityTestingDetailSearchContract qualityTestingDetailSearchContract) {
        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();
        String table = TABLE_NAME;


        if (qualityTestingDetailSearchContract.getSortBy() != null
                && !qualityTestingDetailSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(qualityTestingDetailSearchContract.getSortBy());
            validateEntityFieldName(qualityTestingDetailSearchContract.getSortBy(), QualityTestingDetail.class);
        }

        String orderBy = "order by id";
        if (qualityTestingDetailSearchContract.getSortBy() != null
                && !qualityTestingDetailSearchContract.getSortBy().isEmpty()) {
            orderBy = "order by " + qualityTestingDetailSearchContract.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", table);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (qualityTestingDetailSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", qualityTestingDetailSearchContract.getTenantId());
        }
        if (qualityTestingDetailSearchContract.getIds() != null) {
            addAnd(params);
            params.append("id in(:ids) ");
            paramValues.put("ids", qualityTestingDetailSearchContract.getIds());
        }

        if (qualityTestingDetailSearchContract.getQualityTesting() != null) {
            addAnd(params);
            params.append("qualityTesting in(:qualityTesting) ");
            paramValues.put("qualityTesting", qualityTestingDetailSearchContract.getQualityTesting());
        }

        params.append(" and deleted = false");
        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(QualityTestingDetail.class);

        List<QualityTestingDetail> qualityTestingDetails = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        return qualityTestingDetails;
    }
}
