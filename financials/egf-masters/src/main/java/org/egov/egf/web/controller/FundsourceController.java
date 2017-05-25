package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.Fundsource;
import org.egov.egf.persistence.queue.contract.FundsourceContract;
import org.egov.egf.persistence.queue.contract.FundsourceContractRequest;
import org.egov.egf.persistence.queue.contract.FundsourceContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.FundsourceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fundsources")
public class FundsourceController {
	@Autowired
	private FundsourceService fundsourceService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public FundsourceContractResponse create(@RequestBody @Valid FundsourceContractRequest fundsourceContractRequest,
			BindingResult errors) {
		fundsourceService.validate(fundsourceContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fundsourceContractRequest.getRequestInfo().setAction("create");
		fundsourceService.fetchRelatedContracts(fundsourceContractRequest);
		fundsourceService.push(fundsourceContractRequest);
		FundsourceContractResponse fundsourceContractResponse = new FundsourceContractResponse();
		fundsourceContractResponse.setFundsources(new ArrayList<FundsourceContract>());
		if (fundsourceContractRequest.getFundsources() != null
				&& !fundsourceContractRequest.getFundsources().isEmpty()) {
			for (FundsourceContract fundsourcesourceContract : fundsourceContractRequest.getFundsources()) {
				fundsourceContractResponse.getFundsources().add(fundsourcesourceContract);
			}
		} else if (fundsourceContractRequest.getFundsource() != null) {
			fundsourceContractResponse.setFundsource(fundsourceContractRequest.getFundsource());
		}
		fundsourceContractResponse.setResponseInfo(getResponseInfo(fundsourceContractRequest.getRequestInfo()));
		fundsourceContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return fundsourceContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public FundsourceContractResponse update(@RequestBody @Valid FundsourceContractRequest fundsourceContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {

		fundsourceService.validate(fundsourceContractRequest, "update", errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fundsourceContractRequest.getRequestInfo().setAction("update");
		fundsourceService.fetchRelatedContracts(fundsourceContractRequest);
		fundsourceContractRequest.getFundsource().setId(uniqueId);
		fundsourceService.push(fundsourceContractRequest);
		FundsourceContractResponse fundsourceContractResponse = new FundsourceContractResponse();
		fundsourceContractResponse.setFundsource(fundsourceContractRequest.getFundsource());
		fundsourceContractResponse.setResponseInfo(getResponseInfo(fundsourceContractRequest.getRequestInfo()));
		fundsourceContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return fundsourceContractResponse;
	}

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FundsourceContractResponse view(@ModelAttribute FundsourceContractRequest fundsourceContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		fundsourceService.validate(fundsourceContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fundsourceService.fetchRelatedContracts(fundsourceContractRequest);
		Fundsource fundsourceFromDb = fundsourceService.findOne(uniqueId);
		FundsourceContract fundsource = fundsourceContractRequest.getFundsource();

		ModelMapper model = new ModelMapper();
		model.map(fundsourceFromDb, fundsource);

		FundsourceContractResponse fundsourceContractResponse = new FundsourceContractResponse();
		fundsourceContractResponse.setFundsource(fundsource);
		fundsourceContractResponse.setResponseInfo(getResponseInfo(fundsourceContractRequest.getRequestInfo()));
		fundsourceContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return fundsourceContractResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public FundsourceContractResponse updateAll(@RequestBody @Valid FundsourceContractRequest fundsourceContractRequest,
			BindingResult errors) {
		fundsourceService.validate(fundsourceContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fundsourceContractRequest.getRequestInfo().setAction("updateAll");
		fundsourceService.fetchRelatedContracts(fundsourceContractRequest);
		fundsourceService.push(fundsourceContractRequest);
		FundsourceContractResponse fundsourceContractResponse = new FundsourceContractResponse();
		fundsourceContractResponse.setFundsources(new ArrayList<FundsourceContract>());
		for (FundsourceContract fundsourceContract : fundsourceContractRequest.getFundsources()) {
			fundsourceContractResponse.getFundsources().add(fundsourceContract);
		}

		fundsourceContractResponse.setResponseInfo(getResponseInfo(fundsourceContractRequest.getRequestInfo()));
		fundsourceContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return fundsourceContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FundsourceContractResponse search(@ModelAttribute FundsourceContractRequest fundsourceContractRequest,
			BindingResult errors) {
		fundsourceService.validate(fundsourceContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fundsourceService.fetchRelatedContracts(fundsourceContractRequest);
		FundsourceContractResponse fundsourceContractResponse = new FundsourceContractResponse();
		fundsourceContractResponse.setFundsources(new ArrayList<FundsourceContract>());
		fundsourceContractResponse.setPage(new Pagination());
		Page<Fundsource> allFundsources;
		ModelMapper model = new ModelMapper();

		allFundsources = fundsourceService.search(fundsourceContractRequest);
		FundsourceContract fundsourceContract = null;
		for (Fundsource b : allFundsources) {
			fundsourceContract = new FundsourceContract();
			model.map(b, fundsourceContract);
			fundsourceContractResponse.getFundsources().add(fundsourceContract);
		}
		fundsourceContractResponse.getPage().map(allFundsources);
		fundsourceContractResponse.setResponseInfo(getResponseInfo(fundsourceContractRequest.getRequestInfo()));
		fundsourceContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return fundsourceContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}