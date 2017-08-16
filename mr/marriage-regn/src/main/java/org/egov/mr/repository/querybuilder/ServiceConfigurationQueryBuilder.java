package org.egov.mr.repository.querybuilder;

import java.util.List;

import org.egov.mr.web.contract.ServiceConfigurationSearchCriteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceConfigurationQueryBuilder {

	public final String BASEQUERY = "SELECT ck.keyName as keyName, cv.value as value"
			+ " FROM egmr_serviceconfiguration ck JOIN egmr_serviceconfigurationvalues cv ON ck.id = cv.keyId ";

	private StringBuilder selectQuery;

	/**
	 * @Query for Search
	 * 
	 * @param serviceConfigurationSearchCriteria
	 * @param preparedStatementValues
	 * @return
	 */
	public String getSelectQuery(ServiceConfigurationSearchCriteria serviceConfigurationSearchCriteria,
			List<Object> preparedStatementValues) {
		selectQuery = new StringBuilder(BASEQUERY);
		addWhereCluase(serviceConfigurationSearchCriteria, preparedStatementValues);
		log.debug("QueryForSearch: " + selectQuery.toString());
		return selectQuery.toString();
	}

	/**
	 * @HelperMethod
	 * 
	 * @param serviceConfigurationSearchCriteria
	 * @param preparedStatementValues
	 */
	@SuppressWarnings("unchecked")
	private void addWhereCluase(ServiceConfigurationSearchCriteria serviceConfigurationSearchCriteria,
			@SuppressWarnings("rawtypes") List preparedStatementValues) {
		if (serviceConfigurationSearchCriteria.getTenantId() == null
				&& serviceConfigurationSearchCriteria.getName() == null
				&& serviceConfigurationSearchCriteria.getId() == null
				&& serviceConfigurationSearchCriteria.getEffectiveFrom() == null) {
			return;
		}
		selectQuery.append("WHERE ");
		boolean addappendAndClauseFlag = false;

		addappendAndClauseFlag = addAndClauseIfRequired(addappendAndClauseFlag, selectQuery);
		selectQuery.append("ck.tenantId=? ");
		preparedStatementValues.add(serviceConfigurationSearchCriteria.getTenantId());
		if (serviceConfigurationSearchCriteria.getName() != null
				&& serviceConfigurationSearchCriteria.getName() != "") {
			addappendAndClauseFlag = addAndClauseIfRequired(addappendAndClauseFlag, selectQuery);
			selectQuery.append("keyName=? ");
			preparedStatementValues.add(serviceConfigurationSearchCriteria.getName());
		}
		if (serviceConfigurationSearchCriteria.getId() != null && serviceConfigurationSearchCriteria.getId() != 0) {
			addappendAndClauseFlag = addAndClauseIfRequired(addappendAndClauseFlag, selectQuery);
			selectQuery.append("ck.id=? ");
			preparedStatementValues.add(serviceConfigurationSearchCriteria.getId());
		}
		if (serviceConfigurationSearchCriteria.getEffectiveFrom() != null) {
			addappendAndClauseFlag = addAndClauseIfRequired(addappendAndClauseFlag, selectQuery);
			selectQuery.append("cv.effectivefrom=? ");
			preparedStatementValues.add(serviceConfigurationSearchCriteria.getEffectiveFrom());
		}
		selectQuery.append("ORDER BY ck.createdTime ASC;");
	}

	/**
	 * @HelperMethod
	 * 
	 * @param appendAndClauseFlag
	 * @param queryString
	 * @return
	 */
	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag) {
			queryString.append("AND ");
		}
		return true;
	}

}
