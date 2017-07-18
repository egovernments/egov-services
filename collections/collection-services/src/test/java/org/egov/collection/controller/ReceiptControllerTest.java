package org.egov.collection.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.egov.collection.TestConfiguration;
import org.egov.collection.model.ReceiptCommonModel;
import org.egov.collection.model.ReceiptDetail;
import org.egov.collection.model.ReceiptHeader;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.service.ReceiptService;
import org.egov.collection.util.ReceiptReqValidator;
import org.egov.collection.web.contract.factory.ResponseInfoFactory;
import org.egov.collection.web.controller.ReceiptController;
import org.egov.collection.web.errorhandlers.ErrorHandler;
import org.egov.common.contract.request.RequestInfo;
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

	@MockBean
	private ErrorHandler errHandler;

	@Autowired
	private MockMvc mockMvc;

/*	@Test
	public void test_should_search_receipts_as_per_criteria() throws Exception {
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
				.thenReturn(getResponseInfo());
		when(receiptService.getReceipts(getReceiptSearchCriteria())).thenReturn(getReceiptCommonModel());
		mockMvc.perform(post("/receipts/v1/_search?fromDate=2016-02-02 00:00:00&toDate=2017-07-11 13:25:45.794050"
				+ "&tenantId=default&collectedBy=1&status=CREATED&sortBy=payeename&sortOrder=desc")
						.contentType(MediaType.APPLICATION_JSON_UTF8).content(getFileContents("receiptRequest.json")))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("receiptResponse.json")));

	} */

	private ReceiptCommonModel getReceiptCommonModel() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ReceiptHeader header = ReceiptHeader.builder().id(1L).payeename("abc").payeeAddress("abc nagara")
				.payeeEmail("abc567@gmail.com").paidBy("abc").referenceNumber("REF1234").receiptType("ghj")
				.receiptNumber("REC1234").referenceDesc("REFDESC45").manualReceiptNumber("MAN67").businessDetails("TL")
				.collectionType("C").displayMsg("receipt created successfully").reference_ch_id(1L).stateId(3L)
				.location(1L).isReconciled(true).status("CREATED").reasonForCancellation("documents not proper")
				.minimumAmount(125.00).totalAmount(150.00).collModesNotAllwd("Bill based").consumerCode("CON12343556")
				.channel("567hfghr").consumerType("Known").fund("56").fundSource("78").function("678").boundary("67")
				.department("78").voucherheader("VOUHEAD").depositedBranch("ICICI").version(1L).createdBy(1L)
				.lastModifiedBy(1L).tenantId("default").receiptDate(dateFormat.parse("2016-02-02")).build();
		ReceiptHeader receiptHeader = ReceiptHeader.builder().id(1L).build();
		ReceiptDetail detail1 = ReceiptDetail.builder().id(1L).chartOfAccount("456").dramount(600.00).cramount(800.00)
				.ordernumber(5L).receiptHeader(receiptHeader).actualcramountToBePaid(800.00)
				.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
				.tenantId("default").build();
		ReceiptDetail detail2 = ReceiptDetail.builder().id(2L).chartOfAccount("490").dramount(700.00).cramount(800.00)
				.ordernumber(6L).receiptHeader(receiptHeader).actualcramountToBePaid(800.00)
				.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
				.tenantId("default").build();
		return ReceiptCommonModel.builder().receiptHeaders(Arrays.asList(header))
				.receiptDetails(Arrays.asList(detail1, detail2)).build();
	}

	private ReceiptSearchCriteria getReceiptSearchCriteria() {
		return ReceiptSearchCriteria.builder().collectedBy("1").tenantId("default").status("CREATED")
				.sortBy("payeename").sortOrder("desc").fromDate("2016-02-02 00:00:00")
				.toDate("2017-07-11 13:25:45.794050").build();
	}

	private ResponseInfo getResponseInfo() {
		return ResponseInfo.builder().apiId("org.egov.collection").ver("1.0").resMsgId("uief87324").msgId("654654")
				.status("successful").ts("Wed Dec 21 18:30:00 UTC 2016").build();
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
