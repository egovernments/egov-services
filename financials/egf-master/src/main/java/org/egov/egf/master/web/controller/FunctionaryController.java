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
import org.egov.egf.master.domain.model.Functionary;
import org.egov.egf.master.domain.model.FunctionarySearch;
import org.egov.egf.master.domain.service.FunctionaryService;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FunctionarySearchContract;
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
@RequestMapping("/functionaries")
public class FunctionaryController {

	@Autowired
	private FunctionaryService functionaryService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<FunctionaryContract> create(
			@RequestBody @Valid CommonRequest<FunctionaryContract> functionaryContractRequest, BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<FunctionaryContract> functionaryResponse = new CommonResponse<>();
		List<Functionary> functionaries = new ArrayList<>();
		Functionary functionary = null;
		List<FunctionaryContract> functionaryContracts = new ArrayList<FunctionaryContract>();
		FunctionaryContract contract = null;

		functionaryContractRequest.getRequestInfo().setAction("create");

		for (FunctionaryContract functionaryContract : functionaryContractRequest.getData()) {
			functionary = new Functionary();
			model.map(functionaryContract, functionary);
			functionary.setCreatedBy(functionaryContractRequest.getRequestInfo().getUserInfo());
			functionary.setLastModifiedBy(functionaryContractRequest.getRequestInfo().getUserInfo());
			functionaries.add(functionary);
		}

		functionaries = functionaryService.add(functionaries, errors);

		for (Functionary f : functionaries) {
			contract = new FunctionaryContract();
			model.map(f, contract);
			functionaryContracts.add(contract);
		}

		functionaryContractRequest.setData(functionaryContracts);
		functionaryService.addToQue(functionaryContractRequest);
		functionaryResponse.setData(functionaryContracts);

		return functionaryResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<FunctionaryContract> update(
			@RequestBody @Valid CommonRequest<FunctionaryContract> functionaryContractRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		functionaryContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<FunctionaryContract> functionaryResponse = new CommonResponse<>();
		List<Functionary> functionaries = new ArrayList<>();
		Functionary functionary;
		FunctionaryContract contract;
		List<FunctionaryContract> functionaryContracts = new ArrayList<>();

		for (FunctionaryContract functionaryContract : functionaryContractRequest.getData()) {
			functionary = new Functionary();
			model.map(functionaryContract, functionary);
			functionary.setLastModifiedBy(functionaryContractRequest.getRequestInfo().getUserInfo());
			functionaries.add(functionary);
		}

		functionaries = functionaryService.update(functionaries, errors);

		for (Functionary functionaryObj : functionaries) {
			contract = new FunctionaryContract();
			model.map(functionaryObj, contract);
			functionaryContracts.add(contract);
		}

		functionaryContractRequest.setData(functionaryContracts);
		functionaryService.addToQue(functionaryContractRequest);
		functionaryResponse.setData(functionaryContracts);

		return functionaryResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<FunctionaryContract> search(
			@ModelAttribute FunctionarySearchContract functionarySearchContract, RequestInfo requestInfo,
			BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		FunctionarySearch domain = new FunctionarySearch();
		mapper.map(functionarySearchContract, domain);
		FunctionaryContract contract = null;
		ModelMapper model = new ModelMapper();
		List<FunctionaryContract> functionaryContracts = new ArrayList<FunctionaryContract>();

		Pagination<Functionary> functionaries = functionaryService.search(domain);

		for (Functionary functionary : functionaries.getPagedData()) {
			contract = new FunctionaryContract();
			model.map(functionary, contract);
			functionaryContracts.add(contract);
		}

		CommonResponse<FunctionaryContract> response = new CommonResponse<>();
		response.setData(functionaryContracts);
		response.setPage(new PaginationContract(functionaries));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}