package org.egov.egf.master.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.common.web.contract.CommonResponse;
import org.egov.common.web.contract.PaginationContract;
import org.egov.common.web.contract.RequestInfo;
import org.egov.common.web.contract.ResponseInfo;
import org.egov.egf.master.domain.model.FinancialYear;
import org.egov.egf.master.domain.model.FinancialYearSearch;
import org.egov.egf.master.domain.service.FinancialYearService;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.egov.egf.master.web.contract.FinancialYearSearchContract;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	public CommonResponse<FinancialYearContract> create(
			@RequestBody @Valid CommonRequest<FinancialYearContract> financialYearContractRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<FinancialYearContract> financialYearResponse = new CommonResponse<>();
		List<FinancialYear> financialyears = new ArrayList<>();
		FinancialYear financialYear;
		List<FinancialYearContract> financialYearContracts = new ArrayList<>();
		FinancialYearContract contract;

		financialYearContractRequest.getRequestInfo().setAction("create");

		for (FinancialYearContract financialYearContract : financialYearContractRequest.getData()) {
			financialYear = new FinancialYear();
			model.map(financialYearContract, financialYear);
			financialYear.setCreatedDate(new Date());
			financialYear.setCreatedBy(financialYearContractRequest.getRequestInfo().getUserInfo());
			financialYear.setLastModifiedBy(financialYearContractRequest.getRequestInfo().getUserInfo());
			financialyears.add(financialYear);
		}

		financialyears = financialYearService.add(financialyears, errors);

		for (FinancialYear f : financialyears) {
			contract = new FinancialYearContract();
			contract.setCreatedDate(new Date());
			model.map(f, contract);
			financialYearContracts.add(contract);
		}

		financialYearContractRequest.setData(financialYearContracts);
		financialYearService.addToQue(financialYearContractRequest);
		financialYearResponse.setData(financialYearContracts);

		return financialYearResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<FinancialYearContract> update(
			@RequestBody @Valid CommonRequest<FinancialYearContract> financialYearContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		financialYearContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<FinancialYearContract> financialYearResponse = new CommonResponse<>();
		List<FinancialYear> financialyears = new ArrayList<>();
		FinancialYear financialYear;
		FinancialYearContract contract;
		List<FinancialYearContract> financialYearContracts = new ArrayList<>();

		for (FinancialYearContract financialYearContract : financialYearContractRequest.getData()) {
			financialYear = new FinancialYear();
			model.map(financialYearContract, financialYear);
			financialYear.setLastModifiedDate(new Date());
			financialYear.setLastModifiedBy(financialYearContractRequest.getRequestInfo().getUserInfo());
			financialyears.add(financialYear);
		}

		financialyears = financialYearService.update(financialyears, errors);

		for (FinancialYear financialYearObj : financialyears) {
			contract = new FinancialYearContract();
			model.map(financialYearObj, contract);
			contract.setLastModifiedDate(new Date());
			financialYearContracts.add(contract);
		}

		financialYearContractRequest.setData(financialYearContracts);
		financialYearService.addToQue(financialYearContractRequest);
		financialYearResponse.setData(financialYearContracts);

		return financialYearResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<FinancialYearContract> search(
			@ModelAttribute FinancialYearSearchContract financialYearSearchContract, RequestInfo requestInfo,
			BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		FinancialYearSearch domain = new FinancialYearSearch();
		mapper.map(financialYearSearchContract, domain);
		FinancialYearContract contract;
		ModelMapper model = new ModelMapper();
		List<FinancialYearContract> financialYearContracts = new ArrayList<>();

		Pagination<FinancialYear> financialyears = financialYearService.search(domain);

		for (FinancialYear financialYear : financialyears.getPagedData()) {
			contract = new FinancialYearContract();
			model.map(financialYear, contract);
			financialYearContracts.add(contract);
		}

		CommonResponse<FinancialYearContract> response = new CommonResponse<>();
		response.setData(financialYearContracts);
		response.setPage(new PaginationContract(financialyears));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}