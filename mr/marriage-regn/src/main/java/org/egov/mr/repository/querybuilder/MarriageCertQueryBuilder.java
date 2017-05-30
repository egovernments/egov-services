package org.egov.mr.repository.querybuilder;

import java.util.List;

import org.egov.mr.config.ApplicationProperties;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MarriageCertQueryBuilder {
	
	private static final Logger logger = LoggerFactory.getLogger(MarriageCertQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;
	
	private static final String MARRIAGE_CERT_IDS_QUERY = "SELECT distinct applicationnumber"
			+ " FROM egmr_marriage_certificate";
	
	private static final String BASE_QUERY = "SELECT certificateno, certificatedate, certificatetype, regnnumber, bridegroomphoto,"
			+" bridephoto, husbandname, husbandaddress, wifename, wifeaddress, marriagedate, marriagevenueaddress, regndate,"
			+" regnserialno, regnvolumeno, certificateplace, templateversion, applicationnumber, stateid, approvaldepartment,"
			+" approvaldesignation, approvalassignee, approvalaction, approvalstatus, approvalcomments, tenantid"
			+" FROM egmr_marriage_certificate";
	
	public String getQueryForListOfMarriageCertIds(MarriageRegnCriteria marriageRegnCriteria,
			List<Object> preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(MARRIAGE_CERT_IDS_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, marriageRegnCriteria, null);
		addPagingClause(selectQuery, preparedStatementValues, marriageRegnCriteria);

		logger.debug("selectIdQuery : " + selectQuery);
		return selectQuery.toString();
	}
	
	public String getQuery(MarriageRegnCriteria marriageRegnCriteria, List<Object> preparedStatementValues,
			List<String> listOfApplNos) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, marriageRegnCriteria, listOfApplNos);
		addOrderByClause(selectQuery, marriageRegnCriteria);

		logger.info("selectQuery : " + selectQuery);
		return selectQuery.toString();
	}

	private void addWhereClause(StringBuilder selectQuery, List<Object> preparedStatementValues,
			MarriageRegnCriteria marriageRegnCriteria, List<String> listOfApplNos) {

		if (marriageRegnCriteria.getApplicationNumber() == null && marriageRegnCriteria.getRegnNo() == null
				&& marriageRegnCriteria.getMarriageDate() == null && marriageRegnCriteria.getHusbandName() == null
				&& marriageRegnCriteria.getWifeName() == null && marriageRegnCriteria.getFromDate() == null
				&& marriageRegnCriteria.getToDate() == null && marriageRegnCriteria.getRegnUnit() == null
				&& marriageRegnCriteria.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (marriageRegnCriteria.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" tenantid = ?");
			preparedStatementValues.add(marriageRegnCriteria.getTenantId());
		}
		
		if(marriageRegnCriteria.getApplicationNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" applicationnumber IN " + getIdQuery(marriageRegnCriteria.getApplicationNumber()));
		} else if(listOfApplNos != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" applicationnumber IN " + getIdQuery(listOfApplNos)); 
		}
		if (marriageRegnCriteria.getRegnNo() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" regnnumber = ?");
			preparedStatementValues.add(marriageRegnCriteria.getRegnNo());
		}
		if (marriageRegnCriteria.getMarriageDate() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" marriagedate = ?");
			preparedStatementValues.add(marriageRegnCriteria.getMarriageDate());
		}
		if ((marriageRegnCriteria.getFromDate() != null) && (marriageRegnCriteria.getToDate() != null)) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" mr.marriagedate between ? and ?");
			preparedStatementValues.add(marriageRegnCriteria.getFromDate());
			preparedStatementValues.add(marriageRegnCriteria.getToDate());
		}
		if (marriageRegnCriteria.getHusbandName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" husbandname = ?");
			preparedStatementValues.add(marriageRegnCriteria.getHusbandName());
		}
		if (marriageRegnCriteria.getWifeName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" wifename = ?");
			preparedStatementValues.add(marriageRegnCriteria.getWifeName());
		}
	}
	
	private void addOrderByClause(StringBuilder selectQuery, MarriageRegnCriteria marriageRegnCriteria) {
		String sortBy = (marriageRegnCriteria.getSortBy() == null ? "marriagedate" : marriageRegnCriteria.getSortBy());
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
	
	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");

		return true;
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
