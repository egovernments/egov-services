package org.egov.lams.repository.builder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.enums.Status;
import org.junit.Test;

public class AgreementQueryBuilderTest {

	@Test
	public void no_input_test(){
		
		AgreementCriteria agreementsModel = new AgreementCriteria();
		
		assertEquals("SELECT *,agreement.id as lamsagreementid FROM eglams_agreement agreement LEFT OUTER JOIN eglams_demand demand "
				+ "ON agreement.id=demand.agreementid ORDER BY AGREEMENT.ID LIMIT ? OFFSET ?", AgreementQueryBuilder.getAgreementSearchQuery(agreementsModel, new ArrayList<>()));
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
		//agreementsModel.setFromDate(new Date());
		//agreementsModel.setToDate(toDate);
		agreementsModel.setOffSet(0l);
		agreementsModel.setSize(10l);
		agreementsModel.setStateId("1");
		agreementsModel.setStatus(Status.ACTIVE);
		agreementsModel.setTenderNumber("ack");
		agreementsModel.setTinNumber("ack");
		agreementsModel.setTradelicenseNumber("ack");
		
		assertEquals("SELECT *,agreement.id as lamsagreementid FROM eglams_agreement agreement LEFT OUTER JOIN eglams_demand demand "
						+ "ON agreement.id=demand.agreementid"
						+ " WHERE AGREEMENT.ID IN (1,2) AND AGREEMENT.AGREEMENT_NO=? AND AGREEMENT.STATUS=?"
						+ " AND AGREEMENT.TENANT_ID=? AND AGREEMENT.TENDER_NUMBER=? AND AGREEMENT.TIN_NUMBER=?"
						+ " AND AGREEMENT.TRADE_LICENSE_NUMBER=? AND AGREEMENT.acknowledgementnumber=?"
						+ " AND AGREEMENT.stateid=? AND AGREEMENT.ASSET IN (1,2) AND AGREEMENT.ALLOTTEE IN (1,2)"
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
						+ "ON agreement.id=demand.agreementid WHERE AGREEMENT.STATUS IN ('ACTIVE','WORKFLOW','RENEWED','REJECTED') AND"
						+ " AGREEMENT.TENANT_ID=?"
						+ " AND AGREEMENT.AGREEMENT_DATE>=? AND AGREEMENT.AGREEMENT_DATE<=?"
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
		//RuntimeException runtimeException = new RuntimeException("Invalid date Range, please enter Both fromDate and toDate");
		try{
			AgreementQueryBuilder.getAgreementSearchQuery(agreementsModel, new ArrayList<>());
		}catch (Exception e) {
			assertThat(runtimeException.equals(e));
		}
	}
}
