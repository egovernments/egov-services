package org.egov.egf.voucher.web.controller;

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
import org.egov.egf.voucher.domain.model.Budget;
import org.egov.egf.voucher.domain.model.BudgetSearch;
import org.egov.egf.voucher.domain.service.BudgetService;
import org.egov.egf.voucher.web.contract.BudgetContract;
import org.egov.egf.voucher.web.contract.BudgetSearchContract;
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
@RequestMapping("/budgets")
public class BudgetController {

	@Autowired
	private BudgetService budgetService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BudgetContract> create(@RequestBody CommonRequest<BudgetContract> budgetRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<BudgetContract> budgetResponse = new CommonResponse<>();
		budgetResponse.setResponseInfo(getResponseInfo(budgetRequest.getRequestInfo()));
		List<Budget> budgets = new ArrayList<>();
		Budget budget = null;
		List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();
		BudgetContract contract = null;

		budgetRequest.getRequestInfo().setAction("create");

		for (BudgetContract budgetContract : budgetRequest.getData()) {
			budget = new Budget();
			model.map(budgetContract, budget);
			budget.setCreatedBy(budgetRequest.getRequestInfo().getUserInfo());
			budget.setLastModifiedBy(budgetRequest.getRequestInfo().getUserInfo());
			budgets.add(budget);
		}

		budgets = budgetService.add(budgets, errors);

		for (Budget f : budgets) {
			contract = new BudgetContract();
			model.map(f, contract);
			budgetContracts.add(contract);
		}

		budgetRequest.setData(budgetContracts);
		budgetService.addToQue(budgetRequest);
		budgetResponse.setData(budgetContracts);

		return budgetResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BudgetContract> update(@RequestBody @Valid CommonRequest<BudgetContract> budgetContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		budgetContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<BudgetContract> budgetResponse = new CommonResponse<>();
		List<Budget> budgets = new ArrayList<>();
		Budget budget = null;
		BudgetContract contract = null;
		List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();

		for (BudgetContract budgetContract : budgetContractRequest.getData()) {
			budget = new Budget();
			model.map(budgetContract, budget);
			budget.setLastModifiedBy(budgetContractRequest.getRequestInfo().getUserInfo());
			budgets.add(budget);
		}

		budgets = budgetService.update(budgets, errors);

		for (Budget budgetObj : budgets) {
			contract = new BudgetContract();
			model.map(budgetObj, contract);
			budgetContracts.add(contract);
		}

		budgetContractRequest.setData(budgetContracts);
		budgetService.addToQue(budgetContractRequest);
		budgetResponse.setData(budgetContracts);

		return budgetResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<BudgetContract> search(@ModelAttribute BudgetSearchContract budgetSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		BudgetSearch domain = new BudgetSearch();
		mapper.map(budgetSearchContract, domain);
		BudgetContract contract = null;
		ModelMapper model = new ModelMapper();
		List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();
		Pagination<Budget> budgets = budgetService.search(domain);

		for (Budget budget : budgets.getPagedData()) {
			contract = new BudgetContract();
			model.map(budget, contract);
			budgetContracts.add(contract);
		}

		CommonResponse<BudgetContract> response = new CommonResponse<>();
		response.setData(budgetContracts);
		response.setPage(new PaginationContract(budgets));
		response.setResponseInfo(getResponseInfo(requestInfo)); 

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}