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
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.model.FundSearch;
import org.egov.egf.master.domain.service.FundService;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundSearchContract;
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
@RequestMapping("/funds")
public class FundController {

	@Autowired
	private FundService fundService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<FundContract> create(@RequestBody @Valid CommonRequest<FundContract> fundContractRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<FundContract> fundResponse = new CommonResponse<>();
		List<Fund> funds = new ArrayList<>();
		Fund fund = null;
		List<FundContract> fundContracts = new ArrayList<FundContract>();
		FundContract contract = null;

		fundContractRequest.getRequestInfo().setAction("create");

		for (FundContract fundContract : fundContractRequest.getData()) {
			fund = new Fund();
			model.map(fundContract, fund);
			fund.setCreatedBy(fundContractRequest.getRequestInfo().getUserInfo());
			fund.setLastModifiedBy(fundContractRequest.getRequestInfo().getUserInfo());
			funds.add(fund);
		}

		funds = fundService.add(funds, errors);

		for (Fund f : funds) {
			contract = new FundContract();
			model.map(f, contract);
			fundContracts.add(contract);
		}

		fundContractRequest.setData(fundContracts);
		fundService.addToQue(fundContractRequest);
		fundResponse.setData(fundContracts);

		return fundResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<FundContract> update(@RequestBody @Valid CommonRequest<FundContract> fundContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		fundContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<FundContract> fundResponse = new CommonResponse<>();
		List<Fund> funds = new ArrayList<>();
		Fund fund = null;
		FundContract contract = null;
		List<FundContract> fundContracts = new ArrayList<FundContract>();

		for (FundContract fundContract : fundContractRequest.getData()) {
			fund = new Fund();
			model.map(fundContract, fund);
			fund.setLastModifiedBy(fundContractRequest.getRequestInfo().getUserInfo());
			funds.add(fund);
		}

		funds = fundService.update(funds, errors);

		for (Fund fundObj : funds) {
			contract = new FundContract();
			model.map(fundObj, contract);
			fundContracts.add(contract);
		}

		fundContractRequest.setData(fundContracts);
		fundService.addToQue(fundContractRequest);
		fundResponse.setData(fundContracts);

		return fundResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<FundContract> search(@ModelAttribute FundSearchContract fundSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		FundSearch domain = new FundSearch();
		mapper.map(fundSearchContract, domain);
		FundContract contract = null;
		ModelMapper model = new ModelMapper();
		List<FundContract> fundContracts = new ArrayList<FundContract>();

		Pagination<Fund> funds = fundService.search(domain);

		for (Fund fund : funds.getPagedData()) {
			contract = new FundContract();
			model.map(fund, contract);
			fundContracts.add(contract);
		}

		CommonResponse<FundContract> response = new CommonResponse<>();
		response.setData(fundContracts);
		response.setPage(new PaginationContract(funds));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}
