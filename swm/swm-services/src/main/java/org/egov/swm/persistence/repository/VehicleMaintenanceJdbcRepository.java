package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleMaintenance;
import org.egov.swm.domain.model.VehicleMaintenanceSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.service.VehicleService;
import org.egov.swm.persistence.entity.VehicleMaintenanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VehicleMaintenanceJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_vehiclemaintenance";

    @Autowired
    private VehicleService vehicleService;

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<VehicleMaintenance> search(final VehicleMaintenanceSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), VehicleMaintenanceSearch.class);
        }

        String orderBy = "order by code";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getRegNumber() != null) {
            addAnd(params);
            params.append("vehicle in (:vehicle)");
            paramValues.put("vehicle", searchRequest.getRegNumber());
        }

        if (searchRequest.getCode() != null) {
            addAnd(params);
            params.append("code in (:code)");
            paramValues.put("code", searchRequest.getCode());
        }

        if (searchRequest.getCodes() != null) {
            addAnd(params);
            params.append("code in(:codes) ");
            paramValues.put("codes", new ArrayList<>(Arrays.asList(searchRequest.getCodes().split(","))));
        }

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getDowntimeforMaintenance() != null) {
            addAnd(params);
            params.append("downtimeforMaintenance =:downtimeforMaintenance");
            paramValues.put("downtimeforMaintenance", searchRequest.getDowntimeforMaintenance());
        }

        if (searchRequest.getMaintenanceAfter() != null) {
            addAnd(params);
            params.append("maintenanceAfter =:maintenanceAfter");
            paramValues.put("maintenanceAfter", searchRequest.getMaintenanceAfter());
        }

        Pagination<VehicleMaintenance> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<VehicleMaintenance>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(VehicleMaintenanceEntity.class);

        final List<VehicleMaintenance> vehicleMaintenanceList = new ArrayList<>();

        final List<VehicleMaintenanceEntity> vehicleMaintenanceEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (final VehicleMaintenanceEntity vehicleMaintenanceEntity : vehicleMaintenanceEntities)
            vehicleMaintenanceList.add(vehicleMaintenanceEntity.toDomain());

        if (vehicleMaintenanceList != null && !vehicleMaintenanceList.isEmpty())
            populateVehicles(vehicleMaintenanceList);

        page.setPagedData(vehicleMaintenanceList);

        return page;
    }

    private void populateVehicles(final List<VehicleMaintenance> vehicleMaintenanceList) {

        VehicleSearch vehicleSearch;
        Pagination<Vehicle> vehicles;
        final StringBuffer vehicleNos = new StringBuffer();
        final Set<String> vehicleNoSet = new HashSet<>();

        for (final VehicleMaintenance vfd : vehicleMaintenanceList)
            if (vfd.getVehicle() != null && vfd.getVehicle().getRegNumber() != null
                    && !vfd.getVehicle().getRegNumber().isEmpty())
                vehicleNoSet.add(vfd.getVehicle().getRegNumber());

        final List<String> vehicleNoList = new ArrayList(vehicleNoSet);

        for (final String vehicleNo : vehicleNoList) {

            if (vehicleNos.length() >= 1)
                vehicleNos.append(",");

            vehicleNos.append(vehicleNo);

        }
        if (vehicleNos != null && vehicleNos.length() > 0) {
            String tenantId = null;
            final Map<String, Vehicle> vehicleMap = new HashMap<>();

            if (vehicleMaintenanceList != null && !vehicleMaintenanceList.isEmpty())
                tenantId = vehicleMaintenanceList.get(0).getTenantId();

            vehicleSearch = new VehicleSearch();
            vehicleSearch.setTenantId(tenantId);
            vehicleSearch.setRegNumbers(vehicleNos.toString());

            vehicles = vehicleService.search(vehicleSearch);

            if (vehicles != null && vehicles.getPagedData() != null)
                for (final Vehicle v : vehicles.getPagedData())
                    vehicleMap.put(v.getRegNumber(), v);

            for (final VehicleMaintenance vfd : vehicleMaintenanceList)
                if (vfd.getVehicle() != null && vfd.getVehicle().getRegNumber() != null
                        && !vfd.getVehicle().getRegNumber().isEmpty())
                    vfd.setVehicle(vehicleMap.get(vfd.getVehicle().getRegNumber()));
        }
    }

}