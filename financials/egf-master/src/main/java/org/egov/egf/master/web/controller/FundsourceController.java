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
import org.egov.egf.master.domain.model.Fundsource;
import org.egov.egf.master.domain.model.FundsourceSearch;
import org.egov.egf.master.domain.service.FundsourceService;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.egov.egf.master.web.contract.FundsourceSearchContract;
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
@RequestMapping("/fundsources")
public class FundsourceController {

	@Autowired
	private FundsourceService fundsourceService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<FundsourceContract> create(
			@RequestBody @Valid CommonRequest<FundsourceContract> fundsourceContractRequest, BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<FundsourceContract> fundsourceResponse = new CommonResponse<>();
		List<Fundsource> fundsources = new ArrayList<>();
		Fundsource fundsource = null;
		List<FundsourceContract> fundsourceContracts = new ArrayList<FundsourceContract>();
		FundsourceContract contract = null;

		fundsourceContractRequest.getRequestInfo().setAction("create");

		for (FundsourceContract fundsourceContract : fundsourceContractRequest.getData()) {
			fundsource = new Fundsource();
			model.map(fundsourceContract, fundsource);
			fundsource.setCreatedBy(fundsourceContractRequest.getRequestInfo().getUserInfo());
			fundsource.setLastModifiedBy(fundsourceContractRequest.getRequestInfo().getUserInfo());
			fundsources.add(fundsource);
		}

		fundsources = fundsourceService.add(fundsources, errors);

		for (Fundsource f : fundsources) {
			contract = new FundsourceContract();
			model.map(f, contract);
			fundsourceContracts.add(contract);
		}

		fundsourceContractRequest.setData(fundsourceContracts);
		fundsourceService.addToQue(fundsourceContractRequest);
		fundsourceResponse.setData(fundsourceContracts);

		return fundsourceResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<FundsourceContract> update(
			@RequestBody @Valid CommonRequest<FundsourceContract> fundsourceContractRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<FundsourceContract> fundsourceResponse = new CommonResponse<>();
		List<Fundsource> fundsources = new ArrayList<>();
		Fundsource fundsource = null;
		FundsourceContract contract = null;
		List<FundsourceContract> fundsourceContracts = new ArrayList<FundsourceContract>();

		for (FundsourceContract fundsourceContract : fundsourceContractRequest.getData()) {
			fundsource = new Fundsource();
			model.map(fundsourceContract, fundsource);
			fundsource.setLastModifiedBy(fundsourceContractRequest.getRequestInfo().getUserInfo());
			fundsources.add(fundsource);
		}

		fundsources = fundsourceService.update(fundsources, errors);

		for (Fundsource fundsourceObj : fundsources) {
			contract = new FundsourceContract();
			model.map(fundsourceObj, contract);
			fundsourceContracts.add(contract);
		}

		fundsourceContractRequest.setData(fundsourceContracts);
		fundsourceService.addToQue(fundsourceContractRequest);
		fundsourceResponse.setData(fundsourceContracts);

		return fundsourceResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<FundsourceContract> search(@ModelAttribute FundsourceSearchContract fundsourceSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		FundsourceSearch domain = new FundsourceSearch();
		mapper.map(fundsourceSearchContract, domain);
		FundsourceContract contract = null;
		ModelMapper model = new ModelMapper();
		List<FundsourceContract> fundsourceContracts = new ArrayList<FundsourceContract>();

		Pagination<Fundsource> fundsources = fundsourceService.search(domain);

		for (Fundsource fundsource : fundsources.getPagedData()) {
			contract = new FundsourceContract();
			model.map(fundsource, contract);
			fundsourceContracts.add(contract);
		}

		CommonResponse<FundsourceContract> response = new CommonResponse<>();
		response.setData(fundsourceContracts);
		response.setPage(new PaginationContract(fundsources));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}