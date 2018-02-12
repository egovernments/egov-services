package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.RefillingPumpStationSearch;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleFuellingDetails;
import org.egov.swm.domain.model.VehicleFuellingDetailsSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.service.RefillingPumpStationService;
import org.egov.swm.domain.service.VehicleService;
import org.egov.swm.persistence.entity.VehicleFuellingDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VehicleFuellingDetailsJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_vehiclefuellingdetails";

    @Autowired
    private RefillingPumpStationService refillingPumpStationService;

    @Autowired
    private VehicleService vehicleService;

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<VehicleFuellingDetails> search(final VehicleFuellingDetailsSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), VehicleFuellingDetailsSearch.class);
        }

        String orderBy = "order by transactionNo";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }
        if (searchRequest.getTransactionNo() != null) {
            addAnd(params);
            params.append("transactionNo =:transactionNo");
            paramValues.put("transactionNo", searchRequest.getTransactionNo());
        }
        if (searchRequest.getTransactionDate() != null) {
            addAnd(params);
            params.append("transactionDate =:transactionDate");
            paramValues.put("transactionDate", searchRequest.getTransactionDate());
        }
        if (searchRequest.getTransactionFromDate() != null) {
            addAnd(params);
            params.append("transactionDate >= :transactionFromDate");
            paramValues.put("transactionFromDate", searchRequest.getTransactionFromDate());
        }
        if (searchRequest.getTransactionToDate() != null) {
            addAnd(params);
            params.append("transactionDate <= :transactionToDate");
            paramValues.put("transactionToDate", searchRequest.getTransactionToDate());
        }
        if (searchRequest.getRegNumber() != null) {
            addAnd(params);
            params.append("vehicle =:vehicle");
            paramValues.put("vehicle", searchRequest.getRegNumber());
        }
        if (searchRequest.getVehicleReadingDuringFuelling() != null) {
            addAnd(params);
            params.append("vehicleReadingDuringFuelling =:vehicleReadingDuringFuelling");
            paramValues.put("vehicleReadingDuringFuelling", searchRequest.getVehicleReadingDuringFuelling());
        }

        if (searchRequest.getRefuellingStationName() != null) {
            addAnd(params);
            params.append("refuellingStation =:refuellingStation");
            paramValues.put("refuellingStation", searchRequest.getRefuellingStationName());
        }

        if (searchRequest.getFuelFilled() != null) {
            addAnd(params);
            params.append("fuelFilled =:fuelFilled");
            paramValues.put("fuelFilled", searchRequest.getFuelFilled());
        }

        if (searchRequest.getTotalCostIncurred() != null) {
            addAnd(params);
            params.append("totalCostIncurred =:totalCostIncurred");
            paramValues.put("totalCostIncurred", searchRequest.getTotalCostIncurred());
        }

        if (searchRequest.getCostIncurredFrom() != null) {
            addAnd(params);
            params.append("totalCostIncurred >=:costIncurredFrom");
            paramValues.put("costIncurredFrom", searchRequest.getCostIncurredFrom());
        }

        if (searchRequest.getCostIncurredTo() != null) {
            addAnd(params);
            params.append("totalCostIncurred <=:costIncurredTo");
            paramValues.put("costIncurredTo", searchRequest.getCostIncurredTo());
        }

        if (searchRequest.getReceiptNo() != null) {
            addAnd(params);
            params.append("receiptNo =:receiptNo");
            paramValues.put("receiptNo", searchRequest.getReceiptNo());
        }

        if (searchRequest.getReceiptDate() != null) {

            addAnd(params);
            params.append("receiptDate =:receiptDate");
            paramValues.put("receiptDate", searchRequest.getReceiptDate());
        }

        Pagination<VehicleFuellingDetails> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<VehicleFuellingDetails>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(VehicleFuellingDetailsEntity.class);

        final List<VehicleFuellingDetails> vehicleFuellingDetailsList = new ArrayList<>();

        final List<VehicleFuellingDetailsEntity> vehicleFuellingDetailsEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (final VehicleFuellingDetailsEntity vehicleFuellingDetailsEntity : vehicleFuellingDetailsEntities) {

            vehicleFuellingDetailsList.add(vehicleFuellingDetailsEntity.toDomain());
        }
        if (vehicleFuellingDetailsList != null && !vehicleFuellingDetailsList.isEmpty()) {

            populateVehicles(vehicleFuellingDetailsList);

            populateRefillingPumpStations(vehicleFuellingDetailsList);
        }
        page.setPagedData(vehicleFuellingDetailsList);

        return page;
    }

    private void populateVehicles(List<VehicleFuellingDetails> vehicleFuellingDetailsList) {

        VehicleSearch vehicleSearch;
        Pagination<Vehicle> vehicles;
        StringBuffer vehicleNos = new StringBuffer();
        Set<String> vehicleNoSet = new HashSet<>();

        for (VehicleFuellingDetails vfd : vehicleFuellingDetailsList) {

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

            if (vehicleFuellingDetailsList != null && !vehicleFuellingDetailsList.isEmpty())
                tenantId = vehicleFuellingDetailsList.get(0).getTenantId();

            vehicleSearch = new VehicleSearch();
            vehicleSearch.setTenantId(tenantId);
            vehicleSearch.setRegNumbers(vehicleNos.toString());

            vehicles = vehicleService.search(vehicleSearch);

            if (vehicles != null && vehicles.getPagedData() != null)
                for (Vehicle v : vehicles.getPagedData()) {

                    vehicleMap.put(v.getRegNumber(), v);

                }

            for (VehicleFuellingDetails vfd : vehicleFuellingDetailsList) {

                if (vfd.getVehicle() != null && vfd.getVehicle().getRegNumber() != null
                        && !vfd.getVehicle().getRegNumber().isEmpty()) {

                    vfd.setVehicle(vehicleMap.get(vfd.getVehicle().getRegNumber()));
                }

            }

        }

    }

    private void populateRefillingPumpStations(List<VehicleFuellingDetails> vehicleFuellingDetailsList) {

        RefillingPumpStationSearch refillingPumpStationSearch;
        Pagination<RefillingPumpStation> refillingPumpStations;
        StringBuffer refillingPumpStationNos = new StringBuffer();
        Set<String> refillingPumpStationNoSet = new HashSet<>();

        for (VehicleFuellingDetails v : vehicleFuellingDetailsList) {

            if (v.getRefuellingStation() != null && v.getRefuellingStation().getCode() != null
                    && !v.getRefuellingStation().getCode().isEmpty()) {

                refillingPumpStationNoSet.add(v.getRefuellingStation().getCode());

            }

        }

        List<String> refillingPumpStationNoList = new ArrayList(refillingPumpStationNoSet);

        for (String refillingPumpStationNo : refillingPumpStationNoList) {

            if (refillingPumpStationNos.length() >= 1)
                refillingPumpStationNos.append(",");

            refillingPumpStationNos.append(refillingPumpStationNo);

        }

        if (refillingPumpStationNos != null && refillingPumpStationNos.length() > 0) {

            String tenantId = null;
            Map<String, RefillingPumpStation> refillingPumpStationMap = new HashMap<>();

            if (vehicleFuellingDetailsList != null && !vehicleFuellingDetailsList.isEmpty())
                tenantId = vehicleFuellingDetailsList.get(0).getTenantId();

            refillingPumpStationSearch = new RefillingPumpStationSearch();
            refillingPumpStationSearch.setTenantId(tenantId);
            refillingPumpStationSearch.setCodes(refillingPumpStationNos.toString());

            refillingPumpStations = refillingPumpStationService.search(refillingPumpStationSearch);

            if (refillingPumpStations != null && refillingPumpStations.getPagedData() != null)
                for (RefillingPumpStation rps : refillingPumpStations.getPagedData()) {

                    refillingPumpStationMap.put(rps.getCode(), rps);

                }

            for (VehicleFuellingDetails vehicleFuellingDetails : vehicleFuellingDetailsList) {

                if (vehicleFuellingDetails.getRefuellingStation() != null
                        && vehicleFuellingDetails.getRefuellingStation().getCode() != null
                        && !vehicleFuellingDetails.getRefuellingStation().getCode().isEmpty()) {

                    vehicleFuellingDetails.setRefuellingStation(refillingPumpStationMap
                            .get(vehicleFuellingDetails.getRefuellingStation().getCode()));
                }

            }
        }
    }

}