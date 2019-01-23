package org.egov.hrms.repository;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.hrms.web.contract.EmployeeSearchCriteria;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class EmployeeQueryBuilder {
	
	@Value("${egov.hrms.default.pagination.limit}")
	private Integer defaultLimit;
	
	public String getEmployeeSearchQuery(EmployeeSearchCriteria criteria) {
		StringBuilder builder = new StringBuilder(EmployeeQueries.HRMS_GET_EMPLOYEES);
		addWhereClause(criteria, builder);
		return paginationClause(criteria, builder);
	}
	
	public void addWhereClause(EmployeeSearchCriteria criteria, StringBuilder builder) {
		if(!StringUtils.isEmpty(criteria.getTenantId()))
			builder.append(" employee.tenantid = "+criteria.getTenantId());
		else
			builder.append(" employee.tenantid NOT NULL");
		
		if(!CollectionUtils.isEmpty(criteria.getCodes()))
			builder.append(" employee.code IN ("+createINClauseForList(criteria.getCodes())+")");
		if(!CollectionUtils.isEmpty(criteria.getIds()))
			builder.append(" employee.id IN ("+createINClauseForIntList(criteria.getIds())+")");
		if(!CollectionUtils.isEmpty(criteria.getUuids()))
			builder.append(" employee.uuid IN ("+createINClauseForList(criteria.getUuids())+")");
		if(!CollectionUtils.isEmpty(criteria.getDepartments()))
			builder.append(" employee.department IN ("+createINClauseForList(criteria.getDepartments())+")");
		if(!CollectionUtils.isEmpty(criteria.getDesignations()))
			builder.append(" employee.designation IN ("+createINClauseForList(criteria.getDesignations())+")");
		if(!CollectionUtils.isEmpty(criteria.getEmployeestatuses()))
			builder.append(" employee.employeestatus IN ("+createINClauseForList(criteria.getEmployeestatuses())+")");
		if(!CollectionUtils.isEmpty(criteria.getEmployeetypes()))
			builder.append(" employee.employeetype IN ("+createINClauseForList(criteria.getEmployeetypes())+")");
		if(!CollectionUtils.isEmpty(criteria.getPositions()))
			builder.append(" assignment.position IN ("+createINClauseForIntList(criteria.getPositions())+")");
	}
	
	public String paginationClause(EmployeeSearchCriteria criteria, StringBuilder builder) {
		String pagination = EmployeeQueries.HRMS_PAGINATION_WRAPPER;
		pagination.replace("{}", builder.toString());
		if(null != criteria.getOffset())
			pagination.replace("$offset", criteria.getOffset().toString());
		else
			pagination.replace("$offset", "0");
		
		if(null != criteria.getLimit())
			pagination.replace("$limit", criteria.getLimit().toString());
		else
			pagination.replace("$limit", defaultLimit.toString());
		
		return pagination;
	}
	
	private String createINClauseForList(List<String> ids) {
		StringBuilder builder = new StringBuilder();
		int length = ids.size();
		for (int i = 0; i < length; i++) {
			builder.append("'"+ids.get(i)+"'");
			if (i != length - 1)
				builder.append(",");
		}
		return builder.toString();
	}
	
	private String createINClauseForIntList(List<Integer> ids) {
		StringBuilder builder = new StringBuilder();
		int length = ids.size();
		for (int i = 0; i < length; i++) {
			builder.append(i);
			if (i != length - 1)
				builder.append(",");
		}
		return builder.toString();
	}
}
