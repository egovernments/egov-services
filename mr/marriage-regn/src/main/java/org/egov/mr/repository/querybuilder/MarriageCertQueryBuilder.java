package org.egov.mr.repository.querybuilder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.mr.config.ApplicationProperties;
import org.egov.mr.web.contract.MarriageCertCriteria;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MarriageCertQueryBuilder {

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String BASE_QUERY = " select rc.id as rc_id,rc.regnno as rc_regnno,rc.applicantname as rc_applicantname, rc.applicantaddress as rc_applicantaddress,"
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
			+ " LEFT OUTER JOIN egmr_documents ds ON rc.id=ds.reissuecertificateid " + " WHERE rc.tenantId = ? ";

	public String getQuery(MarriageCertCriteria marriageCertCriteria, List<Object> preparedStatementValues) {
		log.info("marriageCertCriteria  getTenantId: " + marriageCertCriteria.getTenantId());
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		log.info("selectQuery  getQuery: " +selectQuery);
		addWhereClause(selectQuery, preparedStatementValues, marriageCertCriteria);
		addPagingClause(selectQuery, preparedStatementValues, marriageCertCriteria);
		log.info("selectQuery  getQuery: " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List<Object> preparedStatementValues,
			MarriageCertCriteria marriageCertCriteria) {
		preparedStatementValues.add(marriageCertCriteria.getTenantId());

		if (marriageCertCriteria.getApplicationNumber() == null && marriageCertCriteria.getRegnNo() == null)
			return;

		if (StringUtils.isNotBlank(marriageCertCriteria.getApplicationNumber())) {
			selectQuery.append(" AND rc.applicationnumber = ? ");
			preparedStatementValues.add(marriageCertCriteria.getApplicationNumber());
		}
		if (StringUtils.isNotBlank(marriageCertCriteria.getRegnNo())) {
			selectQuery.append(" AND rc.regnno = ? ");
			preparedStatementValues.add(marriageCertCriteria.getRegnNo());
		}

	}

	private void addPagingClause(StringBuilder selectQuery, List<Object> preparedStatementValues,
			MarriageCertCriteria marriageCertCriteria) {

		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.marriageCertSearchPageSizeDefault());
		log.info("pageSize :" + pageSize);
		if (marriageCertCriteria.getPageSize() != null)
			pageSize = marriageCertCriteria.getPageSize();
		log.info("pageSize :" + pageSize);

		preparedStatementValues.add(pageSize);

		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (marriageCertCriteria.getPageNo() != null)
			pageNumber = marriageCertCriteria.getPageNo() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
		// pageNo * pageSize
	}

}
