package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.service.VehicleFuellingDetailsService;
import org.egov.swm.web.requests.VehicleFuellingDetailsRequest;
import org.egov.swm.web.requests.VehicleFuellingDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehiclefuellingdetailses")
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
				.setVehicleFuellingDetailses(vehicleFuellingDetailsRequest.getVehicleFuellingDetailses());

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
				.setVehicleFuellingDetailses(vehicleFuellingDetailsRequest.getVehicleFuellingDetailses());

		return vehicleFuellingDetailsResponse;
	}

	/*
	 * @PostMapping("/_search")
	 * 
	 * @ResponseBody
	 * 
	 * @ResponseStatus(HttpStatus.OK) public VehicleFuellingDetailsResponse
	 * search(
	 * 
	 * @ModelAttribute VehicleFuellingDetailsSearchContract
	 * vehicleFuellingDetailsSearchContract,
	 * 
	 * @RequestBody RequestInfo requestInfo, BindingResult errors, @RequestParam
	 * String tenantId) {
	 * 
	 * System.out.println(
	 * "requestInfo in VehicleFuellingDetailsController Search " +
	 * requestInfo.toString()); System.out.println(
	 * "requestInfo in VehicleFuellingDetailsController Search " +
	 * requestInfo.getAuthToken()); ModelMapper mapper = new ModelMapper();
	 * VehicleFuellingDetailsSearch domain = new VehicleFuellingDetailsSearch();
	 * mapper.map(vehicleFuellingDetailsSearchContract, domain);
	 * VehicleFuellingDetails contract; ModelMapper model = new ModelMapper();
	 * List<VehicleFuellingDetails> VehicleFuellingDetailss = new ArrayList<>();
	 * Pagination<VehicleFuellingDetails> vehiclefuellingdetailses =
	 * vehicleFuellingDetailsService.search(domain, errors);
	 * 
	 * if (vehiclefuellingdetailses.getPagedData() != null) { for
	 * (VehicleFuellingDetails vehicleFuellingDetails :
	 * vehiclefuellingdetailses.getPagedData()) { contract = new
	 * VehicleFuellingDetails(); model.map(vehicleFuellingDetails, contract);
	 * VehicleFuellingDetailss.add(contract); } }
	 * 
	 * VehicleFuellingDetailsResponse response = new
	 * VehicleFuellingDetailsResponse();
	 * response.setVehicleFuellingDetailses(VehicleFuellingDetailss);
	 * response.setPage(new PaginationContract(vehiclefuellingdetailses));
	 * response.setResponseInfo(getResponseInfo(requestInfo));
	 * 
	 * return response;
	 * 
	 * }
	 */
	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}