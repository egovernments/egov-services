package org.egov.hrms.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.hrms.web.contract.EmployeeSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

import org.egov.hrms.model.Employee;

@Repository
@Slf4j
public class EmployeeRepository {
	
	@Autowired
	private EmployeeQueryBuilder queryBuilder;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private EmployeeRowMapper rowMapper;
	
	public List<Employee> fetchEmployees(EmployeeSearchCriteria criteria, RequestInfo requestInfo){
		List<Employee> employees = new ArrayList<>();
		String query = queryBuilder.getEmployeeSearchQuery(criteria);
		try {
			employees = jdbcTemplate.query(query, rowMapper);
		}catch(Exception e) {
			log.error("Exception while making the db call: ",e);
			log.error("query; "+query);
		}
		return employees;
	}

}
