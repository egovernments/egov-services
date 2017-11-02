package org.egov.lcms.repository.builder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.egov.lcms.models.Case;
import org.egov.lcms.models.CaseSearchCriteria;
import org.egov.lcms.repository.AdvocateSearchRepository;
import org.egov.lcms.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CaseBuilder {

	@Autowired
	AdvocateSearchRepository advocateSearchRepository;

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

		// if (caseSearchCriteria.getAdvocateName() != null &&
		// caseSearchCriteria.getSummonReferenceNo() == null
		// && caseSearchCriteria.getCaseRefernceNo() == null &&
		// caseSearchCriteria.getCaseStatus() == null
		// && caseSearchCriteria.getCaseType() == null &&
		// caseSearchCriteria.getDepartmentName() == null
		// && caseSearchCriteria.getCaseCategory() == null &&
		// caseSearchCriteria.getFromDate() == null
		// && caseSearchCriteria.getToDate() == null) {
		// selectQuery.append(" AND code IN (" +
		// getAdvocateInnerQuery(caseSearchCriteria.getAdvocateName(),
		// caseSearchCriteria.getTenantId(), preparedStatementValues));
		// }

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

		if (caseSearchCriteria.getCaseRefernceNo() != null && !caseSearchCriteria.getCaseRefernceNo().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" caserefernceno= ?");
			preparedStatementValues.add(caseSearchCriteria.getCaseRefernceNo());
		}

		if (caseSearchCriteria.getCaseType() != null && !caseSearchCriteria.getCaseType().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" casetype ->>'name'=?");
			preparedStatementValues.add(caseSearchCriteria.getCaseType());

		}

		if (caseSearchCriteria.getCaseStatus() != null && !caseSearchCriteria.getCaseStatus().isEmpty()) {
			/*
			 * isAppendAndClause = addAndClauseIfRequired(isAppendAndClause,
			 * selectQuery); selectQuery.append(" casetype ->>'name'=?");
			 * preparedStatementValues.add(caseSearchCriteria.getCaseType());
			 */
			// TODO need to add after hearing api integration

		}

		if (caseSearchCriteria.getDepartmentName() != null && !caseSearchCriteria.getDepartmentName().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" departmentname =?");
			preparedStatementValues.add(caseSearchCriteria.getDepartmentName());

		}

		if (caseSearchCriteria.getAdvocateName() != null && !caseSearchCriteria.getAdvocateName().isEmpty()) {
			List<String> caseCodes = advocateSearchRepository
					.getcaseCodeByAdvocateName(caseSearchCriteria.getAdvocateName());
			int count = 1;

			String caseCodeIds = "";
			for (String caseObj : caseCodes) {
				if (count < caseSearchCriteria.getCode().length)
					caseCodeIds = caseCodeIds + "'" + caseObj + "',";
				else
					caseCodeIds = caseCodeIds + "'" + caseObj + "'";

				count++;
			}
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" code IN(" + caseCodeIds + ")");

		}

		if (caseSearchCriteria.getCaseType() != null && !caseSearchCriteria.getCaseType().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			preparedStatementValues.add(caseSearchCriteria.getCaseType());
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

	}

	private String getAdvocateInnerQuery(String advocateName, String tenantId,
			final List<Object> preparedStatementValues) {
		StringBuilder advocateInnerQuery = new StringBuilder();
		advocateInnerQuery.append("select casecode from " + ConstantUtility.ADVOCATE_DETAILS_TABLE_NAME
				+ " WHERE advocate ->> 'name'=? )");
		preparedStatementValues.add(advocateName);

		if (tenantId != null) {
			advocateInnerQuery.append(" AND tenantid=?");
			preparedStatementValues.add(tenantId);
		}
		return null;
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
		}
	}

	public String searchByCaseCodeQuery(Case casee, String tableName, Boolean isTenantIdExixts,
			final List<Object> preparedStatementValues) {
		StringBuilder paraWiseSearchQuery = new StringBuilder();
		paraWiseSearchQuery.append("SELECT * FROM " + tableName);

		paraWiseSearchQuery.append(" WHERE casecode=?");
		preparedStatementValues.add(casee.getCode());

		if (isTenantIdExixts) {
			paraWiseSearchQuery.append(" AND tenantid=?");
			preparedStatementValues.add(casee.getTenantId());
		}

		return paraWiseSearchQuery.toString();
	}
}
