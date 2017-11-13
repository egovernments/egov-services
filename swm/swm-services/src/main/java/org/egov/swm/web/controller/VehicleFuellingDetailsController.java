package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.VehicleFuellingDetails;
import org.egov.swm.domain.model.VehicleFuellingDetailsSearch;
import org.egov.swm.domain.service.VehicleFuellingDetailsService;
import org.egov.swm.web.requests.VehicleFuellingDetailsRequest;
import org.egov.swm.web.requests.VehicleFuellingDetailsResponse;
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
@RequestMapping("/vehiclefuellingdetails")
public class VehicleFuellingDetailsController {

	@Autowired
	private VehicleFuellingDetailsService vehicleFuellingDetailsService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public VehicleFuellingDetailsResponse create(
			@RequestBody @Valid VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		VehicleFuellingDetailsResponse vehicleFuellingDetailsResponse = new VehicleFuellingDetailsResponse();
		vehicleFuellingDetailsResponse.setResponseInfo(getResponseInfo(vehicleFuellingDetailsRequest.getRequestInfo()));

		vehicleFuellingDetailsRequest = vehicleFuellingDetailsService.create(vehicleFuellingDetailsRequest);

		vehicleFuellingDetailsResponse
				.setVehicleFuellingDetails(vehicleFuellingDetailsRequest.getVehicleFuellingDetails());

		return vehicleFuellingDetailsResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public VehicleFuellingDetailsResponse update(
			@RequestBody @Valid VehicleFuellingDetailsRequest vehicleFuellingDetailsRequest) {

		VehicleFuellingDetailsResponse vehicleFuellingDetailsResponse = new VehicleFuellingDetailsResponse();
		vehicleFuellingDetailsResponse.setResponseInfo(getResponseInfo(vehicleFuellingDetailsRequest.getRequestInfo()));

		vehicleFuellingDetailsRequest = vehicleFuellingDetailsService.update(vehicleFuellingDetailsRequest);

		vehicleFuellingDetailsResponse
				.setVehicleFuellingDetails(vehicleFuellingDetailsRequest.getVehicleFuellingDetails());

		return vehicleFuellingDetailsResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public VehicleFuellingDetailsResponse search(
			@ModelAttribute VehicleFuellingDetailsSearch vehicleFuellingDetailsSearch,
			@RequestBody RequestInfo requestInfo, @RequestParam String tenantId) {

		Pagination<VehicleFuellingDetails> vehicleFuellingDetailsList = vehicleFuellingDetailsService
				.search(vehicleFuellingDetailsSearch);

		VehicleFuellingDetailsResponse response = new VehicleFuellingDetailsResponse();
		response.setVehicleFuellingDetails(vehicleFuellingDetailsList.getPagedData());
		response.setResponseInfo(getResponseInfo(requestInfo));
		response.setPage(new PaginationContract(vehicleFuellingDetailsList));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}