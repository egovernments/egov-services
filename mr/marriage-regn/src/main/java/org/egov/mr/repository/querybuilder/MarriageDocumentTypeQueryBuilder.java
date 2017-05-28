package org.egov.mr.repository.querybuilder;

import java.util.List;

import org.egov.mr.web.contract.MarriageDocumentTypeSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MarriageDocumentTypeQueryBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(MarriageDocumentTypeQueryBuilder.class);

	public final String BASEQUERY = "SELECT * FROM marriage_document_type ";

	StringBuilder selectQuery;

	// Query for _search
	public String getSelectQuery(MarriageDocumentTypeSearchCriteria marriageDocumentTypeSearchCriteria,
			List<Object> preparedStatementValues) {
		selectQuery = new StringBuilder(BASEQUERY);
		addWhereClause(marriageDocumentTypeSearchCriteria, preparedStatementValues);
		LOGGER.info(selectQuery.toString());
		return selectQuery.toString();
	}

	private void addWhereClause(MarriageDocumentTypeSearchCriteria marriageDocumentTypeSearchCriteria,
			List<Object> preparedStatementValues) {
		if (marriageDocumentTypeSearchCriteria.getIsActive() == null
				&& marriageDocumentTypeSearchCriteria.getApplicationType() == null
				&& marriageDocumentTypeSearchCriteria.getTenantId() == null) {
			return;
		}
		selectQuery.append("WHERE ");
		boolean addAndClauseIfRequiredFlag = false;

		if (marriageDocumentTypeSearchCriteria.getIsActive() != null) {
			addAndClauseIfRequiredFlag = addAndClauseIfRequired(addAndClauseIfRequiredFlag);
			selectQuery.append("isactive=? ");
			preparedStatementValues.add(marriageDocumentTypeSearchCriteria.getIsActive());
		}
		if (marriageDocumentTypeSearchCriteria.getApplicationType() != null) {
			addAndClauseIfRequiredFlag = addAndClauseIfRequired(addAndClauseIfRequiredFlag);
			selectQuery.append("applicationtype=? ");
			preparedStatementValues.add(marriageDocumentTypeSearchCriteria.getApplicationType());
		}
		addAndClauseIfRequiredFlag = addAndClauseIfRequired(addAndClauseIfRequiredFlag);
		selectQuery.append("tenantid=? ");
		preparedStatementValues.add(marriageDocumentTypeSearchCriteria.getTenantId());
		selectQuery.append(";");
	}

	// Helper Method
	private Boolean addAndClauseIfRequired(Boolean addAndClauseIfRequiredFlag) {
		if (addAndClauseIfRequiredFlag) {
			selectQuery.append("AND ");
		}
		return true;
	}
}
