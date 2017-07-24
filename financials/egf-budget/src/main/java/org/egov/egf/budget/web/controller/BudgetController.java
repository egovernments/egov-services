package org.egov.egf.budget.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.common.web.contract.CommonResponse;
import org.egov.common.web.contract.PaginationContract;
import org.egov.common.web.contract.RequestInfo;
import org.egov.common.web.contract.ResponseInfo;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.service.BudgetService;
import org.egov.egf.budget.persistence.queue.BudgetServiceQueueRepository;
import org.egov.egf.budget.web.contract.BudgetContract;
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
	private BudgetServiceQueueRepository budgetServiceQueueRepository;

	@Value("${persist.through.kafka}")
	private String persistThroughKafka;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BudgetContract> create(@RequestBody CommonRequest<BudgetContract> budgetRequest,
			BindingResult errors) {

		BudgetMapper mapper = new BudgetMapper();
		CommonResponse<BudgetContract> budgetResponse = new CommonResponse<>();
		budgetResponse.setResponseInfo(getResponseInfo(budgetRequest.getRequestInfo()));
		List<Budget> budgets = new ArrayList<>();
		Budget budget = null;
		List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();
		BudgetContract contract = null;

		budgetRequest.getRequestInfo().setAction(ACTION_CREATE);

		for (BudgetContract budgetContract : budgetRequest.getData()) {
			budget = mapper.toDomain(budgetContract);
			budget.setCreatedBy(budgetRequest.getRequestInfo().getUserInfo());
			budget.setLastModifiedBy(budgetRequest.getRequestInfo().getUserInfo());
			budgets.add(budget);
		}

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			budgets = budgetService.save(budgets, errors, budgetRequest.getRequestInfo().getAction());

			for (Budget b : budgets) {
				contract = mapper.toContract(b);
				budgetContracts.add(contract);
			}

			budgetRequest.setData(budgetContracts);

			budgetServiceQueueRepository.addToQue(budgetRequest);

		} else {

			budgets = budgetService.save(budgets, errors);

			for (Budget b : budgets) {
				contract = mapper.toContract(b);
				budgetContracts.add(contract);
			}

			budgetRequest.setData(budgetContracts);

			budgetServiceQueueRepository.addToSearchQue(budgetRequest);

		}

		budgetResponse.setData(budgetContracts);

		return budgetResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BudgetContract> update(@RequestBody @Valid CommonRequest<BudgetContract> budgetRequest,
			BindingResult errors) {

		BudgetMapper mapper = new BudgetMapper();
		CommonResponse<BudgetContract> budgetResponse = new CommonResponse<>();
		budgetResponse.setResponseInfo(getResponseInfo(budgetRequest.getRequestInfo()));
		List<Budget> budgets = new ArrayList<>();
		Budget budget = null;
		BudgetContract contract = null;
		List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();

		budgetRequest.getRequestInfo().setAction(ACTION_UPDATE);

		for (BudgetContract budgetContract : budgetRequest.getData()) {
			budget = mapper.toDomain(budgetContract);
			budget.setLastModifiedBy(budgetRequest.getRequestInfo().getUserInfo());
			budgets.add(budget);
		}

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			budgets = budgetService.save(budgets, errors, budgetRequest.getRequestInfo().getAction());

			for (Budget b : budgets) {
				contract = mapper.toContract(b);
				budgetContracts.add(contract);
			}

			budgetRequest.setData(budgetContracts);

			budgetServiceQueueRepository.addToQue(budgetRequest);

		} else {

			budgets = budgetService.update(budgets, errors);

			for (Budget b : budgets) {
				contract = mapper.toContract(b);
				budgetContracts.add(contract);
			}

			budgetRequest.setData(budgetContracts);

			budgetServiceQueueRepository.addToSearchQue(budgetRequest);

		}
		
		budgetResponse.setData(budgetContracts);

		return budgetResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<BudgetContract> search(@ModelAttribute BudgetSearchContract budgetSearchContract,
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

		CommonResponse<BudgetContract> response = new CommonResponse<>();
		response.setData(budgetContracts);
		response.setPage(new PaginationContract(budgets));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId(PLACEHOLDER).status(PLACEHOLDER).build();
	}

}