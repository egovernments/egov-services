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
		selectQuery.append("WHERE ck.tenantId=? ");
		preparedStatementValues.add(serviceConfigurationSearchCriteria.getTenantId());
		
		if (serviceConfigurationSearchCriteria.getName() != null
				&& serviceConfigurationSearchCriteria.getName() != "") {
			selectQuery.append("AND keyName=? ");
			preparedStatementValues.add(serviceConfigurationSearchCriteria.getName());
		}
		if (serviceConfigurationSearchCriteria.getId() != null && serviceConfigurationSearchCriteria.getId() != 0) {
			selectQuery.append("AND ck.id=? ");
			preparedStatementValues.add(serviceConfigurationSearchCriteria.getId());
		}
		if (serviceConfigurationSearchCriteria.getEffectiveFrom() != null) {
			selectQuery.append("AND cv.effectivefrom=? ");
			preparedStatementValues.add(serviceConfigurationSearchCriteria.getEffectiveFrom());
		}
		selectQuery.append("ORDER BY ck.keyName ASC,cv.effectivefrom DESC;");
	}
}
