package org.egov.swm.domain.service;

import java.util.Date;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.model.SanitationStaffTarget;
import org.egov.swm.domain.model.SanitationStaffTargetSearch;
import org.egov.swm.domain.model.SwmProcess;
import org.egov.swm.domain.repository.SanitationStaffTargetRepository;
import org.egov.swm.persistence.entity.DumpingGroundEntity;
import org.egov.swm.web.contract.EmployeeResponse;
import org.egov.swm.web.contract.IdGenerationResponse;
import org.egov.swm.web.repository.BoundaryRepository;
import org.egov.swm.web.repository.EmployeeRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.swm.web.requests.SanitationStaffTargetRequest;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.Error;
import org.egov.tracer.model.ErrorRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class SanitationStaffTargetService {

	@Autowired
	private SanitationStaffTargetRepository sanitationStaffTargetRepository;

	@Autowired
	private IdgenRepository idgenRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private RouteService routeService;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Autowired
	private BoundaryRepository boundaryRepository;

	@Value("${egov.swm.sanitationstaff.targetnum.idgen.name}")
	private String idGenNameForTargetNumPath;

	@Transactional
	public SanitationStaffTargetRequest create(SanitationStaffTargetRequest sanitationStaffTargetRequest) {

		validate(sanitationStaffTargetRequest);

		Long userId = null;

		for (SanitationStaffTarget v : sanitationStaffTargetRequest.getSanitationStaffTargets()) {

			if (sanitationStaffTargetRequest.getRequestInfo() != null
					&& sanitationStaffTargetRequest.getRequestInfo().getUserInfo() != null
					&& null != sanitationStaffTargetRequest.getRequestInfo().getUserInfo().getId()) {
				userId = sanitationStaffTargetRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(v, userId);

			v.setTargetNo(generateTargetNumber(v.getTenantId(), sanitationStaffTargetRequest.getRequestInfo()));

		}

		return sanitationStaffTargetRepository.save(sanitationStaffTargetRequest);

	}

	@Transactional
	public SanitationStaffTargetRequest update(SanitationStaffTargetRequest sanitationStaffTargetRequest) {

		validate(sanitationStaffTargetRequest);

		Long userId = null;

		for (SanitationStaffTarget v : sanitationStaffTargetRequest.getSanitationStaffTargets()) {

			if (sanitationStaffTargetRequest.getRequestInfo() != null
					&& sanitationStaffTargetRequest.getRequestInfo().getUserInfo() != null
					&& null != sanitationStaffTargetRequest.getRequestInfo().getUserInfo().getId()) {
				userId = sanitationStaffTargetRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(v, userId);

		}

		return sanitationStaffTargetRepository.update(sanitationStaffTargetRequest);

	}

	private void validate(SanitationStaffTargetRequest sanitationStaffTargetRequest) {

		JSONArray responseJSONArray = null;
		ObjectMapper mapper = new ObjectMapper();
		EmployeeResponse employeeResponse = null;
		RouteSearch routeSearch = new RouteSearch();
		Pagination<Route> routes;
		for (SanitationStaffTarget sanitationStaffTarget : sanitationStaffTargetRequest.getSanitationStaffTargets()) {

			// Validate Boundary

			if (sanitationStaffTarget.getLocation() != null && sanitationStaffTarget.getLocation().getCode() != null) {

				Boundary boundary = boundaryRepository.fetchBoundaryByCode(
						sanitationStaffTarget.getLocation().getCode(), sanitationStaffTarget.getTenantId());

				if (boundary != null)
					sanitationStaffTarget.setLocation(boundary);
				else
					throw new CustomException("Location",
							"Given Location is Invalid: " + sanitationStaffTarget.getLocation().getCode());
			}

			// Validate Swm Process

			if (sanitationStaffTarget.getSwmProcess() != null
					&& sanitationStaffTarget.getSwmProcess().getCode() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(sanitationStaffTarget.getTenantId(),
						Constants.MODULE_CODE, Constants.SWMPROCESS_MASTER_NAME, "code",
						sanitationStaffTarget.getSwmProcess().getCode(), sanitationStaffTargetRequest.getRequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0) {
					sanitationStaffTarget
							.setSwmProcess(mapper.convertValue(responseJSONArray.get(0), SwmProcess.class));
				} else
					throw new CustomException("ServicesOffered",
							"Given ServicesOffered is invalid: " + sanitationStaffTarget.getSwmProcess().getCode());

			}

			// Validate Route

			if (sanitationStaffTarget.getRoute() != null && sanitationStaffTarget.getRoute().getCode() != null) {

				routeSearch.setTenantId(sanitationStaffTarget.getTenantId());
				routeSearch.setCode(sanitationStaffTarget.getRoute().getCode());
				routes = routeService.search(routeSearch);

				if (routes == null || routes.getPagedData() == null || routes.getPagedData().isEmpty()) {
					throw new CustomException("Route",
							"Given Route is invalid: " + sanitationStaffTarget.getRoute().getCode());
				} else {
					sanitationStaffTarget.setRoute(routes.getPagedData().get(0));
				}

			}

			// Validate Employee

			if (sanitationStaffTarget.getEmployee() != null && sanitationStaffTarget.getEmployee().getCode() != null) {

				employeeResponse = employeeRepository.getEmployeeByCode(sanitationStaffTarget.getEmployee().getCode(),
						sanitationStaffTarget.getTenantId(), sanitationStaffTargetRequest.getRequestInfo());

				if (employeeResponse == null || employeeResponse.getEmployees() == null
						|| employeeResponse.getEmployees().isEmpty()) {
					throw new CustomException("Employee",
							"Given Employee is invalid: " + sanitationStaffTarget.getEmployee().getCode());
				} else {
					sanitationStaffTarget.setEmployee(employeeResponse.getEmployees().get(0));
				}

			}

			// Validate Ending Dumping ground
			if (sanitationStaffTarget.getDumpingGround() != null
					&& sanitationStaffTarget.getDumpingGround().getCode() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(sanitationStaffTarget.getTenantId(),
						Constants.MODULE_CODE, Constants.DUMPINGGROUND_MASTER_NAME, "code",
						sanitationStaffTarget.getDumpingGround().getCode(),
						sanitationStaffTargetRequest.getRequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0)
					sanitationStaffTarget.setDumpingGround(
							mapper.convertValue(responseJSONArray.get(0), DumpingGroundEntity.class).toDomain());
				else
					throw new CustomException("DumpingGround",
							"Given DumpingGround is invalid: " + sanitationStaffTarget.getDumpingGround().getCode());

			}

		}
	}

	private String generateTargetNumber(String tenantId, RequestInfo requestInfo) {

		String targetNumber = null;
		String response = null;
		response = idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForTargetNumPath);
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		ErrorRes errorResponse = gson.fromJson(response, ErrorRes.class);
		IdGenerationResponse idResponse = gson.fromJson(response, IdGenerationResponse.class);

		if (errorResponse.getErrors() != null && errorResponse.getErrors().size() > 0) {
			Error error = errorResponse.getErrors().get(0);
			throw new CustomException(error.getMessage(), error.getDescription());
		} else if (idResponse.getResponseInfo() != null) {
			if (idResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase("SUCCESSFUL")) {
				if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
					targetNumber = idResponse.getIdResponses().get(0).getId();
			}
		}

		return targetNumber;
	}

	public Pagination<SanitationStaffTarget> search(SanitationStaffTargetSearch sanitationStaffTargetSearch) {

		return sanitationStaffTargetRepository.search(sanitationStaffTargetSearch);
	}

	private void setAuditDetails(SanitationStaffTarget contract, Long userId) {

		if (contract.getAuditDetails() == null)
			contract.setAuditDetails(new AuditDetails());

		if (null == contract.getTargetNo() || contract.getTargetNo().isEmpty()) {
			contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
			contract.getAuditDetails().setCreatedTime(new Date().getTime());
		}

		contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
		contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
	}

}