package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.FinancialConfiguration;
import org.egov.egf.persistence.queue.contract.FinancialConfigurationContract;
import org.egov.egf.persistence.queue.contract.FinancialConfigurationContractRequest;
import org.egov.egf.persistence.queue.contract.FinancialConfigurationContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.FinancialConfigurationService;
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
@RequestMapping("/financialconfiguration")
public class FinancialConfigurationController {

	@Autowired
	private FinancialConfigurationService financialConfigurationService;

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FinancialConfigurationContractResponse view(
			@ModelAttribute FinancialConfigurationContractRequest financialConfigurationContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		financialConfigurationService.validate(financialConfigurationContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		financialConfigurationService.fetchRelatedContracts(financialConfigurationContractRequest);
		FinancialConfiguration financialConfigurationFromDb = financialConfigurationService.findOne(uniqueId);
		FinancialConfigurationContract financialConfiguration = financialConfigurationContractRequest
				.getFinancialConfiguration();

		ModelMapper model = new ModelMapper();
		model.map(financialConfigurationFromDb, financialConfiguration);

		FinancialConfigurationContractResponse financialConfigurationContractResponse = new FinancialConfigurationContractResponse();
		financialConfigurationContractResponse.setFinancialConfiguration(financialConfiguration);
		financialConfigurationContractResponse
				.setResponseInfo(getResponseInfo(financialConfigurationContractRequest.getRequestInfo()));
		financialConfigurationContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return financialConfigurationContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FinancialConfigurationContractResponse search(
			@ModelAttribute FinancialConfigurationContract financialConfigurationContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		FinancialConfigurationContractRequest financialConfigurationContractRequest = new FinancialConfigurationContractRequest();
		financialConfigurationContractRequest.setFinancialConfiguration(financialConfigurationContracts);
		financialConfigurationContractRequest.setRequestInfo(requestInfo);
		financialConfigurationService.validate(financialConfigurationContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		financialConfigurationService.fetchRelatedContracts(financialConfigurationContractRequest);
		FinancialConfigurationContractResponse financialConfigurationContractResponse = new FinancialConfigurationContractResponse();
		financialConfigurationContractResponse
				.setFinancialConfigurations(new ArrayList<FinancialConfigurationContract>());
		financialConfigurationContractResponse.setPage(new Pagination());
		Page<FinancialConfiguration> allFinancialConfigurations;
		ModelMapper model = new ModelMapper();

		allFinancialConfigurations = financialConfigurationService.search(financialConfigurationContractRequest);
		FinancialConfigurationContract financialConfigurationContract = null;
		for (FinancialConfiguration b : allFinancialConfigurations) {
			financialConfigurationContract = new FinancialConfigurationContract();
			model.map(b, financialConfigurationContract);
			financialConfigurationContractResponse.getFinancialConfigurations().add(financialConfigurationContract);
		}
		financialConfigurationContractResponse.getPage().map(allFinancialConfigurations);
		financialConfigurationContractResponse
				.setResponseInfo(getResponseInfo(financialConfigurationContractRequest.getRequestInfo()));
		financialConfigurationContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return financialConfigurationContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}