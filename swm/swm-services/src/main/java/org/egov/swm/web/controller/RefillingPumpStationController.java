package org.egov.swm.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.RefillingPumpStationSearch;
import org.egov.swm.domain.service.RefillingPumpStationService;
import org.egov.swm.web.requests.RefillingPumpStationRequest;
import org.egov.swm.web.requests.RefillingPumpStationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/refillingpumpstations")
public class RefillingPumpStationController {

	private RefillingPumpStationService refillingPumpStationService;

	public RefillingPumpStationController(RefillingPumpStationService refillingPumpStationService) {
		this.refillingPumpStationService = refillingPumpStationService;
	}

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public RefillingPumpStationResponse create(
			@RequestBody @Valid RefillingPumpStationRequest refillingPumpStationRequest) {

		RefillingPumpStationResponse refillingPumpStationResponse = new RefillingPumpStationResponse();
		refillingPumpStationResponse.setResponseInfo(getResponseInfo(refillingPumpStationRequest.getRequestInfo()));

		refillingPumpStationRequest = refillingPumpStationService.create(refillingPumpStationRequest);

		refillingPumpStationResponse.setRefillingPumpStations(refillingPumpStationRequest.getRefillingPumpStations());

		return refillingPumpStationResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public RefillingPumpStationResponse update(
			@RequestBody @Valid RefillingPumpStationRequest refillingPumpStationRequest) {

		RefillingPumpStationResponse refillingPumpStationResponse = new RefillingPumpStationResponse();
		refillingPumpStationResponse.setResponseInfo(getResponseInfo(refillingPumpStationRequest.getRequestInfo()));

		refillingPumpStationRequest = refillingPumpStationService.update(refillingPumpStationRequest);

		refillingPumpStationResponse.setRefillingPumpStations(refillingPumpStationRequest.getRefillingPumpStations());

		return refillingPumpStationResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public RefillingPumpStationResponse search(@ModelAttribute RefillingPumpStationSearch refillingPumpStationSearch,
			@RequestBody RequestInfo requestInfo, @RequestParam String tenantId) {

		Pagination<RefillingPumpStation> refillingPumpStationList = refillingPumpStationService
				.search(refillingPumpStationSearch);
		
		return RefillingPumpStationResponse.builder().refillingPumpStations(refillingPumpStationList.getPagedData())
				.responseInfo(getResponseInfo(requestInfo)).page(new PaginationContract(refillingPumpStationList))
				.build();
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}
