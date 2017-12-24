package org.egov.egf.bill.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillChecklistSearch;
import org.egov.egf.bill.persistence.entity.BillChecklistEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillChecklistJdbcRepository extends JdbcRepository {

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(BillChecklistEntity.TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    @Transactional
    public void delete(final String tenantId, final String billNumber) {
        delete(BillChecklistEntity.TABLE_NAME, tenantId, "bill", billNumber);
    }

    public List<BillChecklist> search(final BillChecklistSearch searchRequest) {

        String searchQuery = "select * from " + BillChecklistEntity.TABLE_NAME + " :condition ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getBill() != null) {
            addAnd(params);
            params.append("bill =:bill");
            paramValues.put("bill", searchRequest.getBill());
        }

        if (searchRequest.getBillNumbers() != null) {
            addAnd(params);
            params.append("bill in (:billNumbers)");
            paramValues.put("billNumbers", new ArrayList<>(Arrays.asList(searchRequest.getBillNumbers().split(","))));
        }

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(BillChecklistEntity.class);
        final List<BillChecklist> billChecklistList = new ArrayList<>();
        final List<BillChecklistEntity> billChecklistEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
                paramValues,
                row);

        for (final BillChecklistEntity billChecklistEntity : billChecklistEntities) {

            billChecklistList.add(billChecklistEntity.toDomain());

        }
        return billChecklistList;
    }
}