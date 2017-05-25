package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.EgfConfigurationValues;
import org.egov.egf.persistence.queue.contract.EgfConfigurationValuesContract;
import org.egov.egf.persistence.queue.contract.EgfConfigurationValuesContractRequest;
import org.egov.egf.persistence.queue.contract.EgfConfigurationValuesContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.EgfConfigurationValuesService;
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
@RequestMapping("/egfconfigurationvalues")
public class EgfConfigurationValuesController {
	
	@Autowired
	private EgfConfigurationValuesService egfConfigurationValuesService;

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public EgfConfigurationValuesContractResponse view(
			@ModelAttribute EgfConfigurationValuesContractRequest egfConfigurationValuesContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		egfConfigurationValuesService.validate(egfConfigurationValuesContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		egfConfigurationValuesService.fetchRelatedContracts(egfConfigurationValuesContractRequest);
		EgfConfigurationValues egfConfigurationValuesFromDb = egfConfigurationValuesService.findOne(uniqueId);
		EgfConfigurationValuesContract egfConfigurationValues = egfConfigurationValuesContractRequest
				.getEgfConfigurationValues();

		ModelMapper model = new ModelMapper();
		model.map(egfConfigurationValuesFromDb, egfConfigurationValues);

		EgfConfigurationValuesContractResponse egfConfigurationValuesContractResponse = new EgfConfigurationValuesContractResponse();
		egfConfigurationValuesContractResponse.setEgfConfigurationValues(egfConfigurationValues);
		egfConfigurationValuesContractResponse
				.setResponseInfo(getResponseInfo(egfConfigurationValuesContractRequest.getRequestInfo()));
		egfConfigurationValuesContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return egfConfigurationValuesContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public EgfConfigurationValuesContractResponse search(
			@ModelAttribute EgfConfigurationValuesContract egfConfigurationValuesContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		EgfConfigurationValuesContractRequest egfConfigurationValuesContractRequest = new EgfConfigurationValuesContractRequest();
		egfConfigurationValuesContractRequest.setEgfConfigurationValues(egfConfigurationValuesContracts);
		egfConfigurationValuesContractRequest.setRequestInfo(requestInfo);
		egfConfigurationValuesService.validate(egfConfigurationValuesContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		egfConfigurationValuesService.fetchRelatedContracts(egfConfigurationValuesContractRequest);
		EgfConfigurationValuesContractResponse egfConfigurationValuesContractResponse = new EgfConfigurationValuesContractResponse();
		egfConfigurationValuesContractResponse
				.setEgfConfigurationValueses(new ArrayList<EgfConfigurationValuesContract>());
		egfConfigurationValuesContractResponse.setPage(new Pagination());
		Page<EgfConfigurationValues> allEgfConfigurationValueses;
		ModelMapper model = new ModelMapper();

		allEgfConfigurationValueses = egfConfigurationValuesService.search(egfConfigurationValuesContractRequest);
		EgfConfigurationValuesContract egfConfigurationValuesContract = null;
		for (EgfConfigurationValues b : allEgfConfigurationValueses) {
			egfConfigurationValuesContract = new EgfConfigurationValuesContract();
			model.map(b, egfConfigurationValuesContract);
			egfConfigurationValuesContractResponse.getEgfConfigurationValueses()
					.add(egfConfigurationValuesContract);
		}
		egfConfigurationValuesContractResponse.getPage().map(allEgfConfigurationValueses);
		egfConfigurationValuesContractResponse
				.setResponseInfo(getResponseInfo(egfConfigurationValuesContractRequest.getRequestInfo()));
		egfConfigurationValuesContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return egfConfigurationValuesContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}