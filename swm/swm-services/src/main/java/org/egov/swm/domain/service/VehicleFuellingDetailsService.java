package org.egov.swm.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.FuelType;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleFuellingDetails;
import org.egov.swm.domain.model.VehicleFuellingDetailsSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.model.VehicleType;
import org.egov.swm.domain.repository.VehicleFuellingDetailsRepository;
import org.egov.swm.domain.repository.VehicleRepository;
import org.egov.swm.persistence.entity.RefillingPumpStationEntity;
import org.egov.swm.web.contract.IdGenerationResponse;
import org.egov.swm.web.contract.MasterDetails;
import org.egov.swm.web.contract.MdmsCriteria;
import org.egov.swm.web.contract.MdmsRequest;
import org.egov.swm.web.contract.MdmsResponse;
import org.egov.swm.web.contract.ModuleDetails;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.swm.web.requests.VehicleFuellingDetailsRequest;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.Error;
import org.egov.tracer.model.ErrorRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class VehicleFuellingDetailsService {

	@Autowired
	private VehicleFuellingDetailsRepository vehicleFuellingDetailsRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private IdgenRepository idgenRepository;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Transactional
	public VehicleFuellingDetailsRequest create(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		validate(vehicleFuellingDetailsRequest);

		Long userId = null;

		for (VehicleFuellingDetails v : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

			v.setTransactionNo(
					generateTransactionNumber(v.getTenantId(), vehicleFuellingDetailsRequest.getRequestInfo()));

			if (vehicleFuellingDetailsRequest.getRequestInfo() != null
					&& vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo() != null
					&& null != vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId();
			}
			setAuditDetails(v, userId);
			v.setId(UUID.randomUUID().toString().replace("-", ""));

		}

		return vehicleFuellingDetailsRepository.save(vehicleFuellingDetailsRequest);

	}

	@Transactional
	public VehicleFuellingDetailsRequest update(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		validate(vehicleFuellingDetailsRequest);

		Long userId = null;

		for (VehicleFuellingDetails v : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

			if (vehicleFuellingDetailsRequest.getRequestInfo() != null
					&& vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo() != null
					&& null != vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(v, userId);

		}

		return vehicleFuellingDetailsRepository.update(vehicleFuellingDetailsRequest);

	}

	public Pagination<VehicleFuellingDetails> search(VehicleFuellingDetailsSearch vehicleFuellingDetailsSearch) {

		return vehicleFuellingDetailsRepository.search(vehicleFuellingDetailsSearch);
	}

	private void validate(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		JSONArray responseJSONArray = null;
		MasterDetails[] masterDetailsArray;
		ModuleDetails[] moduleDetailsArray;
		MdmsRequest request;
		MdmsResponse response;
		ArrayList<VehicleType> vtResponseList;
		ArrayList<FuelType> ftResponseList;
		ArrayList<RefillingPumpStation> rpsResponseList;
		ObjectMapper mapper = new ObjectMapper();

		for (VehicleFuellingDetails details : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

			// Validate VehicleType
			if (details.getVehicleType() != null && details.getVehicleType().getCode() != null) {
				masterDetailsArray = new MasterDetails[1];
				masterDetailsArray[0] = MasterDetails.builder().name(Constants.VEHICLETYPE_MASTER_NAME)
						.filter("[?(@.code == '" + details.getVehicleType().getCode() + "')]").build();
				moduleDetailsArray = new ModuleDetails[1];
				moduleDetailsArray[0] = ModuleDetails.builder().moduleName(Constants.MODULE_CODE)
						.masterDetails(masterDetailsArray).build();

				request = MdmsRequest.builder()
						.mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailsArray)
								.tenantId(details.getTenantId()).build())
						.requestInfo(vehicleFuellingDetailsRequest.getRequestInfo()).build();
				response = mdmsRepository.getByCriteria(request);
				if (response == null || response.getMdmsRes() == null
						|| !response.getMdmsRes().containsKey(Constants.MODULE_CODE)
						|| response.getMdmsRes().get(Constants.MODULE_CODE) == null
						|| !response.getMdmsRes().get(Constants.MODULE_CODE)
								.containsKey(Constants.VEHICLETYPE_MASTER_NAME)
						|| response.getMdmsRes().get(Constants.MODULE_CODE)
								.get(Constants.VEHICLETYPE_MASTER_NAME) == null) {
					throw new CustomException("VehicleType",
							"Given VehicleType is invalid: " + details.getVehicleType().getCode());
				} else {
					vtResponseList = new ArrayList<VehicleType>();

					responseJSONArray = response.getMdmsRes().get(Constants.MODULE_CODE)
							.get(Constants.VEHICLETYPE_MASTER_NAME);

					for (int i = 0; i < responseJSONArray.size(); i++) {
						vtResponseList.add(mapper.convertValue(responseJSONArray.get(i), VehicleType.class));
					}

					if (vtResponseList.isEmpty())
						throw new CustomException("VehicleType",
								"Given VehicleType is invalid: " + details.getVehicleType().getCode());
					else
						details.setVehicleType(vtResponseList.get(0));
				}
			}

			// Validate Fuel Type
			if (details.getTypeOfFuel() != null) {
				masterDetailsArray = new MasterDetails[1];
				masterDetailsArray[0] = MasterDetails.builder().name(Constants.FUELTYPE_MASTER_NAME)
						.filter("[?(@.code == '" + details.getTypeOfFuel().getCode() + "')]").build();
				moduleDetailsArray = new ModuleDetails[1];
				moduleDetailsArray[0] = ModuleDetails.builder().moduleName(Constants.MODULE_CODE)
						.masterDetails(masterDetailsArray).build();

				request = MdmsRequest.builder()
						.mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailsArray)
								.tenantId(details.getTenantId()).build())
						.requestInfo(vehicleFuellingDetailsRequest.getRequestInfo()).build();
				response = mdmsRepository.getByCriteria(request);
				if (response == null || response.getMdmsRes() == null
						|| !response.getMdmsRes().containsKey(Constants.MODULE_CODE)
						|| response.getMdmsRes().get(Constants.MODULE_CODE) == null
						|| !response.getMdmsRes().get(Constants.MODULE_CODE).containsKey(Constants.FUELTYPE_MASTER_NAME)
						|| response.getMdmsRes().get(Constants.MODULE_CODE)
								.get(Constants.FUELTYPE_MASTER_NAME) == null) {
					throw new CustomException("FuelType",
							"Given FuelType is invalid: " + details.getTypeOfFuel().getCode());
				} else {
					ftResponseList = new ArrayList<FuelType>();

					responseJSONArray = response.getMdmsRes().get(Constants.MODULE_CODE)
							.get(Constants.FUELTYPE_MASTER_NAME);

					for (int i = 0; i < responseJSONArray.size(); i++) {
						ftResponseList.add(mapper.convertValue(responseJSONArray.get(i), FuelType.class));
					}

					if (ftResponseList.isEmpty())
						throw new CustomException("FuelType",
								"Given FuelType is invalid: " + details.getTypeOfFuel().getCode());
					else
						details.setTypeOfFuel(ftResponseList.get(0));
				}
			}

			// Validate Vehicle
			if (details.getVehicleRegNo() != null && details.getVehicleRegNo().getRegNumber() != null) {

				VehicleSearch vehicleSearch = new VehicleSearch();
				vehicleSearch.setTenantId(details.getTenantId());
				vehicleSearch.setRegNumber(details.getVehicleRegNo().getRegNumber());
				Pagination<Vehicle> vehicleList = vehicleRepository.search(vehicleSearch);

				if (vehicleList == null || vehicleList.getPagedData() == null || vehicleList.getPagedData().isEmpty())
					throw new CustomException("VehicleRegNo",
							"Given VehicleReg .No is invalid: " + details.getVehicleRegNo().getRegNumber());
				else {
					details.setVehicleRegNo(vehicleList.getPagedData().get(0));
				}

			}

			// Validate RefuellingPumpStation
			if (details.getRefuellingStation() != null) {
				masterDetailsArray = new MasterDetails[1];
				masterDetailsArray[0] = MasterDetails.builder().name(Constants.REFILLINGPUMPSTATION_MASTER_NAME)
						.filter("[?(@.name == '" + details.getRefuellingStation().getName() + "')]").build();
				moduleDetailsArray = new ModuleDetails[1];
				moduleDetailsArray[0] = ModuleDetails.builder().moduleName(Constants.MODULE_CODE)
						.masterDetails(masterDetailsArray).build();

				request = MdmsRequest.builder()
						.mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailsArray)
								.tenantId(details.getTenantId()).build())
						.requestInfo(vehicleFuellingDetailsRequest.getRequestInfo()).build();
				response = mdmsRepository.getByCriteria(request);
				if (response == null || response.getMdmsRes() == null
						|| !response.getMdmsRes().containsKey(Constants.MODULE_CODE)
						|| response.getMdmsRes().get(Constants.MODULE_CODE) == null
						|| !response.getMdmsRes().get(Constants.MODULE_CODE)
								.containsKey(Constants.REFILLINGPUMPSTATION_MASTER_NAME)
						|| response.getMdmsRes().get(Constants.MODULE_CODE)
								.get(Constants.REFILLINGPUMPSTATION_MASTER_NAME) == null) {
					throw new CustomException("RefuellingPumpStation",
							"Given RefuellingPumpStation is invalid: " + details.getRefuellingStation().getId());
				} else {
					rpsResponseList = new ArrayList<RefillingPumpStation>();

					responseJSONArray = response.getMdmsRes().get(Constants.MODULE_CODE)
							.get(Constants.REFILLINGPUMPSTATION_MASTER_NAME);

					for (int i = 0; i < responseJSONArray.size(); i++) {
						rpsResponseList.add(mapper
								.convertValue(responseJSONArray.get(i), RefillingPumpStationEntity.class).toDomain());
					}

					if (rpsResponseList.isEmpty())
						throw new CustomException("RefuellingPumpStation",
								"Given RefuellingPumpStation is invalid: " + details.getRefuellingStation().getId());
					else
						details.setRefuellingStation(rpsResponseList.get(0));
				}
			}

			if (details.getVehicleType() != null && details.getVehicleRegNo() != null
					&& details.getVehicleType().getCode() != null && details.getVehicleRegNo().getRegNumber() != null) {
				if (!details.getVehicleType().getCode()
						.equalsIgnoreCase(details.getVehicleRegNo().getVehicleType().getCode())) {
					throw new CustomException("VehicleRegNo",
							"Given Vehicle Registration No is invalid. Vehicle type is not matching with vehicle's vehicle type: "
									+ details.getRefuellingStation().getId());
				}
			}
		}
	}

	private void setAuditDetails(VehicleFuellingDetails contract, Long userId) {

		if (contract.getAuditDetails() == null)
			contract.setAuditDetails(new AuditDetails());

		if (null == contract.getId() || contract.getId().isEmpty()) {
			contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
			contract.getAuditDetails().setCreatedTime(new Date().getTime());
		}

		contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
		contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
	}

	private String generateTransactionNumber(String tenantId, RequestInfo requestInfo) {

		String transactionNumber = null;
		String response = null;
		response = idgenRepository.getIdGeneration(tenantId, requestInfo);
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		ErrorRes errorResponse = gson.fromJson(response, ErrorRes.class);
		IdGenerationResponse idResponse = gson.fromJson(response, IdGenerationResponse.class);

		if (errorResponse.getErrors() != null && errorResponse.getErrors().size() > 0) {
			Error error = errorResponse.getErrors().get(0);
			throw new CustomException(error.getMessage(), error.getDescription());
		} else if (idResponse.getResponseInfo() != null) {
			if (idResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase("SUCCESSFUL")) {
				if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
					transactionNumber = idResponse.getIdResponses().get(0).getId();
			}
		}

		return transactionNumber;
	}

}