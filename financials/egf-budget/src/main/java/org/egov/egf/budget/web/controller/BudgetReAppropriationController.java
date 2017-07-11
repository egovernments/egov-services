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
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.service.BudgetReAppropriationService;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationSearchContract;
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
@RequestMapping("/budgetreappropriations")
public class BudgetReAppropriationController {

	@Autowired
	private BudgetReAppropriationService budgetReAppropriationService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BudgetReAppropriationContract> create(
			@RequestBody @Valid CommonRequest<BudgetReAppropriationContract> budgetReAppropriationContractRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		CommonResponse<BudgetReAppropriationContract> budgetReAppropriationResponse = new CommonResponse<>();
		List<BudgetReAppropriation> budgetreappropriations = new ArrayList<>();
		BudgetReAppropriation budgetReAppropriation = null;
		List<BudgetReAppropriationContract> budgetReAppropriationContracts = new ArrayList<BudgetReAppropriationContract>();
		BudgetReAppropriationContract contract = null;

		budgetReAppropriationContractRequest.getRequestInfo().setAction("create");

		for (BudgetReAppropriationContract brc : budgetReAppropriationContractRequest.getData()) {
			budgetReAppropriation = brc.toDomain();
			budgetReAppropriation.setCreatedBy(budgetReAppropriationContractRequest.getRequestInfo().getUserInfo());
			budgetReAppropriation
					.setLastModifiedBy(budgetReAppropriationContractRequest.getRequestInfo().getUserInfo());
			budgetreappropriations.add(budgetReAppropriation);
		}

		budgetreappropriations = budgetReAppropriationService.add(budgetreappropriations, errors);

		for (BudgetReAppropriation bra : budgetreappropriations) {
			contract = new BudgetReAppropriationContract();
			contract.toContract(bra);
			budgetReAppropriationContracts.add(contract);
		}

		budgetReAppropriationContractRequest.setData(budgetReAppropriationContracts);
		budgetReAppropriationService.addToQue(budgetReAppropriationContractRequest);
		budgetReAppropriationResponse.setData(budgetReAppropriationContracts);

		return budgetReAppropriationResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BudgetReAppropriationContract> update(
			@RequestBody @Valid CommonRequest<BudgetReAppropriationContract> budgetReAppropriationContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		budgetReAppropriationContractRequest.getRequestInfo().setAction("update");

		CommonResponse<BudgetReAppropriationContract> budgetReAppropriationResponse = new CommonResponse<>();
		List<BudgetReAppropriation> budgetreappropriations = new ArrayList<>();
		BudgetReAppropriation budgetReAppropriation = null;
		BudgetReAppropriationContract contract = null;
		List<BudgetReAppropriationContract> budgetReAppropriationContracts = new ArrayList<BudgetReAppropriationContract>();

		for (BudgetReAppropriationContract brc : budgetReAppropriationContractRequest.getData()) {
			budgetReAppropriation = new BudgetReAppropriation();
			budgetReAppropriation = brc.toDomain();
			budgetReAppropriation
					.setLastModifiedBy(budgetReAppropriationContractRequest.getRequestInfo().getUserInfo());
			budgetreappropriations.add(budgetReAppropriation);
		}

		budgetreappropriations = budgetReAppropriationService.update(budgetreappropriations, errors);

		for (BudgetReAppropriation budgetReAppropriationObj : budgetreappropriations) {
			contract = new BudgetReAppropriationContract();
			contract.toContract(budgetReAppropriationObj);
			budgetReAppropriationContracts.add(contract);
		}

		budgetReAppropriationContractRequest.setData(budgetReAppropriationContracts);
		budgetReAppropriationService.addToQue(budgetReAppropriationContractRequest);
		budgetReAppropriationResponse.setData(budgetReAppropriationContracts);

		return budgetReAppropriationResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<BudgetReAppropriationContract> search(
			@ModelAttribute BudgetReAppropriationSearchContract budgetReAppropriationSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		BudgetReAppropriationSearch domain = budgetReAppropriationSearchContract.toDomain();
		BudgetReAppropriationContract contract = null;
		List<BudgetReAppropriationContract> budgetReAppropriationContracts = new ArrayList<BudgetReAppropriationContract>();

		Pagination<BudgetReAppropriation> budgetreappropriations = budgetReAppropriationService.search(domain);

		for (BudgetReAppropriation budgetReAppropriation : budgetreappropriations.getPagedData()) {
			contract = new BudgetReAppropriationContract();
			contract.toContract(budgetReAppropriation);
			budgetReAppropriationContracts.add(contract);
		}

		CommonResponse<BudgetReAppropriationContract> response = new CommonResponse<>();
		response.setData(budgetReAppropriationContracts);
		response.setPage(new PaginationContract(budgetreappropriations));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}