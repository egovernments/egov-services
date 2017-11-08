package org.egov.swm.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.*;
import org.egov.swm.domain.repository.RefillingPumpStationRepository;
import org.egov.swm.web.repository.BoundaryRepository;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.swm.web.requests.RefillingPumpStationRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RefillingPumpStationService {

	private MdmsRepository mdmsRepository;

	private BoundaryRepository boundaryRepository;

	private RefillingPumpStationRepository refillingPumpStationRepository;

	public RefillingPumpStationService(MdmsRepository mdmsRepository, BoundaryRepository boundaryRepository,
									   RefillingPumpStationRepository refillingPumpStationRepository) {
		this.mdmsRepository = mdmsRepository;
		this.boundaryRepository = boundaryRepository;
		this.refillingPumpStationRepository = refillingPumpStationRepository;
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
			refillingPumpStation.setCode(UUID.randomUUID().toString().replace("-", ""));
		}

		return refillingPumpStationRepository.save(refillingPumpStationRequest);
	}

	public RefillingPumpStationRequest update(RefillingPumpStationRequest refillingPumpStationRequest) {
		Long userId = null;
		if (refillingPumpStationRequest.getRequestInfo() != null
				&& refillingPumpStationRequest.getRequestInfo().getUserInfo() != null
				&& null != refillingPumpStationRequest.getRequestInfo().getUserInfo().getId()) {
			userId = refillingPumpStationRequest.getRequestInfo().getUserInfo().getId();
		}

		for(RefillingPumpStation refillingPumpStation : refillingPumpStationRequest.getRefillingPumpStations()) {
			setAuditDetails(refillingPumpStation, userId);
		}

		validateForUniqueCodesInRequest(refillingPumpStationRequest);
		validate(refillingPumpStationRequest);

		refillingPumpStationRepository.update(refillingPumpStationRequest);

		return refillingPumpStationRequest;
	}

	public Pagination<RefillingPumpStation> search(RefillingPumpStationSearch refillingPumpStationSearch){

		return refillingPumpStationRepository.search(refillingPumpStationSearch);
	}

	private void validateForUniqueCodesInRequest(RefillingPumpStationRequest refillingPumpStationRequest){

		List<String> codesList = refillingPumpStationRequest.getRefillingPumpStations()
				.stream().map(RefillingPumpStation::getCode)
				.collect(Collectors.toList());

		if(codesList.size() != codesList.stream().distinct().count())
			throw new CustomException("Code",
					"Duplicate codes in given Refilling Pump Stations:");
	}

	private void validate(RefillingPumpStationRequest refillingPumpStationRequest) {

		JSONArray responseJSONArray;
		ObjectMapper mapper = new ObjectMapper();

		for (RefillingPumpStation refillingPumpStation : refillingPumpStationRequest.getRefillingPumpStations()) {

			// Validate Fuel Type
			if(refillingPumpStation.getTypeOfFuel() != null && refillingPumpStation.getTypeOfFuel().getCode() == null)
				throw new CustomException("FuelType",
						"typeOfFuel code is mandatory: ");

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
			if(refillingPumpStation.getTypeOfPump() != null && refillingPumpStation.getTypeOfPump().getCode() == null)
				throw new CustomException("OilCompany",
						"typeOfPump code is mandatory ");

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
			if(refillingPumpStation.getLocation() != null && refillingPumpStation.getLocation().getCode() == null)
				throw new CustomException("Boundary",
						"Boundary code is Mandatory");

			if (refillingPumpStation.getLocation() != null && refillingPumpStation.getLocation().getCode() != null) {

				org.egov.swm.domain.model.Boundary boundary = boundaryRepository.fetchBoundaryByCode(refillingPumpStation.getLocation().getCode(),
						refillingPumpStation.getTenantId());

				if (boundary != null)
					refillingPumpStation.setLocation(boundary);
				else
					throw new CustomException("Boundary",
							"Given Boundary is Invalid: " + refillingPumpStation.getLocation().getCode());
			}

		}

	}

	private void setAuditDetails(RefillingPumpStation contract, Long userId) {

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
