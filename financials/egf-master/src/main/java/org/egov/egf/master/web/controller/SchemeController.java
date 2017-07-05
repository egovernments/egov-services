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
import org.egov.egf.master.domain.model.Scheme;
import org.egov.egf.master.domain.model.SchemeSearch;
import org.egov.egf.master.domain.service.SchemeService;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SchemeSearchContract;
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
@RequestMapping("/schemes")
public class SchemeController {

	@Autowired
	private SchemeService schemeService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<SchemeContract> create(
			@RequestBody @Valid CommonRequest<SchemeContract> schemeContractRequest, BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<SchemeContract> schemeResponse = new CommonResponse<>();
		List<Scheme> schemes = new ArrayList<>();
		Scheme scheme = null;
		List<SchemeContract> schemeContracts = new ArrayList<SchemeContract>();
		SchemeContract contract = null;

		schemeContractRequest.getRequestInfo().setAction("create");

		for (SchemeContract schemeContract : schemeContractRequest.getData()) {
			scheme = new Scheme();
			model.map(schemeContract, scheme);
			scheme.setCreatedBy(schemeContractRequest.getRequestInfo().getUserInfo());
			scheme.setLastModifiedBy(schemeContractRequest.getRequestInfo().getUserInfo());
			schemes.add(scheme);
		}

		schemes = schemeService.add(schemes, errors);

		for (Scheme f : schemes) {
			contract = new SchemeContract();
			model.map(f, contract);
			schemeContracts.add(contract);
		}

		schemeContractRequest.setData(schemeContracts);
		schemeService.addToQue(schemeContractRequest);
		schemeResponse.setData(schemeContracts);

		return schemeResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<SchemeContract> update(
			@RequestBody @Valid CommonRequest<SchemeContract> schemeContractRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		schemeContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<SchemeContract> schemeResponse = new CommonResponse<>();
		List<Scheme> schemes = new ArrayList<>();
		Scheme scheme = null;
		SchemeContract contract = null;
		List<SchemeContract> schemeContracts = new ArrayList<SchemeContract>();

		for (SchemeContract schemeContract : schemeContractRequest.getData()) {
			scheme = new Scheme();
			model.map(schemeContract, scheme);
			scheme.setLastModifiedBy(schemeContractRequest.getRequestInfo().getUserInfo());
			schemes.add(scheme);
		}

		schemes = schemeService.update(schemes, errors);

		for (Scheme schemeObj : schemes) {
			contract = new SchemeContract();
			model.map(schemeObj, contract);
			schemeContracts.add(contract);
		}

		schemeContractRequest.setData(schemeContracts);
		schemeService.addToQue(schemeContractRequest);
		schemeResponse.setData(schemeContracts);

		return schemeResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<SchemeContract> search(@ModelAttribute SchemeSearchContract schemeSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		SchemeSearch domain = new SchemeSearch();
		mapper.map(schemeSearchContract, domain);
		SchemeContract contract = null;
		ModelMapper model = new ModelMapper();
		List<SchemeContract> schemeContracts = new ArrayList<SchemeContract>();

		Pagination<Scheme> schemes = schemeService.search(domain);

		for (Scheme scheme : schemes.getPagedData()) {
			contract = new SchemeContract();
			model.map(scheme, contract);
			schemeContracts.add(contract);
		}

		CommonResponse<SchemeContract> response = new CommonResponse<>();
		response.setData(schemeContracts);
		response.setPage(new PaginationContract(schemes));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}