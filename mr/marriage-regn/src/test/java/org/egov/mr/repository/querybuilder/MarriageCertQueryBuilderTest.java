package org.egov.mr.repository.querybuilder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.egov.mr.config.ApplicationProperties;
import org.egov.mr.web.contract.MarriageCertCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarriageCertQueryBuilderTest {

	@InjectMocks
	private MarriageCertQueryBuilder marriageCertQueryBuilder;

	@Mock
	private ApplicationProperties applicationProperties;

	@Mock
	private MarriageCertCriteria marriageCertCriteria;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testForGetQuery() {
		Mockito.doReturn("20").when(applicationProperties).marriageCertSearchPageSizeDefault();
		List<Object> preparedStatementValues = new ArrayList<>();
		MarriageCertCriteria marriageCertCriteria = MarriageCertCriteria.builder().tenantId("ap.kurnool").build();
		String SelectQuery = " select rc.id as rc_id,rc.regnno as rc_regnno,rc.applicantname as rc_applicantname, rc.applicantaddress as rc_applicantaddress,"
				+ " rc.applicantmobileno as rc_applicantmobileno, rc.applicantfee as rc_applicantfee, rc.applicantaadhaar as  rc_applicantaadhaar,"
				+ " rc.applicationnumber as rc_applicationnumber, rc.reissueapplstatus as rc_reissueapplstatus ,rc.stateid as rc_stateid,"
				+ " rc.approvaldepartment as rc_approvaldepartment, rc.approvaldesignation as rc_approvaldesignation,rc.approvalassignee as rc_approvalassignee,"
				+ " rc.approvalaction as rc_approvalaction,rc.approvalstatus as rc_approvalstatus ,rc.approvalcomments as rc_approvalcomments,"
				+ " rc.demands as rc_demands,rc.rejectionreason as rc_rejectionreason,rc.remarks as rc_remarks,"
				+ " rc.isactive as rc_isactive, rc.createdby  as rc_createdby,rc.createdtime as rc_createdtime, rc.lastmodifiedby as rc_lastmodifiedby,"
				+ " rc.lastmodifiedtime as rc_lastmodifiedtime, rc.tenantid as rc_tenantid, mc.certificateno as mc_certificateno,"
				+ " mc.certificatedate as mc_certificatedate, mc.certificatetype as mc_certificatetype, mc.regnnumber as mc_regnnumber,"
				+ " mc.bridegroomphoto as mc_bridegroomphoto, mc.bridephoto as mc_bridephoto, mc.husbandname as mc_husbandname, mc.husbandaddress as mc_husbandaddress,"
				+ " mc.wifename as mc_wifename, mc.wifeaddress as mc_wifeaddress, mc.marriagedate as mc_marriagedate, mc.marriagevenueaddress as mc_marriagevenueaddress,"
				+ " mc.regndate as mc_regndate, mc.regnserialno as mc_regnserialno, mc.regnvolumeno as mc_regnvolumeno, mc.certificateplace as mc_certificateplace,"
				+ " mc.templateversion as mc_templateversion, mc.applicationnumber as mc_applicationnumber, mc.tenantid as mc_tenantid, ds.id as ds_id,ds.reissuecertificateid as ds_reissuecertificateid, ds.documenttypecode as ds_documenttypecode,"
				+ " ds.location as ds_location, ds.createdby  as ds_createdby,ds.createdtime as ds_createdtime, ds.lastmodifiedby as ds_lastmodifiedby,"
				+ " ds.lastmodifiedtime as ds_lastmodifiedtime, ds.tenantid as ds_tenantid"
				+ " FROM egmr_reissuecertificate rc"
				+ " LEFT OUTER JOIN egmr_marriage_certificate mc ON mc.regnnumber=rc.regnno"
				+ " LEFT OUTER JOIN egmr_documents ds ON rc.id=ds.reissuecertificateid "
				+ " WHERE rc.tenantId = ?  LIMIT ? OFFSET ?";
		String ExpectedQuery = marriageCertQueryBuilder.getQuery(marriageCertCriteria, preparedStatementValues);
		assertEquals(ExpectedQuery.toString(), SelectQuery.toString());
		assertEquals((String) preparedStatementValues.get(0), "ap.kurnool");

	}

}
