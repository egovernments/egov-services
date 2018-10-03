package org.egov.tlcalculator.web.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tlcalculator.service.CalculationService;
import org.egov.tlcalculator.web.models.*;
import org.egov.tlcalculator.web.models.demand.Category;
import org.egov.tlcalculator.web.models.demand.TaxHeadEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-27T14:56:03.454+05:30")

@Controller
@RequestMapping("/tl-calculator/")
public class CalculatorController {

	private ObjectMapper objectMapper;

	private HttpServletRequest request;

	private CalculationService calculationService;

	@Autowired
	public CalculatorController(ObjectMapper objectMapper, HttpServletRequest request,
								CalculationService calculationService) {
		this.objectMapper = objectMapper;
		this.request = request;
		this.calculationService=calculationService;
	}

	@RequestMapping(value = "/v1/_calculate", method = RequestMethod.POST)
	public ResponseEntity<CalculationRes> calculate(@Valid @RequestBody CalculationReq calculationReq) {
       //   calculationService.calculate(calculationReq.getRequestInfo(),calculationReq.getCalulationCriteria());

       List<Calculation> calculations = Collections.singletonList(Calculation.builder()
				.applicationNumber(calculationReq.getCalulationCriteria().get(0).getApplicationNumber())
		        .tenantId(calculationReq.getCalulationCriteria().get(0).getTenantId())
				.taxHeadEstimates(Collections.singletonList(TaxHeadEstimate.builder()
						.category(Category.TAX)
						.estimateAmount(new BigDecimal(ThreadLocalRandom.current().nextInt(100, 1000 + 1)))
						.taxHeadCode("TL_TAX")
						.build())
				).build());

        CalculationRes response = CalculationRes.builder().calculation(calculations).build();


		return new ResponseEntity<CalculationRes>(response,HttpStatus.OK);
	}

	@RequestMapping(value = "/v1/_getbill", method = RequestMethod.POST)
	public ResponseEntity<Bill> v1GetbillPost(
			@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,
			@NotNull @ApiParam(value = "Unique Trade application number.", required = true) @Valid @RequestParam(value = "applicationNumber", required = true) String applicationNumber,
			@ApiParam(value = "Parameter to carry Request metadata in the request body") @Valid @RequestBody RequestInfo requestInfo) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<Bill>(objectMapper.readValue(
						"{  \"billDetails\" : [ {    \"businessService\" : \"businessService\",    \"collectionModesNotAllowed\" : [ \"collectionModesNotAllowed\", \"collectionModesNotAllowed\" ],    \"collectedAmount\" : 5.962133916683182,    \"receiptDate\" : 0,    \"bill\" : \"bill\",    \"billDate\" : \"2000-01-23\",    \"consumerType\" : \"consumerType\",    \"totalAmount\" : 1.4658129805029452,    \"displayMessage\" : \"displayMessage\",    \"billAccountDetails\" : [ {      \"glcode\" : \"glcode\",      \"crAmountToBePaid\" : 2.3021358869347655,      \"accountDescription\" : \"accountDescription\",      \"billDetail\" : \"billDetail\",      \"purpose\" : \"ARREAR_AMOUNT\",      \"tenantId\" : \"tenantId\",      \"id\" : \"id\",      \"debitAmount\" : 9.301444243932576,      \"creditAmount\" : 7.061401241503109,      \"order\" : 5,      \"isActualDemand\" : true    }, {      \"glcode\" : \"glcode\",      \"crAmountToBePaid\" : 2.3021358869347655,      \"accountDescription\" : \"accountDescription\",      \"billDetail\" : \"billDetail\",      \"purpose\" : \"ARREAR_AMOUNT\",      \"tenantId\" : \"tenantId\",      \"id\" : \"id\",      \"debitAmount\" : 9.301444243932576,      \"creditAmount\" : 7.061401241503109,      \"order\" : 5,      \"isActualDemand\" : true    } ],    \"tenantId\" : \"tenantId\",    \"minimumAmount\" : 6.027456183070403,    \"partPaymentAllowed\" : true,    \"consumerCode\" : \"consumerCode\",    \"id\" : \"id\",    \"billDescription\" : \"billDescription\",    \"billNumber\" : \"billNumber\",    \"callBackForApportioning\" : true,    \"receiptNumber\" : \"receiptNumber\",    \"status\" : \"CREATED\"  }, {    \"businessService\" : \"businessService\",    \"collectionModesNotAllowed\" : [ \"collectionModesNotAllowed\", \"collectionModesNotAllowed\" ],    \"collectedAmount\" : 5.962133916683182,    \"receiptDate\" : 0,    \"bill\" : \"bill\",    \"billDate\" : \"2000-01-23\",    \"consumerType\" : \"consumerType\",    \"totalAmount\" : 1.4658129805029452,    \"displayMessage\" : \"displayMessage\",    \"billAccountDetails\" : [ {      \"glcode\" : \"glcode\",      \"crAmountToBePaid\" : 2.3021358869347655,      \"accountDescription\" : \"accountDescription\",      \"billDetail\" : \"billDetail\",      \"purpose\" : \"ARREAR_AMOUNT\",      \"tenantId\" : \"tenantId\",      \"id\" : \"id\",      \"debitAmount\" : 9.301444243932576,      \"creditAmount\" : 7.061401241503109,      \"order\" : 5,      \"isActualDemand\" : true    }, {      \"glcode\" : \"glcode\",      \"crAmountToBePaid\" : 2.3021358869347655,      \"accountDescription\" : \"accountDescription\",      \"billDetail\" : \"billDetail\",      \"purpose\" : \"ARREAR_AMOUNT\",      \"tenantId\" : \"tenantId\",      \"id\" : \"id\",      \"debitAmount\" : 9.301444243932576,      \"creditAmount\" : 7.061401241503109,      \"order\" : 5,      \"isActualDemand\" : true    } ],    \"tenantId\" : \"tenantId\",    \"minimumAmount\" : 6.027456183070403,    \"partPaymentAllowed\" : true,    \"consumerCode\" : \"consumerCode\",    \"id\" : \"id\",    \"billDescription\" : \"billDescription\",    \"billNumber\" : \"billNumber\",    \"callBackForApportioning\" : true,    \"receiptNumber\" : \"receiptNumber\",    \"status\" : \"CREATED\"  } ],  \"payeeAddress\" : \"payeeAddress\",  \"isCancelled\" : true,  \"mobileNumber\" : \"mobileNumber\",  \"auditDetails\" : {    \"lastModifiedTime\" : 1,    \"createdBy\" : \"createdBy\",    \"lastModifiedBy\" : \"lastModifiedBy\",    \"createdTime\" : 1  },  \"tenantId\" : \"tenantId\",  \"id\" : \"id\",  \"isActive\" : true,  \"payeeEmail\" : \"payeeEmail\"}",
						Bill.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				return new ResponseEntity<Bill>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<Bill>(HttpStatus.NOT_IMPLEMENTED);
	}

}
