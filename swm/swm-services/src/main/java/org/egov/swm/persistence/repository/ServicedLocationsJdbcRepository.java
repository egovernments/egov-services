package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.ServicedLocations;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicedLocationsJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_vendorservicedlocations";

    @Transactional
    public void delete(final String tenantId, final String vendor) {
        delete(TABLE_NAME, tenantId, "vendor", vendor);
    }

    public List<ServicedLocations> search(final ServicedLocations searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getVendor() != null) {
            addAnd(params);
            params.append("vendor =:vendor");
            paramValues.put("vendor", searchRequest.getVendor());
        }

        if (searchRequest.getVendorNos() != null) {
            addAnd(params);
            params.append("vendor in (:vendors)");
            paramValues.put("vendors", new ArrayList<>(Arrays.asList(searchRequest.getVendorNos().split(","))));
        }

        if (searchRequest.getLocation() != null) {
            addAnd(params);
            params.append("location =:location");
            paramValues.put("location", searchRequest.getLocation());
        }

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(ServicedLocations.class);

        return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
    }

}