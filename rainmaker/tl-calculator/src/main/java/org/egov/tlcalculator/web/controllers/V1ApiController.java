package org.egov.tlcalculator.web.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tlcalculator.web.models.Bill;
import org.egov.tlcalculator.web.models.CalculationReq;
import org.egov.tlcalculator.web.models.CalculationRes;
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
public class V1ApiController {

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	public V1ApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@RequestMapping(value = "/v1/_calculate", method = RequestMethod.POST)
	public ResponseEntity<CalculationRes> v1CalculatePost(
			@ApiParam(value = "required parameters have to be populated", required = true) @Valid @RequestBody CalculationReq calculationReq) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<CalculationRes>(objectMapper.readValue(
						"{  \"ResponseInfo\" : {    \"ver\" : \"ver\",    \"resMsgId\" : \"resMsgId\",    \"msgId\" : \"msgId\",    \"apiId\" : \"apiId\",    \"ts\" : 0,    \"status\" : \"SUCCESSFUL\"  },  \"Calculation\" : [ {    \"totalAmount\" : 6.027456183070403,    \"applicationNumber\" : \"applicationNumber\",    \"penalty\" : 1.4658129805029452,    \"rebate\" : 5.637376656633329,    \"tenantId\" : \"tenantId\",    \"exemption\" : 5.962133916683182  }, {    \"totalAmount\" : 6.027456183070403,    \"applicationNumber\" : \"applicationNumber\",    \"penalty\" : 1.4658129805029452,    \"rebate\" : 5.637376656633329,    \"tenantId\" : \"tenantId\",    \"exemption\" : 5.962133916683182  } ]}",
						CalculationRes.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				return new ResponseEntity<CalculationRes>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<CalculationRes>(HttpStatus.NOT_IMPLEMENTED);
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
