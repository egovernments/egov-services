package org.egov.demand.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.demand.domain.service.BillService;
import org.egov.demand.web.contract.BillAddlInfo;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.BillResponse;
import org.egov.demand.web.contract.RequestInfo;
import org.egov.demand.web.contract.ResponseInfo;
import org.egov.demand.web.contract.factory.ResponseInfoFactory;
import org.egov.demand.web.errorhandler.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bill")
public class BillController {
	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private ErrorHandler errHandler;

	@Autowired
	private BillService billService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody @Valid BillRequest billRequest, BindingResult bindingResult) {
		BillAddlInfo bill = null;
		List<BillAddlInfo> bills = new ArrayList<BillAddlInfo>();
		List<Long> demandIds = null;
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult, billRequest.getRequestInfo());
		}
		if ((billRequest.getDemandId() == null || billRequest.getDemandId().size() == 0)
				&& billRequest.getBillAddlInfo() == null) {
			return errHandler.getErrorResponseEntityForMissingRequestInfo(billRequest.getRequestInfo());
		}
		try {
			demandIds = billRequest.getDemandId();
			for (Long demandId : demandIds) {
				bill = billService.createBill(demandId, billRequest.getBillAddlInfo());
				bills.add(bill);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getSuccessResponse(bills, billRequest.getRequestInfo());
	}

	/**
	 * Populate Response object and return Demand List
	 * 
	 * @param employeesList
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(List<BillAddlInfo> bills, RequestInfo requestInfo) {
		BillResponse billResponse = new BillResponse();
		billResponse.setBills(bills);

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		billResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BillResponse>(billResponse, HttpStatus.OK);
	}

	private ResponseEntity<?> getSuccessResponseForSearch(List<BillAddlInfo> bills, RequestInfo requestInfo) {
		BillResponse billResponse = new BillResponse();
		billResponse.setBills(bills);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		billResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BillResponse>(billResponse, HttpStatus.OK);
	}
}
