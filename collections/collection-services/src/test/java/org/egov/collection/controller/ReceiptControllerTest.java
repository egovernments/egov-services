/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *//*

package org.egov.collection.controller;

import org.apache.commons.io.IOUtils;
import org.egov.collection.TestConfiguration;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.model.enums.CollectionType;
import org.egov.collection.service.ReceiptService;
import org.egov.collection.service.WorkflowService;
import org.egov.collection.util.ReceiptReqValidator;
import org.egov.collection.web.contract.*;
import org.egov.collection.web.contract.factory.ResponseInfoFactory;
import org.egov.collection.web.controller.ReceiptController;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ReceiptController.class)
@Import(TestConfiguration.class)
public class ReceiptControllerTest {
    @MockBean
    private ReceiptService receiptService;

    @MockBean
    private ReceiptReqValidator receiptReqValidator;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    WorkflowService workFlowService;

*/
/*	@Ignore
	@Test
	public void test_should_search_receipts_as_per_criteria() throws Exception {
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
				.thenReturn(getResponseInfo());
		when(receiptService.getReceipts(getReceiptSearchCriteria(),new RequestInfo())).thenReturn(getReceiptCommonModel());
		mockMvc.perform(post("/receipts/_search?fromDate=2016-02-02 00:00:00&toDate=2017-07-11 13:25:45.794050"
				+ "&tenantId=default&collectedBy=1&status=CREATED&sortBy=payeename&sortOrder=desc")
						.contentType(MediaType.APPLICATION_JSON_UTF8).content(getFileContents("receiptRequest.json")))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("receiptResponse.json")));

	}*//*


    @Test
    public void test_should_be_able_to_cancel_receipts_before_bank_remmitance() throws Exception {
        ErrorResponse errorResponse = new ErrorResponse();
        when(receiptReqValidator.validateReceiptForCreate(any())).thenReturn(errorResponse);
        when(receiptService.cancelReceiptPushToQueue(any())).thenReturn(Arrays.asList(getReceipt()));
        when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
                .thenReturn(getResponseInfo());
        mockMvc.perform(post("/receipts/_cancel").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getFileContents("receiptRequestForCancellation.json"))).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("receiptResponseForCancellation.json")));
    }

    private Receipt getReceipt() {
        BillAccountDetail detail1 = BillAccountDetail.builder().glcode("456").isActualDemand(true).id("1")
                .tenantId("default").billDetail("1").creditAmount(BigDecimal.valueOf(800))
                .debitAmount(BigDecimal.valueOf(600)).purpose(Purpose.REBATE).build();
        BillAccountDetail detail2 = BillAccountDetail.builder().glcode("490").isActualDemand(true).id("2")
                .tenantId("default").billDetail("1").creditAmount(BigDecimal.valueOf(800))
                .debitAmount(BigDecimal.valueOf(700)).purpose(Purpose.REBATE).build();

        BillDetail detail = BillDetail.builder().id("1").billNumber("REF1234").consumerCode("CON12343556")
                .consumerType("Good").minimumAmount(BigDecimal.valueOf(125)).totalAmount(BigDecimal.valueOf(150))
                .collectionModesNotAllowed(Arrays.asList("Bill based")).tenantId("default").receiptNumber("REC1234")
                .receiptType("ADHOC").channel("567hfghr").voucherHeader("VOUHEAD").collectionType(CollectionType.valueOf("COUNTER")).boundary("67")
                .reasonForCancellation("Data entry mistake")
                .cancellationRemarks("receipt number data entered is not proper").status("CANCELLED")
                .displayMessage("receipt created successfully").billAccountDetails(Arrays.asList(detail1, detail2))
                .businessService("TL").build();
        Bill billInfo = Bill.builder().payeeName("abc").payeeAddress("abc nagara").payeeEmail("abc567@gmail.com")
                .billDetails(Arrays.asList(detail)).tenantId("default").paidBy("abc").build();

        return Receipt.builder().tenantId("default").transactionId("10127859476354").bill(Arrays.asList(billInfo)).build();
    }
*/
/*
	private ReceiptCommonModel getReceiptCommonModel() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ReceiptHeader header = ReceiptHeader.builder().id(1L).payeename("abc").payeeAddress("abc nagara")
				.payeeEmail("abc567@gmail.com").paidBy("abc").referenceNumber("REF1234").receiptType("ADHOC")
				.receiptNumber("REC1234").referenceDesc("REFDESC45").manualReceiptNumber("MAN67").businessDetails("TL")
				.collectionType("COUNTER").displayMsg("receipt created successfully").reference_ch_id(1L).stateId(3L)
				.location(1L).isReconciled(true).status("CREATED").reasonForCancellation("Data entry mistake")
				.minimumAmount(125.00).totalAmount(150.00).collModesNotAllwd("Bill based").consumerCode("CON12343556")
				.channel("567hfghr").consumerType("Good").fund("56").fundSource("78").function("678").boundary("67")
				.department("78").voucherheader("VOUHEAD").depositedBranch("ICICI").version(1L).createdBy(1L)
				.lastModifiedBy(1L).tenantId("default").receiptDate(dateFormat.parse("2016-02-02"))
				.cancellationRemarks("payee name data entered is not proper").transactionId("10127859476354").build();
		ReceiptHeader receiptHeader = ReceiptHeader.builder().id(1L).build();
		ReceiptDetail detail1 = ReceiptDetail.builder().id(1L).chartOfAccount("456").dramount(600.0).cramount(800.0)
				.ordernumber(5L).receiptHeader(receiptHeader).actualcramountToBePaid(800.0)
				.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
				.tenantId("default").build();
		ReceiptDetail detail2 = ReceiptDetail.builder().id(2L).chartOfAccount("490").dramount(700.0).cramount(800.0)
				.ordernumber(6L).receiptHeader(receiptHeader).actualcramountToBePaid(800.0)
				.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
				.tenantId("default").build();
		return ReceiptCommonModel.builder().receiptHeaders(Arrays.asList(header)).build();
	}*//*


    private ReceiptSearchCriteria getReceiptSearchCriteria() {
        return ReceiptSearchCriteria.builder().collectedBy("1").tenantId("default").status("CREATED")
                .sortBy("payeename").sortOrder("desc").fromDate(1502272932389L)
                .toDate(1502280236077L).build();
    }

    private ResponseInfo getResponseInfo() {
        return ResponseInfo.builder().apiId("org.egov.collection").ver("1.0").resMsgId("uief87324").msgId("654654")
                .status("successful").build();
    }

    private String getFileContents(String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}*/
