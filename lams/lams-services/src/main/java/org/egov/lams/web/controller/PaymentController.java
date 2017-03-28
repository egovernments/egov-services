package org.egov.lams.web.controller;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.service.AgreementService;
import org.egov.lams.service.PaymentService;
import org.egov.lams.web.contract.BillReceiptInfoReq;
import org.egov.lams.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	PaymentService paymentService;

	@Autowired
	AgreementService agreementService;

	@PostMapping("/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody RequestInfo requestInfo) {
		AgreementCriteria agreementCriteria = new AgreementCriteria();
		agreementCriteria.setAgreementNumber("LA-17-0001");
		Agreement agreement = agreementService.searchAgreement(
				agreementCriteria).get(0);
		return new ResponseEntity<>(paymentService.generateBillXml(agreement,requestInfo),
				HttpStatus.OK);
	}
	
	
	@PostMapping("/_update")
	@ResponseBody
	public ResponseEntity<?> update(BillReceiptInfoReq billReceiptInfoReq) {
		return new ResponseEntity<>(paymentService.updateDemand(billReceiptInfoReq),HttpStatus.OK);
	}
	
}
