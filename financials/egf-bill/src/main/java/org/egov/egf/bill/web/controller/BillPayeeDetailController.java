package org.egov.egf.bill.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.service.BillPayeeDetailService;
import org.egov.egf.bill.web.contract.BillPayeeDetailContract;
import org.egov.egf.bill.web.requests.BillPayeeDetailRequest;
import org.egov.egf.bill.web.requests.BillPayeeDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billpayeedetails")
public class BillPayeeDetailController {
	
	@Autowired
	private BillPayeeDetailService billPayeeDetailService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public BillPayeeDetailResponse create(@RequestBody BillPayeeDetailRequest billPayeeDetailRequest,  BindingResult errors) {
		
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		BillPayeeDetailResponse billPayeeDetailResponse = new BillPayeeDetailResponse();
		billPayeeDetailResponse.setResponseInfo(getResponseInfo(billPayeeDetailRequest.getRequestInfo()));
		List<BillPayeeDetail> billregisters = new ArrayList<>();
		BillPayeeDetail billPayeeDetail;
		List<BillPayeeDetailContract> billPayeeDetailContracts = new ArrayList<>();
		BillPayeeDetailContract contract;
		
		billPayeeDetailRequest.getRequestInfo().setAction(Constants.ACTION_CREATE);
		for (BillPayeeDetailContract billPayeeDetailContract : billPayeeDetailRequest.getBillPayeeDetails()) {
			billPayeeDetail = new BillPayeeDetail();
			model.map(billPayeeDetailContract, billPayeeDetail);
			billPayeeDetail.setCreatedDate(new Date());
			billPayeeDetail.setCreatedBy(billPayeeDetailRequest.getRequestInfo().getUserInfo());
			billPayeeDetail.setLastModifiedBy(billPayeeDetailRequest.getRequestInfo().getUserInfo());
			billregisters.add(billPayeeDetail);
		}
		
		billregisters = billPayeeDetailService.create(billregisters, errors,
				billPayeeDetailRequest.getRequestInfo());
		
		for (BillPayeeDetail f : billregisters) {
			contract = new BillPayeeDetailContract();
			contract.setCreatedDate(new Date());
			model.map(f, contract);
			billPayeeDetailContracts.add(contract);
		}
		
		billPayeeDetailResponse.setBillPayeeDetails(billPayeeDetailContracts);
		
		return billPayeeDetailResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public BillPayeeDetailResponse update(
			@RequestBody BillPayeeDetailRequest billPayeeDetailRequest, BindingResult errors) {
		
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		
		billPayeeDetailRequest.getRequestInfo().setAction(Constants.ACTION_UPDATE);
		ModelMapper model = new ModelMapper();
		BillPayeeDetailResponse billPayeeDetailResponse = new BillPayeeDetailResponse();
		List<BillPayeeDetail> billregisters = new ArrayList<>();
		billPayeeDetailResponse.setResponseInfo(getResponseInfo(billPayeeDetailRequest.getRequestInfo()));
		BillPayeeDetail billPayeeDetail;
		BillPayeeDetailContract contract;
		List<BillPayeeDetailContract> billPayeeDetailContracts = new ArrayList<>();
		
		for (BillPayeeDetailContract billPayeeDetailContract : billPayeeDetailRequest
				.getBillPayeeDetails()) {
			billPayeeDetail = new BillPayeeDetail();
			model.map(billPayeeDetailContract, billPayeeDetail);
			billPayeeDetail.setLastModifiedBy(billPayeeDetailRequest.getRequestInfo()
					.getUserInfo());
			billPayeeDetail.setLastModifiedDate(new Date());
			billregisters.add(billPayeeDetail);
		}
		
		billregisters = billPayeeDetailService.update(billregisters, errors,
				billPayeeDetailRequest.getRequestInfo());
		
		for (BillPayeeDetail billPayeeDetailObj : billregisters) {
			contract = new BillPayeeDetailContract();
			model.map(billPayeeDetailObj, contract);
			billPayeeDetailObj.setLastModifiedDate(new Date());
			billPayeeDetailContracts.add(contract);
		}
		
		billPayeeDetailResponse.setBillPayeeDetails(billPayeeDetailContracts);
		return billPayeeDetailResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId())
				.ver(requestInfo.getVer()).resMsgId(requestInfo.getMsgId())
				.resMsgId("placeholder").status("placeholder").build();
	}
}