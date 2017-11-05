package org.egov.swm.domain.service;

import java.util.Date;
import java.util.UUID;

import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.FuelType;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.model.VehicleType;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.repository.VehicleRepository;
import org.egov.swm.persistence.entity.VendorEntity;
import org.egov.swm.web.contract.DesignationResponse;
import org.egov.swm.web.contract.EmployeeResponse;
import org.egov.swm.web.repository.DesignationRepository;
import org.egov.swm.web.repository.EmployeeRepository;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.swm.web.requests.VehicleRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class VehicleService {

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Autowired
	private DesignationRepository designationRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private VendorService vendorService;

	@Transactional
	public VehicleRequest create(VehicleRequest vehicleRequest) {

		validate(vehicleRequest);
		Long userId = null;
		for (Vehicle v : vehicleRequest.getVehicles()) {

			if (vehicleRequest.getRequestInfo() != null && vehicleRequest.getRequestInfo().getUserInfo() != null
					&& null != vehicleRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vehicleRequest.getRequestInfo().getUserInfo().getId();
			}
			setAuditDetails(v, userId);

			prepareInsuranceDocument(v);
		}

		return vehicleRepository.save(vehicleRequest);

	}

	private void prepareInsuranceDocument(Vehicle v) {

		if (v.getInsuranceDetails().getInsuranceDocument() != null
				&& v.getInsuranceDetails().getInsuranceDocument().getFileStoreId() != null) {
			v.getInsuranceDetails().getInsuranceDocument().setId(UUID.randomUUID().toString().replace("-", ""));
			v.getInsuranceDetails().getInsuranceDocument().setTenantId(v.getTenantId());
			v.getInsuranceDetails().getInsuranceDocument().setRefCode(v.getRegNumber());
			v.getInsuranceDetails().getInsuranceDocument().setAuditDetails(v.getAuditDetails());
		}
	}

	@Transactional
	public VehicleRequest update(VehicleRequest vehicleRequest) {

		validate(vehicleRequest);
		Long userId = null;
		for (Vehicle v : vehicleRequest.getVehicles()) {

			if (vehicleRequest.getRequestInfo() != null && vehicleRequest.getRequestInfo().getUserInfo() != null
					&& null != vehicleRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vehicleRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(v, userId);

			prepareInsuranceDocument(v);

		}

		return vehicleRepository.update(vehicleRequest);

	}

	public Pagination<Vehicle> search(VehicleSearch vehicleSearch) {

		return vehicleRepository.search(vehicleSearch);
	}

	private void validate(VehicleRequest vehicleRequest) {

		JSONArray responseJSONArray = null;
		ObjectMapper mapper = new ObjectMapper();
		DesignationResponse designationResponse = null;
		String designationId = null;
		EmployeeResponse employeeResponse = null;
		VendorSearch vendorSearch;
		Pagination<Vendor> vendors;
		for (Vehicle vehicle : vehicleRequest.getVehicles()) {

			// Validate vehicle Type
			if (vehicle.getVehicleType() != null && vehicle.getVehicleType().getCode() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(vehicle.getTenantId(), Constants.MODULE_CODE,
						Constants.VEHICLETYPE_MASTER_NAME, "code", vehicle.getVehicleType().getCode(),
						vehicleRequest.getRequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0)
					vehicle.setVehicleType(mapper.convertValue(responseJSONArray.get(0), VehicleType.class));
				else
					throw new CustomException("VehicleType",
							"Given VehicleType is invalid: " + vehicle.getVehicleType().getCode());

			}

			// Validate vendor
			if (vehicle.getVendor() != null) {

				if (vehicle.getVendor() != null && vehicle.getVendor().getVendorNo() != null) {
					vendorSearch = new VendorSearch();
					vendorSearch.setTenantId(vehicle.getTenantId());
					vendorSearch.setVendorNo(vehicle.getVendor().getVendorNo());
					vendors = vendorService.search(vendorSearch);
					if (vendors != null && vendors.getPagedData() != null && !vendors.getPagedData().isEmpty()) {
						vehicle.setVendor(vendors.getPagedData().get(0));
					} else {
						throw new CustomException("Vendor",
								"Given Vendor is invalid: " + vehicle.getVendor().getVendorNo());
					}
				}

			}

			// Validate Fuel Type
			if (vehicle.getFuelType() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(vehicle.getTenantId(), Constants.MODULE_CODE,
						Constants.FUELTYPE_MASTER_NAME, "code", vehicle.getFuelType().getCode(),
						vehicleRequest.getRequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0)
					vehicle.setFuelType(mapper.convertValue(responseJSONArray.get(0), FuelType.class));
				else
					throw new CustomException("FuelType",
							"Given FuelType is invalid: " + vehicle.getFuelType().getCode());

			}

			// Validate Driver
			if (vehicle.getDriver() != null && vehicle.getDriver().getCode() != null) {

				designationResponse = designationRepository.getDesignationByName("Driver", vehicle.getTenantId(),
						vehicleRequest.getRequestInfo());
				if (designationResponse != null && designationResponse.getDesignation() != null
						&& !designationResponse.getDesignation().isEmpty()) {
					designationId = designationResponse.getDesignation().get(0).getId().toString();
				} else {
					throw new CustomException("Driver", "Given Driver is invalid: " + vehicle.getDriver().getCode());
				}

				if (designationId != null) {
					employeeResponse = employeeRepository.getEmployeeByDesgIdAndCode(designationId,
							vehicle.getDriver().getCode(), vehicle.getTenantId(), vehicleRequest.getRequestInfo());
				} else {
					throw new CustomException("Driver", "Given Driver is invalid: " + vehicle.getDriver().getCode());
				}

				if (employeeResponse == null || employeeResponse.getEmployees() == null
						|| employeeResponse.getEmployees().isEmpty()) {
					throw new CustomException("Driver", "Given Driver is invalid: " + vehicle.getDriver().getCode());
				} else {
					vehicle.setDriver(employeeResponse.getEmployees().get(0));
				}

			}

		}
	}

	private void setAuditDetails(Vehicle contract, Long userId) {

		if (contract.getAuditDetails() == null)
			contract.setAuditDetails(new AuditDetails());

		if (null == contract.getRegNumber() || contract.getRegNumber().isEmpty()) {
			contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
			contract.getAuditDetails().setCreatedTime(new Date().getTime());
		}

		contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
		contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
	}

}