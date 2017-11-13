package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.service.VehicleService;
import org.egov.swm.web.requests.VehicleRequest;
import org.egov.swm.web.requests.VehicleResponse;
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
@RequestMapping("/vehicles")
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public VehicleResponse create(@RequestBody @Valid VehicleRequest vehicleRequest) {

		VehicleResponse vehicleResponse = new VehicleResponse();
		vehicleResponse.setResponseInfo(getResponseInfo(vehicleRequest.getRequestInfo()));

		vehicleRequest = vehicleService.create(vehicleRequest);

		vehicleResponse.setVehicles(vehicleRequest.getVehicles());

		return vehicleResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public VehicleResponse update(@RequestBody @Valid VehicleRequest vehicleRequest) {

		VehicleResponse vehicleResponse = new VehicleResponse();
		vehicleResponse.setResponseInfo(getResponseInfo(vehicleRequest.getRequestInfo()));

		vehicleRequest = vehicleService.update(vehicleRequest);

		vehicleResponse.setVehicles(vehicleRequest.getVehicles());

		return vehicleResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public VehicleResponse search(@ModelAttribute VehicleSearch vehicleSearch, @RequestBody RequestInfo requestInfo,
			@RequestParam String tenantId) {

		Pagination<Vehicle> vehicleList = vehicleService.search(vehicleSearch);

		VehicleResponse response = new VehicleResponse();
		response.setVehicles(vehicleList.getPagedData());
		response.setResponseInfo(getResponseInfo(requestInfo));
		response.setPage(new PaginationContract(vehicleList));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}