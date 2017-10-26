package org.egov.citizen.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import org.egov.citizen.model.Comment;
import org.egov.citizen.model.CommentResponse;
import org.egov.citizen.model.Document;
import org.egov.citizen.model.ServiceReq;
import org.egov.citizen.model.ServiceReqRequest;
import org.egov.citizen.repository.querybuilder.ServiceReqQueryBuilder;
import org.egov.citizen.repository.rowmapper.CommentsAndDocsRowMapper;
import org.egov.citizen.web.contract.PGPayload;
import org.egov.citizen.web.contract.PGPayloadResponse;
import org.egov.citizen.web.contract.ServiceRequestSearchCriteria;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

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
        String detailsQueryStr = null;
        if(null == serviceRequestSearchCriteria.getAnonymous())
        	detailsQueryStr = serviceReqQueryBuilder.getDetailsQuery(serviceRequestSearchCriteria, preparedStatementValuesForDetailsSearch);
        else if(!serviceRequestSearchCriteria.getAnonymous())
        	detailsQueryStr = serviceReqQueryBuilder.getDetailsQuery(serviceRequestSearchCriteria, preparedStatementValuesForDetailsSearch);
        else
        	detailsQueryStr = serviceReqQueryBuilder.getDetailsQueryForAnonymous(serviceRequestSearchCriteria, preparedStatementValuesForDetailsSearch);

        List<Map<String, Object>> maps = null;
        try {
        	    LOGGER.info("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
	            maps = jdbcTemplate.queryForList(queryStr, preparedStatementValues.toArray());
	        	LOGGER.info("comment search queryStr::" + detailsQueryStr + " preparedStatementValuesForDetailsSearch::" + preparedStatementValuesForDetailsSearch.toString());    
	            List<CommentResponse> commentRes = jdbcTemplate.query(detailsQueryStr, preparedStatementValuesForDetailsSearch.toArray(), new CommentsAndDocsRowMapper());
	            LOGGER.info("service request applications ::" + maps);
	            LOGGER.info("comments and documents ::" + commentRes);
	            List<Comment> comments = new ArrayList<>();
	            List<Document> documents = new ArrayList<>();
	            for(CommentResponse commentResponse: commentRes){
	            	Comment comment = new Comment();
	            	Document document = new Document();
	            	
	            	comment.setSrn(commentResponse.getSrn());
	            	comment.setFrom(commentResponse.getFrom());
	            	comment.setText(commentResponse.getText());
	            	comment.setTimeStamp(commentResponse.getTimeStamp());
	            
	            	
	            	document.setSrn(commentResponse.getSrn());
	            	document.setFilePath(commentResponse.getFilePath());
	            	document.setFrom(commentResponse.getDocFrom());
	            	document.setTimeStamp(commentResponse.getDocTimeStamp());
	            	document.setUploadedbyrole(commentResponse.getUploadedbyrole());
	            	
	            	comments.add(comment);	          
	            	documents.add(document);
	         
	            }
	            Map<String, Object> map = new HashMap<String, Object>();
	            
	            List<Comment> commentList = comments.stream()
                        .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(Comment :: getTimeStamp))),
                                                   ArrayList::new));
	            List<Document> documentList = documents.stream()
                        .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(Document :: getTimeStamp))),
                                                   ArrayList::new));

	            map.put("comments", commentList);
	            map.put("documents", documentList);
	            
	            maps.add(map);
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
			if(null != serviceReqRequest.getServiceReq().getComments()){
				if(!serviceReqRequest.getServiceReq().getComments().isEmpty()){
				LOGGER.info("Persisting comments");
				persistComments(serviceReqRequest);
			   }
			}
			if(null != serviceReqRequest.getServiceReq().getDocuments()){
				if(!serviceReqRequest.getServiceReq().getDocuments().isEmpty()){
				LOGGER.info("Persisting documents");
				persistDocuments(serviceReqRequest, false);
				}
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
		String query = "UPDATE egov_citizen_service_req SET  servicecode=?, consumercode=?, email=?,"
				+ " mobilenumber=?, assignedto=?,  lastmodifiedddate=?, lastmodifiedby=?,"
				+ " jsonvalue=?, status=?, modulestatus=?, additionalfee=? WHERE id=? and tenantid=?;";
		final Object[] obj = new Object[] {serviceReq.getServiceCode(),
				serviceReq.getConsumerCode(), serviceReq.getEmail(), serviceReq.getPhone(), serviceReq.getAssignedTo(),
				new Date().getTime(), requestInfo.getUserInfo().getId(), jsonValue, serviceReq.getStatus(), 
				serviceReq.getModuleStatus(), serviceReq.getAdditionalFee(), serviceReq.getServiceRequestId(), serviceReq.getTenantId() };
		try {
			jdbcTemplate.update(query, obj);
			if(null != serviceReqRequest.getServiceReq().getComments()){
				if(!serviceReqRequest.getServiceReq().getComments().isEmpty()){
				LOGGER.info("Persisting comments");
				persistComments(serviceReqRequest);
			   }
			}
			if(null != serviceReqRequest.getServiceReq().getDocuments()){
				if(!serviceReqRequest.getServiceReq().getDocuments().isEmpty()){
				LOGGER.info("Persisting documents");
				persistDocuments(serviceReqRequest, true);
				}
		  }
		} catch (final Exception ex) {
			log.info("the exception from insert query : ", ex);
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
				+ "comment, commentdate, createddate, "
				+ "lastmodifiedddate, createdby, lastmodifiedby) VALUES "
				+ "(NEXTVAL('seq_citizen_service_comments'), ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		List<Object[]> batchArgs = new ArrayList<>();
		for(Comment comment: serviceReq.getComments()){
		
			Object[] obj = new Object[] { serviceReq.getServiceRequestId(), serviceReq.getTenantId(),
					comment.getFrom(), comment.getText(), comment.getTimeStamp(), new Date().getTime(),
					new Date().getTime(), requestInfo.getUserInfo().getId(), requestInfo.getUserInfo().getId()};
			
			batchArgs.add(obj);
		}
		try {
			jdbcTemplate.batchUpdate(query, batchArgs);
		} catch (final Exception e) {
			LOGGER.info("Exception while inserting comments: ", e);
		}
	}
	
	public void persistDocuments(ServiceReqRequest serviceReqRequest, boolean isUpdate){
		RequestInfo requestInfo = serviceReqRequest.getRequestInfo();
		ServiceReq serviceReq = serviceReqRequest.getServiceReq();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonValue = null;
		try {
			jsonValue = objectMapper.writeValueAsString(serviceReq);
		} catch (JsonProcessingException e) {
			LOGGER.info("Exception while parsing the json: ", e);
		}
		if(isUpdate){
			try{
				String query = "DELETE from egov_citizen_service_req_documents WHERE srn = ?";
				jdbcTemplate.update(query, new Object[]{serviceReq.getServiceRequestId()});
			}catch(Exception e){
				LOGGER.info("Exception while deleting docs for re insert: ", e);
			}
		}
		String query = "INSERT INTO egov_citizen_service_req_documents(id, srn, tenantid, filestoreid, uploadedby, uploaddate, uploadedbyrole"
				+ ", isactive, isfinal, createddate, lastmodifiedddate, createdby, lastmodifiedby) VALUES "
				+ "(NEXTVAL('seq_citizen_service_documents'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		List<Object[]> batchArgs = new ArrayList<>();
		for(Document document: serviceReq.getDocuments()){
		
			Object[] obj = new Object[] { serviceReq.getServiceRequestId(), serviceReq.getTenantId(),
					document.getFilePath(), document.getFrom(), document.getTimeStamp(), document.getUploadedbyrole(), true, document.getIsFinal(),
					new Date().getTime(), new Date().getTime(), requestInfo.getUserInfo().getId(), requestInfo.getUserInfo().getId()};
			
			batchArgs.add(obj);
		}
		try {
			jdbcTemplate.batchUpdate(query, batchArgs);
		} catch (final Exception e) {
			LOGGER.info("Exception while inserting documents: ", e);
		}
	}
	
	public void persistPaymentData(PGPayload pgPayLoad, PGPayloadResponse pGPayLoadResponse, boolean isRequest){
		ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
		if(isRequest){
			String query = "INSERT INTO egov_citizen_service_payments(id, srn, userid, pgrequest,"
					+ " amount, tenantid, createddate, lastmodifiedddate, createdby, lastmodifiedby)"
					+ " VALUES(NEXTVAL('seq_citizen_payment'), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try{
			LOGGER.info("Json for payload: "+ writer.writeValueAsString(pgPayLoad));
			jdbcTemplate.update(query, new Object[] {
					pgPayLoad.getServiceRequestId(),
					pgPayLoad.getUid(),
					writer.writeValueAsString(pgPayLoad),
					pgPayLoad.getAmountPaid(),
					pgPayLoad.getTenantId(),
					new Date().getTime(),
					new Date().getTime(),
					pgPayLoad.getRequestInfo().getUserInfo().getId(),
					pgPayLoad.getRequestInfo().getUserInfo().getId()
			});
		}catch(Exception e){
			LOGGER.error("Payment details failed to persist", e);
		}
		}else{
			String query = "UPDATE egov_citizen_service_payments SET pgresponse = ?, transactionid = ? WHERE srn = ?";
			try{
			LOGGER.info("Json for payloadresponse: "+ writer.writeValueAsString(pGPayLoadResponse));
			jdbcTemplate.update(query, new Object[] {
					writer.writeValueAsString(pGPayLoadResponse),
					pGPayLoadResponse.getTransactionId(),
					pGPayLoadResponse.getServiceRequestId()
			});
		}catch(Exception e){
			LOGGER.error("Payment details failed to persist", e);
		}
		}
	}
}
