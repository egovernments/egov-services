package org.egov.pt.calculator.repository;

import org.apache.commons.lang3.StringUtils;
import org.egov.pt.calculator.web.models.property.BillingSlabSearcCriteria;
import org.springframework.stereotype.Component;

@Component
public class BillingSlabQueryBuilder {

	public String getBillingSlabSearchQuery(BillingSlabSearcCriteria billingSlabSearcCriteria) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT * FROM eg_pt_billingslab");
		addWhereClause(queryBuilder, billingSlabSearcCriteria);
		
		return queryBuilder.toString();
		
	}
	
	public void addWhereClause(StringBuilder queryBuilder, BillingSlabSearcCriteria billingSlabSearcCriteria) {
		queryBuilder.append(" WHERE (").append(" tenantId = "+billingSlabSearcCriteria.getTenantId());
		
		if(!StringUtils.isEmpty(billingSlabSearcCriteria.getPropertyType()))
			queryBuilder.append(" AND propertyType = "+billingSlabSearcCriteria.getPropertyType());
		
		if(!StringUtils.isEmpty(billingSlabSearcCriteria.getPropertySubType()))
			queryBuilder.append(" AND propertySubType = "+billingSlabSearcCriteria.getPropertySubType());
		
		if(!StringUtils.isEmpty(billingSlabSearcCriteria.getUsageCategoryMajor()))
			queryBuilder.append(" AND usageCategoryMajor = "+billingSlabSearcCriteria.getUsageCategoryMajor());
		
		if(!StringUtils.isEmpty(billingSlabSearcCriteria.getPropertyType()))
			queryBuilder.append(" AND usageCategoryMinor = "+billingSlabSearcCriteria.getUsageCategoryMinor());
		
		queryBuilder.append(")");
	}
}
