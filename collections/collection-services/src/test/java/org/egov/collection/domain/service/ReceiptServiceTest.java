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

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.AuditDetails;
import org.egov.collection.model.Instrument;
import org.egov.collection.model.OnlinePayment;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.model.enums.CollectionType;
import org.egov.collection.model.enums.ReceiptType;
import org.egov.collection.repository.BusinessDetailsRepository;
import org.egov.collection.repository.InstrumentRepository;
import org.egov.collection.repository.ReceiptRepository;
import org.egov.collection.service.ReceiptService;
import org.egov.collection.web.contract.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(ReceiptService.class)
@WebAppConfiguration
@Ignore
public class ReceiptServiceTest {
	@Mock
	private ReceiptRepository receiptRepository;

	@Mock
	private BusinessDetailsRepository businessDetailsRepository;
	
	@Mock
	private InstrumentRepository instrumentRepository;

	@Mock
	private ApplicationProperties applicationProperties;

	@InjectMocks
	private ReceiptService receiptService;

	@Test()
	@Ignore
	public void test_should_apportion_and_create_receipt_exception() {

		ReceiptReq receiptReq = getReceiptRequest();
		ApplicationProperties applicationProperty = new ApplicationProperties();
		String hostname = applicationProperty.getEgovServiceHost();
		String baseUri = applicationProperty.getChartOfAccountsSearch();
		List<String> businessDetailCodes = new ArrayList<>();
		businessDetailCodes.add("TL");
		BusinessDetailsResponse businessDetailsRes = getBusinessDetails();
		businessDetailsRes.getBusinessDetails().get(0)
				.setCallBackForApportioning(true);

		Mockito.when(
				businessDetailsRepository.getBusinessDetails(
						businessDetailCodes, receiptReq.getReceipt().get(0)
								.getTenantId(), receiptReq.getRequestInfo()))
				.thenReturn(businessDetailsRes);
		Mockito.when(applicationProperties.getEgovServiceHost()).thenReturn(
				hostname);
		Mockito.when(applicationProperties.getChartOfAccountsSearch())
				.thenReturn(baseUri);

		Mockito.doNothing().when(receiptRepository).pushToQueue(any());

		assertNotNull(receiptService.createReceipt(receiptReq));

	}


	@Test
	@Ignore
	public void test_should_create_receipt() {
		ReceiptReq receiptReq = getReceiptRequest();
		List<String> businessDetailCodes = new ArrayList<>();
		businessDetailCodes.add("TL");
		BusinessDetailsResponse businessDetailsRes = getBusinessDetails();
		businessDetailsRes.getBusinessDetails().get(0)
				.setCallBackForApportioning(true);
		final Map<String, Object> parametersMap = new HashMap<>();
		Map<String, Object>[] parametersReceiptDetails = new Map[100];

		Mockito.when(
				businessDetailsRepository.getBusinessDetails(
						businessDetailCodes, receiptReq.getReceipt().get(0)
								.getTenantId(), receiptReq.getRequestInfo()))
				.thenReturn(businessDetailsRes);
		Mockito.doNothing().when(receiptRepository)
				.persistToReceiptHeader(parametersMap);
		Mockito.doNothing().when(receiptRepository)
				.persistToReceiptDetails(parametersReceiptDetails);
		Mockito.when(
				receiptRepository.persistReceipt(parametersMap,
						parametersReceiptDetails, 1L, "instrumentId"))
				.thenReturn(true);

		assertNotNull(receiptService.create(receiptReq.getReceipt().get(0)
				.getBill().get(0), receiptReq.getRequestInfo(),
				receiptReq.getTenantId(), receiptReq.getReceipt().get(0)
						.getInstrument(),new OnlinePayment()));

	}

	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	public void test_should_not_create_receipt() {
		ReceiptReq receiptReq = getReceiptRequest();
		List<String> businessDetailCodes = new ArrayList<>();
		businessDetailCodes.add("ABC");
		final Map<String, Object> parametersMap = new HashMap<>();
		Map<String, Object>[] parametersReceiptDetails = new Map[100];

		Mockito.when(
				businessDetailsRepository.getBusinessDetails(
						businessDetailCodes, receiptReq.getReceipt().get(0)
								.getTenantId(), receiptReq.getRequestInfo()))
				.thenThrow(Exception.class);
		Mockito.doNothing().when(receiptRepository)
				.persistToReceiptHeader(parametersMap);
		Mockito.doNothing().when(receiptRepository)
				.persistToReceiptDetails(parametersReceiptDetails);
		Mockito.when(
				receiptRepository.persistReceipt(parametersMap,
						parametersReceiptDetails, 1L, "instrumentId"))
				.thenReturn(true);
		Mockito.when(instrumentRepository.createInstrument(
				receiptReq.getRequestInfo(), receiptReq.getReceipt().get(0).getInstrument())).thenThrow(Exception.class);

		receiptService.create(receiptReq.getReceipt().get(0).getBill().get(0),
				receiptReq.getRequestInfo(), receiptReq.getTenantId(),
				receiptReq.getReceipt().get(0).getInstrument(),new OnlinePayment());

	}

/*	@Test
    @Ignore
	public void test_should_search_business_details() throws ParseException {
		when(
				receiptRepository
						.findAllReceiptsByCriteria(getReceiptSearchCriteria(),new RequestInfo()))
				.thenReturn(getReceiptCommonModel());

		ReceiptCommonModel commonModel = receiptService
				.getReceipts(getReceiptSearchCriteria(),new RequestInfo());
		assertEquals(getReceiptCommonModel(), commonModel);
	}*/

