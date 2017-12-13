package org.egov.works.workorder.domain.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.persistence.helper.LOAMeasurementSheetHelper;
import org.egov.works.workorder.web.contract.LOAMeasurementSheet;
import org.egov.works.workorder.web.contract.LOAMeasurementSheetSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LOAMeasurementSheetRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_loameasurementsheet loams";

    public List<LOAMeasurementSheet> searchLoaMeasurementSheet(
            final LOAMeasurementSheetSearchContract loaMeasurementSheetSearchCriteria) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (loaMeasurementSheetSearchCriteria.getSortBy() != null
                && !loaMeasurementSheetSearchCriteria.getSortBy().isEmpty()) {
            validateSortByOrder(loaMeasurementSheetSearchCriteria.getSortBy());
            validateEntityFieldName(loaMeasurementSheetSearchCriteria.getSortBy(), LOAMeasurementSheet.class);
        }

        String orderBy = "order by loams.id";
        if (loaMeasurementSheetSearchCriteria.getSortBy() != null
                && !loaMeasurementSheetSearchCriteria.getSortBy().isEmpty()) {
            orderBy = "order by loams." + loaMeasurementSheetSearchCriteria.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (loaMeasurementSheetSearchCriteria.getTenantId() != null) {
            addAnd(params);
            params.append("loams.tenantId =:tenantId");
            paramValues.put("tenantId", loaMeasurementSheetSearchCriteria.getTenantId());
        }
        if (loaMeasurementSheetSearchCriteria.getIds() != null) {
            addAnd(params);
            params.append("loams.id in(:ids) ");
            paramValues.put("ids", loaMeasurementSheetSearchCriteria.getIds());
        }

        if (loaMeasurementSheetSearchCriteria.getLoaActivity() != null
                && !loaMeasurementSheetSearchCriteria.getLoaActivity().isEmpty()) {
            addAnd(params);
            params.append("loams.loaactivity in(:loaactivity)");
            paramValues.put("loaactivity", loaMeasurementSheetSearchCriteria.getLoaActivity());
        }

        params.append(" and loams.deleted = false");

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(LOAMeasurementSheetHelper.class);

        List<LOAMeasurementSheetHelper> assetsForLoaList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
                row);
        List<LOAMeasurementSheet> loaMeasurementSheets = new ArrayList<>();
        for (LOAMeasurementSheetHelper lOAMeasurementSheetHelper : assetsForLoaList) {
            LOAMeasurementSheet loaMeasurementSheet = lOAMeasurementSheetHelper.toDomain();
            loaMeasurementSheets.add(loaMeasurementSheet);

        }
        return loaMeasurementSheets;
    }
}
