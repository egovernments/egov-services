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
import org.egov.egf.instrument.domain.model.InstrumentStatus;
import org.egov.egf.instrument.domain.model.InstrumentStatusSearch;
import org.egov.egf.instrument.domain.service.InstrumentStatusService;
import org.egov.egf.instrument.web.contract.InstrumentStatusContract;
import org.egov.egf.instrument.web.contract.InstrumentStatusSearchContract;
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
@RequestMapping("/instrumentstatuses")
public class InstrumentStatusController {

	@Autowired
	private InstrumentStatusService instrumentStatusService;
	
	@GetMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<InstrumentStatusContract> get() {
		
		 InstrumentStatusContract build = InstrumentStatusContract.builder().build();
		 build.setCreatedDate(new Date());
		 CommonResponse<InstrumentStatusContract> response=new CommonResponse<>();
		 List<InstrumentStatusContract> instrumentstatuses=new ArrayList();
		 instrumentstatuses.add(build);
		 response.setData(instrumentstatuses);
		 return response;
		
	}
	

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<InstrumentStatusContract> create(@RequestBody CommonRequest<InstrumentStatusContract> instrumentStatusRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<InstrumentStatusContract> instrumentStatusResponse = new CommonResponse<>();
		instrumentStatusResponse.setResponseInfo(getResponseInfo(instrumentStatusRequest.getRequestInfo()));
		List<InstrumentStatus> instrumentstatuses = new ArrayList<>();
		InstrumentStatus instrumentStatus;
		List<InstrumentStatusContract> instrumentStatusContracts = new ArrayList<>();
		InstrumentStatusContract contract;

		instrumentStatusRequest.getRequestInfo().setAction("create");

		for (InstrumentStatusContract instrumentStatusContract : instrumentStatusRequest.getData()) {
			instrumentStatus = new InstrumentStatus();
			model.map(instrumentStatusContract, instrumentStatus);
			instrumentStatus.setCreatedBy(instrumentStatusRequest.getRequestInfo().getUserInfo());
			instrumentStatus.setLastModifiedBy(instrumentStatusRequest.getRequestInfo().getUserInfo());
			instrumentstatuses.add(instrumentStatus);
		}

		instrumentstatuses = instrumentStatusService.add(instrumentstatuses, errors);

		for (InstrumentStatus f : instrumentstatuses) {
			contract = new InstrumentStatusContract();
			model.map(f, contract);
			instrumentStatusContracts.add(contract);
		}

		instrumentStatusRequest.setData(instrumentStatusContracts);
		instrumentStatusService.addToQue(instrumentStatusRequest);
		instrumentStatusResponse.setData(instrumentStatusContracts);

		return instrumentStatusResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<InstrumentStatusContract> update(@RequestBody @Valid CommonRequest<InstrumentStatusContract> instrumentStatusContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		instrumentStatusContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<InstrumentStatusContract> instrumentStatusResponse = new CommonResponse<>();
		List<InstrumentStatus> instrumentstatuses = new ArrayList<>();
		instrumentStatusResponse.setResponseInfo(getResponseInfo(instrumentStatusContractRequest.getRequestInfo()));
		InstrumentStatus instrumentStatus;
		InstrumentStatusContract contract;
		List<InstrumentStatusContract> instrumentStatusContracts = new ArrayList<>();

		for (InstrumentStatusContract instrumentStatusContract : instrumentStatusContractRequest.getData()) {
			instrumentStatus = new InstrumentStatus();
			model.map(instrumentStatusContract, instrumentStatus);
			instrumentStatus.setLastModifiedBy(instrumentStatusContractRequest.getRequestInfo().getUserInfo());
			instrumentstatuses.add(instrumentStatus);
		}

		instrumentstatuses = instrumentStatusService.update(instrumentstatuses, errors);

		for (InstrumentStatus instrumentStatusObj : instrumentstatuses) {
			contract = new InstrumentStatusContract();
			model.map(instrumentStatusObj, contract);
			instrumentStatusContracts.add(contract);
		}

		instrumentStatusContractRequest.setData(instrumentStatusContracts);
		instrumentStatusService.addToQue(instrumentStatusContractRequest);
		instrumentStatusResponse.setData(instrumentStatusContracts);

		return instrumentStatusResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<InstrumentStatusContract> search(@ModelAttribute InstrumentStatusSearchContract instrumentStatusSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		InstrumentStatusSearch domain = new InstrumentStatusSearch();
		mapper.map(instrumentStatusSearchContract, domain);
		InstrumentStatusContract contract;
		ModelMapper model = new ModelMapper();
		List<InstrumentStatusContract> instrumentStatusContracts = new ArrayList<>();
		Pagination<InstrumentStatus> instrumentstatuses = instrumentStatusService.search(domain);

		for (InstrumentStatus instrumentStatus : instrumentstatuses.getPagedData()) {
			contract = new InstrumentStatusContract();
			model.map(instrumentStatus, contract);
			instrumentStatusContracts.add(contract);
		}

		CommonResponse<InstrumentStatusContract> response = new CommonResponse<>();
		response.setData(instrumentStatusContracts);
		response.setPage(new PaginationContract(instrumentstatuses));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}