package org.egov.egf.budget.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.PaginationContract;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.service.BudgetReAppropriationService;
import org.egov.egf.budget.persistence.queue.repository.BudgetReAppropriationQueueRepository;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationRequest;
import org.egov.egf.budget.web.contract.BudgetReAppropriationResponse;
import org.egov.egf.budget.web.contract.BudgetReAppropriationSearchContract;
import org.egov.egf.budget.web.mapper.BudgetReAppropriationMapper;
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
@RequestMapping("/budgetreappropriations")
public class BudgetReAppropriationController {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String PLACEHOLDER = "placeholder";

	@Autowired
	private BudgetReAppropriationService budgetReAppropriationService;

	@Autowired
	private BudgetReAppropriationQueueRepository budgetReAppropriationQueueRepository;

	@Value("${persist.through.kafka}")
	private static String persistThroughKafka;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public BudgetReAppropriationResponse create(@RequestBody BudgetReAppropriationRequest budgetReAppropriationRequest,
			BindingResult errors) {

		BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();
		BudgetReAppropriationResponse budgetReAppropriationResponse = new BudgetReAppropriationResponse();
		budgetReAppropriationResponse.setResponseInfo(getResponseInfo(budgetReAppropriationRequest.getRequestInfo()));
		List<BudgetReAppropriation> budgetreappropriations = new ArrayList<>();
		BudgetReAppropriation budgetReAppropriation = null;
		List<BudgetReAppropriationContract> budgetReAppropriationContracts = new ArrayList<BudgetReAppropriationContract>();
		BudgetReAppropriationContract contract = null;

		budgetReAppropriationRequest.getRequestInfo().setAction(ACTION_CREATE);

		for (BudgetReAppropriationContract budgetReAppropriationContract : budgetReAppropriationRequest
				.getBudgetReAppropriations()) {
			budgetReAppropriation = mapper.toDomain(budgetReAppropriationContract);
			budgetReAppropriation.setCreatedBy(budgetReAppropriationRequest.getRequestInfo().getUserInfo());
			budgetReAppropriation.setLastModifiedBy(budgetReAppropriationRequest.getRequestInfo().getUserInfo());
			budgetreappropriations.add(budgetReAppropriation);
		}

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			budgetreappropriations = budgetReAppropriationService.fetchAndValidate(budgetreappropriations, errors,
					budgetReAppropriationRequest.getRequestInfo().getAction());

			for (BudgetReAppropriation bra : budgetreappropriations) {
				contract = mapper.toContract(bra);
				budgetReAppropriationContracts.add(contract);
			}

			budgetReAppropriationRequest.setBudgetReAppropriations(budgetReAppropriationContracts);
			budgetReAppropriationQueueRepository.addToQue(budgetReAppropriationRequest);

		} else {

			budgetreappropriations = budgetReAppropriationService.save(budgetreappropriations, errors);

			for (BudgetReAppropriation bra : budgetreappropriations) {
				contract = mapper.toContract(bra);
				budgetReAppropriationContracts.add(contract);
			}

			budgetReAppropriationRequest.setBudgetReAppropriations(budgetReAppropriationContracts);

			budgetReAppropriationQueueRepository.addToSearchQue(budgetReAppropriationRequest);

		}
		budgetReAppropriationResponse.setBudgetReAppropriations(budgetReAppropriationContracts);

		return budgetReAppropriationResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public BudgetReAppropriationResponse update(
			@RequestBody @Valid BudgetReAppropriationRequest budgetReAppropriationRequest, BindingResult errors) {

		BudgetReAppropriationMapper mapper = new BudgetReAppropriationMapper();
		budgetReAppropriationRequest.getRequestInfo().setAction(ACTION_UPDATE);
		BudgetReAppropriationResponse budgetReAppropriationResponse = new BudgetReAppropriationResponse();
		budgetReAppropriationResponse.setResponseInfo(getResponseInfo(budgetReAppropriationRequest.getRequestInfo()));
		List<BudgetReAppropriation> budgetreappropriations = new ArrayList<>();
		BudgetReAppropriation budgetReAppropriation = null;
		BudgetReAppropriationContract contract = null;
		List<BudgetReAppropriationContract> budgetReAppropriationContracts = new ArrayList<BudgetReAppropriationContract>();

		for (BudgetReAppropriationContract budgetReAppropriationContract : budgetReAppropriationRequest
				.getBudgetReAppropriations()) {
			budgetReAppropriation = mapper.toDomain(budgetReAppropriationContract);
			budgetReAppropriation.setLastModifiedBy(budgetReAppropriationRequest.getRequestInfo().getUserInfo());
			budgetreappropriations.add(budgetReAppropriation);
		}

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			budgetreappropriations = budgetReAppropriationService.fetchAndValidate(budgetreappropriations, errors,
					budgetReAppropriationRequest.getRequestInfo().getAction());

			for (BudgetReAppropriation bra : budgetreappropriations) {
				contract = mapper.toContract(bra);
				budgetReAppropriationContracts.add(contract);
			}

			budgetReAppropriationRequest.setBudgetReAppropriations(budgetReAppropriationContracts);
			budgetReAppropriationQueueRepository.addToQue(budgetReAppropriationRequest);

		} else {

			budgetreappropriations = budgetReAppropriationService.update(budgetreappropriations, errors);

			for (BudgetReAppropriation bra : budgetreappropriations) {
				contract = mapper.toContract(bra);
				budgetReAppropriationContracts.add(contract);
			}

			budgetReAppropriationRequest.setBudgetReAppropriations(budgetReAppropriationContracts);

			budgetReAppropriationQueueRepository.addToSearchQue(budgetReAppropriationRequest);

		}

		budgetReAppropriationResponse.setBudgetReAppropriations(budgetReAppropriationContracts);

		return budgetReAppropriationResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public BudgetReAppropriationResponse search(
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

		BudgetReAppropriationResponse response = new BudgetReAppropriationResponse();
		response.setBudgetReAppropriations(budgetReAppropriationContracts);
		response.setPage(new PaginationContract(budgetreappropriations));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.ts(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())).resMsgId(requestInfo.getMsgId())
				.resMsgId(PLACEHOLDER).status(PLACEHOLDER).build();
	}

}