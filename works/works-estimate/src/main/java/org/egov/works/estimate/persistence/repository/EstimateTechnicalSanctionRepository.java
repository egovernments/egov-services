package org.egov.works.estimate.persistence.repository;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.persistence.helper.EstimateOverheadHelper;
import org.egov.works.estimate.persistence.helper.EstimateTechnicalSanctionHelper;
import org.egov.works.estimate.web.contract.EstimateOverhead;
import org.egov.works.estimate.web.contract.EstimateOverheadSearchContract;
import org.egov.works.estimate.web.contract.EstimateTechnicalSanction;
import org.egov.works.estimate.web.contract.TechnicalSanctionSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EstimateTechnicalSanctionRepository  extends JdbcRepository
{

    public static final String TABLE_NAME = "egw_estimate_technicalsanction";

    public List<EstimateTechnicalSanction> search(
            TechnicalSanctionSearchContract technicalSanctionSearchContract) {
        String searchQuery = "select * from egw_estimate_technicalsanction where id is not null ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        if (technicalSanctionSearchContract.getSortBy() != null
                && !technicalSanctionSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(technicalSanctionSearchContract.getSortBy());
            validateEntityFieldName(technicalSanctionSearchContract.getSortBy(), EstimateTechnicalSanctionHelper.class);
        }

        StringBuilder orderBy = new StringBuilder("order by createdtime");
        if (technicalSanctionSearchContract.getSortBy() != null
                && !technicalSanctionSearchContract.getSortBy().isEmpty()) {
            orderBy.delete(0,orderBy.length()).append("order by ").append(technicalSanctionSearchContract.getSortBy());
        }


        if (technicalSanctionSearchContract.getTenantId() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", technicalSanctionSearchContract.getTenantId());
        }
        if (technicalSanctionSearchContract.getIds() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("id in(:ids) ");
            paramValues.put("ids", technicalSanctionSearchContract.getIds());
        }

        if (technicalSanctionSearchContract.getDetailedEstimateIds() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("detailedEstimate in(:detailedEstimateIds) ");
            paramValues.put("detailedEstimateIds", technicalSanctionSearchContract.getDetailedEstimateIds());
        }

        if(technicalSanctionSearchContract.getTechnicalSanctionNumbers() != null && !technicalSanctionSearchContract.getTechnicalSanctionNumbers().isEmpty()) {

            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("technicalsanctionnumber in(:technicalsanctionnumbers) ");
            paramValues.put("technicalsanctionnumbers", technicalSanctionSearchContract.getTechnicalSanctionNumbers());
        }

        params.append(" and deleted = false");

        if (params.length() > 0) {
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        } else
            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);
        BeanPropertyRowMapper row = new BeanPropertyRowMapper(EstimateTechnicalSanctionHelper.class);

        List<EstimateTechnicalSanctionHelper> estimateTechnicalSanctionHelpers = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        List<EstimateTechnicalSanction> estimateTechnicalSanctions = new ArrayList<>();

        for (EstimateTechnicalSanctionHelper estimateTechnicalSanctionHelper : estimateTechnicalSanctionHelpers) {
            estimateTechnicalSanctions.add(estimateTechnicalSanctionHelper.toDomain());
        }

        return estimateTechnicalSanctions;
    }
}
