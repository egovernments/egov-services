package org.egov.collection.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertTrue;
import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.ReceiptCommonModel;
import org.egov.collection.model.ReceiptDetail;
import org.egov.collection.model.ReceiptHeader;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.repository.ReceiptRepository;
import org.egov.collection.repository.QueryBuilder.ReceiptDetailQueryBuilder;
import org.egov.collection.repository.rowmapper.ReceiptRowMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ReceiptRepositoryTest {
	@Mock
	JdbcTemplate jdbcTemplate;

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
		receiptRepository = new ReceiptRepository(jdbcTemplate, collectionProducer,applicationProperties,receiptDetailQueryBuilder,
				receiptRowMapper,restTemplate,namedParameterJdbcTemplate);
	}

	@Test
	public void test_should_search_receipt_as_per_criteria() throws ParseException {
		ReceiptCommonModel commonModel=getReceiptCommonModel();
		when(receiptDetailQueryBuilder.getQuery(any(ReceiptSearchCriteria.class), any(List.class))).thenReturn("");
		when(jdbcTemplate.query(any(String.class), any(Object[].class), any(ReceiptRowMapper.class)))
				.thenReturn(getListReceiptHeader());
		assertTrue(commonModel.equals(receiptRepository.findAllReceiptsByCriteria(getReceiptSearchCriteria())));
	}
	
	private ReceiptSearchCriteria getReceiptSearchCriteria() {
		return ReceiptSearchCriteria.builder().collectedBy("1").tenantId("default").status("CREATED")
				.sortBy("payeename").sortOrder("desc").fromDate("2016-02-02 00:00:00")
				.toDate("2017-07-11 13:25:45.794050").build();
	}

	private ReceiptCommonModel getReceiptCommonModel() throws ParseException {
		
	ReceiptHeader receiptHeader = ReceiptHeader.builder().id(1L).build();
	ReceiptDetail detail1 = ReceiptDetail.builder().id(1L).chartOfAccount("456").dramount(600.00).cramount(800.00)
			.ordernumber(5L).receiptHeader(receiptHeader).actualcramountToBePaid(800.00)
			.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
			.tenantId("default").build();
	ReceiptDetail detail2 = ReceiptDetail.builder().id(2L).chartOfAccount("490").dramount(700.00).cramount(800.00)
			.ordernumber(6L).receiptHeader(receiptHeader).actualcramountToBePaid(800.00)
			.description("receipt details received").financialYear("sixteen").isActualDemand(true).purpose("REBATE")
			.tenantId("default").build();
	 Set<ReceiptDetail>detailSet=new HashSet<>();
	 detailSet.add(detail1);
	 detailSet.add(detail2);
		ReceiptHeader header = ReceiptHeader.builder().id(1L).payeename("abc").payeeAddress("abc nagara")
				.payeeEmail("abc567@gmail.com").paidBy("abc").referenceNumber("REF1234").receiptType("ghj")
				.receiptNumber("REC1234").referenceDesc("REFDESC45").manualReceiptNumber("MAN67").businessDetails("TL")
				.collectionType("C").displayMsg("receipt created successfully").reference_ch_id(1L).stateId(3L)
				.location(1L).isReconciled(true).status("CREATED").reasonForCancellation("documents not proper")
				.minimumAmount(125.00).totalAmount(150.00).collModesNotAllwd("Bill based").consumerCode("CON12343556")
				.channel("567hfghr").consumerType("Known").fund("56").fundSource("78").function("678").boundary("67")
				.department("78").voucherheader("VOUHEAD").depositedBranch("ICICI").version(1L).createdBy(1L)
				.lastModifiedBy(1L).tenantId("default").build();
	
		return ReceiptCommonModel.builder().receiptHeaders(Arrays.asList(header))
				.receiptDetails(Arrays.asList(detail1, detail2)).build();
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
				.payeeEmail("abc567@gmail.com").paidBy("abc").referenceNumber("REF1234").receiptType("ghj")
				.receiptNumber("REC1234").referenceDesc("REFDESC45").manualReceiptNumber("MAN67").businessDetails("TL")
				.collectionType("C").displayMsg("receipt created successfully").reference_ch_id(1L).stateId(3L)
				.location(1L).isReconciled(true).status("CREATED").reasonForCancellation("documents not proper")
				.minimumAmount(125.00).totalAmount(150.00).collModesNotAllwd("Bill based").consumerCode("CON12343556")
				.channel("567hfghr").consumerType("Known").fund("56").fundSource("78").function("678").boundary("67")
				.department("78").voucherheader("VOUHEAD").depositedBranch("ICICI").version(1L).createdBy(1L)
				.lastModifiedBy(1L).tenantId("default").receiptDetails(setReceipt).build();
		ReceiptHeader header2 = ReceiptHeader.builder().id(1L).payeename("abc").payeeAddress("abc nagara")
				.payeeEmail("abc567@gmail.com").paidBy("abc").referenceNumber("REF1234").receiptType("ghj")
				.receiptNumber("REC1234").referenceDesc("REFDESC45").manualReceiptNumber("MAN67").businessDetails("TL")
				.collectionType("C").displayMsg("receipt created successfully").reference_ch_id(1L).stateId(3L)
				.location(1L).isReconciled(true).status("CREATED").reasonForCancellation("documents not proper")
				.minimumAmount(125.00).totalAmount(150.00).collModesNotAllwd("Bill based").consumerCode("CON12343556")
				.channel("567hfghr").consumerType("Known").fund("56").fundSource("78").function("678").boundary("67")
				.department("78").voucherheader("VOUHEAD").depositedBranch("ICICI").version(1L).createdBy(1L)
				.lastModifiedBy(1L).tenantId("default").receiptDetails(setReceiptDetail).build();

		return Arrays.asList(header1, header2);
	}
}
