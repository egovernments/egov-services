package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.ContractServicesOffered;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VendorContractServicesOfferedJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_vendorcontractservicesoffered";

    @Transactional
    public void delete(final String tenantId, final String vendor) {
        delete(TABLE_NAME, tenantId, "vendorcontract", vendor);
    }

    public List<ContractServicesOffered> search(final ContractServicesOffered searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getService() != null) {
            addAnd(params);
            params.append("service =:service");
            paramValues.put("service", searchRequest.getService());
        }

        if (searchRequest.getServices() != null) {
            addAnd(params);
            params.append("service in (:services)");
            paramValues.put("services", new ArrayList<>(Arrays.asList(searchRequest.getServices().split(","))));
        }

        if (searchRequest.getVendorcontract() != null) {
            addAnd(params);
            params.append("vendorcontract =:vendorcontract");
            paramValues.put("vendorcontract", searchRequest.getVendorcontract());
        }

        if (searchRequest.getVendorcontracts() != null) {
            addAnd(params);
            params.append("vendorcontract in (:vendorcontracts)");
            paramValues.put("vendorcontracts", new ArrayList<>(Arrays.asList(searchRequest.getVendorcontracts().split(","))));
        }

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(ContractServicesOffered.class);

        return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
    }

}