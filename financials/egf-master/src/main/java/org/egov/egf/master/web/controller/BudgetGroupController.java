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
import org.egov.egf.master.domain.model.BudgetGroup;
import org.egov.egf.master.domain.model.BudgetGroupSearch;
import org.egov.egf.master.domain.service.BudgetGroupService;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.contract.BudgetGroupSearchContract;
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
@RequestMapping("/budgetgroups")
public class BudgetGroupController {

	@Autowired
	private BudgetGroupService budgetGroupService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BudgetGroupContract> create(
			@RequestBody @Valid CommonRequest<BudgetGroupContract> budgetGroupContractRequest, BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<BudgetGroupContract> budgetGroupResponse = new CommonResponse<>();
		List<BudgetGroup> budgetgroups = new ArrayList<>();
		BudgetGroup budgetGroup;
		List<BudgetGroupContract> budgetGroupContracts = new ArrayList<>();
		BudgetGroupContract contract;

		budgetGroupContractRequest.getRequestInfo().setAction("create");

		for (BudgetGroupContract budgetGroupContract : budgetGroupContractRequest.getData()) {
			budgetGroup = new BudgetGroup();
			model.map(budgetGroupContract, budgetGroup);
			budgetGroup.setCreatedBy(budgetGroupContractRequest.getRequestInfo().getUserInfo());
			budgetGroup.setLastModifiedBy(budgetGroupContractRequest.getRequestInfo().getUserInfo());
			budgetgroups.add(budgetGroup);
		}

		budgetgroups = budgetGroupService.add(budgetgroups, errors);

		for (BudgetGroup f : budgetgroups) {
			contract = new BudgetGroupContract();
			model.map(f, contract);
			budgetGroupContracts.add(contract);
		}

		budgetGroupContractRequest.setData(budgetGroupContracts);
		budgetGroupService.addToQue(budgetGroupContractRequest);
		budgetGroupResponse.setData(budgetGroupContracts);

		return budgetGroupResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BudgetGroupContract> update(
			@RequestBody @Valid CommonRequest<BudgetGroupContract> budgetGroupContractRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		budgetGroupContractRequest.getRequestInfo().setAction("update");

		ModelMapper model = new ModelMapper();
		CommonResponse<BudgetGroupContract> budgetGroupResponse = new CommonResponse<>();
		List<BudgetGroup> budgetgroups = new ArrayList<>();
		BudgetGroup budgetGroup;
		BudgetGroupContract contract;
		List<BudgetGroupContract> budgetGroupContracts = new ArrayList<>();

		for (BudgetGroupContract budgetGroupContract : budgetGroupContractRequest.getData()) {
			budgetGroup = new BudgetGroup();
			model.map(budgetGroupContract, budgetGroup);
			budgetGroup.setLastModifiedBy(budgetGroupContractRequest.getRequestInfo().getUserInfo());
			budgetgroups.add(budgetGroup);
		}

		budgetgroups = budgetGroupService.update(budgetgroups, errors);

		for (BudgetGroup budgetGroupObj : budgetgroups) {
			contract = new BudgetGroupContract();
			model.map(budgetGroupObj, contract);
			budgetGroupContracts.add(contract);
		}

		budgetGroupContractRequest.setData(budgetGroupContracts);
		budgetGroupService.addToQue(budgetGroupContractRequest);
		budgetGroupResponse.setData(budgetGroupContracts);

		return budgetGroupResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<BudgetGroupContract> search(
			@ModelAttribute BudgetGroupSearchContract budgetGroupSearchContract, @RequestBody RequestInfo requestInfo,
			BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		BudgetGroupSearch domain = new BudgetGroupSearch();
		mapper.map(budgetGroupSearchContract, domain);
		BudgetGroupContract contract = null;
		ModelMapper model = new ModelMapper();
		List<BudgetGroupContract> budgetGroupContracts = new ArrayList<BudgetGroupContract>();

		Pagination<BudgetGroup> budgetgroups = budgetGroupService.search(domain);

		for (BudgetGroup budgetGroup : budgetgroups.getPagedData()) {
			contract = new BudgetGroupContract();
			model.map(budgetGroup, contract);
			budgetGroupContracts.add(contract);
		}

		CommonResponse<BudgetGroupContract> response = new CommonResponse<>();
		response.setData(budgetGroupContracts);
		response.setPage(new PaginationContract(budgetgroups));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}