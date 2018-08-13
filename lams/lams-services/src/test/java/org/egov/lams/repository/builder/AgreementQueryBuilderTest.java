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
        Map params = new HashMap();
		assertEquals("SELECT *,agreement.id as lamsagreementid FROM eglams_agreement agreement LEFT OUTER JOIN eglams_demand demand "
				+ "ON agreement.id = demand.agreementid "
				+ "LEFT OUTER JOIN eglams_rentincrementtype rent ON agreement.rent_increment_method = rent.id WHERE agreement.id is not null"
                + " and AGREEMENT.STATUS IN ('ACTIVE','WORKFLOW','RENEWED','REJECTED') "
				+ "ORDER BY AGREEMENT.ID LIMIT :pageSize OFFSET :pageNumber", AgreementQueryBuilder.getAgreementSearchQuery(agreementsModel, params));
        assertEquals(0, params.size() - 2);
	}
	
	@Test
	public void all_input_test(){
		
		AgreementCriteria agreementsModel = new AgreementCriteria();
        Map params = new HashMap();
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
						+ "ON agreement.id = demand.agreementid"
				        + " LEFT OUTER JOIN eglams_rentincrementtype rent ON agreement.rent_increment_method = rent.id"
						+ " WHERE agreement.id is not null and AGREEMENT.ID IN (:agreementId) and AGREEMENT.AGREEMENT_NO = :agreementNumber and AGREEMENT.STATUS = :status"
						+ " and AGREEMENT.TENANT_ID = :tenantId and AGREEMENT.TENDER_NUMBER = :tenderNumber and AGREEMENT.TIN_NUMBER = :tinNumber"
						+ " and AGREEMENT.TRADE_LICENSE_NUMBER = :tradeLicenseNumber and AGREEMENT.acknowledgementnumber = :acknowledgementnumber"
						+ " and AGREEMENT.stateid = :stateId and AGREEMENT.ASSET IN (:assetId) and AGREEMENT.ALLOTTEE IN (:allottee)"
						+ " and AGREEMENT.CREATED_DATE >= :fromDate and AGREEMENT.CREATED_DATE <= :toDate"
						+ " ORDER BY AGREEMENT.ID LIMIT :pageSize OFFSET :pageNumber",
						AgreementQueryBuilder.getAgreementSearchQuery(agreementsModel, params));
		assertEquals(15, params.size());
	}
	
	@Test
	public void date_test(){
		
		AgreementCriteria agreementsModel = new AgreementCriteria();
        Map params = new HashMap();
		agreementsModel.setTenantId("ap.kurnool");
		
		Calendar calendar = Calendar.getInstance();
		agreementsModel.setFromDate(calendar.getTime());
		calendar.add(Calendar.YEAR,1);
		agreementsModel.setToDate(calendar.getTime());
		
		assertEquals("SELECT *,agreement.id as lamsagreementid FROM eglams_agreement agreement LEFT OUTER JOIN eglams_demand demand "
						+ "ON agreement.id = demand.agreementid "
						+ "LEFT OUTER JOIN eglams_rentincrementtype rent ON agreement.rent_increment_method = rent.id "
						+ "WHERE agreement.id is not null and AGREEMENT.STATUS IN ('ACTIVE','WORKFLOW','RENEWED','REJECTED',) and"
						+ " AGREEMENT.TENANT_ID = :tenantId"
						+ " and AGREEMENT.CREATED_DATE >= :fromDate and AGREEMENT.CREATED_DATE <= :toDate"
						+ " ORDER BY AGREEMENT.ID LIMIT :pageSize OFFSET :pageNumber",
						AgreementQueryBuilder.getAgreementSearchQuery(agreementsModel, params));
		assertEquals(3, params.size() - 2);
	}
	
	@Test
	public void date_failure_test(){
		
		AgreementCriteria agreementsModel = new AgreementCriteria();
        Map params = new HashMap();
		agreementsModel.setTenantId("ap.kurnool");
		
		Calendar calendar = Calendar.getInstance();
		agreementsModel.setToDate(calendar.getTime());
		calendar.add(Calendar.YEAR,1);
		agreementsModel.setFromDate(calendar.getTime());
		
		RuntimeException runtimeException = new RuntimeException("ToDate cannot be lesser than fromdate");
		try{
			AgreementQueryBuilder.getAgreementSearchQuery(agreementsModel, params);
		}catch (Exception e) {
			assertThat(runtimeException.equals(e));
		}
        assertEquals(1, params.size());
	}

	@Test
	public void test_should_throw_exception_fromdate_null() {
		expectedException.reportMissingExceptionWithMessage("Invalid date Range, please enter Both fromDate and toDate");
		expectedException.expect(RuntimeException.class);
		AgreementCriteria criteria = new AgreementCriteria();
        Map params = new HashMap();
		criteria.setToDate(new Date());
		AgreementQueryBuilder.getAgreementSearchQuery(criteria, params);
        assertEquals(0, params.size());
	}

	@Test
	public void test_should_throw_exception_todate_null() {
		expectedException.reportMissingExceptionWithMessage("Invalid date Range, please enter Both fromDate and toDate");
		expectedException.expect(RuntimeException.class);
		AgreementCriteria criteria = new AgreementCriteria();
        Map params = new HashMap();
		criteria.setFromDate(new Date());
		AgreementQueryBuilder.getAgreementSearchQuery(criteria, params);
        assertEquals(0, params.size());
	}
}