	@Test
	public void test_should_be_able_to_cancel_receipt_before_bank_remittance() {
		when(receiptRepository.cancelReceipt(any())).thenReturn(
				getReceiptRequest());
		List<Receipt> receipt = receiptService
				.cancelReceiptBeforeRemittance(getReceiptRequest());
		assertNotNull(receipt);
	}

	@Test
	public void test_should_be_able_to_push_cancel_request_to_kafka() {
		when(receiptRepository.pushReceiptCancelDetailsToQueue(any()))
				.thenReturn(Arrays.asList(getReceipt()));
		List<Receipt> receipt = receiptService
				.cancelReceiptPushToQueue(getReceiptRequest());
		assertEquals(Arrays.asList(getReceipt()), receipt);
	}

	/*@Test
	public void test_should_be_able_to_push_update_receiptRequest_to_queue()
			throws ParseException {
		RequestInfo requestInfo = Mockito.mock(RequestInfo.class);
		when(
				receiptRepository
						.findAllReceiptsByCriteria(getReceiptSearchCriteriaForUpdate()))
				.thenReturn(getReceiptsForUpdate());
		receiptService.pushUpdateReceiptDetailsToQueque(1L, 2L, "SUBMITTED",
				"default", requestInfo);
		verify(receiptRepository).pushUpdateDetailsToQueque(
				any(ReceiptReq.class));
	}

	@Test
	public void test_should_be_able_to_update_status_and_stateId_and_return_true_after_update_workflow_is_called() {
		when(receiptRepository.updateReceipt(any())).thenReturn(true);
		Boolean value = receiptService
				.updateReceipt(getReceiptRequestForUpdate());
		assertEquals(true, value);
	}

	@Test
	public void test_should_be_able_to_return_false_if_status_and_stateId_are_not_updated_inDB__after_update_workflow_is_called_() {
		when(receiptRepository.updateReceipt(any())).thenReturn(false);
		Boolean value = receiptService
				.updateReceipt(getReceiptRequestForUpdate());
		assertEquals(false, value);
	}*/

