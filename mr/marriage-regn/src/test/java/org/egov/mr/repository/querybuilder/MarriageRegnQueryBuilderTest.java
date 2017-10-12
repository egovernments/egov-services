package org.egov.mr.repository.querybuilder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.egov.mr.config.ApplicationProperties;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarriageRegnQueryBuilderTest {

	@Mock
	private ApplicationProperties applicationProperties;

	@InjectMocks
	private MarriageRegnQueryBuilder marriageRegnQueryBuilder;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testEmptyQuery() {
		Mockito.doReturn("3").when(applicationProperties).marriageRegnSearchPageSizeDefault();
		MarriageRegnCriteria marriageRegnCriteria = MarriageRegnCriteria.builder().tenantId("1").build();
		List<Object> preparedStatementValues = new ArrayList();

		String queryString = marriageRegnQueryBuilder.getQueryForListOfMarriageRegnIds(marriageRegnCriteria,
				preparedStatementValues);
		String expectedQueryString = "SELECT distinct mr.applicationnumber as mr_applicationnumber FROM egmr_marriage_regn mr"
				+ " JOIN egmr_marrying_person mpbg ON mr.bridegroomid = mpbg.id AND mr.tenantid = mpbg.tenantid"
				+ " JOIN egmr_marrying_person mpb ON mr.brideid = mpb.id AND mr.tenantid = mpb.tenantid"
				+ " JOIN egmr_registration_unit ru ON mr.regnunitid = ru.id AND mr.tenantid = ru.tenantid"
				+ " JOIN egmr_marriageregn_witness w ON mr.applicationnumber = w.applicationnumber"
				+ " JOIN egmr_fee f ON f.id=mr.feeid LEFT OUTER JOIN egmr_marriage_certificate mc"
				+ " ON mr.applicationnumber = mc.applicationnumber AND mr.tenantid = mc.tenantid"
				+ " WHERE mr.tenantid = ?  LIMIT ? OFFSET ?";

		assertEquals(queryString, expectedQueryString);
		assertEquals((String) preparedStatementValues.get(0), "1"); // tenant id
																	// is 1
		assertEquals((long) preparedStatementValues.get(1), 3); // limit is 3
		assertEquals((long) preparedStatementValues.get(2), 0); // offset is 0
	}

	@Test
	public void testApplicationNoQuery() {
		Mockito.doReturn("3").when(applicationProperties).marriageRegnSearchPageSizeDefault();
		List<String> appNos = new ArrayList();
		appNos.add("9");
		appNos.add("10");
		MarriageRegnCriteria marriageRegnCriteria = MarriageRegnCriteria.builder().tenantId("1")
				.applicationNumber(appNos).build();
		List<Object> preparedStatementValues = new ArrayList();

		String queryString = marriageRegnQueryBuilder.getQueryForListOfMarriageRegnIds(marriageRegnCriteria,
				preparedStatementValues);
		String expectedQueryString = "SELECT distinct mr.applicationnumber as mr_applicationnumber FROM egmr_marriage_regn mr"
				+ " JOIN egmr_marrying_person mpbg ON mr.bridegroomid = mpbg.id AND mr.tenantid = mpbg.tenantid"
				+ " JOIN egmr_marrying_person mpb ON mr.brideid = mpb.id AND mr.tenantid = mpb.tenantid"
				+ " JOIN egmr_registration_unit ru ON mr.regnunitid = ru.id AND mr.tenantid = ru.tenantid"
				+ " JOIN egmr_marriageregn_witness w ON mr.applicationnumber = w.applicationnumber"
				+ " JOIN egmr_fee f ON f.id=mr.feeid"
				+ " LEFT OUTER JOIN egmr_marriage_certificate mc ON mr.applicationnumber = mc.applicationnumber AND mr.tenantid = mc.tenantid"
				+ " WHERE mr.tenantid = ?  AND mr.applicationnumber IN ('9', '10') LIMIT ? OFFSET ?";

		assertEquals(queryString, expectedQueryString);
		assertEquals((String) preparedStatementValues.get(0), "1"); // tenant id
																	// is 1
		assertEquals((long) preparedStatementValues.get(1), 3); // limit is 3
		assertEquals((long) preparedStatementValues.get(2), 0); // offset is 0
	}

	@Test
	public void testSelectQuery() {
		Mockito.doReturn("3").when(applicationProperties).marriageRegnSearchPageSizeDefault();
		List<String> appNos = new ArrayList();
		appNos.add("9");
		appNos.add("10");
		MarriageRegnCriteria marriageRegnCriteria = MarriageRegnCriteria.builder().tenantId("1").build();
		List<Object> preparedStatementValues = new ArrayList();

		String queryString = marriageRegnQueryBuilder.getQuery(marriageRegnCriteria, preparedStatementValues, appNos);
		String expectedQueryString = "SELECT  mr.id as mr_id , mr.marriagedate as mr_marriagedate, mr.venue as mr_venue,"
				+ " mr.street as mr_street, mr.placeofmarriage as mr_placeofmarriage, mr.locality as mr_locality, mr.city as mr_city,"
				+ " mr.marriagephoto as mr_marriagephoto, mr.feeid as mr_feeid, mr.priestname as mr_priestname, mr.priestreligion as mr_priestreligion,"
				+ " mr.priestaddress as mr_priestaddress, mr.priestaadhaar as mr_priestaadhaar, mr.priestmobileno as mr_priestmobileno,"
				+ " mr.priestemail as mr_priestemail, mr.serialno as mr_serialno, mr.volumeno as mr_volumeno, mr.demandid as mr_demandid,"
				+ " mr.applicationnumber as mr_applicationnumber, mr.regnnumber as mr_regnumber, mr.regndate as mr_regndate,"
				+ " mr.status as mr_status, mr.source as mr_source, mr.stateid as mr_stateid, mr.tenantid as mr_tenantid,"
				+ " mr.approvaldepartment as mr_approvaldepartment, mr.approvaldesignation as mr_approvaldesignation, mr.approvalassignee as mr_approvalassignee,"
				+ " mr.approvalaction as mr_approvalaction, mr.approvalstatus as mr_approvalstatus, mr.approvalcomments as mr_approvalcomments,"
				+ " mr.createdby as mr_createdby, mr.lastmodifiedby as mr_lastmodifiedby, mr.lastmodifiedtime as mr_lastmodifiedtime,"
				+ " mr.createdtime as mr_createdtime, mr.isactive as mr_isactive, ru.id as ru_id, ru.name as ru_name, ru.isactive as ru_isactive, ru.tenantid as ru_tenantid,"
				+ " ru.ismainregistrationunit as ru_ismainregistrationunit,  ru.locality as ru_locality, ru.zone as ru_zone, ru.revenueward as ru_revenueward, ru.block as ru_block,"
				+ " ru.street as ru_street, ru.electionward as ru_electionward, ru.doorno as ru_doorno, ru.pincode as ru_pincode, mpb.id as mpb_id, mpb.name as mpb_name,"
				+ " mpb.parentname as mpb_parentname, mpb.dob as mpb_dob, mpb.status as mpb_status, mpb.street as mpb_street, mpb.locality as mpb_locality, mpb.city as mpb_city,"
				+ " mpb.aadhaar as mpb_aadhaar, mpb.mobileno as mpb_mobileno, mpb.email as mpb_email, mpb.religionpractice as mpb_religionpractice, mpb.religion as mpb_religion,"
				+ " mpb.occupation as mpb_occupation, mpb.education as mpb_education, mpb.handicapped as mpb_handicapped, mpb.residenceaddress as mpb_residenceaddress,"
				+ " mpb.photo as mpb_photo, mpb.nationality as mpb_nationality,mpb.officeaddress as mpb_officeaddress, mpbg.id as mpbg_id, mpbg.name as mpbg_name,"
				+ " mpbg.parentname as mpbg_parentname, mpbg.dob as mpbg_dob, mpbg.status as mpbg_status, mpbg.street as mpbg_street, mpbg.locality as mpbg_locality,"
				+ " mpbg.city as mpbg_city, mpbg.aadhaar as mpbg_aadhaar, mpbg.mobileno as mpbg_mobileno, mpbg.email as mpbg_email, mpbg.religionpractice as mpbg_religionpractice,"
				+ " mpbg.religion as mpbg_religion, mpbg.occupation as mpbg_occupation, mpbg.education as mpbg_education, mpbg.handicapped as mpbg_handicapped,"
				+ " mpbg.residenceaddress as mpbg_residenceaddress, mpbg.photo as mpbg_photo, mpbg.nationality as mpbg_nationality,mpbg.officeaddress as mpbg_officeaddress,"
				+ " w.id as w_id, w.witnessno as w_witnessno, w.name as w_name, w.relation as w_relation, w.relatedto as w_relatedto, w.dob as w_dob, w.address as w_address,"
				+ " w.relationshipwithapplicants as w_relationship, w.occupation as w_occupation, w.aadhaar as w_aadhaar, w.applicationnumber as w_applicationnumber,"
				+ " w.relationshipwithapplicants as w_relationshipwithapplicants, w.photo as w_photo,  f.id as f_id, f.tenantid as f_tenantid, f.feecriteria as f_feecriteria, f.fee as f_fee,"
				+ " f.fromdate as f_fromdate, f.todate as f_todate, mc.certificateno as mc_certificateno, mc.certificatedate as mc_certificatedate, mc.certificatetype as mc_certificatetype,"
				+ " mc.regnnumber as mc_regnnumber, mc.bridegroomphoto as mc_bridegroomphoto, mc.bridephoto as mc_bridephoto, mc.husbandname as mc_husbandname, mc.husbandaddress as mc_husbandaddress,"
				+ " mc.wifename as mc_wifename, mc.wifeaddress as mc_wifeaddress, mc.marriagedate as mc_marriagedate, mc.marriagevenueaddress as mc_marriagevenueaddress, mc.regndate as mc_regndate,"
				+ " mc.regnserialno as mc_regnserialno, mc.regnvolumeno as mc_regnvolumeno, mc.certificateplace as mc_certificateplace, mc.templateversion as mc_templateversion, mc.applicationnumber as mc_applicationnumber,"
				+ " mc.tenantid as mc_tenantid FROM egmr_marriage_regn mr JOIN egmr_registration_unit ru ON mr.regnunitid = ru.id AND mr.tenantid = ru.tenantid"
				+ " JOIN egmr_marrying_person mpbg ON mr.bridegroomid = mpbg.id AND mr.tenantid = mpbg.tenantid JOIN egmr_marrying_person mpb ON mr.brideid = mpb.id"
				+ " AND mr.tenantid = mpb.tenantid JOIN egmr_marriageregn_witness w ON mr.applicationnumber = w.applicationnumber JOIN egmr_fee f ON f.id=mr.feeid"
				+ " LEFT OUTER JOIN egmr_marriage_certificate mc ON mr.applicationnumber = mc.applicationnumber AND mr.tenantid = mc.tenantid WHERE mr.tenantid = ? "
				+ " AND mr.applicationnumber IN ('9', '10') ORDER BY mr_marriagedate DESC";
		assertEquals(queryString, expectedQueryString);
		assertEquals((String) preparedStatementValues.get(0), "1"); // tenant id
																	// is 1
		// assertEquals((long)preparedStatementValues.get(1), 3); // limit is 3
		// assertEquals((long)preparedStatementValues.get(2), 0); // offset is 0
	}
}
