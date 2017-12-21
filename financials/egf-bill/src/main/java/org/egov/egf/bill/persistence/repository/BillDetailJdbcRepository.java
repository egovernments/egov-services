package org.egov.egf.bill.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillDetailSearch;
import org.egov.egf.bill.persistence.entity.BillDetailEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillDetailJdbcRepository extends JdbcRepository {

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(BillDetailEntity.TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    @Transactional
    public void delete(final String tenantId, final String billNumber) {
        delete(BillDetailEntity.TABLE_NAME, tenantId, "bill", billNumber);
    }

    public List<BillDetail> search(final BillDetailSearch searchRequest) {

        String searchQuery = "select * from " + BillDetailEntity.TABLE_NAME + " :condition ";

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

        if (searchRequest.getBillNumber() != null) {
            addAnd(params);
            params.append("bill =:billNumber");
            paramValues.put("billNumber", searchRequest.getBillNumber());
        }

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(BillDetail.class);

        return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
    }
}