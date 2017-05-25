package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.EgfConfiguration;
import org.egov.egf.persistence.queue.contract.EgfConfigurationContract;
import org.egov.egf.persistence.queue.contract.EgfConfigurationContractRequest;
import org.egov.egf.persistence.queue.contract.EgfConfigurationContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.EgfConfigurationService;
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
@RequestMapping("/egfconfiguration")
public class EgfConfigurationController {

	@Autowired
	private EgfConfigurationService egfConfigurationService;

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public EgfConfigurationContractResponse view(
			@ModelAttribute EgfConfigurationContractRequest egfConfigurationContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		egfConfigurationService.validate(egfConfigurationContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		egfConfigurationService.fetchRelatedContracts(egfConfigurationContractRequest);
		EgfConfiguration egfConfigurationFromDb = egfConfigurationService.findOne(uniqueId);
		EgfConfigurationContract egfConfiguration = egfConfigurationContractRequest
				.getEgfConfiguration();

		ModelMapper model = new ModelMapper();
		model.map(egfConfigurationFromDb, egfConfiguration);

		EgfConfigurationContractResponse egfConfigurationContractResponse = new EgfConfigurationContractResponse();
		egfConfigurationContractResponse.setEgfConfiguration(egfConfiguration);
		egfConfigurationContractResponse
				.setResponseInfo(getResponseInfo(egfConfigurationContractRequest.getRequestInfo()));
		egfConfigurationContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return egfConfigurationContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public EgfConfigurationContractResponse search(
			@ModelAttribute EgfConfigurationContract egfConfigurationContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		EgfConfigurationContractRequest egfConfigurationContractRequest = new EgfConfigurationContractRequest();
		egfConfigurationContractRequest.setEgfConfiguration(egfConfigurationContracts);
		egfConfigurationContractRequest.setRequestInfo(requestInfo);
		egfConfigurationService.validate(egfConfigurationContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		egfConfigurationService.fetchRelatedContracts(egfConfigurationContractRequest);
		EgfConfigurationContractResponse egfConfigurationContractResponse = new EgfConfigurationContractResponse();
		egfConfigurationContractResponse
				.setEgfConfigurations(new ArrayList<EgfConfigurationContract>());
		egfConfigurationContractResponse.setPage(new Pagination());
		Page<EgfConfiguration> allEgfConfigurations;
		ModelMapper model = new ModelMapper();

		allEgfConfigurations = egfConfigurationService.search(egfConfigurationContractRequest);
		EgfConfigurationContract egfConfigurationContract = null;
		for (EgfConfiguration b : allEgfConfigurations) {
			egfConfigurationContract = new EgfConfigurationContract();
			model.map(b, egfConfigurationContract);
			egfConfigurationContractResponse.getEgfConfigurations().add(egfConfigurationContract);
		}
		egfConfigurationContractResponse.getPage().map(allEgfConfigurations);
		egfConfigurationContractResponse
				.setResponseInfo(getResponseInfo(egfConfigurationContractRequest.getRequestInfo()));
		egfConfigurationContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return egfConfigurationContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}