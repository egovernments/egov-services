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
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.domain.service.BudgetDetailService;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetDetailSearchContract;
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
@RequestMapping("/budgetdetails")
public class BudgetDetailController {

	@Autowired
	private BudgetDetailService budgetDetailService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BudgetDetailContract> create(@RequestBody @Valid CommonRequest<BudgetDetailContract> budgetDetailContractRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<BudgetDetailContract> budgetDetailResponse = new CommonResponse<>();
		List<BudgetDetail> budgetdetails = new ArrayList<>();
		BudgetDetail budgetDetail = null;
		List<BudgetDetailContract> budgetDetailContracts = new ArrayList<BudgetDetailContract>();
		BudgetDetailContract contract = null;

		budgetDetailContractRequest.getRequestInfo().setAction("create");

		for (BudgetDetailContract budgetDetailContract : budgetDetailContractRequest.getData()) {
			budgetDetail = new BudgetDetail();
			model.map(budgetDetailContract, budgetDetail);
			budgetDetail.setCreatedBy(budgetDetailContractRequest.getRequestInfo().getUserInfo());
			budgetDetail.setLastModifiedBy(budgetDetailContractRequest.getRequestInfo().getUserInfo());
			budgetdetails.add(budgetDetail);
		}

		budgetdetails = budgetDetailService.add(budgetdetails, errors);

		for (BudgetDetail f : budgetdetails) {
			contract = new BudgetDetailContract();
			model.map(f, contract);
			budgetDetailContracts.add(contract);
		}

		budgetDetailContractRequest.setData(budgetDetailContracts);
		budgetDetailService.addToQue(budgetDetailContractRequest);
		budgetDetailResponse.setData(budgetDetailContracts);

		return budgetDetailResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BudgetDetailContract> update(@RequestBody @Valid CommonRequest<BudgetDetailContract> budgetDetailContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<BudgetDetailContract> budgetDetailResponse = new CommonResponse<>();
		List<BudgetDetail> budgetdetails = new ArrayList<>();
		BudgetDetail budgetDetail = null;
		BudgetDetailContract contract = null;
		List<BudgetDetailContract> budgetDetailContracts = new ArrayList<BudgetDetailContract>();

		for (BudgetDetailContract budgetDetailContract : budgetDetailContractRequest.getData()) {
			budgetDetail = new BudgetDetail();
			model.map(budgetDetailContract, budgetDetail);
			budgetDetail.setLastModifiedBy(budgetDetailContractRequest.getRequestInfo().getUserInfo());
			budgetdetails.add(budgetDetail);
		}

		budgetdetails = budgetDetailService.update(budgetdetails, errors);

		for (BudgetDetail budgetDetailObj : budgetdetails) {
			contract = new BudgetDetailContract();
			model.map(budgetDetailObj, contract);
			budgetDetailContracts.add(contract);
		}

		budgetDetailContractRequest.setData(budgetDetailContracts);
		budgetDetailService.addToQue(budgetDetailContractRequest);
		budgetDetailResponse.setData(budgetDetailContracts);

		return budgetDetailResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<BudgetDetailContract> search(@ModelAttribute BudgetDetailSearchContract budgetDetailSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		BudgetDetailSearch domain = new BudgetDetailSearch();
		mapper.map(budgetDetailSearchContract, domain);
		BudgetDetailContract contract = null;
		ModelMapper model = new ModelMapper();
		List<BudgetDetailContract> budgetDetailContracts = new ArrayList<BudgetDetailContract>();

		Pagination<BudgetDetail> budgetdetails = budgetDetailService.search(domain);

		for (BudgetDetail budgetDetail : budgetdetails.getPagedData()) {
			contract = new BudgetDetailContract();
			model.map(budgetDetail, contract);
			budgetDetailContracts.add(contract);
		}

		CommonResponse<BudgetDetailContract> response = new CommonResponse<>();
		response.setData(budgetDetailContracts);
		response.setPage(new PaginationContract(budgetdetails));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}