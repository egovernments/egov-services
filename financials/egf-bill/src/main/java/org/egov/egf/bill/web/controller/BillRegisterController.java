package org.egov.egf.bill.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.PaginationContract;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.service.BillRegisterService;
import org.egov.egf.bill.web.contract.BillRegisterContract;
import org.egov.egf.bill.web.contract.BillRegisterSearchContract;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
import org.egov.egf.bill.web.requests.BillRegisterResponse;
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
@RequestMapping("/billregisters")
public class BillRegisterController {
	
	@Autowired
	private BillRegisterService billRegisterService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public BillRegisterResponse create(@RequestBody BillRegisterRequest billRegisterRequest,  BindingResult errors) {
		
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		BillRegisterResponse billRegisterResponse = new BillRegisterResponse();
		billRegisterResponse.setResponseInfo(getResponseInfo(billRegisterRequest.getRequestInfo()));
		List<BillRegister> billregisters = new ArrayList<>();
		BillRegister billRegister;
		List<BillRegisterContract> billRegisterContracts = new ArrayList<>();
		BillRegisterContract contract;
		
		billRegisterRequest.getRequestInfo().setAction(Constants.ACTION_CREATE);
		for (BillRegisterContract billRegisterContract : billRegisterRequest.getBillRegisters()) {
			billRegister = new BillRegister();
			model.map(billRegisterContract, billRegister);
			billRegister.setCreatedDate(new Date());
			billRegister.setCreatedBy(billRegisterRequest.getRequestInfo().getUserInfo());
			billRegister.setLastModifiedBy(billRegisterRequest.getRequestInfo().getUserInfo());
			billregisters.add(billRegister);
		}
		
		billregisters = billRegisterService.create(billregisters, errors,
				billRegisterRequest.getRequestInfo());
		
		for (BillRegister f : billregisters) {
			contract = new BillRegisterContract();
			contract.setCreatedDate(new Date());
			model.map(f, contract);
			billRegisterContracts.add(contract);
		}
		
		billRegisterResponse.setBillRegisters(billRegisterContracts);
		
		return billRegisterResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public BillRegisterResponse update(
			@RequestBody BillRegisterRequest billRegisterRequest, BindingResult errors) {
		
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		
		billRegisterRequest.getRequestInfo().setAction(Constants.ACTION_UPDATE);
		ModelMapper model = new ModelMapper();
		BillRegisterResponse billRegisterResponse = new BillRegisterResponse();
		List<BillRegister> billregisters = new ArrayList<>();
		billRegisterResponse.setResponseInfo(getResponseInfo(billRegisterRequest.getRequestInfo()));
		BillRegister billRegister;
		BillRegisterContract contract;
		List<BillRegisterContract> billRegisterContracts = new ArrayList<>();
		
		for (BillRegisterContract billRegisterContract : billRegisterRequest
				.getBillRegisters()) {
			billRegister = new BillRegister();
			model.map(billRegisterContract, billRegister);
			billRegister.setLastModifiedBy(billRegisterRequest.getRequestInfo()
					.getUserInfo());
			billRegister.setLastModifiedDate(new Date());
			billregisters.add(billRegister);
		}
		
		billregisters = billRegisterService.update(billregisters, errors,
				billRegisterRequest.getRequestInfo());
		
		for (BillRegister billRegisterObj : billregisters) {
			contract = new BillRegisterContract();
			model.map(billRegisterObj, contract);
			billRegisterObj.setLastModifiedDate(new Date());
			billRegisterContracts.add(contract);
		}
		
		billRegisterResponse.setBillRegisters(billRegisterContracts);
		return billRegisterResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public BillRegisterResponse search(@ModelAttribute BillRegisterSearchContract billRegisterSearchContract, 
			RequestInfo requestInfo, BindingResult errors) {
		
		ModelMapper mapper = new ModelMapper();
		BillRegisterSearch domain = new BillRegisterSearch();
		mapper.map(billRegisterSearchContract, domain);
		BillRegisterContract contract;
		ModelMapper model = new ModelMapper();
		List<BillRegisterContract> billRegisterContracts = new ArrayList<>();
		
		Pagination<BillRegister> billregisters = billRegisterService.search(
				domain, errors);
		
		if (billregisters.getPagedData() != null) {
			for (BillRegister billRegister : billregisters.getPagedData()) {
				contract = new BillRegisterContract();
				model.map(billRegister, contract);
				billRegisterContracts.add(contract);
			}
		}
		
		BillRegisterResponse response = new BillRegisterResponse();
		response.setBillRegisters(billRegisterContracts);
		response.setPage(new PaginationContract(billregisters));
		response.setResponseInfo(getResponseInfo(requestInfo));
		return response;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId())
				.ver(requestInfo.getVer()).resMsgId(requestInfo.getMsgId())
				.resMsgId("placeholder").status("placeholder").build();
	}
}