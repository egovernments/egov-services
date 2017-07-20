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
import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentSearch;
import org.egov.egf.instrument.domain.service.InstrumentService;
import org.egov.egf.instrument.web.contract.InstrumentContract;
import org.egov.egf.instrument.web.contract.InstrumentSearchContract;
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
@RequestMapping("/instruments")
public class InstrumentController {

	@Autowired
	private InstrumentService instrumentService;
	
	@GetMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<InstrumentContract> get() {
		
		 InstrumentContract build = InstrumentContract.builder().build();
		 build.setCreatedDate(new Date());
		 CommonResponse<InstrumentContract> response=new CommonResponse<>();
		 List<InstrumentContract> instruments=new ArrayList();
		 instruments.add(build);
		 response.setData(instruments);
		 return response;
		
	}
	

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<InstrumentContract> create(@RequestBody CommonRequest<InstrumentContract> instrumentRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<InstrumentContract> instrumentResponse = new CommonResponse<>();
		instrumentResponse.setResponseInfo(getResponseInfo(instrumentRequest.getRequestInfo()));
		List<Instrument> instruments = new ArrayList<>();
		Instrument instrument;
		List<InstrumentContract> instrumentContracts = new ArrayList<>();
		InstrumentContract contract;

		instrumentRequest.getRequestInfo().setAction("create");

		for (InstrumentContract instrumentContract : instrumentRequest.getData()) {
			instrument = new Instrument();
			model.map(instrumentContract, instrument);
			instrument.setCreatedBy(instrumentRequest.getRequestInfo().getUserInfo());
			instrument.setLastModifiedBy(instrumentRequest.getRequestInfo().getUserInfo());
			instruments.add(instrument);
		}

		instruments = instrumentService.add(instruments, errors);

		for (Instrument f : instruments) {
			contract = new InstrumentContract();
			model.map(f, contract);
			instrumentContracts.add(contract);
		}

		instrumentRequest.setData(instrumentContracts);
		instrumentService.addToQue(instrumentRequest);
		instrumentResponse.setData(instrumentContracts);

		return instrumentResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<InstrumentContract> update(@RequestBody @Valid CommonRequest<InstrumentContract> instrumentContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		instrumentContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<InstrumentContract> instrumentResponse = new CommonResponse<>();
		List<Instrument> instruments = new ArrayList<>();
		instrumentResponse.setResponseInfo(getResponseInfo(instrumentContractRequest.getRequestInfo()));
		Instrument instrument;
		InstrumentContract contract;
		List<InstrumentContract> instrumentContracts = new ArrayList<>();

		for (InstrumentContract instrumentContract : instrumentContractRequest.getData()) {
			instrument = new Instrument();
			model.map(instrumentContract, instrument);
			instrument.setLastModifiedBy(instrumentContractRequest.getRequestInfo().getUserInfo());
			instruments.add(instrument);
		}

		instruments = instrumentService.update(instruments, errors);

		for (Instrument instrumentObj : instruments) {
			contract = new InstrumentContract();
			model.map(instrumentObj, contract);
			instrumentContracts.add(contract);
		}

		instrumentContractRequest.setData(instrumentContracts);
		instrumentService.addToQue(instrumentContractRequest);
		instrumentResponse.setData(instrumentContracts);

		return instrumentResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<InstrumentContract> search(@ModelAttribute InstrumentSearchContract instrumentSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		InstrumentSearch domain = new InstrumentSearch();
		mapper.map(instrumentSearchContract, domain);
		InstrumentContract contract;
		ModelMapper model = new ModelMapper();
		List<InstrumentContract> instrumentContracts = new ArrayList<>();
		Pagination<Instrument> instruments = instrumentService.search(domain);

		for (Instrument instrument : instruments.getPagedData()) {
			contract = new InstrumentContract();
			model.map(instrument, contract);
			instrumentContracts.add(contract);
		}

		CommonResponse<InstrumentContract> response = new CommonResponse<>();
		response.setData(instrumentContracts);
		response.setPage(new PaginationContract(instruments));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}