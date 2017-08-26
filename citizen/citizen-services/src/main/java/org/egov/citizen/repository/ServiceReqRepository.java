package org.egov.citizen.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.citizen.model.ServiceReq;
import org.egov.citizen.model.ServiceReqRequest;
import org.egov.citizen.repository.querybuilder.ServiceReqQueryBuilder;
import org.egov.citizen.web.contract.ServiceRequestSearchCriteria;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ServiceReqRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ServiceReqQueryBuilder serviceReqQueryBuilder;
	
	public List<Map<String, Object>> search(ServiceRequestSearchCriteria serviceRequestSearchCriteria){
		log.info("ServiceReqRepository");
		final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = serviceReqQueryBuilder.getQuery(serviceRequestSearchCriteria, preparedStatementValues);
        List<Map<String, Object>> maps = null;
      //  List<Asset> assets = new ArrayList<Asset>();
        try {
            log.info("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
            maps = jdbcTemplate.queryForList(queryStr, preparedStatementValues.toArray());
            log.info("maps::" + maps);
        } catch (final Exception ex) {
        	ex.printStackTrace();
            log.info("the exception from findforcriteria : " + ex);
        }
        return maps;
		
	}

	public void persistServiceReq(ServiceReqRequest serviceReqRequest) {
		RequestInfo requestInfo = serviceReqRequest.getRequestInfo();
		ServiceReq serviceReq = serviceReqRequest.getServiceReq();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonValue = null;
		try {
			jsonValue = objectMapper.writeValueAsString(serviceReq);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query = "INSERT INTO egov_citizen_service_req(id, tenantid, userid, "
				+ "servicecode, consumercode, email, mobilenumber, assignedto, createddate, "
				+ "lastmodifiedddate, createdby, lastmodifiedby, jsonvalue, status) VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		final Object[] obj = new Object[] { serviceReq.getServiceRequestId(), serviceReq.getTenantId(),
				requestInfo.getUserInfo().getId(), serviceReq.getServiceCode(), serviceReq.getConsumerCode(),
				serviceReq.getEmail(), serviceReq.getPhone(), serviceReq.getAssignedTo(), new Date().getTime(),
				new Date().getTime(), requestInfo.getUserInfo().getId(), requestInfo.getUserInfo().getId(), jsonValue,serviceReq.getStatus() };
		try {
			jdbcTemplate.update(query, obj);
		} catch (final Exception ex) {
			log.info("the exception from insert query : " + ex);
		}
	}

	public void updateServiceReq(ServiceReqRequest serviceReqRequest) {
		RequestInfo requestInfo = serviceReqRequest.getRequestInfo();
		ServiceReq serviceReq = serviceReqRequest.getServiceReq();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonValue = null;
		try {
			jsonValue = objectMapper.writeValueAsString(serviceReqRequest);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query = "UPDATE egov_citizen_service_req SET  userid=?, servicecode=?, consumercode=?, email=?,"
				+ " mobilenumber=?, assignedto=?,  lastmodifiedddate=?, lastmodifiedby=?,"
				+ " jsonvalue=?, status=? WHERE id=? and tenantid=?;";
		final Object[] obj = new Object[] { requestInfo.getUserInfo().getId(), serviceReq.getServiceCode(),
				serviceReq.getConsumerCode(), serviceReq.getEmail(), serviceReq.getPhone(), serviceReq.getAssignedTo(),
				new Date().getTime(), requestInfo.getUserInfo().getId(), jsonValue, serviceReq.getStatus(), serviceReq.getServiceRequestId(),
				serviceReq.getTenantId() };
		try {
			jdbcTemplate.update(query, obj);
		} catch (final Exception ex) {
			log.info("the exception from insert query : " + ex);
		}
	}

}
