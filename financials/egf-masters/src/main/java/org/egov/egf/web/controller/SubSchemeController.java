package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.SubScheme;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.queue.contract.SubSchemeContract;
import org.egov.egf.persistence.queue.contract.SubSchemeContractRequest;
import org.egov.egf.persistence.queue.contract.SubSchemeContractResponse;
import org.egov.egf.persistence.service.SubSchemeService;
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
@RequestMapping("/subschemes")
public class SubSchemeController {

	@Autowired
	private SubSchemeService subSchemeService;

	@PostMapping("_create")
	@ResponseStatus(HttpStatus.CREATED)
	public SubSchemeContractResponse create(@RequestBody @Valid final SubSchemeContractRequest subSchemeContractRequest,
			final BindingResult errors) {
		subSchemeService.validate(subSchemeContractRequest, "create", errors);
		if (errors.hasErrors())
			throw new CustomBindException(errors);
		subSchemeContractRequest.getRequestInfo().setAction("create");
		subSchemeService.fetchRelatedContracts(subSchemeContractRequest);
		subSchemeService.push(subSchemeContractRequest);
		final SubSchemeContractResponse subSchemeContractResponse = new SubSchemeContractResponse();
		subSchemeContractResponse.setSubSchemes(new ArrayList<SubSchemeContract>());
		if (subSchemeContractRequest.getSubSchemes() != null && !subSchemeContractRequest.getSubSchemes().isEmpty()) {
			for (final SubSchemeContract subSchemeContract : subSchemeContractRequest.getSubSchemes()) {
				subSchemeContractResponse.getSubSchemes().add(subSchemeContract);
			}
		} else if (subSchemeContractRequest.getSubScheme() != null) {
			subSchemeContractResponse.setSubScheme(subSchemeContractRequest.getSubScheme());
		}
		subSchemeContractResponse.setResponseInfo(getResponseInfo(subSchemeContractRequest.getRequestInfo()));

		return subSchemeContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public SubSchemeContractResponse update(@RequestBody @Valid final SubSchemeContractRequest subSchemeContractRequest,
			final BindingResult errors, @PathVariable final Long uniqueId) {

		subSchemeService.validate(subSchemeContractRequest, "update", errors);

		if (errors.hasErrors())
			throw new CustomBindException(errors);
		subSchemeContractRequest.getRequestInfo().setAction("update");
		subSchemeService.fetchRelatedContracts(subSchemeContractRequest);
		subSchemeContractRequest.getSubScheme().setId(uniqueId);
		subSchemeService.push(subSchemeContractRequest);
		final SubSchemeContractResponse subSchemeContractResponse = new SubSchemeContractResponse();
		subSchemeContractResponse.setSubScheme(subSchemeContractRequest.getSubScheme());
		subSchemeContractResponse.setResponseInfo(getResponseInfo(subSchemeContractRequest.getRequestInfo()));
		subSchemeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return subSchemeContractResponse;
	}

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public SubSchemeContractResponse view(@ModelAttribute final SubSchemeContractRequest subSchemeContractRequest,
			final BindingResult errors, @PathVariable final Long uniqueId) {
		subSchemeService.validate(subSchemeContractRequest, "view", errors);
		if (errors.hasErrors())
			throw new CustomBindException(errors);
		subSchemeService.fetchRelatedContracts(subSchemeContractRequest);
		subSchemeContractRequest.getRequestInfo();
		final SubScheme subSchemeFromDb = subSchemeService.findOne(uniqueId);
		final SubSchemeContract subScheme = subSchemeContractRequest.getSubScheme();

		final ModelMapper model = new ModelMapper();
		model.map(subSchemeFromDb, subScheme);

		final SubSchemeContractResponse subSchemeContractResponse = new SubSchemeContractResponse();
		subSchemeContractResponse.setSubScheme(subScheme);
		subSchemeContractResponse.setResponseInfo(getResponseInfo(subSchemeContractRequest.getRequestInfo()));
		subSchemeContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return subSchemeContractResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public SubSchemeContractResponse updateAll(
			@RequestBody @Valid final SubSchemeContractRequest subSchemeContractRequest, final BindingResult errors) {
		subSchemeService.validate(subSchemeContractRequest, "updateAll", errors);
		if (errors.hasErrors())
			throw new CustomBindException(errors);
		subSchemeContractRequest.getRequestInfo().setAction("updateAll");
		subSchemeService.fetchRelatedContracts(subSchemeContractRequest);
		subSchemeService.push(subSchemeContractRequest);
		final SubSchemeContractResponse subSchemeContractResponse = new SubSchemeContractResponse();
		subSchemeContractResponse.setSubSchemes(new ArrayList<SubSchemeContract>());
		for (final SubSchemeContract subSchemeContract : subSchemeContractRequest.getSubSchemes()) {
			subSchemeContractResponse.getSubSchemes().add(subSchemeContract);
		}

		subSchemeContractResponse.setResponseInfo(getResponseInfo(subSchemeContractRequest.getRequestInfo()));
		subSchemeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return subSchemeContractResponse;
	}

	@PostMapping("_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public SubSchemeContractResponse search(@ModelAttribute final SubSchemeContract subSchemeContracts,
			@RequestBody final RequestInfo requestInfo, final BindingResult errors) {
		final SubSchemeContractRequest subSchemeContractRequest = new SubSchemeContractRequest();
		subSchemeContractRequest.setSubScheme(subSchemeContracts);
		subSchemeContractRequest.setRequestInfo(requestInfo);
		subSchemeService.validate(subSchemeContractRequest, "search", errors);
		if (errors.hasErrors())
			throw new CustomBindException(errors);
		subSchemeService.fetchRelatedContracts(subSchemeContractRequest);
		final SubSchemeContractResponse subSchemeContractResponse = new SubSchemeContractResponse();
		subSchemeContractResponse.setSubSchemes(new ArrayList<SubSchemeContract>());
		subSchemeContractResponse.setPage(new Pagination());
		Page<SubScheme> allSubSchemes;
		final ModelMapper model = new ModelMapper();

		allSubSchemes = subSchemeService.search(subSchemeContractRequest);
		SubSchemeContract subSchemeContract = null;
		for (final SubScheme b : allSubSchemes) {
			subSchemeContract = new SubSchemeContract();
			model.map(b, subSchemeContract);
			subSchemeContractResponse.getSubSchemes().add(subSchemeContract);
		}
		subSchemeContractResponse.getPage().map(allSubSchemes);
		subSchemeContractResponse.setResponseInfo(getResponseInfo(subSchemeContractRequest.getRequestInfo()));
		subSchemeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return subSchemeContractResponse;
	}

	private ResponseInfo getResponseInfo(final RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}