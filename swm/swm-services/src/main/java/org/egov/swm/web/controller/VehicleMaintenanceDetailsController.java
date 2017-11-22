package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.VehicleMaintenanceDetails;
import org.egov.swm.domain.model.VehicleMaintenanceDetailsSearch;
import org.egov.swm.domain.service.VehicleMaintenanceDetailsService;
import org.egov.swm.web.requests.ScheduledMaintenanceDateResponse;
import org.egov.swm.web.requests.VehicleMaintenanceDetailsRequest;
import org.egov.swm.web.requests.VehicleMaintenanceDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehiclemaintenancedetails")
public class VehicleMaintenanceDetailsController {

	VehicleMaintenanceDetailsService vehicleMaintenanceDetailsService;

	public VehicleMaintenanceDetailsController(VehicleMaintenanceDetailsService vehicleMaintenanceDetailsService) {
		this.vehicleMaintenanceDetailsService = vehicleMaintenanceDetailsService;
	}

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public VehicleMaintenanceDetailsResponse create(
			@RequestBody @Valid VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

		vehicleMaintenanceDetailsRequest = vehicleMaintenanceDetailsService.create(vehicleMaintenanceDetailsRequest);

		return VehicleMaintenanceDetailsResponse.builder()
				.responseInfo(getResponseInfo(vehicleMaintenanceDetailsRequest.getRequestInfo()))
				.vehicleMaintenanceDetails(vehicleMaintenanceDetailsRequest.getVehicleMaintenanceDetails()).build();
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public VehicleMaintenanceDetailsResponse update(
			@RequestBody @Valid VehicleMaintenanceDetailsRequest vehicleMaintenanceDetailsRequest) {

		vehicleMaintenanceDetailsRequest = vehicleMaintenanceDetailsService.update(vehicleMaintenanceDetailsRequest);

		return VehicleMaintenanceDetailsResponse.builder()
				.responseInfo(getResponseInfo(vehicleMaintenanceDetailsRequest.getRequestInfo()))
				.vehicleMaintenanceDetails(vehicleMaintenanceDetailsRequest.getVehicleMaintenanceDetails()).build();
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public VehicleMaintenanceDetailsResponse search(
			@ModelAttribute VehicleMaintenanceDetailsSearch vehicleMaintenanceDetailsSearch,
			@RequestBody RequestInfo requestInfo, @RequestParam String tenantId) {

		Pagination<VehicleMaintenanceDetails> vehicleMaintenanceDetailsList = vehicleMaintenanceDetailsService
				.search(vehicleMaintenanceDetailsSearch);

		return VehicleMaintenanceDetailsResponse.builder()
				.vehicleMaintenanceDetails(vehicleMaintenanceDetailsList.getPagedData())
				.responseInfo(getResponseInfo(requestInfo)).page(new PaginationContract(vehicleMaintenanceDetailsList))
				.build();
	}

	@PostMapping("/_getnextscheduleddate")
	@ResponseStatus(HttpStatus.OK)
	public ScheduledMaintenanceDateResponse getScheduledMaintenanceDate(@RequestBody RequestInfo requestInfo,
			@RequestParam String tenantId, @RequestParam String vehicleRegNumber) {

		Long scheduledDate = vehicleMaintenanceDetailsService.calaculateNextSceduledMaintenanceDate(tenantId,
		        vehicleRegNumber);

		return ScheduledMaintenanceDateResponse.builder().responseInfo(new ResponseInfo()).sceduledDate(scheduledDate)
				.build();
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}
