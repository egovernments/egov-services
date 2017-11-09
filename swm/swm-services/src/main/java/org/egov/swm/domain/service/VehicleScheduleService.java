package org.egov.swm.domain.service;

import java.util.Date;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSchedule;
import org.egov.swm.domain.model.VehicleScheduleSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.repository.VehicleScheduleRepository;
import org.egov.swm.web.contract.IdGenerationResponse;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.VehicleScheduleRequest;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.Error;
import org.egov.tracer.model.ErrorRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
@Transactional(readOnly = true)
public class VehicleScheduleService {

	@Autowired
	private VehicleScheduleRepository vehicleScheduleRepository;

	@Autowired
	private IdgenRepository idgenRepository;

	@Value("${egov.swm.vehicleschedule.transaction.num.idgen.name}")
	private String idGenNameForVehicleScheduleTNRNumPath;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private RouteService routeService;

	@Transactional
	public VehicleScheduleRequest create(VehicleScheduleRequest vehicleScheduleRequest) {

		validate(vehicleScheduleRequest);

		Long userId = null;

		for (VehicleSchedule v : vehicleScheduleRequest.getVehicleSchedules()) {

			if (vehicleScheduleRequest.getRequestInfo() != null
					&& vehicleScheduleRequest.getRequestInfo().getUserInfo() != null
					&& null != vehicleScheduleRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vehicleScheduleRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(v, userId);

			v.setTransactionNo(generateTransactionNumber(v.getTenantId(), vehicleScheduleRequest.getRequestInfo()));
		}

		return vehicleScheduleRepository.save(vehicleScheduleRequest);

	}

	@Transactional
	public VehicleScheduleRequest update(VehicleScheduleRequest vehicleScheduleRequest) {

		validate(vehicleScheduleRequest);

		Long userId = null;

		for (VehicleSchedule v : vehicleScheduleRequest.getVehicleSchedules()) {

			if (vehicleScheduleRequest.getRequestInfo() != null
					&& vehicleScheduleRequest.getRequestInfo().getUserInfo() != null
					&& null != vehicleScheduleRequest.getRequestInfo().getUserInfo().getId()) {
				userId = vehicleScheduleRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(v, userId);
		}

		validate(vehicleScheduleRequest);

		return vehicleScheduleRepository.update(vehicleScheduleRequest);

	}

	private void validate(VehicleScheduleRequest vehicleScheduleRequest) {

		RouteSearch routeSearch = new RouteSearch();
		Pagination<Route> routes;
		VehicleSearch vehicleSearch;
		Pagination<Vehicle> vehicleList;

		for (VehicleSchedule vehicleSchedule : vehicleScheduleRequest.getVehicleSchedules()) {

			// Validate Vehicle

			if (vehicleSchedule.getVehicle() != null && vehicleSchedule.getVehicle().getRegNumber() != null) {

				vehicleSearch = new VehicleSearch();
				vehicleSearch.setTenantId(vehicleSchedule.getTenantId());
				vehicleSearch.setRegNumber(vehicleSchedule.getVehicle().getRegNumber());
				vehicleList = vehicleService.search(vehicleSearch);

				if (vehicleList == null || vehicleList.getPagedData() == null || vehicleList.getPagedData().isEmpty())
					throw new CustomException("Vehicle",
							"Given Vehicle is invalid: " + vehicleSchedule.getVehicle().getRegNumber());
				else {
					vehicleSchedule.setVehicle(vehicleList.getPagedData().get(0));
				}

			}

			// Validate Route

			if (vehicleSchedule.getRoute() != null && vehicleSchedule.getRoute().getCode() != null) {

				routeSearch.setTenantId(vehicleSchedule.getTenantId());
				routeSearch.setCode(vehicleSchedule.getRoute().getCode());
				routes = routeService.search(routeSearch);

				if (routes == null || routes.getPagedData() == null || routes.getPagedData().isEmpty()) {
					throw new CustomException("Route",
							"Given Route is invalid: " + vehicleSchedule.getRoute().getCode());
				} else {
					vehicleSchedule.setRoute(routes.getPagedData().get(0));
				}

			}

			if (vehicleSchedule.getScheduledFrom() != null && vehicleSchedule.getScheduledTo() != null) {

				if (new Date(vehicleSchedule.getScheduledTo()).before(new Date(vehicleSchedule.getScheduledFrom()))) {
					throw new CustomException("ScheduledToDate ",
							"Given Scheduled To Date is invalid: " + new Date(vehicleSchedule.getScheduledTo()));
				}
			}
		}
	}

	private String generateTransactionNumber(String tenantId, RequestInfo requestInfo) {

		String transactionNumber = null;
		String response = null;
		response = idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForVehicleScheduleTNRNumPath);
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

	public Pagination<VehicleSchedule> search(VehicleScheduleSearch vehicleScheduleSearch) {

		return vehicleScheduleRepository.search(vehicleScheduleSearch);
	}

	private void setAuditDetails(VehicleSchedule contract, Long userId) {

		if (contract.getAuditDetails() == null)
			contract.setAuditDetails(new AuditDetails());

		if (null == contract.getTransactionNo() || contract.getTransactionNo().isEmpty()) {
			contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
			contract.getAuditDetails().setCreatedTime(new Date().getTime());
		}

		contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
		contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
	}

}