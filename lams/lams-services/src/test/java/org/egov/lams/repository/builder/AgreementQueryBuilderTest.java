package org.egov.lams.repository.builder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.enums.Status;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AgreementQueryBuilderTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void no_input_test(){
		
		AgreementCriteria agreementsModel = new AgreementCriteria();
		
		assertEquals("SELECT *,agreement.id as lamsagreementid FROM eglams_agreement agreement LEFT OUTER JOIN eglams_demand demand "
				+ "ON agreement.id=demand.agreementid "
				+ "LEFT OUTER JOIN eglams_rentincrementtype rent ON agreement.rent_increment_method=rent.id "
				+ "ORDER BY AGREEMENT.ID LIMIT ? OFFSET ?", AgreementQueryBuilder.getAgreementSearchQuery(agreementsModel, new ArrayList<>()));
	}
	
	@Test
	public void all_input_test(){
		
		AgreementCriteria agreementsModel = new AgreementCriteria();
		agreementsModel.setTenantId("ap.kurnool");
		agreementsModel.setAcknowledgementNumber("ack");
		Set<Long> idList = new HashSet<>();
		idList.add(1l);
		idList.add(2l);
		agreementsModel.setAgreementId(idList);
		agreementsModel.setAgreementNumber("ack");
		agreementsModel.setAllottee(idList);
		agreementsModel.setAsset(idList);
		agreementsModel.setFromDate(new Date());
		agreementsModel.setToDate(new Date());
		agreementsModel.setOffSet(0l);
		agreementsModel.setSize(10l);
		agreementsModel.setStateId("1");
		agreementsModel.setStatus(Status.ACTIVE);
		agreementsModel.setTenderNumber("ack");
		agreementsModel.setTinNumber("ack");
		agreementsModel.setTradelicenseNumber("ack");
		
		assertEquals("SELECT *,agreement.id as lamsagreementid FROM eglams_agreement agreement LEFT OUTER JOIN eglams_demand demand "
						+ "ON agreement.id=demand.agreementid"
				        + " LEFT OUTER JOIN eglams_rentincrementtype rent ON agreement.rent_increment_method=rent.id"
						+ " WHERE AGREEMENT.ID IN (1,2) AND AGREEMENT.AGREEMENT_NO=? AND AGREEMENT.STATUS=?"
						+ " AND AGREEMENT.TENANT_ID=? AND AGREEMENT.TENDER_NUMBER=? AND AGREEMENT.TIN_NUMBER=?"
						+ " AND AGREEMENT.TRADE_LICENSE_NUMBER=? AND AGREEMENT.acknowledgementnumber=?"
						+ " AND AGREEMENT.stateid=? AND AGREEMENT.ASSET IN (1,2) AND AGREEMENT.ALLOTTEE IN (1,2)"
						+ " AND AGREEMENT.CREATED_DATE>=? AND AGREEMENT.CREATED_DATE<=?"
						+ " ORDER BY AGREEMENT.ID LIMIT ? OFFSET ?",
						AgreementQueryBuilder.getAgreementSearchQuery(agreementsModel, new ArrayList<>()));
		
	}
	
	@Test
	public void date_test(){
		
		AgreementCriteria agreementsModel = new AgreementCriteria();
		agreementsModel.setTenantId("ap.kurnool");
		
		Calendar calendar = Calendar.getInstance();
		agreementsModel.setFromDate(calendar.getTime());
		calendar.add(Calendar.YEAR,1);
		agreementsModel.setToDate(calendar.getTime());
		
		assertEquals("SELECT *,agreement.id as lamsagreementid FROM eglams_agreement agreement LEFT OUTER JOIN eglams_demand demand "
						+ "ON agreement.id=demand.agreementid "
						+ "LEFT OUTER JOIN eglams_rentincrementtype rent ON agreement.rent_increment_method=rent.id "
						+ "WHERE AGREEMENT.STATUS IN ('ACTIVE','WORKFLOW','RENEWED','REJECTED') AND"
						+ " AGREEMENT.TENANT_ID=?"
						+ " AND AGREEMENT.CREATED_DATE>=? AND AGREEMENT.CREATED_DATE<=?"
						+ " ORDER BY AGREEMENT.ID LIMIT ? OFFSET ?",
						AgreementQueryBuilder.getAgreementSearchQuery(agreementsModel, new ArrayList<>()));
		
	}
	
	@Test
	public void date_failure_test(){
		
		AgreementCriteria agreementsModel = new AgreementCriteria();
		agreementsModel.setTenantId("ap.kurnool");
		
		Calendar calendar = Calendar.getInstance();
		agreementsModel.setToDate(calendar.getTime());
		calendar.add(Calendar.YEAR,1);
		agreementsModel.setFromDate(calendar.getTime());
		
		RuntimeException runtimeException = new RuntimeException("ToDate cannot be lesser than fromdate");
		try{
			AgreementQueryBuilder.getAgreementSearchQuery(agreementsModel, new ArrayList<>());
		}catch (Exception e) {
			assertThat(runtimeException.equals(e));
		}
	}

	@Test
	public void test_should_throw_exception_dates_null() {
		expectedException.reportMissingExceptionWithMessage("Invalid date Range, please enter Both fromDate and toDate");
		expectedException.expect(RuntimeException.class);
		AgreementCriteria criteria = new AgreementCriteria();
		AgreementQueryBuilder.getAgreementSearchQuery(criteria, Collections.emptyList());
	}

	@Test
	public void test_should_throw_exception_fromdate_null() {
		expectedException.reportMissingExceptionWithMessage("Invalid date Range, please enter Both fromDate and toDate");
		expectedException.expect(RuntimeException.class);
		AgreementCriteria criteria = new AgreementCriteria();
		criteria.setToDate(new Date());
		AgreementQueryBuilder.getAgreementSearchQuery(criteria, Collections.emptyList());
	}

	@Test
	public void test_should_throw_exception_todate_null() {
		expectedException.reportMissingExceptionWithMessage("Invalid date Range, please enter Both fromDate and toDate");
		expectedException.expect(RuntimeException.class);
		AgreementCriteria criteria = new AgreementCriteria();
		criteria.setFromDate(new Date());
		AgreementQueryBuilder.getAgreementSearchQuery(criteria, Collections.emptyList());
	}
}
