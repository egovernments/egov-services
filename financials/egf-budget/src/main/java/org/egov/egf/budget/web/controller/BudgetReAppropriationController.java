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
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.service.BudgetReAppropriationService;
import org.egov.egf.budget.persistence.queue.BudgetServiceQueueRepository;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationSearchContract;
import org.egov.egf.budget.web.mapper.BudgetReAppropriationMapper;
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

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String PLACEHOLDER = "placeholder";

	@Autowired
	private BudgetReAppropriationService budgetReAppropriationService;

	@Autowired
	private BudgetServiceQueueRepository budgetServiceQueueRepository;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BudgetReAppropriationContract> create(
			@RequestBody CommonRequest<BudgetReAppropriationContract> budgetReAppropriationRequest,
			BindingResult errors) {

		BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();
		CommonResponse<BudgetReAppropriationContract> budgetReAppropriationResponse = new CommonResponse<>();
		budgetReAppropriationResponse.setResponseInfo(getResponseInfo(budgetReAppropriationRequest.getRequestInfo()));
		List<BudgetReAppropriation> budgetreappropriations = new ArrayList<>();
		BudgetReAppropriation budgetReAppropriation = null;
		List<BudgetReAppropriationContract> budgetReAppropriationContracts = new ArrayList<BudgetReAppropriationContract>();
		BudgetReAppropriationContract contract = null;

		budgetReAppropriationRequest.getRequestInfo().setAction(ACTION_CREATE);

		for (BudgetReAppropriationContract budgetReAppropriationContract : budgetReAppropriationRequest.getData()) {
			budgetReAppropriation = mapper.toDomain(budgetReAppropriationContract);
			budgetReAppropriation.setCreatedBy(budgetReAppropriationRequest.getRequestInfo().getUserInfo());
			budgetReAppropriation.setLastModifiedBy(budgetReAppropriationRequest.getRequestInfo().getUserInfo());
			budgetreappropriations.add(budgetReAppropriation);
		}

		budgetreappropriations = budgetReAppropriationService.save(budgetreappropriations, errors,
				budgetReAppropriationRequest.getRequestInfo().getAction());

		for (BudgetReAppropriation bra : budgetreappropriations) {
			contract = mapper.toContract(bra);
			budgetReAppropriationContracts.add(contract);
		}

		budgetReAppropriationRequest.setData(budgetReAppropriationContracts);
		budgetServiceQueueRepository.addToQue(budgetReAppropriationRequest);
		budgetReAppropriationResponse.setData(budgetReAppropriationContracts);

		return budgetReAppropriationResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BudgetReAppropriationContract> update(
			@RequestBody @Valid CommonRequest<BudgetReAppropriationContract> budgetReAppropriationRequest,
			BindingResult errors) {

		BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();
		budgetReAppropriationRequest.getRequestInfo().setAction(ACTION_UPDATE);
		CommonResponse<BudgetReAppropriationContract> budgetReAppropriationResponse = new CommonResponse<>();
		budgetReAppropriationResponse.setResponseInfo(getResponseInfo(budgetReAppropriationRequest.getRequestInfo()));
		List<BudgetReAppropriation> budgetreappropriations = new ArrayList<>();
		BudgetReAppropriation budgetReAppropriation = null;
		BudgetReAppropriationContract contract = null;
		List<BudgetReAppropriationContract> budgetReAppropriationContracts = new ArrayList<BudgetReAppropriationContract>();

		for (BudgetReAppropriationContract budgetReAppropriationContract : budgetReAppropriationRequest.getData()) {
			budgetReAppropriation = mapper.toDomain(budgetReAppropriationContract);
			budgetReAppropriation.setLastModifiedBy(budgetReAppropriationRequest.getRequestInfo().getUserInfo());
			budgetreappropriations.add(budgetReAppropriation);
		}

		budgetreappropriations = budgetReAppropriationService.save(budgetreappropriations, errors,
				budgetReAppropriationRequest.getRequestInfo().getAction());

		for (BudgetReAppropriation bra : budgetreappropriations) {
			contract = mapper.toContract(bra);
			budgetReAppropriationContracts.add(contract);
		}

		budgetReAppropriationRequest.setData(budgetReAppropriationContracts);
		budgetServiceQueueRepository.addToQue(budgetReAppropriationRequest);
		budgetReAppropriationResponse.setData(budgetReAppropriationContracts);

		return budgetReAppropriationResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<BudgetReAppropriationContract> search(
			@ModelAttribute BudgetReAppropriationSearchContract budgetReAppropriationSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();
		BudgetReAppropriationSearch domain = mapper.toSearchDomain(budgetReAppropriationSearchContract);
		BudgetReAppropriationContract contract = null;
		List<BudgetReAppropriationContract> budgetReAppropriationContracts = new ArrayList<BudgetReAppropriationContract>();
		Pagination<BudgetReAppropriation> budgetreappropriations = budgetReAppropriationService.search(domain);

		for (BudgetReAppropriation budgetReAppropriation : budgetreappropriations.getPagedData()) {
			contract = mapper.toContract(budgetReAppropriation);
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
				.resMsgId(requestInfo.getMsgId()).resMsgId(PLACEHOLDER).status(PLACEHOLDER).build();
	}

}