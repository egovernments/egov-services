package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.FinancialYear;
import org.egov.egf.persistence.queue.contract.FinancialYearContract;
import org.egov.egf.persistence.queue.contract.FinancialYearContractRequest;
import org.egov.egf.persistence.queue.contract.FinancialYearContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.FinancialYearService;
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
@RequestMapping("/financialyears")
public class FinancialYearController {
	@Autowired
	private FinancialYearService financialYearService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public FinancialYearContractResponse create(
			@RequestBody @Valid FinancialYearContractRequest financialYearContractRequest, BindingResult errors) {
		financialYearService.validate(financialYearContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		financialYearContractRequest.getRequestInfo().setAction("create");
		financialYearService.fetchRelatedContracts(financialYearContractRequest);
		financialYearService.push(financialYearContractRequest);
		FinancialYearContractResponse financialYearContractResponse = new FinancialYearContractResponse();
		financialYearContractResponse.setFinancialYears(new ArrayList<FinancialYearContract>());
		if (financialYearContractRequest.getFinancialYears() != null
				&& !financialYearContractRequest.getFinancialYears().isEmpty()) {
			for (FinancialYearContract financialYearContract : financialYearContractRequest.getFinancialYears()) {
				financialYearContractResponse.getFinancialYears().add(financialYearContract);
			}
		} else if (financialYearContractRequest.getFinancialYear() != null) {
			financialYearContractResponse.setFinancialYear(financialYearContractRequest.getFinancialYear());
		}
		financialYearContractResponse.setResponseInfo(getResponseInfo(financialYearContractRequest.getRequestInfo()));
		financialYearContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return financialYearContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public FinancialYearContractResponse update(
			@RequestBody @Valid FinancialYearContractRequest financialYearContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {

		financialYearService.validate(financialYearContractRequest, "update", errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		financialYearContractRequest.getRequestInfo().setAction("update");
		financialYearService.fetchRelatedContracts(financialYearContractRequest);
		financialYearContractRequest.getFinancialYear().setId(uniqueId);
		financialYearService.push(financialYearContractRequest);
		FinancialYearContractResponse financialYearContractResponse = new FinancialYearContractResponse();
		financialYearContractResponse.setFinancialYear(financialYearContractRequest.getFinancialYear());
		financialYearContractResponse.setResponseInfo(getResponseInfo(financialYearContractRequest.getRequestInfo()));
		financialYearContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return financialYearContractResponse;
	}

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public FinancialYearContractResponse view(@ModelAttribute FinancialYearContractRequest financialYearContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		financialYearService.validate(financialYearContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		financialYearService.fetchRelatedContracts(financialYearContractRequest);
		FinancialYear financialYearFromDb = financialYearService.findOne(uniqueId);
		FinancialYearContract financialYear = financialYearContractRequest.getFinancialYear();

		ModelMapper model = new ModelMapper();
		model.map(financialYearFromDb, financialYear);

		FinancialYearContractResponse financialYearContractResponse = new FinancialYearContractResponse();
		financialYearContractResponse.setFinancialYear(financialYear);
		financialYearContractResponse.setResponseInfo(getResponseInfo(financialYearContractRequest.getRequestInfo()));
		financialYearContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return financialYearContractResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public FinancialYearContractResponse updateAll(
			@RequestBody @Valid FinancialYearContractRequest financialYearContractRequest, BindingResult errors) {
		financialYearService.validate(financialYearContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		financialYearContractRequest.getRequestInfo().setAction("updateAll");
		financialYearService.fetchRelatedContracts(financialYearContractRequest);
		financialYearService.push(financialYearContractRequest);
		FinancialYearContractResponse financialYearContractResponse = new FinancialYearContractResponse();
		financialYearContractResponse.setFinancialYears(new ArrayList<FinancialYearContract>());
		for (FinancialYearContract financialYearContract : financialYearContractRequest.getFinancialYears()) {
			financialYearContractResponse.getFinancialYears().add(financialYearContract);
		}

		financialYearContractResponse.setResponseInfo(getResponseInfo(financialYearContractRequest.getRequestInfo()));
		financialYearContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return financialYearContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FinancialYearContractResponse search(@ModelAttribute FinancialYearContract financialYearContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		FinancialYearContractRequest financialYearContractRequest = new FinancialYearContractRequest();
		financialYearContractRequest.setFinancialYear(financialYearContracts);
		financialYearContractRequest.setRequestInfo(requestInfo);
		financialYearService.validate(financialYearContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		financialYearService.fetchRelatedContracts(financialYearContractRequest);
		FinancialYearContractResponse financialYearContractResponse = new FinancialYearContractResponse();
		financialYearContractResponse.setFinancialYears(new ArrayList<FinancialYearContract>());
		financialYearContractResponse.setPage(new Pagination());
		Page<FinancialYear> allFinancialYears;
		ModelMapper model = new ModelMapper();

		allFinancialYears = financialYearService.search(financialYearContractRequest);
		FinancialYearContract financialYearContract = null;
		for (FinancialYear b : allFinancialYears) {
			financialYearContract = new FinancialYearContract();
			model.map(b, financialYearContract);
			financialYearContractResponse.getFinancialYears().add(financialYearContract);
		}
		financialYearContractResponse.getPage().map(allFinancialYears);
		financialYearContractResponse.setResponseInfo(getResponseInfo(financialYearContractRequest.getRequestInfo()));
		financialYearContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return financialYearContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}