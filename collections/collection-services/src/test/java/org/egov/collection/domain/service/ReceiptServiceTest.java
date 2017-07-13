package org.egov.collection.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.egov.collection.model.ReceiptCommonModel;
import org.egov.collection.model.ReceiptDetail;
import org.egov.collection.model.ReceiptHeader;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.repository.ReceiptRepository;
import org.egov.collection.service.ReceiptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(ReceiptService.class)
@WebAppConfiguration
public class ReceiptServiceTest {
	@Mock
	private ReceiptRepository receiptRepository;

	@InjectMocks
	private ReceiptService receiptService;

	@Test
	public void test_should_search_business_details() throws ParseException {
		when(receiptRepository.findAllReceiptsByCriteria(getReceiptSearchCriteria()))
				.thenReturn(getReceiptCommonModel());

		ReceiptCommonModel commonModel = receiptService.getReceipts(getReceiptSearchCriteria());
		assertEquals(getReceiptCommonModel(), commonModel);
	}

	private ReceiptSearchCriteria getReceiptSearchCriteria() {
		return ReceiptSearchCriteria.builder().collectedBy("1").tenantId("default").status("CREATED")
				.sortBy("payeename").sortOrder("desc").fromDate("2016-02-02 00:00:00")
				.toDate("2017-07-11 13:25:45.794050").build();
	}

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

}
