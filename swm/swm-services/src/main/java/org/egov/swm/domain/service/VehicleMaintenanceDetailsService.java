package org.egov.swm.domain.service;

import org.egov.swm.domain.model.*;
import org.egov.swm.domain.repository.VehicleMaintenanceDetailsRepository;
import org.egov.swm.domain.repository.VehicleRepository;
import org.egov.swm.web.requests.VehicleMaintenanceDetailsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class VehicleMaintenanceDetailsService {

    private VehicleRepository vehicleRepository;

    private VehicleMaintenanceDetailsRepository vehicleMaintenanceDetailsRepository;

    public VehicleMaintenanceDetailsService(VehicleRepository vehicleRepository,
                                            VehicleMaintenanceDetailsRepository vehicleMaintenanceDetailsRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMaintenanceDetailsRepository = vehicleMaintenanceDetailsRepository;
    }

    public VehicleMaintenanceDetailsRequest create (VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest){

        validate(vehicleMaintenanceDetailsRequest);
        Long userId = null;
        if (vehicleMaintenanceDetailsRequest.getRequestInfo() != null
                && vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo().getId()) {
            userId = vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo().getId();
        }

        for(VehicleMaintenanceDetails vehicleMaintenanceDetail : vehicleMaintenanceDetailsRequest.getVehicleMaintenanceDetails()){
            setAuditDetails(vehicleMaintenanceDetail, userId);
            vehicleMaintenanceDetail.setCode(UUID.randomUUID().toString().replace("-", ""));
        }

        return vehicleMaintenanceDetailsRepository.create(vehicleMaintenanceDetailsRequest);
    }

    private void validate(VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest){

        for(VehicleMaintenanceDetails vehicleMaintenanceDetails : vehicleMaintenanceDetailsRequest.getVehicleMaintenanceDetails()){

            // Validate Vehicle
            if (vehicleMaintenanceDetails.getVehicle() != null && (vehicleMaintenanceDetails.getVehicle().getRegNumber() == null
            || vehicleMaintenanceDetails.getVehicle().getRegNumber().isEmpty())) {
                throw new CustomException("Vehicle",
                        "Vehicle Registration Number required ");
            }


            if (vehicleMaintenanceDetails.getVehicle() != null && vehicleMaintenanceDetails.getVehicle().getRegNumber() != null) {

                VehicleSearch vehicleSearch = new VehicleSearch();
                vehicleSearch.setTenantId(vehicleMaintenanceDetails.getTenantId());
                vehicleSearch.setRegNumber(vehicleMaintenanceDetails.getVehicle().getRegNumber());
                Pagination<Vehicle> vehicleList = vehicleRepository.search(vehicleSearch);

                if (vehicleList == null || vehicleList.getPagedData() == null || vehicleList.getPagedData().isEmpty())
                    throw new CustomException("Vehicle",
                            "Given Vehicle is invalid: " + vehicleMaintenanceDetails.getVehicle().getRegNumber());
                else {
                    vehicleMaintenanceDetails.setVehicle(vehicleList.getPagedData().get(0));
                }

            }

        }

    }

    private void setAuditDetails(VehicleMaintenanceDetails contract, Long userId) {

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
