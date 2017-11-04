package org.egov.swm.domain.service;

import java.util.Date;

import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.FuelType;
import org.egov.swm.domain.model.OilCompanyName;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.repository.RefillingPumpStationMessageQueueRepository;
import org.egov.swm.web.contract.Boundary;
import org.egov.swm.web.repository.BoundaryRepository;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.swm.web.requests.RefillingPumpStationRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class RefillingPumpStationService {

	private MdmsRepository mdmsRepository;

	private BoundaryRepository boundaryRepository;

	private RefillingPumpStationMessageQueueRepository refillingPumpStationMessageQueueRepository;

	public RefillingPumpStationService(MdmsRepository mdmsRepository, BoundaryRepository boundaryRepository,
			RefillingPumpStationMessageQueueRepository refillingPumpStationMessageQueueRepository) {
		this.mdmsRepository = mdmsRepository;
		this.boundaryRepository = boundaryRepository;
		this.refillingPumpStationMessageQueueRepository = refillingPumpStationMessageQueueRepository;
	}

	public RefillingPumpStationRequest create(RefillingPumpStationRequest refillingPumpStationRequest) {

		validate(refillingPumpStationRequest);
		Long userId = null;
		if (refillingPumpStationRequest.getRequestInfo() != null
				&& refillingPumpStationRequest.getRequestInfo().getUserInfo() != null
				&& null != refillingPumpStationRequest.getRequestInfo().getUserInfo().getId()) {
			userId = refillingPumpStationRequest.getRequestInfo().getUserInfo().getId();
		}

		for (RefillingPumpStation refillingPumpStation : refillingPumpStationRequest.getRefillingPumpStations()) {
			setAuditDetails(refillingPumpStation, userId);
		}

		return refillingPumpStationMessageQueueRepository.save(refillingPumpStationRequest);
	}

	private void validate(RefillingPumpStationRequest refillingPumpStationRequest) {

		JSONArray responseJSONArray;
		ObjectMapper mapper = new ObjectMapper();

		for (RefillingPumpStation refillingPumpStation : refillingPumpStationRequest.getRefillingPumpStations()) {

			// Validate Fuel Type
			if (refillingPumpStation.getTypeOfFuel() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(refillingPumpStation.getTenantId(),
						Constants.MODULE_CODE, Constants.FUELTYPE_MASTER_NAME, "code",
						refillingPumpStation.getTypeOfFuel().getCode(), refillingPumpStationRequest.getRequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0)
					refillingPumpStation.setTypeOfFuel(mapper.convertValue(responseJSONArray.get(0), FuelType.class));
				else
					throw new CustomException("FuelType",
							"Given FuelType is invalid: " + refillingPumpStation.getTypeOfFuel().getCode());

			}

			// validate Oil Company
			if (refillingPumpStation.getTypeOfPump() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(refillingPumpStation.getTenantId(),
						Constants.MODULE_CODE, Constants.OILCOMPANY_MASTER_NAME, "code",
						refillingPumpStation.getTypeOfPump().getCode(), refillingPumpStationRequest.getRequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0)
					refillingPumpStation
							.setTypeOfPump(mapper.convertValue(responseJSONArray.get(0), OilCompanyName.class));
				else
					throw new CustomException("OilCompany",
							"Given OilCompany is invalid: " + refillingPumpStation.getTypeOfPump().getCode());
			}

			// Validate Boundary
			if (refillingPumpStation.getLocation() != null && refillingPumpStation.getLocation().getBndryId() != null) {

				Boundary boundary = boundaryRepository.fetchBoundaryByCode(refillingPumpStation.getLocation().getCode(),
						refillingPumpStation.getTenantId());

				if (boundary != null)
					refillingPumpStation.setLocation(org.egov.swm.domain.model.Boundary.builder()
							.id(String.valueOf(boundary.getId())).name(boundary.getName())
							.boundaryNum(String.valueOf(boundary.getBoundaryNum())).code(boundary.getCode()).build());
				else
					throw new CustomException("Boundary",
							"Given Boundary is Invalid: " + refillingPumpStation.getLocation().getCode());
			}

		}

	}

	private void setAuditDetails(RefillingPumpStation contract, Long userId) {

		if (contract.getAuditDetails() == null)
			contract.setAuditDetails(new AuditDetails());

		if (null == contract.getName() || contract.getName().isEmpty()) {
			contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
			contract.getAuditDetails().setCreatedTime(new Date().getTime());
		}

		contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
		contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
	}
}
