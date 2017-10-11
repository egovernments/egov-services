package org.egov.mr.repository.querybuilder;

import java.util.List;

import org.egov.mr.config.ApplicationProperties;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MarriageRegnQueryBuilder {
	
	private static final Logger logger = LoggerFactory.getLogger(MarriageRegnQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;
	
	private static final String MARRIAGE_REGN_IDS_QUERY = "SELECT distinct mr.applicationnumber as mr_applicationnumber"
			+ " FROM egmr_marriage_regn mr"
	        + " JOIN egmr_marrying_person mpbg ON mr.bridegroomid = mpbg.id AND mr.tenantid = mpbg.tenantid"
			+ " JOIN egmr_marrying_person mpb ON mr.brideid = mpb.id AND mr.tenantid = mpb.tenantid"
			+ " JOIN egmr_registration_unit ru ON mr.regnunitid = ru.id AND mr.tenantid = ru.tenantid"
	        + " JOIN egmr_marriageregn_witness w ON mr.applicationnumber = w.applicationnumber"
			+ " JOIN egmr_fee f ON f.id=mr.feeid"
	        + " LEFT OUTER JOIN egmr_marriage_certificate mc ON mr.applicationnumber = mc.applicationnumber AND mr.tenantid = mc.tenantid";
	
	private static final String BASE_QUERY = "SELECT  mr.id as mr_id , mr.marriagedate as mr_marriagedate, mr.venue as mr_venue,"
			+ " mr.street as mr_street, mr.placeofmarriage as mr_placeofmarriage, mr.locality as mr_locality, mr.city as mr_city,"
			+ " mr.marriagephoto as mr_marriagephoto, mr.feeid as mr_feeid, mr.priestname as mr_priestname, mr.priestreligion as mr_priestreligion,"
			+ " mr.priestaddress as mr_priestaddress, mr.priestaadhaar as mr_priestaadhaar, mr.priestmobileno as mr_priestmobileno,"
			+ " mr.priestemail as mr_priestemail, mr.serialno as mr_serialno, mr.volumeno as mr_volumeno, mr.demandid as mr_demandid,"
			+ " mr.applicationnumber as mr_applicationnumber, mr.regnnumber as mr_regnumber, mr.regndate as mr_regndate, mr.status as mr_status,"
			+ " mr.source as mr_source, mr.stateid as mr_stateid, mr.tenantid as mr_tenantid, mr.approvaldepartment as mr_approvaldepartment,"
			+ " mr.approvaldesignation as mr_approvaldesignation, mr.approvalassignee as mr_approvalassignee,"
			+ " mr.approvalaction as mr_approvalaction, mr.approvalstatus as mr_approvalstatus, mr.approvalcomments as mr_approvalcomments,"
			+ " mr.createdby as mr_createdby, mr.lastmodifiedby as mr_lastmodifiedby,"
			+ " mr.lastmodifiedtime as mr_lastmodifiedtime, mr.createdtime as mr_createdtime, mr.isactive as mr_isactive,"
			+ " ru.id as ru_id, ru.name as ru_name, ru.isactive as ru_isactive, ru.tenantid as ru_tenantid, ru.ismainregistrationunit as ru_ismainregistrationunit, "
			+ " ru.locality as ru_locality, ru.zone as ru_zone, ru.revenueward as ru_revenueward, ru.block as ru_block,"
			+ " ru.street as ru_street, ru.electionward as ru_electionward, ru.doorno as ru_doorno, ru.pincode as ru_pincode,"
			+ " mpb.id as mpb_id, mpb.name as mpb_name, mpb.parentname as mpb_parentname, mpb.dob as mpb_dob, mpb.status as mpb_status,"
			+ " mpb.street as mpb_street, mpb.locality as mpb_locality, mpb.city as mpb_city, mpb.aadhaar as mpb_aadhaar,"
			+ " mpb.mobileno as mpb_mobileno, mpb.email as mpb_email, mpb.religionpractice as mpb_religionpractice, mpb.religion as mpb_religion,"
			+ " mpb.occupation as mpb_occupation, mpb.education as mpb_education, mpb.handicapped as mpb_handicapped,"
			+ " mpb.residenceaddress as mpb_residenceaddress, mpb.photo as mpb_photo, mpb.nationality as mpb_nationality,mpb.officeaddress as mpb_officeaddress,"
			+ " mpbg.id as mpbg_id, mpbg.name as mpbg_name, mpbg.parentname as mpbg_parentname, mpbg.dob as mpbg_dob, mpbg.status as mpbg_status,"
			+ " mpbg.street as mpbg_street, mpbg.locality as mpbg_locality, mpbg.city as mpbg_city, mpbg.aadhaar as mpbg_aadhaar,"
			+ " mpbg.mobileno as mpbg_mobileno, mpbg.email as mpbg_email, mpbg.religionpractice as mpbg_religionpractice, mpbg.religion as mpbg_religion,"
			+ " mpbg.occupation as mpbg_occupation, mpbg.education as mpbg_education, mpbg.handicapped as mpbg_handicapped,"
			+ " mpbg.residenceaddress as mpbg_residenceaddress, mpbg.photo as mpbg_photo, mpbg.nationality as mpbg_nationality,mpbg.officeaddress as mpbg_officeaddress,"
			+ " w.id as w_id, w.witnessno as w_witnessno, w.name as w_name, w.relation as w_relation, w.relatedto as w_relatedto, w.dob as w_dob, w.address as w_address, w.relationshipwithapplicants as w_relationship,"
			+ " w.occupation as w_occupation, w.aadhaar as w_aadhaar, w.applicationnumber as w_applicationnumber, w.relationshipwithapplicants as w_relationshipwithapplicants, w.photo as w_photo, "
			+ " f.id as f_id, f.tenantid as f_tenantid, f.feecriteria as f_feecriteria, f.fee as f_fee, f.fromdate as f_fromdate, f.todate as f_todate,"
			+ " mc.certificateno as mc_certificateno, mc.certificatedate as mc_certificatedate, mc.certificatetype as mc_certificatetype,"
			+ " mc.regnnumber as mc_regnnumber, mc.bridegroomphoto as mc_bridegroomphoto, mc.bridephoto as mc_bridephoto,"
			+ " mc.husbandname as mc_husbandname, mc.husbandaddress as mc_husbandaddress, mc.wifename as mc_wifename, mc.wifeaddress as mc_wifeaddress,"
			+ " mc.marriagedate as mc_marriagedate, mc.marriagevenueaddress as mc_marriagevenueaddress, mc.regndate as mc_regndate,"
			+ " mc.regnserialno as mc_regnserialno, mc.regnvolumeno as mc_regnvolumeno, mc.certificateplace as mc_certificateplace,"
			+ " mc.templateversion as mc_templateversion, mc.applicationnumber as mc_applicationnumber, mc.tenantid as mc_tenantid"
			+ " FROM egmr_marriage_regn mr"
			+ " JOIN egmr_registration_unit ru ON mr.regnunitid = ru.id AND mr.tenantid = ru.tenantid"
	        + " JOIN egmr_marrying_person mpbg ON mr.bridegroomid = mpbg.id AND mr.tenantid = mpbg.tenantid"
			+ " JOIN egmr_marrying_person mpb ON mr.brideid = mpb.id AND mr.tenantid = mpb.tenantid"
	        + " JOIN egmr_marriageregn_witness w ON mr.applicationnumber = w.applicationnumber"
	        + " JOIN egmr_fee f ON f.id=mr.feeid"
	        + " LEFT OUTER JOIN egmr_marriage_certificate mc ON mr.applicationnumber = mc.applicationnumber AND mr.tenantid = mc.tenantid";
	
	public String getQueryForListOfMarriageRegnIds(MarriageRegnCriteria marriageRegnCriteria,
			List<Object> preparedStatementValues) {
		
		StringBuilder selectQuery = new StringBuilder(MARRIAGE_REGN_IDS_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, marriageRegnCriteria, null);
		addPagingClause(selectQuery, preparedStatementValues, marriageRegnCriteria);

		logger.debug("marriage regn selectIdQuery : " + selectQuery);
		return selectQuery.toString();
	}

	public String getQuery(MarriageRegnCriteria marriageRegnCriteria, List<Object> preparedStatementValues,
			List<String> listOfApplNos) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, marriageRegnCriteria, listOfApplNos);
		addOrderByClause(selectQuery, marriageRegnCriteria);

		logger.info(" marriage regn selectQuery : " + selectQuery);
		return selectQuery.toString();
	}
	
	private void addWhereClause(StringBuilder selectQuery, List<Object> preparedStatementValues,
			MarriageRegnCriteria marriageRegnCriteria, List<String> listOfApplNos) {
		
		selectQuery.append(" WHERE mr.tenantid = ? ");
		preparedStatementValues.add(marriageRegnCriteria.getTenantId());
		
		if (marriageRegnCriteria.getApplicationNumber() == null && marriageRegnCriteria.getRegnNo() == null
				&& marriageRegnCriteria.getMarriageDate() == null && marriageRegnCriteria.getHusbandName() == null
				&& marriageRegnCriteria.getWifeName() == null && marriageRegnCriteria.getFromDate() == null
				&& marriageRegnCriteria.getToDate() == null && marriageRegnCriteria.getRegnUnit() == null
				&& marriageRegnCriteria.getTenantId() == null)
			return;

		if(marriageRegnCriteria.getApplicationNumber() != null) {
			selectQuery.append(" AND mr.applicationnumber IN " + getIdQuery(marriageRegnCriteria.getApplicationNumber())) ;
		} else if(listOfApplNos != null) {
			selectQuery.append(" AND mr.applicationnumber IN " + getIdQuery(listOfApplNos));
		}
		if (marriageRegnCriteria.getRegnNo() != null) {
			selectQuery.append(" AND mr.regnnumber = ?");
			preparedStatementValues.add(marriageRegnCriteria.getRegnNo());
		}
		if (marriageRegnCriteria.getMarriageDate() != null) {
			selectQuery.append(" AND mr.marriagedate = ?");
			preparedStatementValues.add(marriageRegnCriteria.getMarriageDate());
		}
		if (marriageRegnCriteria.getHusbandName() != null) {
			selectQuery.append(" AND mr.husbandname = ?");
			preparedStatementValues.add(marriageRegnCriteria.getHusbandName());
		}
		if (marriageRegnCriteria.getWifeName() != null) {
			selectQuery.append(" AND mr.wifename = ?");
			preparedStatementValues.add(marriageRegnCriteria.getWifeName());
		}
		if ((marriageRegnCriteria.getFromDate() != null) && (marriageRegnCriteria.getToDate() != null)) {
			selectQuery.append(" AND mr.marriagedate between ? and ?");
			preparedStatementValues.add(marriageRegnCriteria.getFromDate());
			preparedStatementValues.add(marriageRegnCriteria.getToDate());
		}
		if (marriageRegnCriteria.getMarriageDate() != null) {
			selectQuery.append(" AND mr.marriagedate = ?");
			preparedStatementValues.add(marriageRegnCriteria.getMarriageDate());
		}
		if (marriageRegnCriteria.getRegnUnit() != null) {
			selectQuery.append(" AND mr.regunit = ?");
			preparedStatementValues.add(marriageRegnCriteria.getRegnUnit());
		}
	}
	
	private void addOrderByClause(StringBuilder selectQuery, MarriageRegnCriteria marriageRegnCriteria) {
		String sortBy = (marriageRegnCriteria.getSortBy() == null ? "mr_marriagedate" : marriageRegnCriteria.getSortBy());
		String sortOrder = (marriageRegnCriteria.getSortOrder() == null ? "DESC" : marriageRegnCriteria.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);		
	}
	
	private void addPagingClause(StringBuilder selectQuery, List<Object> preparedStatementValues,
			MarriageRegnCriteria marriageRegnCriteria) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.marriageRegnSearchPageSizeDefault());
		if (marriageRegnCriteria.getPageSize() != null)
			pageSize = marriageRegnCriteria.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (marriageRegnCriteria.getPageNo() != null)
			pageNumber = marriageRegnCriteria.getPageNo() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to pageNo * pageSize
	}
	
	private static String getIdQuery(List<String> nosList) {
		StringBuilder query = new StringBuilder("('");
		if (nosList.size() >= 1) {
			query.append(nosList.get(0).toString());
			for (int i = 1; i < nosList.size(); i++) {
				query.append("', '" + nosList.get(i));
			}
		}
		return query.append("')").toString();
	}

}