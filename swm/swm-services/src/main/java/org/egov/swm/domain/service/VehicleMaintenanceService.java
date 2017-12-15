package org.egov.swm.domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleMaintenance;
import org.egov.swm.domain.model.VehicleMaintenanceSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.repository.VehicleMaintenanceRepository;
import org.egov.swm.web.requests.VehicleMaintenanceRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VehicleMaintenanceService {

    @Autowired
    private VehicleMaintenanceRepository vehicleMaintenanceRepository;

    @Autowired
    private VehicleService vehicleService;

    @Transactional
    public VehicleMaintenanceRequest create(final VehicleMaintenanceRequest vehicleMaintenanceRequest) {

        validate(vehicleMaintenanceRequest);

        Long userId = null;

        if (vehicleMaintenanceRequest.getRequestInfo() != null
                && vehicleMaintenanceRequest.getRequestInfo().getUserInfo() != null
                && null != vehicleMaintenanceRequest.getRequestInfo().getUserInfo().getId())
            userId = vehicleMaintenanceRequest.getRequestInfo().getUserInfo().getId();

        for (final VehicleMaintenance vm : vehicleMaintenanceRequest.getVehicleMaintenances()) {

            setAuditDetails(vm, userId);

            vm.setCode(UUID.randomUUID().toString().replace("-", ""));

        }

        return vehicleMaintenanceRepository.save(vehicleMaintenanceRequest);

    }

    @Transactional
    public VehicleMaintenanceRequest update(final VehicleMaintenanceRequest vehicleMaintenanceRequest) {

        Long userId = null;

        if (vehicleMaintenanceRequest.getRequestInfo() != null
                && vehicleMaintenanceRequest.getRequestInfo().getUserInfo() != null
                && null != vehicleMaintenanceRequest.getRequestInfo().getUserInfo().getId())
            userId = vehicleMaintenanceRequest.getRequestInfo().getUserInfo().getId();

        for (final VehicleMaintenance vm : vehicleMaintenanceRequest.getVehicleMaintenances())
            setAuditDetails(vm, userId);

        validate(vehicleMaintenanceRequest);

        return vehicleMaintenanceRepository.update(vehicleMaintenanceRequest);

    }

    private void validate(final VehicleMaintenanceRequest vehicleMaintenanceRequest) {

        VehicleSearch vehicleSearch;
        Pagination<Vehicle> vehicleList;

        findDuplicatesInUniqueFields(vehicleMaintenanceRequest);

        for (final VehicleMaintenance vehicleMaintenance : vehicleMaintenanceRequest.getVehicleMaintenances()) {

            if (vehicleMaintenance.getVehicle() != null && (vehicleMaintenance.getVehicle().getRegNumber() == null
                    || vehicleMaintenance.getVehicle().getRegNumber().isEmpty()))
                throw new CustomException("Vehicle",
                        "The field Vehicle registration number is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate Vehicle
            if (vehicleMaintenance.getVehicle() != null && vehicleMaintenance.getVehicle().getRegNumber() != null
                    && !vehicleMaintenance.getVehicle().getRegNumber().isEmpty()) {

                vehicleSearch = new VehicleSearch();
                vehicleSearch.setTenantId(vehicleMaintenance.getTenantId());
                vehicleSearch.setRegNumber(vehicleMaintenance.getVehicle().getRegNumber());
                vehicleList = vehicleService.search(vehicleSearch);

                if (vehicleList == null || vehicleList.getPagedData() == null || vehicleList.getPagedData().isEmpty())
                    throw new CustomException("Vehicle",
                            "Given Vehicle is invalid: " + vehicleMaintenance.getVehicle().getRegNumber());
                else
                    vehicleMaintenance.setVehicle(vehicleList.getPagedData().get(0));

            }

            validateUniqueFields(vehicleMaintenance);

        }
    }

    private void findDuplicatesInUniqueFields(final VehicleMaintenanceRequest vehicleMaintenanceRequest) {

        final Map<String, String> regNumberMap = new HashMap<>();

        for (final VehicleMaintenance vehicleMaintenance : vehicleMaintenanceRequest.getVehicleMaintenances())
            if (vehicleMaintenance.getVehicle() != null && vehicleMaintenance.getVehicle().getRegNumber() != null
                    && !vehicleMaintenance.getVehicle().getRegNumber().isEmpty()) {

                if (regNumberMap.get(vehicleMaintenance.getVehicle().getRegNumber()) != null)
                    throw new CustomException("Name",
                            "Duplicate vehicle registration numbers in given VehicleMaintenances: "
                                    + vehicleMaintenance.getVehicle().getRegNumber());

                regNumberMap.put(vehicleMaintenance.getVehicle().getRegNumber(),
                        vehicleMaintenance.getVehicle().getRegNumber());
            }

    }

    private void validateUniqueFields(final VehicleMaintenance vehicleMaintenance) {

        if (vehicleMaintenance.getVehicle() != null && vehicleMaintenance.getVehicle().getRegNumber() != null
                && !vehicleMaintenance.getVehicle().getRegNumber().isEmpty())
            if (!vehicleMaintenanceRepository.uniqueCheck(vehicleMaintenance.getTenantId(), "vehicle",
                    vehicleMaintenance.getVehicle().getRegNumber(), "code", vehicleMaintenance.getCode()))
                throw new CustomException("vehicle",
                        "The field vehicle registration number must be unique in the system for maintenance");

    }

    public Pagination<VehicleMaintenance> search(final VehicleMaintenanceSearch vehicleMaintenanceSearch) {

        return vehicleMaintenanceRepository.search(vehicleMaintenanceSearch);
    }

    private void setAuditDetails(final VehicleMaintenance contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getCode() || contract.getCode().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

}