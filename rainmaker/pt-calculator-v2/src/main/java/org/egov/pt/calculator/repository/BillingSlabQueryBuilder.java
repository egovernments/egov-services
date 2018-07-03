package org.egov.pt.calculator.repository;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.pt.calculator.web.models.BillingSlabSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class BillingSlabQueryBuilder {

	public String getBillingSlabSearchQuery(BillingSlabSearchCriteria billingSlabSearcCriteria) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT * FROM eg_pt_billingslab_v2");
		addWhereClause(queryBuilder, billingSlabSearcCriteria);
		
		return queryBuilder.toString();
		
	}
	
	public void addWhereClause(StringBuilder queryBuilder, BillingSlabSearchCriteria billingSlabSearcCriteria) {
		queryBuilder.append(" WHERE ").append(" tenantId = '"+billingSlabSearcCriteria.getTenantId()+"'");
		
		if(!CollectionUtils.isEmpty(billingSlabSearcCriteria.getId()))
			queryBuilder.append(" AND id IN ("+convertListToString(billingSlabSearcCriteria.getId())+")");
		
		if(!StringUtils.isEmpty(billingSlabSearcCriteria.getPropertyType()))
			queryBuilder.append(" AND propertyType = '"+billingSlabSearcCriteria.getPropertyType()+"'");
		
		if(!StringUtils.isEmpty(billingSlabSearcCriteria.getPropertySubType()))
			queryBuilder.append(" AND propertySubType = '"+billingSlabSearcCriteria.getPropertySubType()+"'");
		
		if(!StringUtils.isEmpty(billingSlabSearcCriteria.getUsageCategoryMajor()))
			queryBuilder.append(" AND usageCategoryMajor = '"+billingSlabSearcCriteria.getUsageCategoryMajor()+"'");
		
		if(!StringUtils.isEmpty(billingSlabSearcCriteria.getPropertyType()))
			queryBuilder.append(" AND usageCategoryMinor = '"+billingSlabSearcCriteria.getUsageCategoryMinor()+"'");
		
	}
	
	private String convertListToString(List<String> ids) {
		
		final String quotes = "'";
		final String comma = ",";
		StringBuilder builder = new StringBuilder();
		Iterator<String> iterator = ids.iterator();
		while(iterator.hasNext()) {
			builder.append(quotes+iterator.next()+quotes);
			if(iterator.hasNext()) builder.append(comma);
		}
		return builder.toString();
	}
}
