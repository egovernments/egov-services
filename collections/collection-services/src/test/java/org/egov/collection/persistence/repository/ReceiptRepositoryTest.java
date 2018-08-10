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
package org.egov.collection.persistence.repository;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.*;
import org.egov.collection.model.enums.CollectionType;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.repository.ReceiptRepository;
import org.egov.collection.repository.querybuilder.ReceiptDetailQueryBuilder;
import org.egov.collection.repository.rowmapper.ReceiptRowMapper;
import org.egov.collection.web.contract.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class ReceiptRepositoryTest {
	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private CollectionProducer collectionProducer;

	@Mock
	@Autowired
	private ApplicationProperties applicationProperties;

	@Mock
	private ReceiptDetailQueryBuilder receiptDetailQueryBuilder;

	@Mock
	private ReceiptRowMapper receiptRowMapper;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private ReceiptRepository receiptRepository;

	@Before
	public void before() {
		receiptRepository = new ReceiptRepository(namedParameterJdbcTemplate,jdbcTemplate,collectionProducer,applicationProperties,receiptDetailQueryBuilder,receiptRowMapper);
	}

	@Test
	public void test_should_push_to_queue(){
		receiptRepository.pushToQueue(getReceiptRequest());
		when(applicationProperties.getCancelReceiptTopicKey()).thenReturn("");
		when(applicationProperties.getCancelReceiptTopicName()).thenReturn("");
		verify(collectionProducer).producer(any(String.class), any(String.class), any(ReceiptReq.class));

	}
	
	/*@Test
	public void test_should_persist_to_receiptheader(){
		Map<String, Object> parametersMap = new HashMap<>();
		parametersMap.put("tenantid", "default");
		Map<String, Object>[] parametersReceiptDetails = new Map[100];
		ReceiptReq receiptReq = getReceiptRequest();
		String query = ReceiptDetailQueryBuilder.insertReceiptHeader();
		String receiptHeaderIdQuery = ReceiptDetailQueryBuilder.getreceiptHeaderId();
		Receipt receiptInfo = receiptReq.getReceipt().get(0);
		Mockito.when(namedParameterJdbcTemplate.update(query, parametersMap)).thenReturn(1);
		Mockito.when(jdbcTemplate.queryForObject(receiptHeaderIdQuery,
					new Object[] { receiptInfo.getBill().get(0).getPayeeName(),
							receiptInfo.getBill().get(0).getPaidBy(), receiptInfo.getAuditDetails().getCreatedDate() },
					Long.class)).thenReturn(1L);
		boolean result = receiptRepository.persistReceipt(parametersMap, parametersReceiptDetails, 1L, "instrumentId");
		assertEquals(true, result);	
		
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = Exception.class)
	@Ignore
	public void test_should_persist_to_receiptheader_exception(){
		Map<String, Object> parametersMap = new HashMap<>();
		ReceiptReq receiptReq = getReceiptRequest();
		String query = ReceiptDetailQueryBuilder.insertReceiptHeader();
		String receiptHeaderIdQuery = ReceiptDetailQueryBuilder.getreceiptHeaderId();
		Receipt receiptInfo = receiptReq.getReceipt().get(0);
		Mockito.when(ReceiptDetailQueryBuilder.insertReceiptHeader()).thenThrow(Exception.class);
		Mockito.when(namedParameterJdbcTemplate.update(query, parametersMap)).thenReturn(1);
		Mockito.when(jdbcTemplate.queryForObject(receiptHeaderIdQuery,
					new Object[] { receiptInfo.getBill().get(0).getPayeeName(),
							receiptInfo.getBill().get(0).getPaidBy(), receiptInfo.getAuditDetails().getCreatedDate() },
					Long.class)).thenThrow(Exception.class);
		
		receiptRepository.persistToReceiptHeader(parametersMap);
		
	} */
	
	/*@SuppressWarnings("unchecked")
	@Test
	public void test_should_persist_to_receiptdetails(){
		Map<String, Object>[] parametersReceiptDetails = new Map[100];	
		int[] result = {1,2};
		String queryReceiptDetails = ReceiptDetailQueryBuilder.insertReceiptDetails();
		Mockito.when(namedParameterJdbcTemplate.batchUpdate(queryReceiptDetails, parametersReceiptDetails)).thenReturn(result);
		receiptRepository.persistToReceiptDetails(parametersReceiptDetails);
		assertNotNull(parametersReceiptDetails);	
		
	}*/
	
	/*@SuppressWarnings("unchecked")
=======
	
	@SuppressWarnings("unchecked")
>>>>>>> Stashed changes
	@Test(expected = Exception.class)
	@Ignore
	public void test_should_persist_to_receiptdetails_exception(){
		Map<String, Object>[] parametersReceiptDetails = new Map[100];	
		int[] result = {1,2};
		String queryReceiptDetails = ReceiptDetailQueryBuilder.insertReceiptDetails();
		Mockito.when(ReceiptDetailQueryBuilder.insertReceiptDetails()).thenThrow(Exception.class);
		Mockito.when(namedParameterJdbcTemplate.batchUpdate(queryReceiptDetails, parametersReceiptDetails)).thenReturn(result);
		
		receiptRepository.persistToReceiptDetails(parametersReceiptDetails);
	} */
	 
	@Test
	public void test_should_get_stateid(){
		String query = "SELECT stateid FROM egcl_receiptheader WHERE id=?";
		Long receiptHeaderId = 116L;
		Mockito.when(jdbcTemplate.queryForObject(query, new Object[] { receiptHeaderId }, Long.class)).thenReturn(1L);
		
		long result = receiptRepository.getStateId(receiptHeaderId);
		assertEquals(1L, result);	
		
	}
	
/*	@Test
    @Ignore
	public void test_should_search_receipt_as_per_criteria() throws ParseException {
		ReceiptCommonModel commonModel = getReceiptCommonModel();
		when(receiptDetailQueryBuilder.getQuery(any(ReceiptSearchCriteria.class), any(List.class))).thenReturn("");
		when(jdbcTemplate.query(any(String.class), any(Object[].class), any(ReceiptRowMapper.class)))
				.thenReturn(getListReceiptHeader());
		assertTrue(commonModel.equals(receiptRepository.findAllReceiptsByCriteria(getReceiptSearchCriteria(), new RequestInfo())));
	}*/

	@Test
	public void test_should_be_able_to_cancel_the_receipt_before_bank_remittance() {
		when(receiptDetailQueryBuilder.getCancelQuery()).thenReturn("");
		int[] integers = { 1, 2 };
		when(jdbcTemplate.batchUpdate(any(String.class), any(List.class))).thenReturn(integers);
		assertNotNull(receiptRepository.cancelReceipt(getReceiptRequest()).getReceipt());
	}

	@Test
	public void test_should_be_able_to_push_cancel_request_to_kafka() {
		receiptRepository.pushReceiptCancelDetailsToQueue(getReceiptRequest());
		when(applicationProperties.getCancelReceiptTopicKey()).thenReturn("");
		when(applicationProperties.getCancelReceiptTopicName()).thenReturn("");
		verify(collectionProducer).producer(any(String.class), any(String.class), any(ReceiptReq.class));
		assertNotNull(receiptRepository.pushReceiptCancelDetailsToQueue(getReceiptRequest()));
	}
	
	/*@Test
	public void test_should_update_status_and_stateId_toDB(){
	
		when(receiptDetailQueryBuilder.getQueryForUpdate(2L,"CANCELLED", 1L, "default"))
		.thenReturn("");
	    Boolean value=true;
		when(jdbcTemplate.update(any(String.class),any(PreparedStatementSetter.class))).thenReturn(1);
		assertTrue(value.equals(receiptRepository.updateReceipt(getReceiptRequest())));
	}
	
	@Test
	public void test_should_be_able_to_push_update_status_request_to_kafka(){
		receiptRepository.pushUpdateDetailsToQueque(getReceiptRequest());
		verify(collectionProducer).producer(any(String.class), any(String.class), any(ReceiptReq.class));
	}*/

	private ReceiptReq getReceiptRequest() {

		User userInfo = User.builder().id(1L).build();
		RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.collection").ver("1.0").action("POST")
				.did("4354648646").key("xyz").msgId("654654").authToken("ksnk").userInfo(userInfo)
				.build();
		BillAccountDetail detail1 = BillAccountDetail.builder().glcode("1405014").isActualDemand(true).id("1")
				.tenantId("default").billDetail("1").creditAmount(BigDecimal.valueOf(800))
				.debitAmount(BigDecimal.valueOf(600)).purpose(Purpose.REBATE).build();
		BillAccountDetail detail2 = BillAccountDetail.builder().glcode("490").isActualDemand(true).id("2")
				.tenantId("default").billDetail("1").creditAmount(BigDecimal.valueOf(800))
				.debitAmount(BigDecimal.valueOf(700)).purpose(Purpose.REBATE).build();
		
		BankBranch bankBranch = BankBranch.builder().name("SBI").build();

		BillDetail detail = BillDetail.builder().id("1").billNumber("REF1234").consumerCode("CON12343556")
				.consumerType("Good").minimumAmount(BigDecimal.valueOf(125)).totalAmount(BigDecimal.valueOf(150))
				.collectionModesNotAllowed(Arrays.asList("Bill based")).tenantId("default").receiptNumber("REC1234")
				.receiptType("ADHOC").channel("567hfghr").voucherHeader("VOUHEAD")
				.collectionType(CollectionType.COUNTER).boundary("67")
				.reasonForCancellation("Data entry mistake")
				.cancellationRemarks("receipt number data entered is not proper").status("CANCELLED")
				.displayMessage("receipt created successfully").billAccountDetails(Arrays.asList(detail1, detail2))
				.businessService("TL").build();
		
		AuditDetails auditDetails = AuditDetails.builder().createdBy(1L).lastModifiedBy(1L)
				.createdDate(new Date().getTime()).lastModifiedDate(new Date().getTime()).build();
		
		Bill billInfo = Bill.builder().payeeName("abc").payeeAddress("abc nagara").payeeEmail("abc567@gmail.com")
				.billDetails(Arrays.asList(detail)).tenantId("default").paidBy("abc").build();
		Receipt receipt = Receipt.builder().tenantId("default").bill(Arrays.asList(billInfo)).auditDetails(auditDetails).build();
		
		return ReceiptReq.builder().requestInfo(requestInfo).receipt(Arrays.asList(receipt)).build();
	}

	private ReceiptSearchCriteria getReceiptSearchCriteria() {
		return ReceiptSearchCriteria.builder().collectedBy("1").tenantId("default").status("CREATED")
				.sortBy("payeename").sortOrder("desc").fromDate(1502272932389L)
				.toDate(1502280236077L).build();
	}

	private ReceiptCommonModel getReceiptCommonModel() {

		ReceiptHeader receiptHeader = ReceiptHeader.builder().id(1L).build();
		ReceiptDetail detail1 = ReceiptDetail.builder().id(1L).chartOfAccount("456").dramount(600.00).cramount(800.00)
				.ordernumber(5L).receiptHeader(receiptHeader).actualcramountToBePaid(800.00)
				.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
				.tenantId("default").build();
		ReceiptDetail detail2 = ReceiptDetail.builder().id(2L).chartOfAccount("490").dramount(700.00).cramount(800.00)
				.ordernumber(6L).receiptHeader(receiptHeader).actualcramountToBePaid(800.00)
				.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
				.tenantId("default").build();
		Set<ReceiptDetail> detailSet = new HashSet<>();
		detailSet.add(detail1);
		detailSet.add(detail2);
		ReceiptHeader header = ReceiptHeader.builder().id(1L).payeename("abc").payeeAddress("abc nagara")
				.payeeEmail("abc567@gmail.com").paidBy("abc").referenceNumber("REF1234").receiptType("ADHOC")
				.receiptNumber("REC1234").referenceDesc("REFDESC45").manualReceiptNumber("MAN67").businessDetails("TL")
				.collectionType(CollectionType.valueOf("COUNTER").toString()).displayMsg("receipt created successfully").reference_ch_id(1L).stateId(3L)
				.location(1L).isReconciled(true).status("CREATED").reasonForCancellation("documents not proper")
				.minimumAmount(125.00).totalAmount(150.00).collModesNotAllwd("Bill based").consumerCode("CON12343556")
				.channel("567hfghr").consumerType("Known").fund("56").fundSource("78").function("678").boundary("67")
				.department("78").voucherheader("VOUHEAD").depositedBranch("ICICI").version(1L).createdBy(1L)
				.lastModifiedBy(1L).tenantId("default").build();
		return ReceiptCommonModel.builder().receiptHeaders(Arrays.asList(header)).build();
	}

	private List<ReceiptHeader> getListReceiptHeader() {
		Set<ReceiptDetail> setReceipt = new HashSet<>();
		Set<ReceiptDetail> setReceiptDetail = new HashSet<>();
		ReceiptHeader receiptHeader = ReceiptHeader.builder().id(1L).build();
		setReceiptDetail.add(ReceiptDetail.builder().id(2L).chartOfAccount("490").dramount(700.00).cramount(800.00)
				.ordernumber(6L).receiptHeader(receiptHeader).actualcramountToBePaid(800.00)
				.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
				.tenantId("default").build());
		setReceipt.add(ReceiptDetail.builder().id(1L).chartOfAccount("456").dramount(600.00).cramount(800.00)
				.ordernumber(5L).receiptHeader(receiptHeader).actualcramountToBePaid(800.00)
				.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
				.tenantId("default").build());
		ReceiptHeader header1 = ReceiptHeader.builder().id(1L).payeename("abc").payeeAddress("abc nagara")
				.payeeEmail("abc567@gmail.com").paidBy("abc").referenceNumber("REF1234").receiptType("ADHOC")
				.receiptNumber("REC1234").referenceDesc("REFDESC45").manualReceiptNumber("MAN67").businessDetails("TL")
				.collectionType(CollectionType.valueOf("COUNTER").toString()).displayMsg("receipt created successfully").reference_ch_id(1L).stateId(3L)
				.location(1L).isReconciled(true).status("CREATED").reasonForCancellation("documents not proper")
				.minimumAmount(125.00).totalAmount(150.00).collModesNotAllwd("Bill based").consumerCode("CON12343556")
				.channel("567hfghr").consumerType("Known").fund("56").fundSource("78").function("678").boundary("67")
				.department("78").voucherheader("VOUHEAD").depositedBranch("ICICI").version(1L).createdBy(1L)
				.lastModifiedBy(1L).tenantId("default").receiptDetails(setReceipt).build();
		ReceiptHeader header2 = ReceiptHeader.builder().id(1L).payeename("abc").payeeAddress("abc nagara")
				.payeeEmail("abc567@gmail.com").paidBy("abc").referenceNumber("REF1234").receiptType("ADHOC")
				.receiptNumber("REC1234").referenceDesc("REFDESC45").manualReceiptNumber("MAN67").businessDetails("TL")
				.collectionType(CollectionType.valueOf("COUNTER").toString()).displayMsg("receipt created successfully").reference_ch_id(1L).stateId(3L)
				.location(1L).isReconciled(true).status("CREATED").reasonForCancellation("documents not proper")
				.minimumAmount(125.00).totalAmount(150.00).collModesNotAllwd("Bill based").consumerCode("CON12343556")
				.channel("567hfghr").consumerType("Known").fund("56").fundSource("78").function("678").boundary("67")
				.department("78").voucherheader("VOUHEAD").depositedBranch("ICICI").version(1L).createdBy(1L)
				.lastModifiedBy(1L).tenantId("default").receiptDetails(setReceiptDetail).build();

		return Arrays.asList(header1, header2);
	}
}