package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.FuelType;
import org.egov.swm.domain.model.PumpStationFuelTypes;
import org.egov.swm.domain.service.FuelTypeService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private FuelTypeService fuelTypeService;

    public List<FuelType> search(final PumpStationFuelTypes searchRequest) {

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
            paramValues.put("pumpStations", searchRequest.getPumpStations());
        }

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(PumpStationFuelTypes.class);

        final List<FuelType> response = new ArrayList<>();

        final List<PumpStationFuelTypes> pumpStationFuelTypes = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        Map<String, FuelType> fuelTypeMap = new HashMap<>();
        String tenantId = null;

        if (pumpStationFuelTypes != null && !pumpStationFuelTypes.isEmpty())
            tenantId = pumpStationFuelTypes.get(0).getTenantId();
        
        if (tenantId != null) {
            List<FuelType> fuelTypes = fuelTypeService.getAll(tenantId, new RequestInfo());

            for (FuelType ft : fuelTypes) {
                fuelTypeMap.put(ft.getCode(), ft);
            }
        }

        for (final PumpStationFuelTypes psft : pumpStationFuelTypes) {

            response.add(fuelTypeMap.get(psft.getFuelType()));
        }

        return response;
    }

}