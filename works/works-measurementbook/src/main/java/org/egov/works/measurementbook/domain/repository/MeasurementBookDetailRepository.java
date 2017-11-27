package org.egov.works.measurementbook.domain.repository;

import org.egov.works.measurementbook.common.persistence.repository.JdbcRepository;
import org.egov.works.measurementbook.persistence.helper.MeasurementBookDetailHelper;
import org.egov.works.measurementbook.web.contract.MBMeasurementSheetSearchContract;
import org.egov.works.measurementbook.web.contract.MeasurementBook;
import org.egov.works.measurementbook.web.contract.MeasurementBookDetail;
import org.egov.works.measurementbook.web.contract.MeasurementBookDetailSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MeasurementBookDetailRepository extends JdbcRepository {

    @Autowired
    private MBMeasurementSheetRepository mbMeasurementSheetRepository;
    
    public static final String TABLE_NAME = "egw_measurementbook_details mbdetail";


    public List<MeasurementBookDetail> searchMeasurementBookDetail(final MeasurementBookDetailSearchContract measurementBookDetailSearchCriteria) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (measurementBookDetailSearchCriteria.getSortBy() != null
                && !measurementBookDetailSearchCriteria.getSortBy().isEmpty()) {
            validateSortByOrder(measurementBookDetailSearchCriteria.getSortBy());
            validateEntityFieldName(measurementBookDetailSearchCriteria.getSortBy(), MeasurementBook.class);
        }


        String orderBy = "order by id";
        if (measurementBookDetailSearchCriteria.getSortBy() != null
                && !measurementBookDetailSearchCriteria.getSortBy().isEmpty()) {
            orderBy = "order by " + measurementBookDetailSearchCriteria.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (measurementBookDetailSearchCriteria.getTenantId() != null) {
            addAnd(params);
            params.append("mbdetail.tenantId =:tenantId");
            paramValues.put("tenantId", measurementBookDetailSearchCriteria.getTenantId());
        }
        if (measurementBookDetailSearchCriteria.getIds() != null) {
            addAnd(params);
            params.append("mbdetail.id in(:ids) ");
            paramValues.put("ids", measurementBookDetailSearchCriteria.getIds());
        }
        if (measurementBookDetailSearchCriteria.getMeasurementBookIds() != null && measurementBookDetailSearchCriteria.getMeasurementBookIds().size() == 1) {
            addAnd(params);
            params.append("mbdetail.measurementBook in (:measurementBook)");
            paramValues.put("measurementBook", measurementBookDetailSearchCriteria.getMeasurementBookIds());
        }

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(MeasurementBookDetailHelper.class);

        List<MeasurementBookDetailHelper> loaList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<MeasurementBookDetail> measurementBookDetails = new ArrayList<>();
        for (MeasurementBookDetailHelper measurementBookDetailHelper : loaList) {
            MeasurementBookDetail measurementBookDetail = measurementBookDetailHelper.toDomain();
            MBMeasurementSheetSearchContract mbMeasurementSheetSearchCriteria = MBMeasurementSheetSearchContract.builder()
                    .tenantId(measurementBookDetail.getTenantId()).measurementBookDetailIds(Arrays.asList(measurementBookDetail.getId())).build();

            measurementBookDetail.setMeasurementSheets(mbMeasurementSheetRepository.searchMBMeasurementSheets(mbMeasurementSheetSearchCriteria));

            measurementBookDetails.add(measurementBookDetail);
        }
        return  measurementBookDetails;
        }
}
