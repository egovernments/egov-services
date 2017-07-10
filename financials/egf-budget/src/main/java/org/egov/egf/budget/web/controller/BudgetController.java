package org.egov.egf.budget.web.controller;

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
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.service.BudgetService;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetSearchContract;
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
	public CommonResponse<BudgetContract> create(
			@RequestBody @Valid CommonRequest<BudgetContract> budgetContractRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		CommonResponse<BudgetContract> budgetResponse = new CommonResponse<>();
		List<Budget> budgets = new ArrayList<>();
		Budget budget = null;
		List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();
		BudgetContract contract = null;

		budgetContractRequest.getRequestInfo().setAction("create");

		for (BudgetContract budgetContract : budgetContractRequest.getData()) {
			budget = budgetContract.toDomain();
			budget.setCreatedBy(budgetContractRequest.getRequestInfo().getUserInfo());
			budget.setLastModifiedBy(budgetContractRequest.getRequestInfo().getUserInfo());
			budgets.add(budget);
		}

		budgets = budgetService.add(budgets, errors);

		for (Budget b : budgets) {
			contract = new BudgetContract();
			contract.toContract(b);
			budgetContracts.add(contract);
		}

		budgetContractRequest.setData(budgetContracts);
		budgetService.addToQue(budgetContractRequest);
		budgetResponse.setData(budgetContracts);

		return budgetResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BudgetContract> update(
			@RequestBody @Valid CommonRequest<BudgetContract> budgetContractRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		budgetContractRequest.getRequestInfo().setAction("update");

		CommonResponse<BudgetContract> budgetResponse = new CommonResponse<>();
		List<Budget> budgets = new ArrayList<>();
		Budget budget = null;
		BudgetContract contract = null;
		List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();

		for (BudgetContract budgetContract : budgetContractRequest.getData()) {
			budget = budgetContract.toDomain();
			budget.setLastModifiedBy(budgetContractRequest.getRequestInfo().getUserInfo());
			budgets.add(budget);
		}

		budgets = budgetService.update(budgets, errors);

		for (Budget budgetObj : budgets) {
			contract = new BudgetContract();
			contract.toContract(budgetObj);
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

		BudgetSearch domain = budgetSearchContract.toDomain();
		BudgetContract contract = null;
		List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();

		Pagination<Budget> budgets = budgetService.search(domain);

		for (Budget budget : budgets.getPagedData()) {
			contract = new BudgetContract();
			contract.toContract(budget);
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