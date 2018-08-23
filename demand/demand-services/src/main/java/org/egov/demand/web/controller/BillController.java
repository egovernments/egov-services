package org.egov.demand.web.controller;

import org.apache.log4j.Logger;
import org.egov.demand.domain.service.BillService;
import org.egov.demand.persistence.entity.EgBill;
import org.egov.demand.persistence.repository.BillRepository;
import org.egov.demand.utils.CollectionUtils;
import org.egov.demand.web.contract.BillInfo;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.BillResponse;
import org.egov.demand.web.contract.BillSearchCriteria;
import org.egov.demand.web.contract.RequestInfo;
import org.egov.demand.web.contract.ResponseInfo;
import org.egov.demand.web.contract.factory.ResponseInfoFactory;
import org.egov.demand.web.errorhandler.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("demand/bill")
public class BillController {

	private static final Logger LOGGER = Logger.getLogger(BillController.class);

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private ErrorHandler errHandler;

	@Autowired
	private BillService billService;

	@Autowired
	private BillRepository billRepository;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid BillSearchCriteria billSearchCriteria,
									@RequestBody RequestInfo requestInfo, BindingResult bindingResult) {
		LOGGER.info("BillController  search billSearchCriteria : "+billSearchCriteria);
		org.egov.demand.web.contract.BillInfo billContract = null;
		List<org.egov.demand.web.contract.BillInfo> billContracts = new ArrayList<org.egov.demand.web.contract.BillInfo>();
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult, requestInfo);
		}
		try {
			EgBill bill = billRepository.findOne(billSearchCriteria.getBillId());
			billContract = bill.toDomain();
			billContract.setConsumerCode(bill.getConsumerId());
			billContracts.add(billContract);
			LOGGER.info("the response of bill search : "+billContract);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getSuccessResponseForSearch(billContracts, requestInfo);
	}

	@PostMapping("_create")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody @Valid BillRequest billRequest, BindingResult bindingResult) {
		EgBill bill = null;
		List<EgBill> bills = new ArrayList<EgBill>();
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult, billRequest.getRequestInfo());
		}
		if ((billRequest.getBillInfos() == null || billRequest.getBillInfos().size() == 0)
				&& billRequest.getBillInfos() == null) {
			return errHandler.getErrorResponseEntityForMissingRequestInfo(billRequest.getRequestInfo());
		}
		try {
			for (BillInfo billInfo : billRequest.getBillInfos()) {
				bill = billService.createBill(billInfo.getDemandId(), billInfo);
				bills.add(bill);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getSuccessResponse(bills, billRequest.getRequestInfo());
	}

	@PostMapping("_update")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@RequestBody @Valid BillRequest billRequest, BindingResult bindingResult) {
	    LOGGER.info("the response of bill update : "+billRequest);

		List<EgBill> bills = new ArrayList<EgBill>();
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult, billRequest.getRequestInfo());
		}
		if ((billRequest.getBillInfos() == null || billRequest.getBillInfos().size() == 0)
				&& billRequest.getBillInfos() == null) {
			return errHandler.getErrorResponseEntityForMissingRequestInfo(billRequest.getRequestInfo());
		}
		try {
			BillInfo billInfo=billRequest.getBillInfos().get(0);

			EgBill bill = billRepository.findOne(billRequest.getBillInfos().get(0).getId());
			billService.updateBill(bill, billInfo);
			bills.add(bill);
			LOGGER.info("the getting bill update : "+billRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getSuccessResponseForUpdate(bills, billRequest.getRequestInfo());
	}

	/**
	 * Populate Response object and return Demand List
	 *
	 * @param employeesList
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(List<EgBill> bills, RequestInfo requestInfo) {
		BillResponse billResponse = new BillResponse();
		List<String> billxmls = new ArrayList<String>();
		CollectionUtils collectionUtils = new CollectionUtils();
		for (EgBill bill : bills) {
			billxmls.add(collectionUtils.generateBillXML(bill, bill.getDisplayMessage()));
		}
		billResponse.setBillXmls(billxmls);

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		billResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BillResponse>(billResponse, HttpStatus.OK);
	}

	private ResponseEntity<?> getSuccessResponseForUpdate(List<EgBill> bills, RequestInfo requestInfo) {
		BillResponse billResponse = new BillResponse();
		List<BillInfo> billInfs = new ArrayList<BillInfo>();
		BillInfo billInfo = null;

		for (EgBill bill : bills) {
			billInfo = bill.toDomain();
			billInfo.setConsumerCode(bill.getConsumerId());
			billInfs.add(billInfo);
		}

		billResponse.setBillInfos(billInfs);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		billResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BillResponse>(billResponse, HttpStatus.OK);
	}

	private ResponseEntity<?> getSuccessResponseForSearch(List<BillInfo> bills, RequestInfo requestInfo) {
		BillResponse billResponse = new BillResponse();
		billResponse.setBillInfos(bills);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		billResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BillResponse>(billResponse, HttpStatus.OK);
	}
}