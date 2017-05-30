package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.Scheme;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.queue.contract.SchemeContract;
import org.egov.egf.persistence.queue.contract.SchemeContractRequest;
import org.egov.egf.persistence.queue.contract.SchemeContractResponse;
import org.egov.egf.persistence.service.SchemeService;
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
@RequestMapping("/schemes")
public class SchemeController {
	@Autowired
	private SchemeService schemeService;

	@PostMapping("_create")
	@ResponseStatus(HttpStatus.CREATED)
	public SchemeContractResponse create(@RequestBody @Valid SchemeContractRequest schemeContractRequest,
			BindingResult errors) {
		schemeService.validate(schemeContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		schemeContractRequest.getRequestInfo().setAction("create");
		schemeService.fetchRelatedContracts(schemeContractRequest);
		schemeService.push(schemeContractRequest);
		SchemeContractResponse schemeContractResponse = new SchemeContractResponse();
		schemeContractResponse.setSchemes(new ArrayList<SchemeContract>());
		if (schemeContractRequest.getSchemes() != null && !schemeContractRequest.getSchemes().isEmpty()) {
			for (SchemeContract schemeContract : schemeContractRequest.getSchemes()) {
				schemeContractResponse.getSchemes().add(schemeContract);
			}
		} else if (schemeContractRequest.getScheme() != null) {
			schemeContractResponse.setScheme(schemeContractRequest.getScheme());
		}
		schemeContractResponse.setResponseInfo(getResponseInfo(schemeContractRequest.getRequestInfo()));
		schemeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return schemeContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public SchemeContractResponse update(@RequestBody @Valid SchemeContractRequest schemeContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {

		schemeService.validate(schemeContractRequest, "update", errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		schemeContractRequest.getRequestInfo().setAction("update");
		schemeService.fetchRelatedContracts(schemeContractRequest);
		schemeContractRequest.getScheme().setId(uniqueId);
		schemeService.push(schemeContractRequest);
		SchemeContractResponse schemeContractResponse = new SchemeContractResponse();
		schemeContractResponse.setScheme(schemeContractRequest.getScheme());
		schemeContractResponse.setResponseInfo(getResponseInfo(schemeContractRequest.getRequestInfo()));
		schemeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return schemeContractResponse;
	}

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public SchemeContractResponse view(@ModelAttribute SchemeContractRequest schemeContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		schemeService.validate(schemeContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		schemeService.fetchRelatedContracts(schemeContractRequest);
		Scheme schemeFromDb = schemeService.findOne(uniqueId);
		SchemeContract scheme = schemeContractRequest.getScheme();

		ModelMapper model = new ModelMapper();
		model.map(schemeFromDb, scheme);

		SchemeContractResponse schemeContractResponse = new SchemeContractResponse();
		schemeContractResponse.setScheme(scheme);
		schemeContractResponse.setResponseInfo(getResponseInfo(schemeContractRequest.getRequestInfo()));
		schemeContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return schemeContractResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public SchemeContractResponse updateAll(@RequestBody @Valid SchemeContractRequest schemeContractRequest,
			BindingResult errors) {
		schemeService.validate(schemeContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		schemeContractRequest.getRequestInfo().setAction("updateAll");
		schemeService.fetchRelatedContracts(schemeContractRequest);
		schemeService.push(schemeContractRequest);
		SchemeContractResponse schemeContractResponse = new SchemeContractResponse();
		schemeContractResponse.setSchemes(new ArrayList<SchemeContract>());
		for (SchemeContract schemeContract : schemeContractRequest.getSchemes()) {
			schemeContractResponse.getSchemes().add(schemeContract);
		}

		schemeContractResponse.setResponseInfo(getResponseInfo(schemeContractRequest.getRequestInfo()));
		schemeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return schemeContractResponse;
	}

	@PostMapping("_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SchemeContractResponse search(@ModelAttribute SchemeContract schemeContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		SchemeContractRequest schemeContractRequest = new SchemeContractRequest();
		schemeContractRequest.setScheme(schemeContracts);
		schemeContractRequest.setRequestInfo(requestInfo);
		schemeService.validate(schemeContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		schemeService.fetchRelatedContracts(schemeContractRequest);
		SchemeContractResponse schemeContractResponse = new SchemeContractResponse();
		schemeContractResponse.setSchemes(new ArrayList<SchemeContract>());
		schemeContractResponse.setPage(new Pagination());
		Page<Scheme> allSchemes;
		ModelMapper model = new ModelMapper();

		allSchemes = schemeService.search(schemeContractRequest);
		SchemeContract schemeContract = null;
		for (Scheme b : allSchemes) {
			schemeContract = new SchemeContract();
			model.map(b, schemeContract);
			schemeContractResponse.getSchemes().add(schemeContract);
		}
		schemeContractResponse.getPage().map(allSchemes);
		schemeContractResponse.setResponseInfo(getResponseInfo(schemeContractRequest.getRequestInfo()));
		schemeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return schemeContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}