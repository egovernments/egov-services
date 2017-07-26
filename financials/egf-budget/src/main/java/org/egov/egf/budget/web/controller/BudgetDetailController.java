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
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.domain.service.BudgetDetailService;
import org.egov.egf.budget.persistence.queue.repository.BudgetDetailQueueRepository;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetDetailRequest;
import org.egov.egf.budget.web.contract.BudgetDetailResponse;
import org.egov.egf.budget.web.contract.BudgetDetailSearchContract;
import org.egov.egf.budget.web.mapper.BudgetDetailMapper;
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
@RequestMapping("/budgetdetails")
public class BudgetDetailController {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String PLACEHOLDER = "placeholder";

	@Autowired
	private BudgetDetailService budgetDetailService;

	@Autowired
	private BudgetDetailQueueRepository budgetDetailQueueRepository;

	@Value("${persist.through.kafka}")
	private static String persistThroughKafka;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public BudgetDetailResponse create(@RequestBody BudgetDetailRequest budgetDetailRequest, BindingResult errors) {

		BudgetDetailMapper mapper = new BudgetDetailMapper();
		BudgetDetailResponse budgetDetailResponse = new BudgetDetailResponse();
		budgetDetailResponse.setResponseInfo(getResponseInfo(budgetDetailRequest.getRequestInfo()));
		List<BudgetDetail> budgetdetails = new ArrayList<>();
		BudgetDetail budgetDetail = null;
		List<BudgetDetailContract> budgetDetailContracts = new ArrayList<BudgetDetailContract>();
		BudgetDetailContract contract = null;

		budgetDetailRequest.getRequestInfo().setAction(ACTION_CREATE);

		for (BudgetDetailContract budgetDetailContract : budgetDetailRequest.getBudgetDetails()) {
			budgetDetail = mapper.toDomain(budgetDetailContract);
			budgetDetail.setCreatedBy(budgetDetailRequest.getRequestInfo().getUserInfo());
			budgetDetail.setLastModifiedBy(budgetDetailRequest.getRequestInfo().getUserInfo());
			budgetdetails.add(budgetDetail);
		}

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			budgetdetails = budgetDetailService.fetchAndValidate(budgetdetails, errors,
					budgetDetailRequest.getRequestInfo().getAction());

			for (BudgetDetail bd : budgetdetails) {
				contract = mapper.toContract(bd);
				budgetDetailContracts.add(contract);
			}

			budgetDetailRequest.setBudgetDetails(budgetDetailContracts);
			budgetDetailQueueRepository.addToQue(budgetDetailRequest);

		} else {

			budgetdetails = budgetDetailService.save(budgetdetails, errors);

			for (BudgetDetail bd : budgetdetails) {
				contract = mapper.toContract(bd);
				budgetDetailContracts.add(contract);
			}

			budgetDetailRequest.setBudgetDetails(budgetDetailContracts);

			budgetDetailQueueRepository.addToSearchQue(budgetDetailRequest);

		}
		budgetDetailResponse.setBudgetDetails(budgetDetailContracts);

		return budgetDetailResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public BudgetDetailResponse update(@RequestBody @Valid BudgetDetailRequest budgetDetailRequest,
			BindingResult errors) {

		BudgetDetailMapper mapper = new BudgetDetailMapper();
		budgetDetailRequest.getRequestInfo().setAction(ACTION_UPDATE);
		BudgetDetailResponse budgetDetailResponse = new BudgetDetailResponse();
		budgetDetailResponse.setResponseInfo(getResponseInfo(budgetDetailRequest.getRequestInfo()));
		List<BudgetDetail> budgetdetails = new ArrayList<>();
		BudgetDetail budgetDetail = null;
		BudgetDetailContract contract = null;
		List<BudgetDetailContract> budgetDetailContracts = new ArrayList<BudgetDetailContract>();

		for (BudgetDetailContract budgetDetailContract : budgetDetailRequest.getBudgetDetails()) {
			budgetDetail = mapper.toDomain(budgetDetailContract);
			budgetDetail.setCreatedBy(budgetDetailRequest.getRequestInfo().getUserInfo());
			budgetDetail.setLastModifiedBy(budgetDetailRequest.getRequestInfo().getUserInfo());
			budgetdetails.add(budgetDetail);
		}

		if (persistThroughKafka != null && !persistThroughKafka.isEmpty()
				&& persistThroughKafka.equalsIgnoreCase("yes")) {

			budgetdetails = budgetDetailService.fetchAndValidate(budgetdetails, errors,
					budgetDetailRequest.getRequestInfo().getAction());

			for (BudgetDetail bd : budgetdetails) {
				contract = mapper.toContract(bd);
				budgetDetailContracts.add(contract);
			}

			budgetDetailRequest.setBudgetDetails(budgetDetailContracts);
			budgetDetailQueueRepository.addToQue(budgetDetailRequest);

		} else {

			budgetdetails = budgetDetailService.update(budgetdetails, errors);

			for (BudgetDetail bd : budgetdetails) {
				contract = mapper.toContract(bd);
				budgetDetailContracts.add(contract);
			}

			budgetDetailRequest.setBudgetDetails(budgetDetailContracts);

			budgetDetailQueueRepository.addToSearchQue(budgetDetailRequest);

		}

		budgetDetailResponse.setBudgetDetails(budgetDetailContracts);

		return budgetDetailResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public BudgetDetailResponse search(@ModelAttribute BudgetDetailSearchContract budgetDetailSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		BudgetDetailMapper mapper = new BudgetDetailMapper();
		BudgetDetailSearch domain = mapper.toSearchDomain(budgetDetailSearchContract);
		BudgetDetailContract contract = null;
		List<BudgetDetailContract> budgetDetailContracts = new ArrayList<BudgetDetailContract>();

		Pagination<BudgetDetail> budgetdetails = budgetDetailService.search(domain);

		for (BudgetDetail budgetDetail : budgetdetails.getPagedData()) {
			contract = mapper.toContract(budgetDetail);
			budgetDetailContracts.add(contract);
		}

		BudgetDetailResponse response = new BudgetDetailResponse();
		response.setBudgetDetails(budgetDetailContracts);
		response.setPage(new PaginationContract(budgetdetails));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.ts(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())).resMsgId(requestInfo.getMsgId())
				.resMsgId(PLACEHOLDER).status(PLACEHOLDER).build();
	}

}