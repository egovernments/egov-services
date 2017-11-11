package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.VehicleTripSheetDetails;
import org.egov.swm.domain.model.VehicleTripSheetDetailsSearch;
import org.egov.swm.domain.service.VehicleTripSheetDetailsService;
import org.egov.swm.web.requests.VehicleTripSheetDetailsRequest;
import org.egov.swm.web.requests.VehicleTripSheetDetailsResponse;
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
@RequestMapping("/vehicletripsheetdetails")
public class VehicleTripSheetDetailsController {

	@Autowired
	private VehicleTripSheetDetailsService vehicleTripSheetDetailsService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public VehicleTripSheetDetailsResponse create(
			@RequestBody @Valid VehicleTripSheetDetailsRequest vehicleTripSheetDetailsRequest) {

		VehicleTripSheetDetailsResponse vehicleTripSheetDetailsResponse = new VehicleTripSheetDetailsResponse();
		vehicleTripSheetDetailsResponse
				.setResponseInfo(getResponseInfo(vehicleTripSheetDetailsRequest.getRequestInfo()));

		vehicleTripSheetDetailsRequest = vehicleTripSheetDetailsService.create(vehicleTripSheetDetailsRequest);

		vehicleTripSheetDetailsResponse
				.setVehicleTripSheetDetails(vehicleTripSheetDetailsRequest.getVehicleTripSheetDetails());

		return vehicleTripSheetDetailsResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public VehicleTripSheetDetailsResponse update(
			@RequestBody @Valid VehicleTripSheetDetailsRequest vehicleTripSheetDetailsRequest) {

		VehicleTripSheetDetailsResponse vehicleTripSheetDetailsResponse = new VehicleTripSheetDetailsResponse();
		vehicleTripSheetDetailsResponse
				.setResponseInfo(getResponseInfo(vehicleTripSheetDetailsRequest.getRequestInfo()));

		vehicleTripSheetDetailsRequest = vehicleTripSheetDetailsService.update(vehicleTripSheetDetailsRequest);

		vehicleTripSheetDetailsResponse
				.setVehicleTripSheetDetails(vehicleTripSheetDetailsRequest.getVehicleTripSheetDetails());

		return vehicleTripSheetDetailsResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public VehicleTripSheetDetailsResponse search(
			@ModelAttribute VehicleTripSheetDetailsSearch vehicleTripSheetDetailsSearch,
			@RequestBody RequestInfo requestInfo, @RequestParam String tenantId) {

		Pagination<VehicleTripSheetDetails> vehicleTripSheetDetailsList = vehicleTripSheetDetailsService
				.search(vehicleTripSheetDetailsSearch);

		VehicleTripSheetDetailsResponse response = new VehicleTripSheetDetailsResponse();
		response.setVehicleTripSheetDetails(vehicleTripSheetDetailsList.getPagedData());
		response.setResponseInfo(getResponseInfo(requestInfo));
		response.setPage(new PaginationContract(vehicleTripSheetDetailsList));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}