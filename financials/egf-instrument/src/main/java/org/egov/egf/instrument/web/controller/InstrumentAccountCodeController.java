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
import org.egov.egf.instrument.domain.model.InstrumentAccountCode;
import org.egov.egf.instrument.domain.model.InstrumentAccountCodeSearch;
import org.egov.egf.instrument.domain.service.InstrumentAccountCodeService;
import org.egov.egf.instrument.web.contract.InstrumentAccountCodeContract;
import org.egov.egf.instrument.web.contract.InstrumentAccountCodeSearchContract;
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
@RequestMapping("/instrumentaccountcodes")
public class InstrumentAccountCodeController {

	@Autowired
	private InstrumentAccountCodeService instrumentAccountCodeService;
	
	@GetMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<InstrumentAccountCodeContract> get() {
		
		 InstrumentAccountCodeContract build = InstrumentAccountCodeContract.builder().build();
		 build.setCreatedDate(new Date());
		 CommonResponse<InstrumentAccountCodeContract> response=new CommonResponse<>();
		 List<InstrumentAccountCodeContract> instrumentaccountcodes=new ArrayList();
		 instrumentaccountcodes.add(build);
		 response.setData(instrumentaccountcodes);
		 return response;
		
	}
	

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<InstrumentAccountCodeContract> create(@RequestBody CommonRequest<InstrumentAccountCodeContract> instrumentAccountCodeRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<InstrumentAccountCodeContract> instrumentAccountCodeResponse = new CommonResponse<>();
		instrumentAccountCodeResponse.setResponseInfo(getResponseInfo(instrumentAccountCodeRequest.getRequestInfo()));
		List<InstrumentAccountCode> instrumentaccountcodes = new ArrayList<>();
		InstrumentAccountCode instrumentAccountCode;
		List<InstrumentAccountCodeContract> instrumentAccountCodeContracts = new ArrayList<>();
		InstrumentAccountCodeContract contract;

		instrumentAccountCodeRequest.getRequestInfo().setAction("create");

		for (InstrumentAccountCodeContract instrumentAccountCodeContract : instrumentAccountCodeRequest.getData()) {
			instrumentAccountCode = new InstrumentAccountCode();
			model.map(instrumentAccountCodeContract, instrumentAccountCode);
			instrumentAccountCode.setCreatedBy(instrumentAccountCodeRequest.getRequestInfo().getUserInfo());
			instrumentAccountCode.setLastModifiedBy(instrumentAccountCodeRequest.getRequestInfo().getUserInfo());
			instrumentaccountcodes.add(instrumentAccountCode);
		}

		instrumentaccountcodes = instrumentAccountCodeService.add(instrumentaccountcodes, errors);

		for (InstrumentAccountCode f : instrumentaccountcodes) {
			contract = new InstrumentAccountCodeContract();
			model.map(f, contract);
			instrumentAccountCodeContracts.add(contract);
		}

		instrumentAccountCodeRequest.setData(instrumentAccountCodeContracts);
		instrumentAccountCodeService.addToQue(instrumentAccountCodeRequest);
		instrumentAccountCodeResponse.setData(instrumentAccountCodeContracts);

		return instrumentAccountCodeResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<InstrumentAccountCodeContract> update(@RequestBody @Valid CommonRequest<InstrumentAccountCodeContract> instrumentAccountCodeContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		instrumentAccountCodeContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<InstrumentAccountCodeContract> instrumentAccountCodeResponse = new CommonResponse<>();
		List<InstrumentAccountCode> instrumentaccountcodes = new ArrayList<>();
		instrumentAccountCodeResponse.setResponseInfo(getResponseInfo(instrumentAccountCodeContractRequest.getRequestInfo()));
		InstrumentAccountCode instrumentAccountCode;
		InstrumentAccountCodeContract contract;
		List<InstrumentAccountCodeContract> instrumentAccountCodeContracts = new ArrayList<>();

		for (InstrumentAccountCodeContract instrumentAccountCodeContract : instrumentAccountCodeContractRequest.getData()) {
			instrumentAccountCode = new InstrumentAccountCode();
			model.map(instrumentAccountCodeContract, instrumentAccountCode);
			instrumentAccountCode.setLastModifiedBy(instrumentAccountCodeContractRequest.getRequestInfo().getUserInfo());
			instrumentaccountcodes.add(instrumentAccountCode);
		}

		instrumentaccountcodes = instrumentAccountCodeService.update(instrumentaccountcodes, errors);

		for (InstrumentAccountCode instrumentAccountCodeObj : instrumentaccountcodes) {
			contract = new InstrumentAccountCodeContract();
			model.map(instrumentAccountCodeObj, contract);
			instrumentAccountCodeContracts.add(contract);
		}

		instrumentAccountCodeContractRequest.setData(instrumentAccountCodeContracts);
		instrumentAccountCodeService.addToQue(instrumentAccountCodeContractRequest);
		instrumentAccountCodeResponse.setData(instrumentAccountCodeContracts);

		return instrumentAccountCodeResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<InstrumentAccountCodeContract> search(@ModelAttribute InstrumentAccountCodeSearchContract instrumentAccountCodeSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		InstrumentAccountCodeSearch domain = new InstrumentAccountCodeSearch();
		mapper.map(instrumentAccountCodeSearchContract, domain);
		InstrumentAccountCodeContract contract;
		ModelMapper model = new ModelMapper();
		List<InstrumentAccountCodeContract> instrumentAccountCodeContracts = new ArrayList<>();
		Pagination<InstrumentAccountCode> instrumentaccountcodes = instrumentAccountCodeService.search(domain);

		for (InstrumentAccountCode instrumentAccountCode : instrumentaccountcodes.getPagedData()) {
			contract = new InstrumentAccountCodeContract();
			model.map(instrumentAccountCode, contract);
			instrumentAccountCodeContracts.add(contract);
		}

		CommonResponse<InstrumentAccountCodeContract> response = new CommonResponse<>();
		response.setData(instrumentAccountCodeContracts);
		response.setPage(new PaginationContract(instrumentaccountcodes));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}