package org.egov.swm.domain.service;

import java.util.Date;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.FuelType;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleFuellingDetails;
import org.egov.swm.domain.model.VehicleFuellingDetailsSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.repository.VehicleFuellingDetailsRepository;
import org.egov.swm.domain.repository.VehicleRepository;
import org.egov.swm.persistence.entity.RefillingPumpStationEntity;
import org.egov.swm.web.contract.IdGenerationResponse;
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

		for (VehicleFuellingDetails vfd : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

			setAuditDetails(vfd, userId);

			vfd.setTransactionNo(
					generateTransactionNumber(vfd.getTenantId(), vehicleFuellingDetailsRequest.getRequestInfo()));

			if (vehicleFuellingDetailsRequest.getRequestInfo() != null
					&& vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo() != null
					&& null != vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId();
			}

			prepareReceiptCopy(vfd);

		}

		return vehicleFuellingDetailsRepository.save(vehicleFuellingDetailsRequest);

	}

	@Transactional
	public VehicleFuellingDetailsRequest update(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		validate(vehicleFuellingDetailsRequest);

		Long userId = null;

		for (VehicleFuellingDetails vfd : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

			if (vehicleFuellingDetailsRequest.getRequestInfo() != null
					&& vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo() != null
					&& null != vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(vfd, userId);

			prepareReceiptCopy(vfd);

		}

		return vehicleFuellingDetailsRepository.update(vehicleFuellingDetailsRequest);

	}

	private void prepareReceiptCopy(VehicleFuellingDetails vfd) {

		if (vfd.getReceiptCopy() != null && vfd.getReceiptCopy().getFileStoreId() != null) {
			vfd.getReceiptCopy().setId(UUID.randomUUID().toString().replace("-", ""));
			vfd.getReceiptCopy().setTenantId(vfd.getTenantId());
			vfd.getReceiptCopy().setRefCode(vfd.getTransactionNo());
			vfd.getReceiptCopy().setAuditDetails(vfd.getAuditDetails());
		}
	}

	public Pagination<VehicleFuellingDetails> search(VehicleFuellingDetailsSearch vehicleFuellingDetailsSearch) {

		return vehicleFuellingDetailsRepository.search(vehicleFuellingDetailsSearch);
	}

	private void validate(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		JSONArray responseJSONArray = null;
		ObjectMapper mapper = new ObjectMapper();

		for (VehicleFuellingDetails details : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

			// Validate Fuel Type
			if (details.getTypeOfFuel() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(details.getTenantId(), Constants.MODULE_CODE,
						Constants.FUELTYPE_MASTER_NAME, "code", details.getTypeOfFuel().getCode(),
						vehicleFuellingDetailsRequest.getRequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0)
					details.setTypeOfFuel(mapper.convertValue(responseJSONArray.get(0), FuelType.class));
				else
					throw new CustomException("FuelType",
							"Given FuelType is invalid: " + details.getTypeOfFuel().getCode());

			}

			// Validate Vehicle
			if (details.getVehicle() != null && details.getVehicle().getRegNumber() != null) {

				VehicleSearch vehicleSearch = new VehicleSearch();
				vehicleSearch.setTenantId(details.getTenantId());
				vehicleSearch.setRegNumber(details.getVehicle().getRegNumber());
				Pagination<Vehicle> vehicleList = vehicleRepository.search(vehicleSearch);

				if (vehicleList == null || vehicleList.getPagedData() == null || vehicleList.getPagedData().isEmpty())
					throw new CustomException("Vehicle",
							"Given Vehicle is invalid: " + details.getVehicle().getRegNumber());
				else {
					details.setVehicle(vehicleList.getPagedData().get(0));
				}

			}

			// Validate RefuellingPumpStation
			if (details.getRefuellingStation() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(details.getTenantId(), Constants.MODULE_CODE,
						Constants.REFILLINGPUMPSTATION_MASTER_NAME, "name", details.getRefuellingStation().getName(),
						vehicleFuellingDetailsRequest.getRequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0)
					details.setRefuellingStation(
							mapper.convertValue(responseJSONArray.get(0), RefillingPumpStationEntity.class).toDomain());
				else
					throw new CustomException("RefuellingPumpStation",
							"Given RefuellingPumpStation is invalid: " + details.getRefuellingStation().getName());

			}

		}
	}

	private void setAuditDetails(VehicleFuellingDetails contract, Long userId) {

		if (contract.getAuditDetails() == null)
			contract.setAuditDetails(new AuditDetails());

		if (null == contract.getTransactionNo() || contract.getTransactionNo().isEmpty()) {
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