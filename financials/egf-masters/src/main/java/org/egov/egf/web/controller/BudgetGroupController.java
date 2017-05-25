package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.BudgetGroup;
import org.egov.egf.persistence.queue.contract.BudgetGroupContract;
import org.egov.egf.persistence.queue.contract.BudgetGroupContractRequest;
import org.egov.egf.persistence.queue.contract.BudgetGroupContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.BudgetGroupService;
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
@RequestMapping("/budgetgroups")
public class BudgetGroupController {
	@Autowired
	private BudgetGroupService budgetGroupService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public BudgetGroupContractResponse create(@RequestBody @Valid BudgetGroupContractRequest budgetGroupContractRequest,
			BindingResult errors) {
		budgetGroupService.validate(budgetGroupContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		budgetGroupContractRequest.getRequestInfo().setAction("create");
		budgetGroupService.fetchRelatedContracts(budgetGroupContractRequest);
		budgetGroupService.push(budgetGroupContractRequest);
		BudgetGroupContractResponse budgetGroupContractResponse = new BudgetGroupContractResponse();
		budgetGroupContractResponse.setBudgetGroups(new ArrayList<BudgetGroupContract>());

		if (budgetGroupContractRequest.getBudgetGroups() != null
				&& !budgetGroupContractRequest.getBudgetGroups().isEmpty()) {
			for (BudgetGroupContract budgetGroupContract : budgetGroupContractRequest.getBudgetGroups()) {

				budgetGroupContractResponse.getBudgetGroups().add(budgetGroupContract);
			}
		} else if (budgetGroupContractRequest.getBudgetGroup() != null) {
			budgetGroupContractResponse.setBudgetGroup(budgetGroupContractRequest.getBudgetGroup());
		}

		budgetGroupContractResponse.setResponseInfo(getResponseInfo(budgetGroupContractRequest.getRequestInfo()));
		budgetGroupContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return budgetGroupContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public BudgetGroupContractResponse update(@RequestBody @Valid BudgetGroupContractRequest budgetGroupContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {

		budgetGroupService.validate(budgetGroupContractRequest, "update", errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		budgetGroupContractRequest.getRequestInfo().setAction("update");
		budgetGroupService.fetchRelatedContracts(budgetGroupContractRequest);
		budgetGroupContractRequest.getBudgetGroup().setId(uniqueId);
		budgetGroupService.push(budgetGroupContractRequest);
		BudgetGroupContractResponse budgetGroupContractResponse = new BudgetGroupContractResponse();
		budgetGroupContractResponse.setBudgetGroup(budgetGroupContractRequest.getBudgetGroup());
		budgetGroupContractResponse.setResponseInfo(getResponseInfo(budgetGroupContractRequest.getRequestInfo()));
		budgetGroupContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return budgetGroupContractResponse;
	}

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public BudgetGroupContractResponse view(@ModelAttribute BudgetGroupContractRequest budgetGroupContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		budgetGroupService.validate(budgetGroupContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		budgetGroupService.fetchRelatedContracts(budgetGroupContractRequest);
		BudgetGroup budgetGroupFromDb = budgetGroupService.findOne(uniqueId);
		BudgetGroupContract budgetGroup = budgetGroupContractRequest.getBudgetGroup();

		ModelMapper model = new ModelMapper();
		model.map(budgetGroupFromDb, budgetGroup);

		BudgetGroupContractResponse budgetGroupContractResponse = new BudgetGroupContractResponse();
		budgetGroupContractResponse.setBudgetGroup(budgetGroup);
		budgetGroupContractResponse.setResponseInfo(getResponseInfo(budgetGroupContractRequest.getRequestInfo()));
		budgetGroupContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return budgetGroupContractResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public BudgetGroupContractResponse updateAll(
			@RequestBody @Valid BudgetGroupContractRequest budgetGroupContractRequest, BindingResult errors) {
		budgetGroupService.validate(budgetGroupContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		budgetGroupContractRequest.getRequestInfo().setAction("updateAll");
		budgetGroupService.fetchRelatedContracts(budgetGroupContractRequest);
		budgetGroupService.push(budgetGroupContractRequest);
		BudgetGroupContractResponse budgetGroupContractResponse = new BudgetGroupContractResponse();
		budgetGroupContractResponse.setBudgetGroups(new ArrayList<BudgetGroupContract>());
		for (BudgetGroupContract budgetGroupContract : budgetGroupContractRequest.getBudgetGroups()) {
			budgetGroupContractResponse.getBudgetGroups().add(budgetGroupContract);
		}

		budgetGroupContractResponse.setResponseInfo(getResponseInfo(budgetGroupContractRequest.getRequestInfo()));
		budgetGroupContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return budgetGroupContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public BudgetGroupContractResponse search(@ModelAttribute BudgetGroupContract budgetGroupContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		BudgetGroupContractRequest budgetGroupContractRequest = new BudgetGroupContractRequest();
		budgetGroupContractRequest.setBudgetGroup(budgetGroupContracts);
		budgetGroupContractRequest.setRequestInfo(requestInfo);
		budgetGroupService.validate(budgetGroupContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		budgetGroupService.fetchRelatedContracts(budgetGroupContractRequest);
		BudgetGroupContractResponse budgetGroupContractResponse = new BudgetGroupContractResponse();
		budgetGroupContractResponse.setBudgetGroups(new ArrayList<BudgetGroupContract>());
		budgetGroupContractResponse.setPage(new Pagination());
		Page<BudgetGroup> allBudgetGroups;
		ModelMapper model = new ModelMapper();

		allBudgetGroups = budgetGroupService.search(budgetGroupContractRequest);
		BudgetGroupContract budgetGroupContract = null;
		for (BudgetGroup b : allBudgetGroups) {
			budgetGroupContract = new BudgetGroupContract();
			model.map(b, budgetGroupContract);
			budgetGroupContractResponse.getBudgetGroups().add(budgetGroupContract);
		}
		budgetGroupContractResponse.getPage().map(allBudgetGroups);
		budgetGroupContractResponse.setResponseInfo(getResponseInfo(budgetGroupContractRequest.getRequestInfo()));
		budgetGroupContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return budgetGroupContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}