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
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.model.EgfStatus;
import org.egov.egf.budget.domain.service.BudgetReAppropriationService;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationSearchContract;
import org.egov.egf.master.web.contract.EgfStatusContract;
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
			budgetReAppropriation = BudgetReAppropriation.builder().additionAmount(brc.getAdditionAmount())
					.anticipatoryAmount(brc.getAnticipatoryAmount()).asOnDate(brc.getAsOnDate())
					.budgetDetailId(BudgetDetail.builder().id(brc.getBudgetDetail().getId()).build())
					.deductionAmount(brc.getDeductionAmount()).id(brc.getId())
					.originalAdditionAmount(brc.getOriginalAdditionAmount())
					.originalDeductionAmount(brc.getOriginalDeductionAmount())
					.statusId(EgfStatus.builder().id(brc.getStatus() != null ? brc.getStatus().getId() : "").build())
					.build();
			budgetReAppropriation.setCreatedBy(budgetReAppropriationContractRequest.getRequestInfo().getUserInfo());
			budgetReAppropriation
					.setLastModifiedBy(budgetReAppropriationContractRequest.getRequestInfo().getUserInfo());
			budgetreappropriations.add(budgetReAppropriation);
		}

		budgetreappropriations = budgetReAppropriationService.add(budgetreappropriations, errors);

		for (BudgetReAppropriation f : budgetreappropriations) {
			contract = BudgetReAppropriationContract.builder().additionAmount(f.getAdditionAmount())
					.anticipatoryAmount(f.getAnticipatoryAmount()).asOnDate(f.getAsOnDate())
					.budgetDetail(BudgetDetailContract.builder().id(f.getBudgetDetailId().getId()).build())
					.deductionAmount(f.getDeductionAmount()).id(f.getId())
					.originalAdditionAmount(f.getOriginalAdditionAmount())
					.originalDeductionAmount(f.getOriginalDeductionAmount()).status(EgfStatusContract.builder()
							.id(f.getStatusId() != null ? f.getStatusId().getId() : "").build())
					.build();
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
			budgetReAppropriation = BudgetReAppropriation.builder().additionAmount(brc.getAdditionAmount())
					.anticipatoryAmount(brc.getAnticipatoryAmount()).asOnDate(brc.getAsOnDate())
					.budgetDetailId(BudgetDetail.builder().id(brc.getBudgetDetail().getId()).build())
					.deductionAmount(brc.getDeductionAmount()).id(brc.getId())
					.originalAdditionAmount(brc.getOriginalAdditionAmount())
					.originalDeductionAmount(brc.getOriginalDeductionAmount())
					.statusId(EgfStatus.builder().id(brc.getStatus() != null ? brc.getStatus().getId() : "").build())
					.build();
			budgetReAppropriation
					.setLastModifiedBy(budgetReAppropriationContractRequest.getRequestInfo().getUserInfo());
			budgetreappropriations.add(budgetReAppropriation);
		}

		budgetreappropriations = budgetReAppropriationService.update(budgetreappropriations, errors);

		for (BudgetReAppropriation budgetReAppropriationObj : budgetreappropriations) {
			contract = new BudgetReAppropriationContract();
			contract = BudgetReAppropriationContract.builder()
					.additionAmount(budgetReAppropriationObj.getAdditionAmount())
					.anticipatoryAmount(budgetReAppropriationObj.getAnticipatoryAmount())
					.asOnDate(budgetReAppropriationObj.getAsOnDate())
					.budgetDetail(BudgetDetailContract.builder()
							.id(budgetReAppropriationObj.getBudgetDetailId().getId()).build())
					.deductionAmount(budgetReAppropriationObj.getDeductionAmount()).id(budgetReAppropriationObj.getId())
					.originalAdditionAmount(budgetReAppropriationObj.getOriginalAdditionAmount())
					.originalDeductionAmount(
							budgetReAppropriationObj.getOriginalDeductionAmount())
					.status(EgfStatusContract.builder().id(budgetReAppropriationObj.getStatusId() != null
							? budgetReAppropriationObj.getStatusId().getId() : "").build())
					.build();
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

		ModelMapper mapper = new ModelMapper();
		BudgetReAppropriationSearch domain = new BudgetReAppropriationSearch();
		mapper.map(budgetReAppropriationSearchContract, domain);
		BudgetReAppropriationContract contract = null;
		ModelMapper model = new ModelMapper();
		List<BudgetReAppropriationContract> budgetReAppropriationContracts = new ArrayList<BudgetReAppropriationContract>();

		Pagination<BudgetReAppropriation> budgetreappropriations = budgetReAppropriationService.search(domain);

		for (BudgetReAppropriation budgetReAppropriation : budgetreappropriations.getPagedData()) {
			contract = new BudgetReAppropriationContract();
			model.map(budgetReAppropriation, contract);
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