	@Test
	public void test_should_be_able_to_return_true_if_voucherCreation_is_true_and_receiptDate_is_greater_than_voucherCutOffDate() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 57);
		Calendar calender2 = Calendar.getInstance();
		calender2.set(2017, 12, 22, 18, 13, 56);
		Boolean value = receiptService.checkVoucherCreation(true,
				calender2.getTime(), calender1.getTime());
		assertTrue(value.equals(true));
	}

	@Test
	public void test_should_be_able_to_return_false_if_voucherCreation_is_false_and_receiptDate_is_greater_than_voucherCutOffDate() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 57);
		Calendar calender2 = Calendar.getInstance();
		calender2.set(2017, 12, 22, 18, 13, 56);
		Boolean value = receiptService.checkVoucherCreation(false,
				calender2.getTime(), calender1.getTime());
		assertTrue(value.equals(false));
	}

	@Test
	public void test_should_be_able_to_return_true_if_voucherCutOffDate_is_null_and_voucherCreation_is_true() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 57);
		Boolean value = receiptService.checkVoucherCreation(true, null,
				calender1.getTime());
		assertTrue(value.equals(true));
	}

	@Test
	public void test_should_be_able_to_return_false_if_voucherCutOffDate_is_null_and_voucherCreation_is_false() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 57);
		Boolean value = receiptService.checkVoucherCreation(false, null,
				calender1.getTime());
		assertTrue(value.equals(false));
	}

	@Test
	public void test_should_be_able_to_return_false_if_voucherCutOffDate_is_null_and_voucherCreation_is_null() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 57);
		Boolean value = receiptService.checkVoucherCreation(null, null,
				calender1.getTime());
		assertTrue(value.equals(false));
	}

	@Test
	public void test_should_be_able_to_return_false_if_voucherCreation_is_true_and_receiptDate_is_equalto_voucherCutOffDate() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 56);
		Calendar calender2 = Calendar.getInstance();
		calender2.set(2017, 12, 22, 18, 13, 56);
		Boolean value = receiptService.checkVoucherCreation(true,
				calender2.getTime(), calender1.getTime());
		assertTrue(value.equals(false));
	}

	@Test
	public void test_should_be_able_to_return_false_if_voucherCreation_is_true_and_receiptDate_is_less_than_voucherCutOffDate() {
		Calendar calender1 = Calendar.getInstance();
		calender1.set(2017, 12, 22, 18, 13, 55);
		Calendar calender2 = Calendar.getInstance();
		calender2.set(2017, 12, 22, 18, 13, 56);
		Boolean value = receiptService.checkVoucherCreation(true,
				calender2.getTime(), calender1.getTime());
		assertTrue(value.equals(false));
	}

	/*private ReceiptCommonModel getReceiptsForUpdate() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		ReceiptHeader header = ReceiptHeader.builder().id(1L).payeename("abc")
				.payeeAddress("abc nagara").payeeEmail("abc567@gmail.com")
				.paidBy("abc").referenceNumber("REF1234").receiptType("ADHOC")
				.receiptNumber("REC1234").referenceDesc("REFDESC45")
				.manualReceiptNumber("MAN67").businessDetails("TL")
				.collectionType("COUNTER")
				.displayMsg("receipt created successfully").reference_ch_id(1L)
				.stateId(2L).location(1L).isReconciled(true)
				.status("SUBMITTED")
				.reasonForCancellation("documents not proper")
				.minimumAmount(125.00).totalAmount(150.00)
				.collModesNotAllwd("Bill based").consumerCode("CON12343556")
				.channel("567hfghr").consumerType("Good").fund("56")
				.fundSource("78").function("678").boundary("67")
				.department("78").voucherheader("VOUHEAD")
				.depositedBranch("ICICI").version(1L).createdBy(1L)
				.lastModifiedBy(1L).tenantId("default")
				.receiptDate(dateFormat.parse("2016-02-02")).build();
		ReceiptHeader receiptHeader = ReceiptHeader.builder().id(1L).build();
		ReceiptDetail detail1 = ReceiptDetail.builder().id(1L)
				.chartOfAccount("456").dramount(600.00).cramount(800.00)
				.ordernumber(5L).receiptHeader(receiptHeader)
				.actualcramountToBePaid(800.00)
				.description("receipt details received")
				.financialYear("sixteen").isActualDemand(true)
				.purpose("REBATE").tenantId("default").build();
		ReceiptDetail detail2 = ReceiptDetail.builder().id(2L)
				.chartOfAccount("490").dramount(700.00).cramount(800.00)
				.ordernumber(6L).receiptHeader(receiptHeader)
				.actualcramountToBePaid(800.00)
				.description("receipt details received")
				.financialYear("sixteen").isActualDemand(true)
				.purpose("REBATE").tenantId("default").build();
		return ReceiptCommonModel.builder()
				.receiptHeaders(Arrays.asList(header)).build();
	}*/

	private ReceiptSearchCriteria getReceiptSearchCriteriaForUpdate() {
		return ReceiptSearchCriteria.builder().tenantId("default")
				.ids(Arrays.asList(1L)).build();
	}

	private Receipt getReceipt() {
		BillAccountDetail detail1 = BillAccountDetail.builder().glcode("456")
				.isActualDemand(true).id("1").tenantId("default")
				.billDetail("1").creditAmount(BigDecimal.valueOf(800))
				.debitAmount(BigDecimal.valueOf(600)).purpose(Purpose.REBATE)
				.build();
		BillAccountDetail detail2 = BillAccountDetail.builder().glcode("490")
				.isActualDemand(true).id("2").tenantId("default")
				.billDetail("1").creditAmount(BigDecimal.valueOf(800))
				.debitAmount(BigDecimal.valueOf(700)).purpose(Purpose.REBATE)
				.build();

		BillDetail detail = BillDetail
				.builder()
				.id("1")
				.billNumber("REF1234")
				.consumerCode("CON12343556")
				.consumerType("Good")
				.minimumAmount(BigDecimal.valueOf(125))
				.totalAmount(BigDecimal.valueOf(150))
				.collectionModesNotAllowed(Arrays.asList("Bill based"))
				.tenantId("default")
				.receiptNumber("REC1234")
				.receiptType("ADHOC")
				.channel("567hfghr")
				.voucherHeader("VOUHEAD")
				.collectionType(CollectionType.COUNTER)
				.boundary("67")
				.reasonForCancellation("Data entry mistake")
				.cancellationRemarks(
						"receipt number data entered is not proper")
				.status("CANCELLED")
				.displayMessage("receipt created successfully")
				.billAccountDetails(Arrays.asList(detail1, detail2))
				.businessService("TL").build();
		Bill billInfo = Bill.builder().payeeName("abc")
				.payeeAddress("abc nagara").payeeEmail("abc567@gmail.com")
				.billDetails(Arrays.asList(detail)).tenantId("default")
				.paidBy("abc").build();
		return Receipt.builder().tenantId("default")
				.bill(Arrays.asList(billInfo)).build();
	}

	private ReceiptReq getReceiptRequest() {

		User userInfo = User.builder().id(1L).build();
		RequestInfo requestInfo = RequestInfo.builder()
				.apiId("org.egov.collection").ver("1.0").action("POST")
				.did("4354648646").key("xyz").msgId("654654")
				.authToken("ksnk").userInfo(userInfo).ts(0L).build();
		BillAccountDetail detail1 = BillAccountDetail.builder()
				.glcode("1405014").isActualDemand(true).id("1")
				.tenantId("default").billDetail("1")
				.creditAmount(BigDecimal.valueOf(800))
				.debitAmount(BigDecimal.valueOf(600)).purpose(Purpose.REBATE)
				.build();
		BillAccountDetail detail2 = BillAccountDetail.builder().glcode("490")
				.isActualDemand(true).id("2").tenantId("default")
				.billDetail("1").creditAmount(BigDecimal.valueOf(800))
				.debitAmount(BigDecimal.valueOf(700)).purpose(Purpose.REBATE)
				.build();

		BillDetail detail = BillDetail
				.builder()
				.id("1")
				.billNumber("REF1234")
				.consumerCode("CON12343556")
				.consumerType("Good")
				.minimumAmount(BigDecimal.valueOf(125))
				.totalAmount(BigDecimal.valueOf(150))
				.collectionModesNotAllowed(Arrays.asList("Bill based"))
				.tenantId("default")
				.receiptNumber("REC1234")
				.receiptType("ADHOC")
				.channel("567hfghr")
				.voucherHeader("VOUHEAD")
				.collectionType(CollectionType.COUNTER)
				.boundary("67")
				.reasonForCancellation("Data entry mistake")
				.cancellationRemarks(
						"receipt number data entered is not proper")
				.status("CANCELLED")
				.displayMessage("receipt created successfully")
				.billAccountDetails(Arrays.asList(detail1, detail2))
				.businessService("TL")
				.amountPaid(BigDecimal.valueOf(125))
				.build();

		AuditDetails auditDetails = AuditDetails.builder().createdBy(1L)
				.lastModifiedBy(1L).createdDate(new Date().getTime())
				.lastModifiedDate(new Date().getTime()).build();

		Bill billInfo = Bill.builder().payeeName("abc")
				.payeeAddress("abc nagara").payeeEmail("abc567@gmail.com")
				.billDetails(Arrays.asList(detail)).tenantId("default")
				.paidBy("abc").build();

		Instrument instrument = Instrument.builder().id("10").build();
		Receipt receipt = Receipt.builder().tenantId("default")
				.bill(Arrays.asList(billInfo)).instrument(instrument)
				.auditDetails(auditDetails).build();

		return ReceiptReq.builder().requestInfo(requestInfo)
				.receipt(Arrays.asList(receipt)).build();
	}

	private ReceiptReq getReceiptRequestForUpdate() {

		User userInfo = User.builder().id(1L).build();
		RequestInfo requestInfo = RequestInfo.builder()
				.apiId("org.egov.collection").ver("1.0").action("POST")
				.did("4354648646").key("xyz").msgId("654654")
				.authToken("ksnk").userInfo(userInfo).build();
		BillAccountDetail detail1 = BillAccountDetail.builder().glcode("456")
				.isActualDemand(true).id("1").tenantId("default")
				.billDetail("1").creditAmount(BigDecimal.valueOf(800))
				.debitAmount(BigDecimal.valueOf(600)).purpose(Purpose.REBATE)
				.build();
		BillAccountDetail detail2 = BillAccountDetail.builder().glcode("490")
				.isActualDemand(true).id("2").tenantId("default")
				.billDetail("1").creditAmount(BigDecimal.valueOf(800))
				.debitAmount(BigDecimal.valueOf(700)).purpose(Purpose.REBATE)
				.build();

		BillDetail detail = BillDetail.builder().id("1").billNumber("REF1234")
				.consumerCode("CON12343556").consumerType("Good")
				.minimumAmount(BigDecimal.valueOf(125))
				.totalAmount(BigDecimal.valueOf(150))
				.collectionModesNotAllowed(Arrays.asList("Bill based"))
				.tenantId("default").receiptNumber("REC1234")
				.receiptType(ReceiptType.valueOf("ADHOC").toString())
				.channel("567hfghr").voucherHeader("VOUHEAD")
				.collectionType(CollectionType.valueOf("COUNTER"))
				.boundary("67").reasonForCancellation("Data entry mistake")
				.cancellationRemarks(null).status("SUBMITTED")
				.displayMessage("receipt created successfully")
				.billAccountDetails(Arrays.asList(detail1, detail2))
				.businessService("TL").build();

		Bill billInfo = Bill.builder().payeeName("abc")
				.payeeAddress("abc nagara").payeeEmail("abc567@gmail.com")
				.billDetails(Arrays.asList(detail)).tenantId("default")
				.paidBy("abc").build();
		Receipt receipt = Receipt.builder().tenantId("default").stateId(2L)
				.bill(Arrays.asList(billInfo)).build();
		return ReceiptReq.builder().requestInfo(requestInfo)
				.receipt(Arrays.asList(receipt)).build();
	}

	private ReceiptSearchCriteria getReceiptSearchCriteria() {
		return ReceiptSearchCriteria.builder().collectedBy("1")
				.tenantId("default").status("CREATED").sortBy("payeename")
				.sortOrder("desc").fromDate(1502272932389L)
				.toDate(1502280236077L).build();
	}

	/*private ReceiptCommonModel getReceiptCommonModel() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		*//*ReceiptHeader header = ReceiptHeader.builder().id(1L).payeename("abc")
				.payeeAddress("abc nagara").payeeEmail("abc567@gmail.com")
				.paidBy("abc").referenceNumber("REF1234").receiptType("ADHOC")
				.receiptNumber("REC1234").referenceDesc("REFDESC45")
				.manualReceiptNumber("MAN67").businessDetails("TL")
				.collectionType("COUNTER")
				.displayMsg("receipt created successfully").reference_ch_id(1L)
				.stateId(3L).location(1L).isReconciled(true).status("CREATED")
				.reasonForCancellation("documents not proper")
				.minimumAmount(125.00).totalAmount(150.00)
				.collModesNotAllwd("Bill based").consumerCode("CON12343556")
				.channel("567hfghr").consumerType("Known").fund("56")
				.fundSource("78").function("678").boundary("67")
				.department("78").voucherheader("VOUHEAD")
				.depositedBranch("ICICI").version(1L).createdBy(1L)
				.lastModifiedBy(1L).tenantId("default")
				.receiptDate(dateFormat.parse("2016-02-02")).build();*//*
		ReceiptHeader receiptHeader = ReceiptHeader.builder().id(1L).build();
		ReceiptDetail detail1 = ReceiptDetail.builder().id(1L)
				.chartOfAccount("456").dramount(600.00).cramount(800.00)
				.ordernumber(5L).receiptHeader(receiptHeader)
				.actualcramountToBePaid(800.00)
				.description("receipt details received")
				.financialYear("sixteen").isActualDemand(true)
				.purpose("REBATE").tenantId("default").build();
		ReceiptDetail detail2 = ReceiptDetail.builder().id(2L)
				.chartOfAccount("490").dramount(700.00).cramount(800.00)
				.ordernumber(6L).receiptHeader(receiptHeader)
				.actualcramountToBePaid(800.00)
				.description("receipt details received")
				.financialYear("sixteen").isActualDemand(true)
				.purpose("REBATE").tenantId("default").build();
		return ReceiptCommonModel.builder()
				.receiptHeaders(Arrays.asList(header)).build();
	}*/

	private BusinessDetailsResponse getBusinessDetails() {
		BusinessDetailsResponse businessDetailsRes = new BusinessDetailsResponse();
		BusinessDetailsRequestInfo businessDetailsRequestInfo = BusinessDetailsRequestInfo
				.builder().businessCategory(1L).businessType("CHALLAN")
				.callBackForApportioning(true).code("code").fund("fund")
				.department("department").build();
		List<BusinessDetailsRequestInfo> businessDetails = new ArrayList<>();
		businessDetails.add(businessDetailsRequestInfo);
		businessDetailsRes.setBusinessDetails(businessDetails);

		return businessDetailsRes;
	}

}