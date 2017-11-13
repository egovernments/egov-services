package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.VehicleSchedule;
import org.egov.swm.domain.model.VehicleScheduleSearch;
import org.egov.swm.domain.service.VehicleScheduleService;
import org.egov.swm.web.requests.VehicleScheduleRequest;
import org.egov.swm.web.requests.VehicleScheduleResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/vehicleschedules")
public class VehicleScheduleController {

	@Autowired
	private VehicleScheduleService vehicleScheduleService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public VehicleScheduleResponse create(@RequestBody @Valid VehicleScheduleRequest vehicleScheduleRequest) {

		VehicleScheduleResponse vehicleScheduleResponse = new VehicleScheduleResponse();
		vehicleScheduleResponse.setResponseInfo(getResponseInfo(vehicleScheduleRequest.getRequestInfo()));

		vehicleScheduleRequest = vehicleScheduleService.create(vehicleScheduleRequest);

		vehicleScheduleResponse.setVehicleSchedules(vehicleScheduleRequest.getVehicleSchedules());

		return vehicleScheduleResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public VehicleScheduleResponse update(@RequestBody @Valid VehicleScheduleRequest vehicleScheduleRequest) {

		VehicleScheduleResponse vehicleScheduleResponse = new VehicleScheduleResponse();
		vehicleScheduleResponse.setResponseInfo(getResponseInfo(vehicleScheduleRequest.getRequestInfo()));

		vehicleScheduleRequest = vehicleScheduleService.update(vehicleScheduleRequest);

		vehicleScheduleResponse.setVehicleSchedules(vehicleScheduleRequest.getVehicleSchedules());

		return vehicleScheduleResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public VehicleScheduleResponse search(@ModelAttribute VehicleScheduleSearch vehicleScheduleSearch,
			@RequestBody RequestInfo requestInfo, @RequestParam String tenantId) {

		Pagination<VehicleSchedule> vehicleScheduleList = vehicleScheduleService.search(vehicleScheduleSearch);

		VehicleScheduleResponse response = new VehicleScheduleResponse();
		response.setVehicleSchedules(vehicleScheduleList.getPagedData());
		response.setResponseInfo(getResponseInfo(requestInfo));
		response.setPage(new PaginationContract(vehicleScheduleList));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}