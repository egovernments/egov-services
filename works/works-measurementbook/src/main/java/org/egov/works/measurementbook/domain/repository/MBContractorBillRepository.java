package org.egov.works.measurementbook.domain.repository;

import org.egov.works.measurementbook.common.persistence.repository.JdbcRepository;
import org.egov.works.measurementbook.persistence.helper.MBContractorBillHelper;
import org.egov.works.measurementbook.web.contract.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MBContractorBillRepository extends JdbcRepository{
    
    public static final String TABLE_NAME = "egw_mb_contractor_bills mbcontractorbill";


    public List<MBContractorBills> searchMBContractorBill(final MBContractorBillSearchContract mbContractorBillSearchCriteria) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (mbContractorBillSearchCriteria.getSortBy() != null
                && !mbContractorBillSearchCriteria.getSortBy().isEmpty()) {
            validateSortByOrder(mbContractorBillSearchCriteria.getSortBy());
            validateEntityFieldName(mbContractorBillSearchCriteria.getSortBy(), MeasurementBook.class);
        }


        String orderBy = "order by id";
        if (mbContractorBillSearchCriteria.getSortBy() != null
                && !mbContractorBillSearchCriteria.getSortBy().isEmpty()) {
            orderBy = "order by " + mbContractorBillSearchCriteria.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (mbContractorBillSearchCriteria.getTenantId() != null) {
            addAnd(params);
            params.append("mbcontractorbill.tenantId =:tenantId");
            paramValues.put("tenantId", mbContractorBillSearchCriteria.getTenantId());
        }
        if (mbContractorBillSearchCriteria.getIds() != null) {
            addAnd(params);
            params.append("mbcontractorbill.id in(:ids) ");
            paramValues.put("ids", mbContractorBillSearchCriteria.getIds());
        }
        if (mbContractorBillSearchCriteria.getMeasurementBookIds() != null && mbContractorBillSearchCriteria.getMeasurementBookIds().size() == 1) {
            addAnd(params);
            params.append("mbcontractorbill.measurementBook in (:measurementBook)");
            paramValues.put("measurementBook", mbContractorBillSearchCriteria.getMeasurementBookIds());
        }

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(MBContractorBillHelper.class);

        List<MBContractorBillHelper> mbContractorBillHelpers = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<MBContractorBills> mbContractorBillsList = new ArrayList<>();
        for (MBContractorBillHelper mbContractorBillHelper : mbContractorBillHelpers) {
            MBContractorBills mbContractorBills = mbContractorBillHelper.toDomain();
            mbContractorBillsList.add(mbContractorBills);
        }
        return  mbContractorBillsList;
    }
}
