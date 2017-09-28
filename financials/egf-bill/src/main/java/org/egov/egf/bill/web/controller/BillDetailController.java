package org.egov.egf.bill.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.service.BillDetailService;
import org.egov.egf.bill.web.contract.BillDetailContract;
import org.egov.egf.bill.web.requests.BillDetailRequest;
import org.egov.egf.bill.web.requests.BillDetailResponse;
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
@RequestMapping("/billdetails")
public class BillDetailController {
	
	@Autowired
	private BillDetailService billDetailService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public BillDetailResponse create(@RequestBody BillDetailRequest billDetailRequest,  BindingResult errors) {
		
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		BillDetailResponse billDetailResponse = new BillDetailResponse();
		billDetailResponse.setResponseInfo(getResponseInfo(billDetailRequest.getRequestInfo()));
		List<BillDetail> billregisters = new ArrayList<>();
		BillDetail billDetail;
		List<BillDetailContract> billDetailContracts = new ArrayList<>();
		BillDetailContract contract;
		
		billDetailRequest.getRequestInfo().setAction(Constants.ACTION_CREATE);
		for (BillDetailContract billDetailContract : billDetailRequest.getBillDetails()) {
			billDetail = new BillDetail();
			model.map(billDetailContract, billDetail);
			billDetail.setCreatedDate(new Date());
			billDetail.setCreatedBy(billDetailRequest.getRequestInfo().getUserInfo());
			billDetail.setLastModifiedBy(billDetailRequest.getRequestInfo().getUserInfo());
			billregisters.add(billDetail);
		}
		
		billregisters = billDetailService.create(billregisters, errors,
				billDetailRequest.getRequestInfo());
		
		for (BillDetail f : billregisters) {
			contract = new BillDetailContract();
			contract.setCreatedDate(new Date());
			model.map(f, contract);
			billDetailContracts.add(contract);
		}
		
		billDetailResponse.setBillDetails(billDetailContracts);
		
		return billDetailResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public BillDetailResponse update(
			@RequestBody BillDetailRequest billDetailRequest, BindingResult errors) {
		
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		
		billDetailRequest.getRequestInfo().setAction(Constants.ACTION_UPDATE);
		ModelMapper model = new ModelMapper();
		BillDetailResponse billDetailResponse = new BillDetailResponse();
		List<BillDetail> billregisters = new ArrayList<>();
		billDetailResponse.setResponseInfo(getResponseInfo(billDetailRequest.getRequestInfo()));
		BillDetail billDetail;
		BillDetailContract contract;
		List<BillDetailContract> billDetailContracts = new ArrayList<>();
		
		for (BillDetailContract billDetailContract : billDetailRequest
				.getBillDetails()) {
			billDetail = new BillDetail();
			model.map(billDetailContract, billDetail);
			billDetail.setLastModifiedBy(billDetailRequest.getRequestInfo()
					.getUserInfo());
			billDetail.setLastModifiedDate(new Date());
			billregisters.add(billDetail);
		}
		
		billregisters = billDetailService.update(billregisters, errors,
				billDetailRequest.getRequestInfo());
		
		for (BillDetail billDetailObj : billregisters) {
			contract = new BillDetailContract();
			model.map(billDetailObj, contract);
			billDetailObj.setLastModifiedDate(new Date());
			billDetailContracts.add(contract);
		}
		
		billDetailResponse.setBillDetails(billDetailContracts);
		return billDetailResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId())
				.ver(requestInfo.getVer()).resMsgId(requestInfo.getMsgId())
				.resMsgId("placeholder").status("placeholder").build();
	}
}
