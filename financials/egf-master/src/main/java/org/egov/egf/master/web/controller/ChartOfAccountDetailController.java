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
import org.egov.egf.master.domain.model.ChartOfAccountDetail;
import org.egov.egf.master.domain.model.ChartOfAccountDetailSearch;
import org.egov.egf.master.domain.service.ChartOfAccountDetailService;
import org.egov.egf.master.web.contract.ChartOfAccountDetailContract;
import org.egov.egf.master.web.contract.ChartOfAccountDetailSearchContract;
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
@RequestMapping("/chartofaccountdetails")
public class ChartOfAccountDetailController {

	@Autowired
	private ChartOfAccountDetailService chartOfAccountDetailService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<ChartOfAccountDetailContract> create(
			@RequestBody @Valid CommonRequest<ChartOfAccountDetailContract> chartOfAccountDetailContractRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<ChartOfAccountDetailContract> chartOfAccountDetailResponse = new CommonResponse<>();
		List<ChartOfAccountDetail> chartofaccountdetails = new ArrayList<>();
		ChartOfAccountDetail chartOfAccountDetail = null;
		List<ChartOfAccountDetailContract> chartOfAccountDetailContracts = new ArrayList<ChartOfAccountDetailContract>();
		ChartOfAccountDetailContract contract = null;

		chartOfAccountDetailContractRequest.getRequestInfo().setAction("create");

		for (ChartOfAccountDetailContract chartOfAccountDetailContract : chartOfAccountDetailContractRequest
				.getData()) {
			chartOfAccountDetail = new ChartOfAccountDetail();
			model.map(chartOfAccountDetailContract, chartOfAccountDetail);
			chartOfAccountDetail.setCreatedDate(new Date());
			chartOfAccountDetail.setCreatedBy(chartOfAccountDetailContractRequest.getRequestInfo().getUserInfo());
			chartOfAccountDetail.setLastModifiedBy(chartOfAccountDetailContractRequest.getRequestInfo().getUserInfo());
			chartofaccountdetails.add(chartOfAccountDetail);
		}

		chartofaccountdetails = chartOfAccountDetailService.add(chartofaccountdetails, errors);

		for (ChartOfAccountDetail f : chartofaccountdetails) {
			contract = new ChartOfAccountDetailContract();
			contract.setCreatedDate(new Date());
			model.map(f, contract);
			chartOfAccountDetailContracts.add(contract);
		}

		chartOfAccountDetailContractRequest.setData(chartOfAccountDetailContracts);
		chartOfAccountDetailService.addToQue(chartOfAccountDetailContractRequest);
		chartOfAccountDetailResponse.setData(chartOfAccountDetailContracts);

		return chartOfAccountDetailResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<ChartOfAccountDetailContract> update(
			@RequestBody @Valid CommonRequest<ChartOfAccountDetailContract> chartOfAccountDetailContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		chartOfAccountDetailContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<ChartOfAccountDetailContract> chartOfAccountDetailResponse = new CommonResponse<>();
		List<ChartOfAccountDetail> chartofaccountdetails = new ArrayList<>();
		ChartOfAccountDetail chartOfAccountDetail;
		ChartOfAccountDetailContract contract;
		List<ChartOfAccountDetailContract> chartOfAccountDetailContracts = new ArrayList<>();

		for (ChartOfAccountDetailContract chartOfAccountDetailContract : chartOfAccountDetailContractRequest
				.getData()) {
			chartOfAccountDetail = new ChartOfAccountDetail();
			model.map(chartOfAccountDetailContract, chartOfAccountDetail);
			chartOfAccountDetail.setLastModifiedDate(new Date());
			chartOfAccountDetail.setLastModifiedBy(chartOfAccountDetailContractRequest.getRequestInfo().getUserInfo());
			chartofaccountdetails.add(chartOfAccountDetail);
		}

		chartofaccountdetails = chartOfAccountDetailService.update(chartofaccountdetails, errors);

		for (ChartOfAccountDetail chartOfAccountDetailObj : chartofaccountdetails) {
			contract = new ChartOfAccountDetailContract();
			model.map(chartOfAccountDetailObj, contract);
			contract.setLastModifiedDate(new Date());
			chartOfAccountDetailContracts.add(contract);
		}

		chartOfAccountDetailContractRequest.setData(chartOfAccountDetailContracts);
		chartOfAccountDetailService.addToQue(chartOfAccountDetailContractRequest);
		chartOfAccountDetailResponse.setData(chartOfAccountDetailContracts);

		return chartOfAccountDetailResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<ChartOfAccountDetailContract> search(
			@ModelAttribute ChartOfAccountDetailSearchContract chartOfAccountDetailSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		ChartOfAccountDetailSearch domain = new ChartOfAccountDetailSearch();
		mapper.map(chartOfAccountDetailSearchContract, domain);
		ChartOfAccountDetailContract contract = null;
		ModelMapper model = new ModelMapper();
		List<ChartOfAccountDetailContract> chartOfAccountDetailContracts = new ArrayList<ChartOfAccountDetailContract>();

		Pagination<ChartOfAccountDetail> chartofaccountdetails = chartOfAccountDetailService.search(domain);

		for (ChartOfAccountDetail chartOfAccountDetail : chartofaccountdetails.getPagedData()) {
			contract = new ChartOfAccountDetailContract();
			model.map(chartOfAccountDetail, contract);
			chartOfAccountDetailContracts.add(contract);
		}

		CommonResponse<ChartOfAccountDetailContract> response = new CommonResponse<>();
		response.setData(chartOfAccountDetailContracts);
		response.setPage(new PaginationContract(chartofaccountdetails));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}