package org.egov.citizen.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.citizen.model.AuditDetails;
import org.egov.citizen.model.Comment;
import org.egov.citizen.model.Document;
import org.egov.citizen.model.ServiceReq;
import org.egov.citizen.model.ServiceReqRequest;
import org.egov.citizen.repository.querybuilder.ServiceReqQueryBuilder;
import org.egov.citizen.web.contract.ServiceRequestSearchCriteria;
import org.egov.citizen.web.controller.ServiceController;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ServiceReqRepository {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ServiceReqRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ServiceReqQueryBuilder serviceReqQueryBuilder;
	
	public List<Map<String, Object>> search(ServiceRequestSearchCriteria serviceRequestSearchCriteria){
		LOGGER.info("ServiceReqRepository");
		final List<Object> preparedStatementValues = new ArrayList<>();
		final List<Object> preparedStatementValuesForDetailsSearch = new ArrayList<>();

        final String queryStr = serviceReqQueryBuilder.getQuery(serviceRequestSearchCriteria, preparedStatementValues);
        final String detailsQueryStr = serviceReqQueryBuilder.getDetailsQuery(serviceRequestSearchCriteria, preparedStatementValuesForDetailsSearch);

        List<Map<String, Object>> maps = null;
        List<Map<String, Object>> detailsMap = null;

      //  List<Asset> assets = new ArrayList<Asset>();
        try {
        	LOGGER.info("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
	            maps = jdbcTemplate.queryForList(queryStr, preparedStatementValues.toArray());
	            detailsMap = jdbcTemplate.queryForList(detailsQueryStr, preparedStatementValues.toArray());
	            LOGGER.info("maps::" + maps);
	            LOGGER.info("detailsMap::" + detailsMap);
	            maps.addAll(detailsMap);
            } catch (final Exception ex) {
            	LOGGER.info("the exception from findforcriteria : ", ex);
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
				+ "lastmodifiedddate, createdby, lastmodifiedby, jsonvalue, status, modulestatus, additionalfee) VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		final Object[] obj = new Object[] { serviceReq.getServiceRequestId(), serviceReq.getTenantId(),
				requestInfo.getUserInfo().getId(), serviceReq.getServiceCode(), serviceReq.getConsumerCode(),
				serviceReq.getEmail(), serviceReq.getPhone(), serviceReq.getAssignedTo(), new Date().getTime(),
				new Date().getTime(), requestInfo.getUserInfo().getId(), requestInfo.getUserInfo().getId(), 
				jsonValue, serviceReq.getStatus(), serviceReq.getModuleStatus(), serviceReq.getAdditionalFee()};
		try {
			jdbcTemplate.update(query, obj);
			if(null != serviceReqRequest.getServiceReq().getComments() || !serviceReqRequest.getServiceReq().getComments().isEmpty()){
				LOGGER.info("Persisting comments");
				persistComments(serviceReqRequest);
			}
			if(null != serviceReqRequest.getServiceReq().getDocuments() || !serviceReqRequest.getServiceReq().getDocuments().isEmpty()){
				LOGGER.info("Persisting documents");
				persistDocuments(serviceReqRequest);
			}
		} catch (final Exception e) {
			LOGGER.info("Exception while trying to persist into svcreq table: ", e);
		}
	}

	public void updateServiceReq(ServiceReqRequest serviceReqRequest) {
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
		String query = "UPDATE egov_citizen_service_req SET  userid=?, servicecode=?, consumercode=?, email=?,"
				+ " mobilenumber=?, assignedto=?,  lastmodifiedddate=?, lastmodifiedby=?,"
				+ " jsonvalue=?, status=?, modulestatus=?, additionalfee=? WHERE id=? and tenantid=?;";
		final Object[] obj = new Object[] { requestInfo.getUserInfo().getId(), serviceReq.getServiceCode(),
				serviceReq.getConsumerCode(), serviceReq.getEmail(), serviceReq.getPhone(), serviceReq.getAssignedTo(),
				new Date().getTime(), requestInfo.getUserInfo().getId(), jsonValue, serviceReq.getStatus(), 
				serviceReq.getModuleStatus(), serviceReq.getAdditionalFee(), serviceReq.getServiceRequestId(), serviceReq.getTenantId() };
		try {
			jdbcTemplate.update(query, obj);
			if(null != serviceReqRequest.getServiceReq().getComments() || !serviceReqRequest.getServiceReq().getComments().isEmpty()){
				persistComments(serviceReqRequest);
			}
			if(null != serviceReqRequest.getServiceReq().getDocuments() || !serviceReqRequest.getServiceReq().getDocuments().isEmpty()){
				persistDocuments(serviceReqRequest);
			}
		} catch (final Exception ex) {
			log.info("the exception from insert query : " + ex);
		}
	}
	
	public void persistComments(ServiceReqRequest serviceReqRequest){
		RequestInfo requestInfo = serviceReqRequest.getRequestInfo();
		ServiceReq serviceReq = serviceReqRequest.getServiceReq();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonValue = null;
		try {
			jsonValue = objectMapper.writeValueAsString(serviceReq);
		} catch (JsonProcessingException e) {
			LOGGER.info("Exception while parsing the json: ", e);
		}
		String query = "INSERT INTO egov_citizen_service_req_comments(id, srn, tenantid, commentfrom, "
				+ "commentaddressedto, comment, commentdate, createddate, "
				+ "lastmodifiedddate, createdby, lastmodifiedby) VALUES "
				+ "(NEXTVAL('seq_citizen_service_comments'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		List<Object[]> batchArgs = new ArrayList<>();
		for(Comment comment: serviceReq.getComments()){
		
			Object[] obj = new Object[] { serviceReq.getServiceRequestId(), serviceReq.getTenantId(),
					comment.getFrom(), comment.getTo(), comment.getText(), comment.getTimeStamp(), new Date().getTime(),
					new Date().getTime(), requestInfo.getUserInfo().getId(), requestInfo.getUserInfo().getId()};
			
			batchArgs.add(obj);
		}
		try {
			jdbcTemplate.batchUpdate(query, batchArgs);
		} catch (final Exception e) {
			LOGGER.info("Exception while inserting comments: ", e);
		}
	}
	
	public void persistDocuments(ServiceReqRequest serviceReqRequest){
		RequestInfo requestInfo = serviceReqRequest.getRequestInfo();
		ServiceReq serviceReq = serviceReqRequest.getServiceReq();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonValue = null;
		try {
			jsonValue = objectMapper.writeValueAsString(serviceReq);
		} catch (JsonProcessingException e) {
			LOGGER.info("Exception while parsing the json: ", e);
		}
		String query = "INSERT INTO egov_citizen_service_req_documents(id, srn, tenantid, filestoreid"
				+ ", isactive, createddate, lastmodifiedddate, createdby, lastmodifiedby) VALUES "
				+ "(NEXTVAL('seq_citizen_service_documents'), ?, ?, ?, ?, ?, ?, ?, ?);";
		List<Object[]> batchArgs = new ArrayList<>();
		for(Document document: serviceReq.getDocuments()){
		
			Object[] obj = new Object[] { serviceReq.getServiceRequestId(), serviceReq.getTenantId(),
					document.getFileStoreId(), true, new Date().getTime(), new Date().getTime(), requestInfo.getUserInfo().getId(), 
					requestInfo.getUserInfo().getId()};
			
			batchArgs.add(obj);
		}
		try {
			jdbcTemplate.batchUpdate(query, batchArgs);
		} catch (final Exception e) {
			LOGGER.info("Exception while inserting comments: ", e);
		}
	}
}
