package org.egov.hrms.repository;

import org.apache.commons.lang3.StringUtils;
import org.egov.hrms.web.contract.EmployeeSearchCriteria;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class EmployeeQueryBuilder {
	
	@Value("${egov.hrms.default.pagination.limit}")
	private Integer defaultLimit;
	
	/**
	 * Returns query for searching employees
	 * 
	 * @param criteria
	 * @return
	 */
	public String getEmployeeSearchQuery(EmployeeSearchCriteria criteria) {
		StringBuilder builder = new StringBuilder(EmployeeQueries.HRMS_GET_EMPLOYEES);
		addWhereClause(criteria, builder);
		return paginationClause(criteria, builder);
	}
	
	public String getPositionSeqQuery() {
		return EmployeeQueries.HRMS_POSITION_SEQ;
	}
	
	/**
	 * Adds where clause to the query based on the requirement.
	 * 
	 * @param criteria
	 * @param builder
	 */
	public void addWhereClause(EmployeeSearchCriteria criteria, StringBuilder builder) {
		if(!StringUtils.isEmpty(criteria.getTenantId()))
			builder.append(" employee.tenantid = '"+criteria.getTenantId()+"'");
		else
			builder.append(" employee.tenantid NOTNULL");
		
		if(!CollectionUtils.isEmpty(criteria.getCodes()))
			builder.append(" and  employee.code IN ("+createINClauseForList(criteria.getCodes())+")");
		if(!CollectionUtils.isEmpty(criteria.getIds()))
			builder.append(" and employee.id IN ("+createINClauseForIntList(criteria.getIds())+")");
		if(!CollectionUtils.isEmpty(criteria.getUuids()))
			builder.append(" and employee.uuid IN ("+createINClauseForList(criteria.getUuids())+")");
		if(!CollectionUtils.isEmpty(criteria.getDepartments()))
			builder.append(" and assignment.department IN ("+createINClauseForList(criteria.getDepartments())+")");
		if(!CollectionUtils.isEmpty(criteria.getDesignations()))
			builder.append(" and assignment.designation IN ("+createINClauseForList(criteria.getDesignations())+")");
		if(!CollectionUtils.isEmpty(criteria.getEmployeestatuses()))
			builder.append(" and employee.employeestatus IN ("+createINClauseForList(criteria.getEmployeestatuses())+")");
		if(!CollectionUtils.isEmpty(criteria.getEmployeetypes()))
			builder.append(" and employee.employeetype IN ("+createINClauseForList(criteria.getEmployeetypes())+")");
		if(!CollectionUtils.isEmpty(criteria.getNames()))
			builder.append(" and employee.name IN ("+createINClauseForList(criteria.getNames())+")");
		if(!CollectionUtils.isEmpty(criteria.getPositions()))
			builder.append(" and assignment.position IN ("+createINClauseForIntList(criteria.getPositions())+")");
		
		builder.append("and employee.active = "+criteria.getIsActive());
	}
	
	public String paginationClause(EmployeeSearchCriteria criteria, StringBuilder builder) {
		String pagination = EmployeeQueries.HRMS_PAGINATION_WRAPPER;
		pagination = pagination.replace("{}", builder.toString());
		if(null != criteria.getOffset())
			pagination = pagination.replace("$offset", criteria.getOffset().toString());
		else
			pagination = pagination.replace("$offset", "0");
		
		if(null != criteria.getLimit())
			pagination = pagination.replace("$limit", criteria.getLimit().toString());
		else
			pagination = pagination.replace("$limit", defaultLimit.toString());
		
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
			builder.append(ids.get(i));
			if (i != length - 1)
				builder.append(",");
		}
		return builder.toString();
	}
}
