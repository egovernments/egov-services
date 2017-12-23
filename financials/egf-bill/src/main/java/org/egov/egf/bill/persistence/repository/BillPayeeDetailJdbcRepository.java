package org.egov.egf.bill.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.model.BillPayeeDetailSearch;
import org.egov.egf.bill.persistence.entity.BillPayeeDetailEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillPayeeDetailJdbcRepository extends JdbcRepository {

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(BillPayeeDetailEntity.TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    @Transactional
    public void delete(final String tenantId, final String billDetail) {
        delete(BillPayeeDetailEntity.TABLE_NAME, tenantId, "billdetail", billDetail);
    }

    public List<BillPayeeDetail> search(final BillPayeeDetailSearch searchRequest) {

        String searchQuery = "select * from " + BillPayeeDetailEntity.TABLE_NAME + " :condition ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getId() != null) {
            addAnd(params);
            params.append("id =:id");
            paramValues.put("id", searchRequest.getId());
        }

        if (searchRequest.getBillDetail() != null) {
            addAnd(params);
            params.append("billDetail =:billDetail");
            paramValues.put("billDetail", searchRequest.getBillDetail());
        }

        if (searchRequest.getBillDetails() != null) {
            addAnd(params);
            params.append("billDetail in (:billDetails)");
            paramValues.put("billDetails", new ArrayList<>(Arrays.asList(searchRequest.getBillDetails().split(","))));
        }

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(BillPayeeDetail.class);

        return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
    }
}