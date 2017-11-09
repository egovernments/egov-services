package org.egov.swm.domain.service;

import java.util.Date;
import java.util.UUID;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleMaintenance;
import org.egov.swm.domain.model.VehicleMaintenanceSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.repository.VehicleMaintenanceRepository;
import org.egov.swm.domain.repository.VehicleRepository;
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
	private VehicleRepository vehicleRepository;

	@Transactional
	public VehicleMaintenanceRequest create(VehicleMaintenanceRequest vehicleMaintenanceRequest) {

		validate(vehicleMaintenanceRequest);

		Long userId = null;

		for (VehicleMaintenance vm : vehicleMaintenanceRequest.getVehicleMaintenances()) {

			if (vehicleMaintenanceRequest.getRequestInfo() != null
					&& vehicleMaintenanceRequest.getRequestInfo().getUserInfo() != null
					&& null != vehicleMaintenanceRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vehicleMaintenanceRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(vm, userId);

			vm.setCode(UUID.randomUUID().toString().replace("-", ""));

		}

		return vehicleMaintenanceRepository.save(vehicleMaintenanceRequest);

	}

	@Transactional
	public VehicleMaintenanceRequest update(VehicleMaintenanceRequest vehicleMaintenanceRequest) {

		validate(vehicleMaintenanceRequest);

		Long userId = null;

		for (VehicleMaintenance vm : vehicleMaintenanceRequest.getVehicleMaintenances()) {

			if (vehicleMaintenanceRequest.getRequestInfo() != null
					&& vehicleMaintenanceRequest.getRequestInfo().getUserInfo() != null
					&& null != vehicleMaintenanceRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vehicleMaintenanceRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(vm, userId);

		}

		return vehicleMaintenanceRepository.update(vehicleMaintenanceRequest);

	}

	private void validate(VehicleMaintenanceRequest vehicleMaintenanceRequest) {

		for (VehicleMaintenance vehicleMaintenance : vehicleMaintenanceRequest.getVehicleMaintenances()) {

			if (vehicleMaintenance.getVehicle() != null && (vehicleMaintenance.getVehicle().getRegNumber() == null
					|| vehicleMaintenance.getVehicle().getRegNumber().isEmpty()))
				throw new CustomException("Vehicle",
						"The field Vehicle registration number is Mandatory . It cannot be not be null or empty.Please provide correct value ");

			// Validate Vehicle
			if (vehicleMaintenance.getVehicle() != null && vehicleMaintenance.getVehicle().getRegNumber() != null) {

				VehicleSearch vehicleSearch = new VehicleSearch();
				vehicleSearch.setTenantId(vehicleMaintenance.getTenantId());
				vehicleSearch.setRegNumber(vehicleMaintenance.getVehicle().getRegNumber());
				Pagination<Vehicle> vehicleList = vehicleRepository.search(vehicleSearch);

				if (vehicleList == null || vehicleList.getPagedData() == null || vehicleList.getPagedData().isEmpty())
					throw new CustomException("Vehicle",
							"Given Vehicle is invalid: " + vehicleMaintenance.getVehicle().getRegNumber());
				else {
					vehicleMaintenance.setVehicle(vehicleList.getPagedData().get(0));
				}

			}

		}
	}

	public Pagination<VehicleMaintenance> search(VehicleMaintenanceSearch vehicleMaintenanceSearch) {

		return vehicleMaintenanceRepository.search(vehicleMaintenanceSearch);
	}

	private void setAuditDetails(VehicleMaintenance contract, Long userId) {

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