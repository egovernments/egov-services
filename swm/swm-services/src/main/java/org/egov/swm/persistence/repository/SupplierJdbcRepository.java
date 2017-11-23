package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Supplier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class SupplierJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_supplier";

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public List<Supplier> search(final Supplier searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSupplierNo() != null) {
            addAnd(params);
            params.append("supplierNo =:supplierNo");
            paramValues.put("supplierNo", searchRequest.getSupplierNo());
        }

        if (searchRequest.getSupplierNos() != null) {
            addAnd(params);
            params.append("supplierNo in (:supplierNos)");
            paramValues.put("supplierNos", new ArrayList<>(Arrays.asList(searchRequest.getSupplierNos().split(","))));
        }

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(Supplier.class);

        return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
    }

}