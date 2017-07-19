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
 */
package org.egov.collection.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import org.egov.collection.model.ReceiptCommonModel;
import org.egov.collection.model.ReceiptDetail;
import org.egov.collection.model.ReceiptHeader;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.repository.ReceiptRepository;
import org.egov.collection.service.ReceiptService;
import org.egov.collection.web.contract.Bill;
import org.egov.collection.web.contract.BillAccountDetail;
import org.egov.collection.web.contract.BillDetail;
import org.egov.collection.web.contract.Purpose;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptReq;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
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

	@Test
	public void test_should_be_able_to_cancel_receipt_before_bank_remittance() {
		when(receiptRepository.cancelReceipt(any())).thenReturn(getReceiptRequest());
		Receipt receipt = receiptService.cancelReceiptBeforeRemittance(getReceiptRequest());
		assertEquals(getReceipt(), receipt);
	}

	@Test
	public void test_should_be_able_to_push_cancel_request_to_kafka() {
		when(receiptRepository.pushReceiptCancelDetailsToQueue(any())).thenReturn(getReceipt());
		Receipt receipt = receiptService.cancelReceiptPushToQueue(getReceiptRequest());
		assertEquals(getReceipt(), receipt);
	}

	@Test
	public void test_should_be_able_to_return_true_if_voucherCreation_is_true_and_receiptDate_is_greater_than_voucherCutOffDate() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 57);
		Calendar calender2 = Calendar.getInstance();
		calender2.set(2017, 12, 22, 18, 13, 56);
		Boolean value = receiptService.checkVoucherCreation(true, calender2.getTime(), calender1.getTime());
		assertTrue(value.equals(true));
	}

	@Test
	public void test_should_be_able_to_return_false_if_voucherCreation_is_false_and_receiptDate_is_greater_than_voucherCutOffDate() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 57);
		Calendar calender2 = Calendar.getInstance();
		calender2.set(2017, 12, 22, 18, 13, 56);
		Boolean value = receiptService.checkVoucherCreation(false, calender2.getTime(), calender1.getTime());
		assertTrue(value.equals(false));
	}

	@Test
	public void test_should_be_able_to_return_true_if_voucherCutOffDate_is_null_and_voucherCreation_is_true() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 57);
		Boolean value = receiptService.checkVoucherCreation(true, null, calender1.getTime());
		assertTrue(value.equals(true));
	}

	@Test
	public void test_should_be_able_to_return_false_if_voucherCutOffDate_is_null_and_voucherCreation_is_false() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 57);
		Boolean value = receiptService.checkVoucherCreation(false, null, calender1.getTime());
		assertTrue(value.equals(false));
	}

	@Test
	public void test_should_be_able_to_return_false_if_voucherCutOffDate_is_null_and_voucherCreation_is_null() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 57);
		Boolean value = receiptService.checkVoucherCreation(null, null, calender1.getTime());
		assertTrue(value.equals(false));
	}

	@Test
	public void test_should_be_able_to_return_false_if_voucherCreation_is_true_and_receiptDate_is_equalto_voucherCutOffDate() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 56);
		Calendar calender2 = Calendar.getInstance();
		calender2.set(2017, 12, 22, 18, 13, 56);
		Boolean value = receiptService.checkVoucherCreation(true, calender2.getTime(), calender1.getTime());
		assertTrue(value.equals(false));
	}

	@Test
	public void test_should_be_able_to_return_false_if_voucherCreation_is_true_and_receiptDate_is_less_than_voucherCutOffDate() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 55);
		Calendar calender2 = Calendar.getInstance();
		calender2.set(2017, 12, 22, 18, 13, 56);
		Boolean value = receiptService.checkVoucherCreation(true, calender2.getTime(), calender1.getTime());
		assertTrue(value.equals(false));
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
				.receiptType("ghj").channel("567hfghr").voucherHeader("VOUHEAD").collectionType("C").boundary("67")
				.reasonForCancellation("Data entry mistake")
				.cancellationRemarks("receipt number data entered is not proper").status("CANCELLED")
				.displayMessage("receipt created successfully").billAccountDetails(Arrays.asList(detail1, detail2))
				.receiptDate(Timestamp.valueOf("2016-02-02 00:00:00.0")).businessService("TL").build();
		Bill billInfo = Bill.builder().payeeName("abc").payeeAddress("abc nagara").payeeEmail("abc567@gmail.com")
				.billDetails(Arrays.asList(detail)).tenantId("default").paidBy("abc").build();

		return Receipt.builder().tenantId("default").bill(billInfo).build();
	}

	private ReceiptReq getReceiptRequest() {

		User userInfo = User.builder().id(1L).build();
		RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.collection").ver("1.0").action("POST")
				.did("4354648646").key("xyz").msgId("654654").requesterId("61").authToken("ksnk").userInfo(userInfo)
				.build();
		BillAccountDetail detail1 = BillAccountDetail.builder().glcode("456").isActualDemand(true).id("1")
				.tenantId("default").billDetail("1").creditAmount(BigDecimal.valueOf(800))
				.debitAmount(BigDecimal.valueOf(600)).purpose(Purpose.REBATE).build();
		BillAccountDetail detail2 = BillAccountDetail.builder().glcode("490").isActualDemand(true).id("2")
				.tenantId("default").billDetail("1").creditAmount(BigDecimal.valueOf(800))
				.debitAmount(BigDecimal.valueOf(700)).purpose(Purpose.REBATE).build();

		BillDetail detail = BillDetail.builder().id("1").billNumber("REF1234").consumerCode("CON12343556")
				.consumerType("Good").minimumAmount(BigDecimal.valueOf(125)).totalAmount(BigDecimal.valueOf(150))
				.collectionModesNotAllowed(Arrays.asList("Bill based")).tenantId("default").receiptNumber("REC1234")
				.receiptType("ghj").channel("567hfghr").voucherHeader("VOUHEAD").collectionType("C").boundary("67")
				.reasonForCancellation("Data entry mistake")
				.cancellationRemarks("receipt number data entered is not proper").status("CANCELLED")
				.displayMessage("receipt created successfully").billAccountDetails(Arrays.asList(detail1, detail2))
				.receiptDate(Timestamp.valueOf("2016-02-02 00:00:00.0")).businessService("TL").build();
		Bill billInfo = Bill.builder().payeeName("abc").payeeAddress("abc nagara").payeeEmail("abc567@gmail.com")
				.billDetails(Arrays.asList(detail)).tenantId("default").paidBy("abc").build();
		Receipt receipt = Receipt.builder().tenantId("default").bill(billInfo).build();
		return ReceiptReq.builder().requestInfo(requestInfo).receipt(receipt).build();
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