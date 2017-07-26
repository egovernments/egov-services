package org.egov.egf.budget.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.PaginationContract;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.service.BudgetService;
import org.egov.egf.budget.persistence.queue.repository.BudgetQueueRepository;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetRequest;
import org.egov.egf.budget.web.contract.BudgetResponse;
import org.egov.egf.budget.web.contract.BudgetSearchContract;
import org.egov.egf.budget.web.mapper.BudgetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String PLACEHOLDER = "placeholder";

	@Autowired
	private BudgetService budgetService;

	@Autowired
	private BudgetQueueRepository budgetQueueRepository;

	@Value("${persist.through.kafka}")
	private static String persistThroughKafka;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public BudgetResponse create(@RequestBody BudgetRequest budgetRequest, BindingResult errors) {

		BudgetMapper mapper = new BudgetMapper();
		BudgetResponse budgetResponse = new BudgetResponse();
		budgetResponse.setResponseInfo(getResponseInfo(budgetRequest.getRequestInfo()));
		List<Budget> budgets = new ArrayList<>();
		Budget budget = null;
		List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();
		BudgetContract contract = null;

		budgetRequest.getRequestInfo().setAction(ACTION_CREATE);

		for (BudgetContract budgetContract : budgetRequest.getBudgets()) {
			budget = mapper.toDomain(budgetContract);
			budget.setCreatedBy(budgetRequest.getRequestInfo().getUserInfo());
			budget.setLastModifiedBy(budgetRequest.getRequestInfo().getUserInfo());
			budgets.add(budget);
		}

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			budgets = budgetService.fetchAndValidate(budgets, errors, budgetRequest.getRequestInfo().getAction());

			for (Budget b : budgets) {
				contract = mapper.toContract(b);
				budgetContracts.add(contract);
			}

			budgetRequest.setBudgets(budgetContracts);

			budgetQueueRepository.addToQue(budgetRequest);

		} else {

			budgets = budgetService.save(budgets, errors);

			for (Budget b : budgets) {
				contract = mapper.toContract(b);
				budgetContracts.add(contract);
			}

			budgetRequest.setBudgets(budgetContracts);

			budgetQueueRepository.addToSearchQue(budgetRequest);

		}

		budgetResponse.setBudgets(budgetContracts);

		return budgetResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public BudgetResponse update(@RequestBody @Valid BudgetRequest budgetRequest, BindingResult errors) {

		BudgetMapper mapper = new BudgetMapper();
		BudgetResponse budgetResponse = new BudgetResponse();
		budgetResponse.setResponseInfo(getResponseInfo(budgetRequest.getRequestInfo()));
		List<Budget> budgets = new ArrayList<>();
		Budget budget = null;
		BudgetContract contract = null;
		List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();

		budgetRequest.getRequestInfo().setAction(ACTION_UPDATE);

		for (BudgetContract budgetContract : budgetRequest.getBudgets()) {
			budget = mapper.toDomain(budgetContract);
			budget.setLastModifiedBy(budgetRequest.getRequestInfo().getUserInfo());
			budgets.add(budget);
		}

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			budgets = budgetService.fetchAndValidate(budgets, errors, budgetRequest.getRequestInfo().getAction());

			for (Budget b : budgets) {
				contract = mapper.toContract(b);
				budgetContracts.add(contract);
			}

			budgetRequest.setBudgets(budgetContracts);

			budgetQueueRepository.addToQue(budgetRequest);

		} else {

			budgets = budgetService.update(budgets, errors);

			for (Budget b : budgets) {
				contract = mapper.toContract(b);
				budgetContracts.add(contract);
			}

			budgetRequest.setBudgets(budgetContracts);

			budgetQueueRepository.addToSearchQue(budgetRequest);

		}

		budgetResponse.setBudgets(budgetContracts);

		return budgetResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public BudgetResponse search(@ModelAttribute BudgetSearchContract budgetSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		BudgetMapper mapper = new BudgetMapper();
		BudgetSearch domain = mapper.toSearchDomain(budgetSearchContract);
		BudgetContract contract = null;
		List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();
		Pagination<Budget> budgets = budgetService.search(domain);

		for (Budget budget : budgets.getPagedData()) {
			contract = mapper.toContract(budget);
			budgetContracts.add(contract);
		}

		BudgetResponse response = new BudgetResponse();
		response.setBudgets(budgetContracts);
		response.setPage(new PaginationContract(budgets));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.ts(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())).resMsgId(requestInfo.getMsgId())
				.resMsgId(PLACEHOLDER).status(PLACEHOLDER).build();
	}

}