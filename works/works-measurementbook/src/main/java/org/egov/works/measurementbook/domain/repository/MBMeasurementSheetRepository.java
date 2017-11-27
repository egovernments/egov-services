package org.egov.works.measurementbook.domain.repository;

import org.egov.works.measurementbook.common.persistence.repository.JdbcRepository;
import org.egov.works.measurementbook.persistence.helper.MBMeasurementSheetHelper;
import org.egov.works.measurementbook.web.contract.MBMeasurementSheet;
import org.egov.works.measurementbook.web.contract.MBMeasurementSheetSearchContract;
import org.egov.works.measurementbook.web.contract.MeasurementBook;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MBMeasurementSheetRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_mb_measurementsheet measurementsheet";


    public List<MBMeasurementSheet> searchMBMeasurementSheets(final MBMeasurementSheetSearchContract mbMeasurementSheetSearchCriteria) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (mbMeasurementSheetSearchCriteria.getSortBy() != null
                && !mbMeasurementSheetSearchCriteria.getSortBy().isEmpty()) {
            validateSortByOrder(mbMeasurementSheetSearchCriteria.getSortBy());
            validateEntityFieldName(mbMeasurementSheetSearchCriteria.getSortBy(), MeasurementBook.class);
        }


        String orderBy = "order by id";
        if (mbMeasurementSheetSearchCriteria.getSortBy() != null
                && !mbMeasurementSheetSearchCriteria.getSortBy().isEmpty()) {
            orderBy = "order by " + mbMeasurementSheetSearchCriteria.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (mbMeasurementSheetSearchCriteria.getTenantId() != null) {
            addAnd(params);
            params.append("measurementsheet.tenantId =:tenantId");
            paramValues.put("tenantId", mbMeasurementSheetSearchCriteria.getTenantId());
        }
        if (mbMeasurementSheetSearchCriteria.getIds() != null) {
            addAnd(params);
            params.append("measurementsheet.id in(:ids) ");
            paramValues.put("ids", mbMeasurementSheetSearchCriteria.getIds());
        }
        if (mbMeasurementSheetSearchCriteria.getMeasurementBookDetailIds() != null && mbMeasurementSheetSearchCriteria.getMeasurementBookDetailIds().size() == 1) {
            addAnd(params);
            params.append("measurementsheet.measurementBookDetail in (:measurementBookDetail)");
            paramValues.put("measurementBookDetail", mbMeasurementSheetSearchCriteria.getMeasurementBookDetailIds());
        }

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(MBMeasurementSheetHelper.class);

        List<MBMeasurementSheetHelper> loaList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<MBMeasurementSheet> mbMeasurementSheets = new ArrayList<>();
        for (MBMeasurementSheetHelper mbMeasurementSheetHelper : loaList) {
            MBMeasurementSheet mbMeasurementSheet = mbMeasurementSheetHelper.toDomain();
            mbMeasurementSheets.add(mbMeasurementSheet);

        }
        return mbMeasurementSheets;
    }
}
