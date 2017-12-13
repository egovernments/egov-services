package org.egov.lcms.repository.builder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.egov.lcms.models.Case;
import org.egov.lcms.models.CaseSearchCriteria;
import org.egov.lcms.models.EventSearchCriteria;
import org.egov.lcms.models.HearingRepository;
import org.egov.lcms.repository.AdvocateRepository;
import org.egov.lcms.util.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CaseBuilder {

	@Autowired
	AdvocateRepository AdvocateRepository;

	@Autowired
	HearingRepository hearingRepository;

	private static final String SELECT_BASE_QUERY = "SELECT * FROM egov_lcms_case";

	public String getQuery(final CaseSearchCriteria caseSearchCriteria, final List<Object> preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(SELECT_BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, caseSearchCriteria);
		addOrderByClause(selectQuery, caseSearchCriteria);
		addPagingClause(selectQuery, preparedStatementValues, caseSearchCriteria);
		log.info("selectQuery::" + selectQuery);
		log.info("preparedstmt values : " + preparedStatementValues);
		return selectQuery.toString();
	}

	private void addWhereClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
			final CaseSearchCriteria caseSearchCriteria) {

		if (caseSearchCriteria.getTenantId() == null && caseSearchCriteria.getTenantId().isEmpty())
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (caseSearchCriteria.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" tenantid = ?");
			preparedStatementValues.add(caseSearchCriteria.getTenantId());
		}

		if (caseSearchCriteria.getCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" code IN ("
					+ Stream.of(caseSearchCriteria.getCode()).collect(Collectors.joining("','", "'", "'")) + ")");
		}

		if (caseSearchCriteria.getSummonReferenceNo() != null && !caseSearchCriteria.getSummonReferenceNo().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" summonreferenceno= ?");
			preparedStatementValues.add(caseSearchCriteria.getSummonReferenceNo());
		}

		if (caseSearchCriteria.getCaseReferenceNo() != null && !caseSearchCriteria.getCaseReferenceNo().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" caserefernceno= ?");
			preparedStatementValues.add(caseSearchCriteria.getCaseReferenceNo());
		}

		if (caseSearchCriteria.getCaseType() != null && !caseSearchCriteria.getCaseType().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" casetype ->>'code'=?");
			preparedStatementValues.add(caseSearchCriteria.getCaseType());

		}

		if (caseSearchCriteria.getCaseStatus() != null && !caseSearchCriteria.getCaseStatus().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" casestatus->>'code'=?");
			preparedStatementValues.add(caseSearchCriteria.getCaseStatus());
		}

		if (caseSearchCriteria.getDepartmentName() != null && !caseSearchCriteria.getDepartmentName().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" departmentname->>'code'=?");
			preparedStatementValues.add(caseSearchCriteria.getDepartmentName());

		}

		if (caseSearchCriteria.getAdvocateName() != null && !caseSearchCriteria.getAdvocateName().isEmpty()) {
			List<String> caseCodes = AdvocateRepository.getcaseCodeByAdvocateCode(caseSearchCriteria.getAdvocateName());
				
			int count = 1;
			String code = "";
			
			if (caseCodes.size() > 0) {
				
				for (String caseCode : caseCodes) {
					if (count < caseCodes.size())
						code = code + "'" + caseCode + "',";
					else
						code = code + "'" + caseCode + "'";
					count++;

				}
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" code IN(" + code + ")");
			} else {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" code IN('" + code + "')");
			}
		}

		if (caseSearchCriteria.getFromDate() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" caseRegistrationDate>=? ");
			preparedStatementValues.add(caseSearchCriteria.getFromDate());
		} else if (caseSearchCriteria.getToDate() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" caseRegistrationDate<=? ");
			preparedStatementValues.add(caseSearchCriteria.getToDate());
		}

		if (caseSearchCriteria.getCaseNo() != null && !caseSearchCriteria.getCaseNo().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" caseno=? ");
			preparedStatementValues.add(caseSearchCriteria.getCaseNo());
		}
		
		if (caseSearchCriteria.getEntryType() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" LOWER(entrytype)= LOWER(?) ");
			preparedStatementValues.add(caseSearchCriteria.getEntryType());
		}
	}

	/**
	 * This method is always called at the beginning of the method so that and
	 * is prepended before the field's predicate is handled.
	 *
	 * @param appendAndClauseFlag
	 * @param queryString
	 * @return boolean indicates if the next predicate should append an "AND"
	 */
	private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");
		return true;
	}

	private void addPagingClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
			final CaseSearchCriteria caseSearchCriteria) {

		if (caseSearchCriteria.getPageNumber() != null && caseSearchCriteria.getPageSize() != null) {
			int offset = 0;
			int limit = caseSearchCriteria.getPageNumber() * caseSearchCriteria.getPageSize();

			if (caseSearchCriteria.getPageNumber() <= 1)
				offset = (limit - caseSearchCriteria.getPageSize());
			else
				offset = (limit - caseSearchCriteria.getPageSize()) + 1;

			selectQuery.append(" offset ?  limit ?");
			preparedStatementValues.add(offset);
			preparedStatementValues.add(limit);
		}
	}

	private void addOrderByClause(final StringBuilder selectQuery, final CaseSearchCriteria caseSearchCriteria) {

		if (caseSearchCriteria.getSort() != null && !caseSearchCriteria.getSort().isEmpty()) {
			selectQuery.append(" ORDER BY " + caseSearchCriteria.getSort());
		} else {
			selectQuery.append(" ORDER BY lastmodifiedtime desc");
		}
	}

	public String searchByCaseCodeQuery(Case caseObj, String tableName, final List<Object> preparedStatementValues) {
		StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT * FROM " + tableName);
		log.info("case code for getting hearing details is" + caseObj.getCode());

		searchQuery.append(" WHERE casecode=?");
		preparedStatementValues.add(caseObj.getCode());

		searchQuery.append(" AND tenantid=?  order by createdtime asc");
		preparedStatementValues.add(caseObj.getTenantId());

		return searchQuery.toString();
	}

	public String searchCaseDetailsByTenantId(String tenantId, String advocateName, String caseTableName,
			List<Object> preparedStatementValues) {
		StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT caseno, summonreferenceno FROM " + caseTableName);

		searchQuery.append(" WHERE tenantId=?");
		preparedStatementValues.add(tenantId);

		if (advocateName != null && !advocateName.isEmpty()) {
			List<String> caseCodes = AdvocateRepository.getcaseCodeByAdvocateCode(advocateName);

			int count = 1;
			String code = "";
			if (caseCodes != null && caseCodes.size() > 0) {
				for (String caseCode : caseCodes) {
					if (count < caseCodes.size())
						code = code + "'" + caseCode + "',";
					else
						code = code + "'" + caseCode + "'";
					count++;

				}
				searchQuery.append(" AND code IN(" + code + ")");
			}
		}

		return searchQuery.toString();
	}

	public String searchEvent(EventSearchCriteria eventSearchCriteria, List<Object> preparedStatementValues) {
		StringBuilder eventSearchQuery = new StringBuilder();
		eventSearchQuery.append("SELECT * FROM " + ConstantUtility.EVENT_TABLE_NAME);
		eventSearchQuery.append(" WHERE tenantId=?");
		preparedStatementValues.add(eventSearchCriteria.getTenantId());

		if (eventSearchCriteria.getDepartmentConcernPerson() != null) {
			eventSearchQuery.append(" AND departmentconcernperson=?");
			preparedStatementValues.add(eventSearchCriteria.getDepartmentConcernPerson());
		}

		if (eventSearchCriteria.getModuleName() != null) {
			eventSearchQuery.append(" AND modulename=?");
			preparedStatementValues.add(eventSearchCriteria.getModuleName());
		}

		if (eventSearchCriteria.getEntity() != null) {
			eventSearchQuery.append(" AND entity=?");
			preparedStatementValues.add(eventSearchCriteria.getEntity());
		}

		eventSearchQuery.append(" ORDER BY ? DESC");
		if (eventSearchCriteria.getSort() != null)
			preparedStatementValues.add(eventSearchCriteria.getSort());
		else
			preparedStatementValues.add("lastmodifiedtime");

		return eventSearchQuery.toString();
	}

	public String searchHearingDetails(String code, String tenantId, String tableName,
			List<Object> preparedStatementValues) {
		StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT nexthearingtime, nexthearingdate FROM " + tableName);
		searchQuery.append(" WHERE casecode=?");
		preparedStatementValues.add(code);

		if (tenantId != null && !tenantId.isEmpty()) {
			searchQuery.append(" AND tenantId=?");
			preparedStatementValues.add(tenantId);
		}

		searchQuery.append(" ORDER BY createdtime DESC LIMIT 1");

		return searchQuery.toString();
	}

}
