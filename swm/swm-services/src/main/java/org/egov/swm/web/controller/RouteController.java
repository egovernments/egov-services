package org.egov.swm.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.service.RouteService;
import org.egov.swm.web.requests.RouteRequest;
import org.egov.swm.web.requests.RouteResponse;
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
@RequestMapping("/routes")
public class RouteController {

	@Autowired
	private RouteService routeService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public RouteResponse create(@RequestBody @Valid RouteRequest routeRequest) {

		RouteResponse routeResponse = new RouteResponse();
		routeResponse.setResponseInfo(getResponseInfo(routeRequest.getRequestInfo()));

		routeRequest = routeService.create(routeRequest);

		routeResponse.setRoutes(routeRequest.getRoutes());

		return routeResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public RouteResponse update(@RequestBody @Valid RouteRequest routeRequest) {

		RouteResponse routeResponse = new RouteResponse();
		routeResponse.setResponseInfo(getResponseInfo(routeRequest.getRequestInfo()));

		routeRequest = routeService.update(routeRequest);

		routeResponse.setRoutes(routeRequest.getRoutes());

		return routeResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public RouteResponse search(@ModelAttribute RouteSearch routeSearch, @RequestBody RequestInfo requestInfo,
			@RequestParam String tenantId) {

		Pagination<Route> routeList = routeService.search(routeSearch);

		RouteResponse response = new RouteResponse();
		response.setRoutes(routeList.getPagedData());
		response.setResponseInfo(getResponseInfo(requestInfo));
		response.setPage(new PaginationContract(routeList));
		
		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}