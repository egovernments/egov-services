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
import org.egov.egf.master.domain.model.Function;
import org.egov.egf.master.domain.model.FunctionSearch;
import org.egov.egf.master.domain.service.FunctionService;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionSearchContract;
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
@RequestMapping("/functions")
public class FunctionController {

	@Autowired
	private FunctionService functionService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<FunctionContract> create(
			@RequestBody @Valid CommonRequest<FunctionContract> functionContractRequest, BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<FunctionContract> functionResponse = new CommonResponse<>();
		functionResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
		List<Function> functions = new ArrayList<>();
		Function function;
		List<FunctionContract> functionContracts = new ArrayList<>();
		FunctionContract contract;

		functionContractRequest.getRequestInfo().setAction("create");

		for (FunctionContract functionContract : functionContractRequest.getData()) {
			function = new Function();
			model.map(functionContract, function);
			function.setCreatedBy(functionContractRequest.getRequestInfo().getUserInfo());
			function.setLastModifiedBy(functionContractRequest.getRequestInfo().getUserInfo());
			functions.add(function);
		}

		functions = functionService.add(functions, errors);

		for (Function f : functions) {
			contract = new FunctionContract();
			model.map(f, contract);
			functionContracts.add(contract);
		}

		functionContractRequest.setData(functionContracts);
		functionService.addToQue(functionContractRequest);
		functionResponse.setData(functionContracts);

		return functionResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<FunctionContract> update(
			@RequestBody @Valid CommonRequest<FunctionContract> functionContractRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		functionContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<FunctionContract> functionResponse = new CommonResponse<>();
		functionResponse.setResponseInfo(getResponseInfo(functionContractRequest.getRequestInfo()));
		List<Function> functions = new ArrayList<>();
		Function function;
		FunctionContract contract;
		List<FunctionContract> functionContracts = new ArrayList<>();

		for (FunctionContract functionContract : functionContractRequest.getData()) {
			function = new Function();
			model.map(functionContract, function);
			function.setLastModifiedBy(functionContractRequest.getRequestInfo().getUserInfo());
			functions.add(function);
		}

		functions = functionService.update(functions, errors);

		for (Function functionObj : functions) {
			contract = new FunctionContract();
			model.map(functionObj, contract);
			functionContracts.add(contract);
		}

		functionContractRequest.setData(functionContracts);
		functionService.addToQue(functionContractRequest);
		functionResponse.setData(functionContracts);

		return functionResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<FunctionContract> search(@ModelAttribute FunctionSearchContract functionSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		FunctionSearch domain = new FunctionSearch();
		mapper.map(functionSearchContract, domain);
		FunctionContract contract;
		ModelMapper model = new ModelMapper();
		List<FunctionContract> functionContracts = new ArrayList<>();

		Pagination<Function> functions = functionService.search(domain);

		for (Function function : functions.getPagedData()) {
			contract = new FunctionContract();
			model.map(function, contract);
			functionContracts.add(contract);
		}

		CommonResponse<FunctionContract> response = new CommonResponse<>();
		response.setData(functionContracts);
		response.setPage(new PaginationContract(functions));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}