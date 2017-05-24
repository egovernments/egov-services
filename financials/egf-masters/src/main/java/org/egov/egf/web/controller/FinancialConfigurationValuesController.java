package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.FinancialConfigurationValues;
import org.egov.egf.persistence.queue.contract.FinancialConfigurationValuesContract;
import org.egov.egf.persistence.queue.contract.FinancialConfigurationValuesContractRequest;
import org.egov.egf.persistence.queue.contract.FinancialConfigurationValuesContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.FinancialConfigurationValuesService;
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
@RequestMapping("/financialconfigurationvalues")
public class FinancialConfigurationValuesController {
	
	@Autowired
	private FinancialConfigurationValuesService financialConfigurationValuesService;

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FinancialConfigurationValuesContractResponse view(
			@ModelAttribute FinancialConfigurationValuesContractRequest financialConfigurationValuesContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		financialConfigurationValuesService.validate(financialConfigurationValuesContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		financialConfigurationValuesService.fetchRelatedContracts(financialConfigurationValuesContractRequest);
		FinancialConfigurationValues financialConfigurationValuesFromDb = financialConfigurationValuesService.findOne(uniqueId);
		FinancialConfigurationValuesContract financialConfigurationValues = financialConfigurationValuesContractRequest
				.getFinancialConfigurationValues();

		ModelMapper model = new ModelMapper();
		model.map(financialConfigurationValuesFromDb, financialConfigurationValues);

		FinancialConfigurationValuesContractResponse financialConfigurationValuesContractResponse = new FinancialConfigurationValuesContractResponse();
		financialConfigurationValuesContractResponse.setFinancialConfigurationValues(financialConfigurationValues);
		financialConfigurationValuesContractResponse
				.setResponseInfo(getResponseInfo(financialConfigurationValuesContractRequest.getRequestInfo()));
		financialConfigurationValuesContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return financialConfigurationValuesContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FinancialConfigurationValuesContractResponse search(
			@ModelAttribute FinancialConfigurationValuesContract financialConfigurationValuesContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		FinancialConfigurationValuesContractRequest financialConfigurationValuesContractRequest = new FinancialConfigurationValuesContractRequest();
		financialConfigurationValuesContractRequest.setFinancialConfigurationValues(financialConfigurationValuesContracts);
		financialConfigurationValuesContractRequest.setRequestInfo(requestInfo);
		financialConfigurationValuesService.validate(financialConfigurationValuesContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		financialConfigurationValuesService.fetchRelatedContracts(financialConfigurationValuesContractRequest);
		FinancialConfigurationValuesContractResponse financialConfigurationValuesContractResponse = new FinancialConfigurationValuesContractResponse();
		financialConfigurationValuesContractResponse
				.setFinancialConfigurationValueses(new ArrayList<FinancialConfigurationValuesContract>());
		financialConfigurationValuesContractResponse.setPage(new Pagination());
		Page<FinancialConfigurationValues> allFinancialConfigurationValueses;
		ModelMapper model = new ModelMapper();

		allFinancialConfigurationValueses = financialConfigurationValuesService.search(financialConfigurationValuesContractRequest);
		FinancialConfigurationValuesContract financialConfigurationValuesContract = null;
		for (FinancialConfigurationValues b : allFinancialConfigurationValueses) {
			financialConfigurationValuesContract = new FinancialConfigurationValuesContract();
			model.map(b, financialConfigurationValuesContract);
			financialConfigurationValuesContractResponse.getFinancialConfigurationValueses()
					.add(financialConfigurationValuesContract);
		}
		financialConfigurationValuesContractResponse.getPage().map(allFinancialConfigurationValueses);
		financialConfigurationValuesContractResponse
				.setResponseInfo(getResponseInfo(financialConfigurationValuesContractRequest.getRequestInfo()));
		financialConfigurationValuesContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return financialConfigurationValuesContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}