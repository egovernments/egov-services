package org.egov.swm.persistence.repository;

import org.egov.swm.domain.enums.MaintenanceType;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleMaintenanceDetails;
import org.egov.swm.domain.model.VehicleMaintenanceDetailsSearch;
import org.egov.swm.persistence.entity.VehicleMaintenanceDetailsEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class VehicleMaintenanceDetailsJdbcRepository extends JdbcRepository {

    public Pagination<VehicleMaintenanceDetails> search(VehicleMaintenanceDetailsSearch vehicleMaintenanceDetailsSearch ) {

        if (vehicleMaintenanceDetailsSearch.getSortBy() != null && !vehicleMaintenanceDetailsSearch.getSortBy().isEmpty()) {
            validateSortByOrder(vehicleMaintenanceDetailsSearch.getSortBy());
            validateEntityFieldName(vehicleMaintenanceDetailsSearch.getSortBy(), VehicleMaintenanceDetailsSearch.class);
        }

        return buildSearchQuery(vehicleMaintenanceDetailsSearch);
    }


    private Pagination<VehicleMaintenanceDetails> buildSearchQuery(VehicleMaintenanceDetailsSearch searchRequest){

        Map<String, Object> paramsMap = new HashMap<>();
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM egswm_vehiclemaintenancedetails");

        if(searchRequest.getTenantId() != null && !searchRequest.getTenantId().isEmpty()){
            addWhereClause(query, "tenantid", "tenantid");
            paramsMap.put("tenantid", searchRequest.getTenantId());
        }

        if(searchRequest.getCode() != null && !searchRequest.getCode().isEmpty()){
            addWhereClauseWithAnd(query, "code", "code");
            paramsMap.put("code", searchRequest.getCode());
        }

        if(searchRequest.getIsScheduled() != null){
            addWhereClauseWithAnd(query,"isscheduled","isscheduled");
            paramsMap.put("isscheduled", searchRequest.getIsScheduled());
        }

        if(searchRequest.getVehicleCode() != null){
            addWhereClauseWithAnd(query,"vehicle", "vehicle");
            paramsMap.put("vehicle", searchRequest.getVehicleCode());
        }

        if(searchRequest.getActualMaintenanceDate() != null){
            addWhereClauseWithAnd(query,"actualmaintenancedate", "actualmaintenancedate");
            paramsMap.put("actualmaintenancedate", searchRequest.getActualMaintenanceDate());
        }

        if(searchRequest.getMaintenanceType() != null && !searchRequest.getMaintenanceType().toString().isEmpty()){
            addWhereClauseWithAnd(query,"maintenancetype", "maintenancetype");
            paramsMap.put("maintenancetype", searchRequest.getMaintenanceType());
        }

        if(searchRequest.getVehicleScheduledMaintenanceDate() != null){
            addWhereClauseWithAnd(query,"vehiclescheduledmaintenancedate", "vehiclescheduledmaintenancedate");
            paramsMap.put("vehiclescheduledmaintenancedate", searchRequest.getVehicleScheduledMaintenanceDate());
        }

        if(searchRequest.getVehicleDowntimeActual() != null){
            addWhereClauseWithAnd(query,"vehicledowntimeactual", "vehicledowntimeactual");
            paramsMap.put("vehicledowntimeactual", searchRequest.getVehicleDowntimeActual());
        }

        if(searchRequest.getVehicleDownTimeActualUom() != null && !searchRequest.getVehicleDownTimeActualUom().isEmpty()){
            addWhereClauseWithAnd(query, "vehicledowntimeactualuom", "vehicledowntimeactualuom");
            paramsMap.put("vehicledowntimeactualuom", searchRequest.getVehicleDownTimeActualUom());
        }

        if(searchRequest.getVehicleReadingDuringMaintenance() != null){
            addWhereClauseWithAnd(query,"vehiclereadingduringmaintenance", "vehiclereadingduringmaintenance");
            paramsMap.put("vehiclereadingduringmaintenance", searchRequest.getVehicleReadingDuringMaintenance());
        }

        if(searchRequest.getRemarks() != null && !searchRequest.getRemarks().isEmpty()){
            addWhereClauseWithAnd(query, "remarks", "remarks");
            paramsMap.put("remarks", searchRequest.getRemarks());
        }

        if(searchRequest.getCostIncurred() != null){
            addWhereClauseWithAnd(query,"costincurred", "costincurred");
            paramsMap.put("costincurred", searchRequest.getCostIncurred());
        }

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            query.append("order by ").append(searchRequest.getSortBy());
        }

        Pagination<VehicleMaintenanceDetails> page = new Pagination<>();
        if (searchRequest.getOffset() != null) {
            page.setOffset(searchRequest.getOffset());
        }
        if (searchRequest.getPageSize() != null) {
            page.setPageSize(searchRequest.getPageSize());
        }

        page = (Pagination<VehicleMaintenanceDetails>) getPagination(query.toString(), page, paramsMap);

        query.append(" limit ").append(page.getPageSize()).append(" offset ").append(page.getOffset() * page.getPageSize());

        List<VehicleMaintenanceDetailsEntity> vehicleMaintenanceDetailsEntityList = namedParameterJdbcTemplate
                .query(query.toString(), paramsMap, new BeanPropertyRowMapper(VehicleMaintenanceDetailsEntity.class));

        List<VehicleMaintenanceDetails> vehicleMaintenanceDetailsListList = vehicleMaintenanceDetailsEntityList.stream()
                .map(VehicleMaintenanceDetailsEntity::toDomain)
                .collect(Collectors.toList());

        page.setTotalResults(vehicleMaintenanceDetailsListList.size());

        page.setPagedData(vehicleMaintenanceDetailsListList);

        return page;
    }

    private StringBuilder addWhereClause(StringBuilder query, String fieldName, String paramName){
        return query.append(" WHERE ").append(fieldName).append("= :").append(paramName);
    }

    private StringBuilder addWhereClauseWithAnd(StringBuilder query, String fieldName, String paramName){
        return query.append(" AND ").append(fieldName).append("= :").append(paramName);
    }
}
