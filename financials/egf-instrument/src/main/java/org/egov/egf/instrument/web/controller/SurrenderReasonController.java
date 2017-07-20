package org.egov.egf.instrument.web.controller;

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
import org.egov.egf.instrument.domain.model.SurrenderReason;
import org.egov.egf.instrument.domain.model.SurrenderReasonSearch;
import org.egov.egf.instrument.domain.service.SurrenderReasonService;
import org.egov.egf.instrument.web.contract.SurrenderReasonContract;
import org.egov.egf.instrument.web.contract.SurrenderReasonSearchContract;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/surrenderreasons")
public class SurrenderReasonController {

	@Autowired
	private SurrenderReasonService surrenderReasonService;
	
	@GetMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<SurrenderReasonContract> get() {
		
		 SurrenderReasonContract build = SurrenderReasonContract.builder().build();
		 build.setCreatedDate(new Date());
		 CommonResponse<SurrenderReasonContract> response=new CommonResponse<>();
		 List<SurrenderReasonContract> surrenderreasons=new ArrayList();
		 surrenderreasons.add(build);
		 response.setData(surrenderreasons);
		 return response;
		
	}
	

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<SurrenderReasonContract> create(@RequestBody CommonRequest<SurrenderReasonContract> surrenderReasonRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<SurrenderReasonContract> surrenderReasonResponse = new CommonResponse<>();
		surrenderReasonResponse.setResponseInfo(getResponseInfo(surrenderReasonRequest.getRequestInfo()));
		List<SurrenderReason> surrenderreasons = new ArrayList<>();
		SurrenderReason surrenderReason;
		List<SurrenderReasonContract> surrenderReasonContracts = new ArrayList<>();
		SurrenderReasonContract contract;

		surrenderReasonRequest.getRequestInfo().setAction("create");

		for (SurrenderReasonContract surrenderReasonContract : surrenderReasonRequest.getData()) {
			surrenderReason = new SurrenderReason();
			model.map(surrenderReasonContract, surrenderReason);
			surrenderReason.setCreatedBy(surrenderReasonRequest.getRequestInfo().getUserInfo());
			surrenderReason.setLastModifiedBy(surrenderReasonRequest.getRequestInfo().getUserInfo());
			surrenderreasons.add(surrenderReason);
		}

		surrenderreasons = surrenderReasonService.add(surrenderreasons, errors);

		for (SurrenderReason f : surrenderreasons) {
			contract = new SurrenderReasonContract();
			model.map(f, contract);
			surrenderReasonContracts.add(contract);
		}

		surrenderReasonRequest.setData(surrenderReasonContracts);
		surrenderReasonService.addToQue(surrenderReasonRequest);
		surrenderReasonResponse.setData(surrenderReasonContracts);

		return surrenderReasonResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<SurrenderReasonContract> update(@RequestBody @Valid CommonRequest<SurrenderReasonContract> surrenderReasonContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		surrenderReasonContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<SurrenderReasonContract> surrenderReasonResponse = new CommonResponse<>();
		List<SurrenderReason> surrenderreasons = new ArrayList<>();
		surrenderReasonResponse.setResponseInfo(getResponseInfo(surrenderReasonContractRequest.getRequestInfo()));
		SurrenderReason surrenderReason;
		SurrenderReasonContract contract;
		List<SurrenderReasonContract> surrenderReasonContracts = new ArrayList<>();

		for (SurrenderReasonContract surrenderReasonContract : surrenderReasonContractRequest.getData()) {
			surrenderReason = new SurrenderReason();
			model.map(surrenderReasonContract, surrenderReason);
			surrenderReason.setLastModifiedBy(surrenderReasonContractRequest.getRequestInfo().getUserInfo());
			surrenderreasons.add(surrenderReason);
		}

		surrenderreasons = surrenderReasonService.update(surrenderreasons, errors);

		for (SurrenderReason surrenderReasonObj : surrenderreasons) {
			contract = new SurrenderReasonContract();
			model.map(surrenderReasonObj, contract);
			surrenderReasonContracts.add(contract);
		}

		surrenderReasonContractRequest.setData(surrenderReasonContracts);
		surrenderReasonService.addToQue(surrenderReasonContractRequest);
		surrenderReasonResponse.setData(surrenderReasonContracts);

		return surrenderReasonResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<SurrenderReasonContract> search(@ModelAttribute SurrenderReasonSearchContract surrenderReasonSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		SurrenderReasonSearch domain = new SurrenderReasonSearch();
		mapper.map(surrenderReasonSearchContract, domain);
		SurrenderReasonContract contract;
		ModelMapper model = new ModelMapper();
		List<SurrenderReasonContract> surrenderReasonContracts = new ArrayList<>();
		Pagination<SurrenderReason> surrenderreasons = surrenderReasonService.search(domain);

		for (SurrenderReason surrenderReason : surrenderreasons.getPagedData()) {
			contract = new SurrenderReasonContract();
			model.map(surrenderReason, contract);
			surrenderReasonContracts.add(contract);
		}

		CommonResponse<SurrenderReasonContract> response = new CommonResponse<>();
		response.setData(surrenderReasonContracts);
		response.setPage(new PaginationContract(surrenderreasons));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}