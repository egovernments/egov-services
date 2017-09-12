package org.egov.citizen.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.citizen.model.ServiceReq;
import org.egov.citizen.repository.querybuilder.CitizenServiceQueryBuilder;
import org.egov.citizen.repository.rowmapper.ServiceRequestRowMapper;
import org.egov.citizen.web.contract.ServiceRequestSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CitizenServiceRepository {
	
	@Autowired
	private CitizenServiceQueryBuilder citizenServiceQueryBuilder;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ServiceRequestRowMapper serviceRequestRowMapper;
	
	public List<ServiceReq> getServiceRequests(ServiceRequestSearchCriteria serviceRequestSearchCriteria){
		List preparedStatementValues = new ArrayList<>();
		String query = citizenServiceQueryBuilder.getQuery(serviceRequestSearchCriteria, preparedStatementValues);
		
		List<ServiceReq> serviceRequests = jdbcTemplate.query(query, preparedStatementValues.toArray(), serviceRequestRowMapper);
		
		return serviceRequests;
	}

}
