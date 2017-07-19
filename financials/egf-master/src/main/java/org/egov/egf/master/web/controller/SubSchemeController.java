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
import org.egov.egf.master.domain.model.SubScheme;
import org.egov.egf.master.domain.model.SubSchemeSearch;
import org.egov.egf.master.domain.service.SubSchemeService;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.egov.egf.master.web.contract.SubSchemeSearchContract;
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
@RequestMapping("/subschemes")
public class SubSchemeController {

	@Autowired
	private SubSchemeService subSchemeService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<SubSchemeContract> create(
			@RequestBody @Valid CommonRequest<SubSchemeContract> subSchemeContractRequest, BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<SubSchemeContract> subSchemeResponse = new CommonResponse<>();
		List<SubScheme> subschemes = new ArrayList<>();
		SubScheme subScheme = null;
		List<SubSchemeContract> subSchemeContracts = new ArrayList<SubSchemeContract>();
		SubSchemeContract contract = null;

		subSchemeContractRequest.getRequestInfo().setAction("create");

		for (SubSchemeContract subSchemeContract : subSchemeContractRequest.getData()) {
			subScheme = new SubScheme();
			model.map(subSchemeContract, subScheme);
			subScheme.setCreatedDate(new Date());
			subScheme.setCreatedBy(subSchemeContractRequest.getRequestInfo().getUserInfo());
			subScheme.setLastModifiedBy(subSchemeContractRequest.getRequestInfo().getUserInfo());
			subschemes.add(subScheme);
		}

		subschemes = subSchemeService.add(subschemes, errors);

		for (SubScheme f : subschemes) {
			contract = new SubSchemeContract();
			contract.setCreatedDate(new Date());
			model.map(f, contract);
			subSchemeContracts.add(contract);
		}

		subSchemeContractRequest.setData(subSchemeContracts);
		subSchemeService.addToQue(subSchemeContractRequest);
		subSchemeResponse.setData(subSchemeContracts);

		return subSchemeResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<SubSchemeContract> update(
			@RequestBody @Valid CommonRequest<SubSchemeContract> subSchemeContractRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		subSchemeContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<SubSchemeContract> subSchemeResponse = new CommonResponse<>();
		List<SubScheme> subschemes = new ArrayList<>();
		SubScheme subScheme;
		SubSchemeContract contract;
		List<SubSchemeContract> subSchemeContracts = new ArrayList<>();

		for (SubSchemeContract subSchemeContract : subSchemeContractRequest.getData()) {
			subScheme = new SubScheme();
			model.map(subSchemeContract, subScheme);
			subScheme.setLastModifiedDate(new Date());
			subScheme.setLastModifiedBy(subSchemeContractRequest.getRequestInfo().getUserInfo());
			subschemes.add(subScheme);
		}

		subschemes = subSchemeService.update(subschemes, errors);

		for (SubScheme subSchemeObj : subschemes) {
			contract = new SubSchemeContract();
			model.map(subSchemeObj, contract);
			contract.setLastModifiedDate(new Date());
			subSchemeContracts.add(contract);
		}

		subSchemeContractRequest.setData(subSchemeContracts);
		subSchemeService.addToQue(subSchemeContractRequest);
		subSchemeResponse.setData(subSchemeContracts);

		return subSchemeResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<SubSchemeContract> search(@ModelAttribute SubSchemeSearchContract subSchemeSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		SubSchemeSearch domain = new SubSchemeSearch();
		mapper.map(subSchemeSearchContract, domain);
		SubSchemeContract contract = null;
		ModelMapper model = new ModelMapper();
		List<SubSchemeContract> subSchemeContracts = new ArrayList<SubSchemeContract>();

		Pagination<SubScheme> subschemes = subSchemeService.search(domain);

		for (SubScheme subScheme : subschemes.getPagedData()) {
			contract = new SubSchemeContract();
			model.map(subScheme, contract);
			subSchemeContracts.add(contract);
		}

		CommonResponse<SubSchemeContract> response = new CommonResponse<>();
		response.setData(subSchemeContracts);
		response.setPage(new PaginationContract(subschemes));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}