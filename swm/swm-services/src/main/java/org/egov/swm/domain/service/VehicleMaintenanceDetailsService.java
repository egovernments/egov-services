package org.egov.swm.domain.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleMaintenance;
import org.egov.swm.domain.model.VehicleMaintenanceDetails;
import org.egov.swm.domain.model.VehicleMaintenanceDetailsSearch;
import org.egov.swm.domain.model.VehicleMaintenanceSearch;
import org.egov.swm.domain.model.VehicleSchedule;
import org.egov.swm.domain.model.VehicleScheduleSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.repository.VehicleMaintenanceDetailsRepository;
import org.egov.swm.domain.repository.VehicleRepository;
import org.egov.swm.web.requests.VehicleMaintenanceDetailsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

@Service
public class VehicleMaintenanceDetailsService {

	private VehicleRepository vehicleRepository;

	private VehicleMaintenanceDetailsRepository vehicleMaintenanceDetailsRepository;

	private VehicleMaintenanceService vehicleMaintenanceService;

	private VehicleScheduleService vehicleScheduleService;

	private RouteService routeService;

	public VehicleMaintenanceDetailsService(VehicleRepository vehicleRepository,
			VehicleMaintenanceDetailsRepository vehicleMaintenanceDetailsRepository,
			VehicleMaintenanceService vehicleMaintenanceService, VehicleScheduleService vehicleScheduleService,
			RouteService routeService) {
		this.vehicleRepository = vehicleRepository;
		this.vehicleMaintenanceDetailsRepository = vehicleMaintenanceDetailsRepository;
		this.vehicleMaintenanceService = vehicleMaintenanceService;
		this.vehicleScheduleService = vehicleScheduleService;
		this.routeService = routeService;
	}

	public VehicleMaintenanceDetailsRequest create(VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

		validate(vehicleMaintenanceDetailsRequest);
		Long userId = null;
		if (vehicleMaintenanceDetailsRequest.getRequestInfo() != null
				&& vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo() != null
				&& null != vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo().getId()) {
			userId = vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo().getId();
		}

		for (VehicleMaintenanceDetails vehicleMaintenanceDetail : vehicleMaintenanceDetailsRequest
				.getVehicleMaintenanceDetails()) {
			setAuditDetails(vehicleMaintenanceDetail, userId);
			vehicleMaintenanceDetail.setCode(UUID.randomUUID().toString().replace("-", ""));
		}

		return vehicleMaintenanceDetailsRepository.create(vehicleMaintenanceDetailsRequest);
	}

