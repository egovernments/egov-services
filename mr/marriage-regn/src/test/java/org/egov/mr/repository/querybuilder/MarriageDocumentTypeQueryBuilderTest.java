package org.egov.mr.repository.querybuilder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.egov.mr.model.enums.ApplicationType;
import org.egov.mr.service.MarriageDocumentTypeService;
import org.egov.mr.web.contract.MarriageDocumentTypeSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarriageDocumentTypeQueryBuilderTest {

	@Mock
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Mock
	private MarriageDocumentTypeService mdtservice;

	@InjectMocks
	private MarriageDocumentTypeQueryBuilder mdtQueryBuilder;

	@SuppressWarnings("static-access")
	@Test
	public void testBatchInsertQuery() {
		String expectedquery = "INSERT INTO egmr_marriage_document_type "
				+ "(id,name,code,isactive,isindividual,isrequired,proof,appltype,tenantid) "
				+ "values (?,?,?,?,?,?,?,?,?)";
		assertEquals(expectedquery, mdtQueryBuilder.BATCH_INSERT_QUERY);
	}

	@SuppressWarnings("static-access")
	@Test
	public void testBatchUpdateQuery() {
		String expectedquery = "UPDATE egmr_marriage_document_type SET name=?, isactive=?, "
				+ "isindividual=?, isrequired=?, proof=?, appltype=? " + "WHERE id=? AND code=? AND tenantid=?";
		assertEquals(expectedquery, mdtQueryBuilder.BATCH_UPDATE_QUERY);
	}

	@Test
	public void testSearchSelectQueryWithTenantId() {
		List<Object> preparedStatementValues = new ArrayList<>();

		MarriageDocumentTypeSearchCriteria marriageDocType = MarriageDocumentTypeSearchCriteria.builder()
				.tenantId("ap.kurnool").build();
		String expectedQuery = "SELECT * FROM egmr_marriage_document_type WHERE tenantid=? ;";

		String actualQuery = mdtQueryBuilder.getSelectQuery(marriageDocType, preparedStatementValues);

		List<Object> expectedPSV = new ArrayList<>();
		expectedPSV.add("ap.kurnool");

		assertEquals(expectedPSV, preparedStatementValues);
		assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testSearchSelectQueryWithTenantIdAndIsActive() {
		List<Object> preparedStatementValues = new ArrayList<>();

		MarriageDocumentTypeSearchCriteria marriageDocType = MarriageDocumentTypeSearchCriteria.builder()
				.tenantId("ap.kurnool").isActive(true).build();
		String expectedQuery = "SELECT * FROM egmr_marriage_document_type WHERE isactive=? AND tenantid=? ;";

		String actualQuery = mdtQueryBuilder.getSelectQuery(marriageDocType, preparedStatementValues);

		List<Object> expectedPSV = new ArrayList<>();
		expectedPSV.add(true);
		expectedPSV.add("ap.kurnool");

		assertEquals(expectedPSV, preparedStatementValues);
		assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testSearchSelectQueryWithNull() {
		List<Object> preparedStatementValues = new ArrayList<>();

		MarriageDocumentTypeSearchCriteria marriageDocType = MarriageDocumentTypeSearchCriteria.builder().build();
		String expectedQuery = "SELECT * FROM egmr_marriage_document_type ";

		String actualQuery = mdtQueryBuilder.getSelectQuery(marriageDocType, preparedStatementValues);

		List<Object> expectedPSV = new ArrayList<>();

		assertEquals(expectedPSV, preparedStatementValues);
		assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testSearchSelectQueryWithTenantIdAndApplType() {
		List<Object> preparedStatementValues = new ArrayList<>();

		MarriageDocumentTypeSearchCriteria marriageDocType = MarriageDocumentTypeSearchCriteria.builder()
				.tenantId("ap.kurnool").applicationType(ApplicationType.REGISTRATION.toString()).build();
		String expectedQuery = "SELECT * FROM egmr_marriage_document_type WHERE appltype=? AND tenantid=? ;";

		String actualQuery = mdtQueryBuilder.getSelectQuery(marriageDocType, preparedStatementValues);

		List<Object> expectedPSV = new ArrayList<>();
		expectedPSV.add("REGISTRATION");
		expectedPSV.add("ap.kurnool");

		assertEquals(expectedPSV, preparedStatementValues);
		assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testSearchSelectQuery() {
		List<Object> preparedStatementValues = new ArrayList<>();

		MarriageDocumentTypeSearchCriteria marriageDocType = MarriageDocumentTypeSearchCriteria.builder()
				.tenantId("ap.kurnool").isActive(true).applicationType(ApplicationType.REGISTRATION.toString()).build();
		String expectedQuery = "SELECT * FROM egmr_marriage_document_type WHERE isactive=? AND appltype=? AND tenantid=? ;";

		String actualQuery = mdtQueryBuilder.getSelectQuery(marriageDocType, preparedStatementValues);

		List<Object> expectedPSV = new ArrayList<>();
		expectedPSV.add(true);
		expectedPSV.add("REGISTRATION");
		expectedPSV.add("ap.kurnool");

		assertEquals(expectedPSV, preparedStatementValues);
		assertEquals(expectedQuery, actualQuery);
	}

	@Test
	public void testgetIdNextValForMarriageDocType() {
		String expectedQuery = "SELECT NEXTVAL('seq_marriage_document_type') ;";
		String actualQuery = mdtQueryBuilder.getIdNextValForMarriageDocType();
		assertEquals(expectedQuery, actualQuery);
	}
}
