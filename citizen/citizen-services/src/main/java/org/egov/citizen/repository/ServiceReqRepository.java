package org.egov.citizen.repository;

import java.util.Date;

import org.egov.citizen.model.ServiceReq;
import org.egov.citizen.model.ServiceReqRequest;
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

	public void persistServiceReq(ServiceReqRequest serviceReqRequest) {
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
				+ " jsonvalue=? WHERE id=? and tenantid=?;";
		final Object[] obj = new Object[] { requestInfo.getUserInfo().getId(), serviceReq.getServiceCode(),
				serviceReq.getConsumerCode(), serviceReq.getEmail(), serviceReq.getPhone(), serviceReq.getAssignedTo(),
				new Date().getTime(), requestInfo.getUserInfo().getId(), jsonValue, serviceReq.getServiceRequestId(),
				serviceReq.getTenantId() };
		try {
			jdbcTemplate.update(query, obj);
		} catch (final Exception ex) {
			log.info("the exception from insert query : " + ex);
		}
	}

}
