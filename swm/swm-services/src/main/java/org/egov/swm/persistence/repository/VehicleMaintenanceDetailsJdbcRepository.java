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
import org.egov.swm.domain.model.VehicleMaintenanceDetails;
import org.egov.swm.domain.model.VehicleMaintenanceDetailsSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.service.VehicleService;
import org.egov.swm.persistence.entity.VehicleMaintenanceDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleMaintenanceDetailsJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_vehiclemaintenancedetails";

    @Autowired
    private VehicleService vehicleService;

    public Pagination<VehicleMaintenanceDetails> search(final VehicleMaintenanceDetailsSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), VehicleMaintenanceDetails.class);
        }

        String orderBy = "order by vehicle";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getTransactionNo() != null) {
            addAnd(params);
            params.append("transactionNo in (:transactionNo)");
            paramValues.put("transactionNo", searchRequest.getTransactionNo());
        }

        if (searchRequest.getTransactionNos() != null) {
            addAnd(params);
            params.append("transactionNo in (:transactionNos)");
            paramValues.put("transactionNos",
                    new ArrayList<>(Arrays.asList(searchRequest.getTransactionNos().split(","))));
        }

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getIsScheduled() != null) {
            addAnd(params);
            params.append("isscheduled =:isscheduled");
            paramValues.put("isscheduled", searchRequest.getIsScheduled());
        }

        if (searchRequest.getRegNumber() != null) {
            addAnd(params);
            params.append("vehicle =:vehicle");
            paramValues.put("vehicle", searchRequest.getRegNumber());
        }

        if (searchRequest.getActualMaintenanceDate() != null) {
            addAnd(params);
            params.append("actualmaintenancedate =:actualmaintenancedate");
            paramValues.put("actualmaintenancedate", searchRequest.getActualMaintenanceDate());
        }

        if (searchRequest.getMaintenanceType() != null) {
            addAnd(params);
            params.append("maintenancetype =:maintenancetype");
            paramValues.put("maintenancetype", searchRequest.getMaintenanceType().getCode());
        }

        if (searchRequest.getVehicleScheduledMaintenanceDate() != null) {
            addAnd(params);
            params.append("vehiclescheduledmaintenancedate =:vehiclescheduledmaintenancedate");
            paramValues.put("vehiclescheduledmaintenancedate", searchRequest.getVehicleScheduledMaintenanceDate());
        }

        if (searchRequest.getVehicleDowntimeActual() != null) {
            addAnd(params);
            params.append("vehicledowntimeactual =:vehicledowntimeactual");
            paramValues.put("vehicledowntimeactual", searchRequest.getVehicleDowntimeActual());
        }

        if (searchRequest.getVehicleDownTimeActualUom() != null) {
            addAnd(params);
            params.append("vehicledowntimeactualuom =:vehicledowntimeactualuom");
            paramValues.put("vehicledowntimeactualuom", searchRequest.getVehicleDownTimeActualUom());
        }

        if (searchRequest.getVehicleReadingDuringMaintenance() != null) {
            addAnd(params);
            params.append("vehiclereadingduringmaintenance =:vehiclereadingduringmaintenance");
            paramValues.put("vehiclereadingduringmaintenance", searchRequest.getVehicleReadingDuringMaintenance());
        }

        if (searchRequest.getRemarks() != null) {
            addAnd(params);
            params.append("remarks =:remarks");
            paramValues.put("remarks", searchRequest.getRemarks());
        }

        if (searchRequest.getCostIncurred() != null) {
            addAnd(params);
            params.append("costincurred =:costincurred");
            paramValues.put("costincurred", searchRequest.getCostIncurred());
        }

        Pagination<VehicleMaintenanceDetails> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<VehicleMaintenanceDetails>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(VehicleMaintenanceDetailsEntity.class);

        final List<VehicleMaintenanceDetails> vehicleMaintenanceDetailsList = new ArrayList<>();

        final List<VehicleMaintenanceDetailsEntity> vehicleMaintenanceDetailsEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (final VehicleMaintenanceDetailsEntity vehicleMaintenanceDetailsEntity : vehicleMaintenanceDetailsEntities) {

            vehicleMaintenanceDetailsList.add(vehicleMaintenanceDetailsEntity.toDomain());
        }
        if (vehicleMaintenanceDetailsList != null && !vehicleMaintenanceDetailsList.isEmpty()) {
            populateVehicles(vehicleMaintenanceDetailsList);
        }

        page.setPagedData(vehicleMaintenanceDetailsList);

        return page;
    }

    private void populateVehicles(List<VehicleMaintenanceDetails> vehicleMaintenanceDetailsList) {

        VehicleSearch vehicleSearch;
        Pagination<Vehicle> vehicles;
        StringBuffer vehicleNos = new StringBuffer();
        Set<String> vehicleNoSet = new HashSet<>();

        for (VehicleMaintenanceDetails vfd : vehicleMaintenanceDetailsList) {

            if (vfd.getVehicle() != null && vfd.getVehicle().getRegNumber() != null
                    && !vfd.getVehicle().getRegNumber().isEmpty()) {

                vehicleNoSet.add(vfd.getVehicle().getRegNumber());

            }

        }

        List<String> vehicleNoList = new ArrayList(vehicleNoSet);

        for (String vehicleNo : vehicleNoList) {

            if (vehicleNos.length() >= 1)
                vehicleNos.append(",");

            vehicleNos.append(vehicleNo);

        }
        if (vehicleNos != null && vehicleNos.length() > 0) {
            String tenantId = null;
            Map<String, Vehicle> vehicleMap = new HashMap<>();

            if (vehicleMaintenanceDetailsList != null && !vehicleMaintenanceDetailsList.isEmpty())
                tenantId = vehicleMaintenanceDetailsList.get(0).getTenantId();

            vehicleSearch = new VehicleSearch();
            vehicleSearch.setTenantId(tenantId);
            vehicleSearch.setRegNumbers(vehicleNos.toString());

            vehicles = vehicleService.search(vehicleSearch);

            if (vehicles != null && vehicles.getPagedData() != null)
                for (Vehicle v : vehicles.getPagedData()) {

                    vehicleMap.put(v.getRegNumber(), v);

                }

            for (VehicleMaintenanceDetails vfd : vehicleMaintenanceDetailsList) {

                if (vfd.getVehicle() != null && vfd.getVehicle().getRegNumber() != null
                        && !vfd.getVehicle().getRegNumber().isEmpty()) {

                    vfd.setVehicle(vehicleMap.get(vfd.getVehicle().getRegNumber()));
                }

            }
        }

    }
}
