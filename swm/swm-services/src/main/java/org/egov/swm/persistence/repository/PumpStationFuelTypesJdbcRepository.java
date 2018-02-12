package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.PumpStationFuelTypes;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PumpStationFuelTypesJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_pumpstationfueltypes";

    @Transactional
    public void delete(final String tenantId, final String pumpStation) {
        delete(TABLE_NAME, tenantId, "pumpStation", pumpStation);
    }

    public List<PumpStationFuelTypes> search(final PumpStationFuelTypes searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getFuelType() != null) {
            addAnd(params);
            params.append("fuelType =:fuelType");
            paramValues.put("fuelType", searchRequest.getFuelType());
        }

        if (searchRequest.getPumpStation() != null) {
            addAnd(params);
            params.append("pumpStation =:pumpStation");
            paramValues.put("pumpStation", searchRequest.getPumpStation());
        }

        if (searchRequest.getPumpStations() != null) {
            addAnd(params);
            params.append("pumpStation in (:pumpStations)");
            paramValues.put("pumpStations", new ArrayList<>(Arrays.asList(searchRequest.getPumpStations().split(","))));
        }

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(PumpStationFuelTypes.class);

        final List<PumpStationFuelTypes> result = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);
        return result;
    }

}