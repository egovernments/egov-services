package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.FinancialStatus;
import org.egov.egf.persistence.queue.contract.FinancialStatusContract;
import org.egov.egf.persistence.queue.contract.FinancialStatusContractRequest;
import org.egov.egf.persistence.queue.contract.FinancialStatusContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.FinancialStatusService;
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
@RequestMapping("/financialstatus")
public class FinancialStatusController {
	
	@Autowired
	private FinancialStatusService financialStatusService;

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FinancialStatusContractResponse view(@ModelAttribute FinancialStatusContractRequest financialStatusContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		financialStatusService.validate(financialStatusContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		financialStatusService.fetchRelatedContracts(financialStatusContractRequest);
		FinancialStatus financialStatusFromDb = financialStatusService.findOne(uniqueId);
		FinancialStatusContract financialStatus = financialStatusContractRequest.getFinancialStatus();

		ModelMapper model = new ModelMapper();
		model.map(financialStatusFromDb, financialStatus);

		FinancialStatusContractResponse financialStatusContractResponse = new FinancialStatusContractResponse();
		financialStatusContractResponse.setFinancialStatus(financialStatus);
		financialStatusContractResponse.setResponseInfo(getResponseInfo(financialStatusContractRequest.getRequestInfo()));
		financialStatusContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return financialStatusContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FinancialStatusContractResponse search(@ModelAttribute FinancialStatusContract financialStatusContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		FinancialStatusContractRequest financialStatusContractRequest = new FinancialStatusContractRequest();
		financialStatusContractRequest.setFinancialStatus(financialStatusContracts);
		financialStatusContractRequest.setRequestInfo(requestInfo);
		financialStatusService.validate(financialStatusContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		financialStatusService.fetchRelatedContracts(financialStatusContractRequest);
		FinancialStatusContractResponse financialStatusContractResponse = new FinancialStatusContractResponse();
		financialStatusContractResponse.setFinancialStatuses(new ArrayList<FinancialStatusContract>());
		financialStatusContractResponse.setPage(new Pagination());
		Page<FinancialStatus> allFinancialStatuses;
		ModelMapper model = new ModelMapper();

		allFinancialStatuses = financialStatusService.search(financialStatusContractRequest);
		FinancialStatusContract financialStatusContract = null;
		for (FinancialStatus b : allFinancialStatuses) {
			financialStatusContract = new FinancialStatusContract();
			model.map(b, financialStatusContract);
			financialStatusContractResponse.getFinancialStatuses().add(financialStatusContract);
		}
		financialStatusContractResponse.getPage().map(allFinancialStatuses);
		financialStatusContractResponse.setResponseInfo(getResponseInfo(financialStatusContractRequest.getRequestInfo()));
		financialStatusContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return financialStatusContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}