	public VehicleMaintenanceDetailsRequest update(VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {
		Long userId = null;
		if (vehicleMaintenanceDetailsRequest.getRequestInfo() != null
				&& vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo() != null
				&& null != vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo().getId()) {
			userId = vehicleMaintenanceDetailsRequest.getRequestInfo().getUserInfo().getId();
		}

		for (VehicleMaintenanceDetails vehicleMaintenanceDetail : vehicleMaintenanceDetailsRequest
				.getVehicleMaintenanceDetails()) {
			setAuditDetails(vehicleMaintenanceDetail, userId);
		}

		validateForUniqueCodesInRequest(vehicleMaintenanceDetailsRequest);
		validate(vehicleMaintenanceDetailsRequest);

		return vehicleMaintenanceDetailsRepository.update(vehicleMaintenanceDetailsRequest);

	}

	public Pagination<VehicleMaintenanceDetails> search(
			VehicleMaintenanceDetailsSearch vehicleMaintenanceDetailsSearch) {

		Pagination<VehicleMaintenanceDetails> vehicleMaintenanceDetailsList = vehicleMaintenanceDetailsRepository
				.search(vehicleMaintenanceDetailsSearch);

		populateVehicleData(vehicleMaintenanceDetailsList.getPagedData());

		return vehicleMaintenanceDetailsList;
	}

	public long calaculateNextSceduledMaintenanceDate(String tenantId, String vehicleCode) {
		// Fetch vehicle maintenance detail using code and tenantid
		VehicleMaintenanceDetailsSearch vehicleMaintenanceDetailsSearch = new VehicleMaintenanceDetailsSearch();
		vehicleMaintenanceDetailsSearch.setTenantId(tenantId);
		vehicleMaintenanceDetailsSearch.setVehicle(Vehicle.builder().regNumber(vehicleCode).build());
		vehicleMaintenanceDetailsSearch.setSortBy("createddate");

		Pagination<VehicleMaintenanceDetails> vehicleMaintenanceDetailsPage = vehicleMaintenanceDetailsRepository
				.search(vehicleMaintenanceDetailsSearch);

		// Check if vehicleMaintenance detail is present
		if (!vehicleMaintenanceDetailsPage.getPagedData().isEmpty()) {
			Long lastServiceDate = vehicleMaintenanceDetailsPage.getPagedData().get(0).getActualMaintenanceDate();
			int noOfDays = fetchMaintenanceAfterFromVehicleMaintenance(
					vehicleMaintenanceDetailsPage.getPagedData().get(0));

			return DateUtils.addDays(new Date(lastServiceDate), noOfDays).getTime();
		} else {
			throw new CustomException("VehicleMaintenanceDetail", "Invalid Vehicle Maintenance Detatils:");
		}
	}

	private void validateForUniqueCodesInRequest(VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

		List<String> codesList = vehicleMaintenanceDetailsRequest.getVehicleMaintenanceDetails().stream()
				.map(VehicleMaintenanceDetails::getCode).collect(Collectors.toList());

		if (codesList.size() != codesList.stream().distinct().count())
			throw new CustomException("Code", "Duplicate codes in given Vehicle Maintenance Details:");
	}

	private List<VehicleMaintenanceDetails> populateVehicleData(
			List<VehicleMaintenanceDetails> vehicleMaintenanceDetailsList) {

		for (VehicleMaintenanceDetails vehicleMaintenanceDetail : vehicleMaintenanceDetailsList) {

			if (vehicleMaintenanceDetail.getVehicle() != null
					&& vehicleMaintenanceDetail.getVehicle().getRegNumber() != null
					&& !vehicleMaintenanceDetail.getVehicle().getRegNumber().isEmpty()) {

				VehicleSearch vehicleSearch = new VehicleSearch();
				vehicleSearch.setTenantId(vehicleMaintenanceDetail.getTenantId());
				vehicleSearch.setRegNumber(vehicleMaintenanceDetail.getVehicle().getRegNumber());
				Pagination<Vehicle> vehicleList = vehicleRepository.search(vehicleSearch);

				if (vehicleList != null || vehicleList.getPagedData() != null || !vehicleList.getPagedData().isEmpty())
					vehicleMaintenanceDetail.setVehicle(vehicleList.getPagedData().get(0));
			}
		}

		return vehicleMaintenanceDetailsList;
	}

	private void validate(VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

		for (VehicleMaintenanceDetails vehicleMaintenanceDetails : vehicleMaintenanceDetailsRequest
				.getVehicleMaintenanceDetails()) {

			// Validate Vehicle
			if (vehicleMaintenanceDetails.getVehicle() != null
					&& (vehicleMaintenanceDetails.getVehicle().getRegNumber() == null
							|| vehicleMaintenanceDetails.getVehicle().getRegNumber().isEmpty())) {
				throw new CustomException("Vehicle", "Vehicle Registration Number required ");
			}

			if (vehicleMaintenanceDetails.getVehicle() != null
					&& vehicleMaintenanceDetails.getVehicle().getRegNumber() != null) {

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

	// Return number of days from vehicle maintenance
	private int fetchMaintenanceAfterFromVehicleMaintenance(VehicleMaintenanceDetails vehicleMaintenanceDetail) {

		if (vehicleMaintenanceDetail.getVehicle() != null
				&& (vehicleMaintenanceDetail.getVehicle().getRegNumber() == null
						|| vehicleMaintenanceDetail.getVehicle().getRegNumber().isEmpty()))
			throw new CustomException("code", "Vehicle code required in maintenance details");

		VehicleMaintenanceSearch vehicleMaintenanceSearch = new VehicleMaintenanceSearch();
		vehicleMaintenanceSearch.setTenantId(vehicleMaintenanceDetail.getTenantId());
		vehicleMaintenanceDetail.setVehicle(vehicleMaintenanceDetail.getVehicle());

		Pagination<VehicleMaintenance> vehicleMaintenancePage = vehicleMaintenanceService
				.search(vehicleMaintenanceSearch);

		if (!vehicleMaintenancePage.getPagedData().isEmpty()) {

			VehicleMaintenance vehicleMaintenance = vehicleMaintenancePage.getPagedData().get(0);

			if (vehicleMaintenance.getMaintenanceUom().equals("DAYS")) {
				return Math.toIntExact(vehicleMaintenance.getMaintenanceAfter());

			} else if (vehicleMaintenance.getMaintenanceUom().equals("KMS")) {
				return Math.toIntExact(vehicleMaintenance.getMaintenanceAfter() / fetchkilometersFromRoutes(
						vehicleMaintenanceDetail, vehicleMaintenance.getMaintenanceAfter()));
			}
		} else
			throw new CustomException("VehicleMaintenance", "Vehicle Maintenance not defin:");

		return 0;
	}

	private int fetchkilometersFromRoutes(VehicleMaintenanceDetails vehicleMaintenanceDetail,
			Long vehicleMaintenanceAfter) {
		VehicleScheduleSearch vehicleScheduleSearch = new VehicleScheduleSearch();
		vehicleScheduleSearch.setTenantId(vehicleMaintenanceDetail.getTenantId());
		vehicleScheduleSearch.setVehicle(vehicleMaintenanceDetail.getVehicle());

		Pagination<VehicleSchedule> vehicleSchedulePage = vehicleScheduleService.search(vehicleScheduleSearch);

		Long dateFrom = DateUtils.addDays(new Date(vehicleMaintenanceDetail.getActualMaintenanceDate()), 1).getTime();
		Long dateTo = new Date().getTime();
		// Filtering vehicle schedule list based on from and to date
		List<VehicleSchedule> vehicleScheduleList = vehicleSchedulePage.getPagedData().stream()
				.filter(v -> (dateFrom >= v.getScheduledFrom() && dateTo <= v.getScheduledTo()))
				.collect(Collectors.toList());
		Double totalKilometers = 0.0;
		Long totalDays = 0l;

		if (!vehicleScheduleList.isEmpty()) {
			// Days calculation for filtered set of routes
			for (VehicleSchedule vehicleSchedule : vehicleScheduleList) {

				RouteSearch routeSearch = new RouteSearch();
				routeSearch.setTenantId(vehicleMaintenanceDetail.getTenantId());
				routeSearch.setCode(vehicleSchedule.getRoute().getCode());

				Pagination<Route> routePage = routeService.search(routeSearch);

				if (!routePage.getPagedData().isEmpty()) {
					Long dateDifferenceInMilliseconds = vehicleSchedule.getScheduledFrom()
							- vehicleSchedule.getScheduledTo();
					Long days = TimeUnit.DAYS.convert(dateDifferenceInMilliseconds, TimeUnit.MILLISECONDS);
					totalDays = totalDays + days;
					Double totalKilometersOnRoute = days * routePage.getPagedData().get(0).getDistance();

					if ((totalKilometers + totalKilometersOnRoute) < vehicleMaintenanceAfter) {
						totalKilometers = totalKilometers + (days * routePage.getPagedData().get(0).getDistance());
					} else {
						Double distanceDifference = vehicleMaintenanceAfter - totalKilometers;
						totalDays = totalDays + (distanceDifference.longValue()
								/ routePage.getPagedData().get(0).getDistance().longValue());

						return totalDays.intValue();
					}
				} else
					throw new CustomException("Route", "Route not defined:");
			}
		} else {
			throw new CustomException("VehicleSchedule", "Vehicle Schedule not defined:");
		}
		return totalDays.intValue();
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
