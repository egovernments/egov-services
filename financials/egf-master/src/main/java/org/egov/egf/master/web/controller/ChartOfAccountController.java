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
import org.egov.egf.master.domain.model.ChartOfAccount;
import org.egov.egf.master.domain.model.ChartOfAccountSearch;
import org.egov.egf.master.domain.service.ChartOfAccountService;
import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.egov.egf.master.web.contract.ChartOfAccountSearchContract;
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
@RequestMapping("/chartofaccounts")
public class ChartOfAccountController {

	@Autowired
	private ChartOfAccountService chartOfAccountService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<ChartOfAccountContract> create(
			@RequestBody @Valid CommonRequest<ChartOfAccountContract> chartOfAccountContractRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<ChartOfAccountContract> chartOfAccountResponse = new CommonResponse<>();
		List<ChartOfAccount> chartofaccounts = new ArrayList<>();
		ChartOfAccount chartOfAccount = null;
		List<ChartOfAccountContract> chartOfAccountContracts = new ArrayList<ChartOfAccountContract>();
		ChartOfAccountContract contract = null;

		chartOfAccountContractRequest.getRequestInfo().setAction("create");

		for (ChartOfAccountContract chartOfAccountContract : chartOfAccountContractRequest.getData()) {
			chartOfAccount = new ChartOfAccount();
			model.map(chartOfAccountContract, chartOfAccount);
			chartOfAccount.setCreatedBy(chartOfAccountContractRequest.getRequestInfo().getUserInfo());
			chartOfAccount.setLastModifiedBy(chartOfAccountContractRequest.getRequestInfo().getUserInfo());
			chartofaccounts.add(chartOfAccount);
		}

		chartofaccounts = chartOfAccountService.add(chartofaccounts, errors);

		for (ChartOfAccount f : chartofaccounts) {
			contract = new ChartOfAccountContract();
			model.map(f, contract);
			chartOfAccountContracts.add(contract);
		}

		chartOfAccountContractRequest.setData(chartOfAccountContracts);
		chartOfAccountService.addToQue(chartOfAccountContractRequest);
		chartOfAccountResponse.setData(chartOfAccountContracts);

		return chartOfAccountResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<ChartOfAccountContract> update(
			@RequestBody @Valid CommonRequest<ChartOfAccountContract> chartOfAccountContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		chartOfAccountContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<ChartOfAccountContract> chartOfAccountResponse = new CommonResponse<>();
		List<ChartOfAccount> chartofaccounts = new ArrayList<>();
		ChartOfAccount chartOfAccount;
		ChartOfAccountContract contract;
		List<ChartOfAccountContract> chartOfAccountContracts = new ArrayList<>();

		for (ChartOfAccountContract chartOfAccountContract : chartOfAccountContractRequest.getData()) {
			chartOfAccount = new ChartOfAccount();
			model.map(chartOfAccountContract, chartOfAccount);
			chartOfAccount.setLastModifiedBy(chartOfAccountContractRequest.getRequestInfo().getUserInfo());
			chartofaccounts.add(chartOfAccount);
		}

		chartofaccounts = chartOfAccountService.update(chartofaccounts, errors);

		for (ChartOfAccount chartOfAccountObj : chartofaccounts) {
			contract = new ChartOfAccountContract();
			model.map(chartOfAccountObj, contract);
			chartOfAccountContracts.add(contract);
		}

		chartOfAccountContractRequest.setData(chartOfAccountContracts);
		chartOfAccountService.addToQue(chartOfAccountContractRequest);
		chartOfAccountResponse.setData(chartOfAccountContracts);

		return chartOfAccountResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<ChartOfAccountContract> search(
			@ModelAttribute ChartOfAccountSearchContract chartOfAccountSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		ChartOfAccountSearch domain = new ChartOfAccountSearch();
		mapper.map(chartOfAccountSearchContract, domain);
		ChartOfAccountContract contract = null;
		ModelMapper model = new ModelMapper();
		List<ChartOfAccountContract> chartOfAccountContracts = new ArrayList<ChartOfAccountContract>();

		Pagination<ChartOfAccount> chartofaccounts = chartOfAccountService.search(domain);

		for (ChartOfAccount chartOfAccount : chartofaccounts.getPagedData()) {
			contract = new ChartOfAccountContract();
			model.map(chartOfAccount, contract);
			chartOfAccountContracts.add(contract);
		}

		CommonResponse<ChartOfAccountContract> response = new CommonResponse<>();
		response.setData(chartOfAccountContracts);
		response.setPage(new PaginationContract(chartofaccounts));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}