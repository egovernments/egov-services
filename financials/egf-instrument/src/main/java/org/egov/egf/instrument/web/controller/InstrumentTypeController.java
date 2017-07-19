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
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.InstrumentTypeSearch;
import org.egov.egf.instrument.domain.service.InstrumentTypeService;
import org.egov.egf.instrument.web.contract.InstrumentTypeContract;
import org.egov.egf.instrument.web.contract.InstrumentTypeSearchContract;
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
@RequestMapping("/instrumenttypes")
public class InstrumentTypeController {

	@Autowired
	private InstrumentTypeService instrumentTypeService;
	
	@GetMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<InstrumentTypeContract> get() {
		
		 InstrumentTypeContract build = InstrumentTypeContract.builder().build();
		 build.setCreatedDate(new Date());
		 CommonResponse<InstrumentTypeContract> response=new CommonResponse<>();
		 List<InstrumentTypeContract> instrumenttypes=new ArrayList();
		 instrumenttypes.add(build);
		 response.setData(instrumenttypes);
		 return response;
		
	}
	

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<InstrumentTypeContract> create(@RequestBody CommonRequest<InstrumentTypeContract> instrumentTypeRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<InstrumentTypeContract> instrumentTypeResponse = new CommonResponse<>();
		instrumentTypeResponse.setResponseInfo(getResponseInfo(instrumentTypeRequest.getRequestInfo()));
		List<InstrumentType> instrumenttypes = new ArrayList<>();
		InstrumentType instrumentType;
		List<InstrumentTypeContract> instrumentTypeContracts = new ArrayList<>();
		InstrumentTypeContract contract;

		instrumentTypeRequest.getRequestInfo().setAction("create");

		for (InstrumentTypeContract instrumentTypeContract : instrumentTypeRequest.getData()) {
			instrumentType = new InstrumentType();
			model.map(instrumentTypeContract, instrumentType);
			instrumentType.setCreatedBy(instrumentTypeRequest.getRequestInfo().getUserInfo());
			instrumentType.setLastModifiedBy(instrumentTypeRequest.getRequestInfo().getUserInfo());
			instrumenttypes.add(instrumentType);
		}

		instrumenttypes = instrumentTypeService.add(instrumenttypes, errors);

		for (InstrumentType f : instrumenttypes) {
			contract = new InstrumentTypeContract();
			model.map(f, contract);
			instrumentTypeContracts.add(contract);
		}

		instrumentTypeRequest.setData(instrumentTypeContracts);
		instrumentTypeService.addToQue(instrumentTypeRequest);
		instrumentTypeResponse.setData(instrumentTypeContracts);

		return instrumentTypeResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<InstrumentTypeContract> update(@RequestBody @Valid CommonRequest<InstrumentTypeContract> instrumentTypeContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		instrumentTypeContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<InstrumentTypeContract> instrumentTypeResponse = new CommonResponse<>();
		List<InstrumentType> instrumenttypes = new ArrayList<>();
		instrumentTypeResponse.setResponseInfo(getResponseInfo(instrumentTypeContractRequest.getRequestInfo()));
		InstrumentType instrumentType;
		InstrumentTypeContract contract;
		List<InstrumentTypeContract> instrumentTypeContracts = new ArrayList<>();

		for (InstrumentTypeContract instrumentTypeContract : instrumentTypeContractRequest.getData()) {
			instrumentType = new InstrumentType();
			model.map(instrumentTypeContract, instrumentType);
			instrumentType.setLastModifiedBy(instrumentTypeContractRequest.getRequestInfo().getUserInfo());
			instrumenttypes.add(instrumentType);
		}

		instrumenttypes = instrumentTypeService.update(instrumenttypes, errors);

		for (InstrumentType instrumentTypeObj : instrumenttypes) {
			contract = new InstrumentTypeContract();
			model.map(instrumentTypeObj, contract);
			instrumentTypeContracts.add(contract);
		}

		instrumentTypeContractRequest.setData(instrumentTypeContracts);
		instrumentTypeService.addToQue(instrumentTypeContractRequest);
		instrumentTypeResponse.setData(instrumentTypeContracts);

		return instrumentTypeResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<InstrumentTypeContract> search(@ModelAttribute InstrumentTypeSearchContract instrumentTypeSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		InstrumentTypeSearch domain = new InstrumentTypeSearch();
		mapper.map(instrumentTypeSearchContract, domain);
		InstrumentTypeContract contract;
		ModelMapper model = new ModelMapper();
		List<InstrumentTypeContract> instrumentTypeContracts = new ArrayList<>();
		Pagination<InstrumentType> instrumenttypes = instrumentTypeService.search(domain);

		for (InstrumentType instrumentType : instrumenttypes.getPagedData()) {
			contract = new InstrumentTypeContract();
			model.map(instrumentType, contract);
			instrumentTypeContracts.add(contract);
		}

		CommonResponse<InstrumentTypeContract> response = new CommonResponse<>();
		response.setData(instrumentTypeContracts);
		response.setPage(new PaginationContract(instrumenttypes));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}