package org.egov.egf.instrument.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.PaginationContract;
import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.InstrumentTypeSearch;
import org.egov.egf.instrument.domain.service.InstrumentTypeService;
import org.egov.egf.instrument.web.contract.InstrumentTypeContract;
import org.egov.egf.instrument.web.contract.InstrumentTypeSearchContract;
import org.egov.egf.instrument.web.requests.InstrumentTypeRequest;
import org.egov.egf.instrument.web.requests.InstrumentTypeResponse;
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
@RequestMapping("/instrumenttypes")
public class InstrumentTypeController {

	@Autowired
	private InstrumentTypeService instrumentTypeService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public InstrumentTypeResponse create(@RequestBody InstrumentTypeRequest instrumentTypeRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		InstrumentTypeResponse instrumentTypeResponse = new InstrumentTypeResponse();
		instrumentTypeResponse.setResponseInfo(getResponseInfo(instrumentTypeRequest.getRequestInfo()));
		List<InstrumentType> instrumenttypes = new ArrayList<>();
		InstrumentType instrumentType;
		List<InstrumentTypeContract> instrumentTypeContracts = new ArrayList<>();
		InstrumentTypeContract contract;

		instrumentTypeRequest.getRequestInfo().setAction("create");

		for (InstrumentTypeContract instrumentTypeContract : instrumentTypeRequest.getInstrumentTypes()) {
			instrumentType = new InstrumentType();
			model.map(instrumentTypeContract, instrumentType);
			instrumentType.setCreatedDate(new Date());
			instrumentType.setCreatedBy(instrumentTypeRequest.getRequestInfo().getUserInfo());
			instrumentType.setLastModifiedBy(instrumentTypeRequest.getRequestInfo().getUserInfo());
			instrumenttypes.add(instrumentType);
		}

		instrumenttypes = instrumentTypeService.add(instrumenttypes, errors);

		for (InstrumentType f : instrumenttypes) {
			contract = new InstrumentTypeContract();
			contract.setCreatedDate(new Date());
			model.map(f, contract);
			instrumentTypeContracts.add(contract);
		}

		instrumentTypeRequest.setInstrumentTypes(instrumentTypeContracts);
		instrumentTypeService.addToQue(instrumentTypeRequest);
		instrumentTypeResponse.setInstrumentTypes(instrumentTypeContracts);

		return instrumentTypeResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public InstrumentTypeResponse update(@RequestBody InstrumentTypeRequest instrumentTypeRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		instrumentTypeRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		InstrumentTypeResponse instrumentTypeResponse = new InstrumentTypeResponse();
		List<InstrumentType> instrumenttypes = new ArrayList<>();
		instrumentTypeResponse.setResponseInfo(getResponseInfo(instrumentTypeRequest.getRequestInfo()));
		InstrumentType instrumentType;
		InstrumentTypeContract contract;
		List<InstrumentTypeContract> instrumentTypeContracts = new ArrayList<>();

		for (InstrumentTypeContract instrumentTypeContract : instrumentTypeRequest.getInstrumentTypes()) {
			instrumentType = new InstrumentType();
			model.map(instrumentTypeContract, instrumentType);
			instrumentType.setLastModifiedBy(instrumentTypeRequest.getRequestInfo().getUserInfo());
			instrumentType.setLastModifiedDate(new Date());
			instrumenttypes.add(instrumentType);
		}

		instrumenttypes = instrumentTypeService.update(instrumenttypes, errors);

		for (InstrumentType instrumentTypeObj : instrumenttypes) {
			contract = new InstrumentTypeContract();
			model.map(instrumentTypeObj, contract);
			instrumentTypeObj.setLastModifiedDate(new Date());
			instrumentTypeContracts.add(contract);
		}

		instrumentTypeRequest.setInstrumentTypes(instrumentTypeContracts);
		instrumentTypeService.addToQue(instrumentTypeRequest);
		instrumentTypeResponse.setInstrumentTypes(instrumentTypeContracts);

		return instrumentTypeResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public InstrumentTypeResponse search(@ModelAttribute InstrumentTypeSearchContract instrumentTypeSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		InstrumentTypeSearch domain = new InstrumentTypeSearch();
		mapper.map(instrumentTypeSearchContract, domain);
		InstrumentTypeContract contract;
		ModelMapper model = new ModelMapper();
		List<InstrumentTypeContract> instrumentTypeContracts = new ArrayList<>();
		Pagination<InstrumentType> instrumenttypes = instrumentTypeService.search(domain);

		if (instrumenttypes.getPagedData() != null) {
			for (InstrumentType instrumentType : instrumenttypes.getPagedData()) {
				contract = new InstrumentTypeContract();
				model.map(instrumentType, contract);
				instrumentTypeContracts.add(contract);
			}
		}

		InstrumentTypeResponse response = new InstrumentTypeResponse();
		response.setInstrumentTypes(instrumentTypeContracts);
		response.setPage(new PaginationContract(instrumenttypes));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}