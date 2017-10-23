package org.egov.swm.domain.service;

import java.util.Date;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleFuellingDetails;
import org.egov.swm.domain.model.VehicleFuellingDetailsSearch;
import org.egov.swm.domain.repository.VehicleFuellingDetailsRepository;
import org.egov.swm.web.contract.IdGenerationResponse;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.VehicleFuellingDetailsRequest;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.Error;
import org.egov.tracer.model.ErrorRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
@Transactional(readOnly = true)
public class VehicleFuellingDetailsService {

	@Autowired
	private VehicleFuellingDetailsRepository vehicleFuellingDetailsRepository;

	@Autowired
	private IdgenRepository idgenRepository;

	@Transactional
	public VehicleFuellingDetailsRequest create(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		for (VehicleFuellingDetails v : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

			v.setTransactionNo(
					generateTransactionNumber(v.getTenantId(), vehicleFuellingDetailsRequest.getRequestInfo()));
			setAuditDetails(v, vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId());
			v.setId(UUID.randomUUID().toString().replace("-", ""));

		}

		return vehicleFuellingDetailsRepository.save(vehicleFuellingDetailsRequest);

	}

	@Transactional
	public VehicleFuellingDetailsRequest update(VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		for (VehicleFuellingDetails v : vehicleFuellingDetailsRequest.getVehicleFuellingDetails()) {

			setAuditDetails(v, vehicleFuellingDetailsRequest.getRequestInfo().getUserInfo().getId());

		}

		return vehicleFuellingDetailsRepository.update(vehicleFuellingDetailsRequest);

	}

	public Pagination<VehicleFuellingDetails> search(VehicleFuellingDetailsSearch vehicleFuellingDetailsSearch) {

		return vehicleFuellingDetailsRepository.search(vehicleFuellingDetailsSearch);
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