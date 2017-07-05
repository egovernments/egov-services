/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.collection.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.model.AuditDetails;
import org.egov.collection.model.CommonDataModel;
import org.egov.collection.model.IdGenRequestInfo;
import org.egov.collection.model.IdRequest;
import org.egov.collection.model.IdRequestWrapper;
import org.egov.collection.model.ReceiptHeader;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.repository.QueryBuilder.ReceiptDetailQueryBuilder;
import org.egov.collection.repository.rowmapper.ReceiptRowMapper;
import org.egov.collection.web.contract.BillAccountDetails;
import org.egov.collection.web.contract.BillDetails;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptReq;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;


@Repository
public class ReceiptRepository {
	public static final Logger logger = LoggerFactory
			.getLogger(ReceiptRepository.class);
	
	@Autowired
	private CollectionProducer collectionProducer;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private RestTemplate restTemplate;
	
	

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private static final String SELECT_RECEIPTHEADER_RCPT_NO = "SELECT * FROM egcl_receiptheader WHERE receiptnumber = :receiptnumber";

	public ReceiptRepository(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public List<ReceiptHeader> find(ReceiptSearchCriteria receiptSearchCriteria) {

		return getReceiptForGivenReceiptNumber(receiptSearchCriteria);
	}

	private List<ReceiptHeader> getReceiptForGivenReceiptNumber(
			ReceiptSearchCriteria receiptSearchCriteria) {
		final Map<String, Object> parametersMap = new HashMap<>();
		parametersMap.put("receiptnumber",
				receiptSearchCriteria.getReceiptNumber());
		return namedParameterJdbcTemplate.query(
				SELECT_RECEIPTHEADER_RCPT_NO, parametersMap,
				new ReceiptRowMapper());
	}
	
	public Receipt pushToQueue(ReceiptReq receiptReq) {
		Receipt receiptInfo = receiptReq.getReceipt();
		AuditDetails auditDetails = new AuditDetails();
	
		auditDetails.setCreatedBy(receiptReq.getRequestInfo().getUserInfo().getId());
		auditDetails.setLastModifiedBy(receiptReq.getRequestInfo().getUserInfo().getId());
		auditDetails.setCreatedDate(new Date(new java.util.Date().getTime()));
		auditDetails.setLastModifiedDate(new Date(new java.util.Date().getTime()));
		receiptInfo.setAuditDetails(auditDetails);
		
		try{
			collectionProducer.producer(applicationProperties.getCreateReceiptTopicName(),
					applicationProperties.getCreateReceiptTopicKey(), receiptReq);
			
		}catch(Exception e){
			logger.error("Pushing to Queue FAILED! ", e.getMessage());
			return null;
		}
		return receiptInfo;
	}
		
	@SuppressWarnings("unchecked") 
	public boolean persistCreateRequest(ReceiptReq receiptReq){
		logger.info("Insert process initiated");
		boolean isInsertionSuccessfull = false;	
		Receipt receiptInfo = receiptReq.getReceipt();		
		String statusCode = null;
		String query = ReceiptDetailQueryBuilder.insertReceiptHeader();
		
		for(BillDetails billdetails: receiptInfo.getBillInfo().getBillDetails()){	
			
			//TODO: Trigger Apportioning logic from billingservice if the amountPaid is less than the totalAmount
			
			if(billdetails.getCollectionType().equals("ONLINE")){
				statusCode = "PENDING";
			}else{
				statusCode = "TO BE SUBMITTED";
			}
			logger.info("StatusCode: "+statusCode);
			final Map<String, Object> parametersMap = new HashMap<>();
			
			Object businessDetails = getBusinessDetails(billdetails.getBusinessDetailsCode(), receiptReq);
			String fund = null;
			String fundSource = null;
			String function = null;
			String department = null;

			try{
				fund = JsonPath.read(businessDetails, "$.BusinessDetailsInfo[0].fund");
				fundSource = JsonPath.read(businessDetails, "$.BusinessDetailsInfo[0].fundSource");
				function= JsonPath.read(businessDetails, "$.BusinessDetailsInfo[0].function");
				department = JsonPath.read(businessDetails, "$.BusinessDetailsInfo[0].department");
			}catch(Exception e){
				logger.error("All business details fields are not available: "+e.getCause());
			}
        	logger.info("FUND: "+fund+" FUNDSOURCE: "+fundSource+" FUNCTION: "+function+" DEPARTMENT: "+department);
        	
        	if(((null != fund && null != fundSource) && null != function) && null != department){
				parametersMap.put("payeename", receiptInfo.getBillInfo().getPayeeName());
				parametersMap.put("payeeaddress", receiptInfo.getBillInfo().getPayeeAddress());
				parametersMap.put("payeeemail", receiptInfo.getBillInfo().getPayeeEmail());
				parametersMap.put("paidby", receiptInfo.getBillInfo().getPaidBy());
				parametersMap.put("referencenumber", billdetails.getRefNo());
				parametersMap.put("receipttype", billdetails.getReceiptType());
				
				String receiptNumber = generateReceiptNumber(receiptReq);
				logger.info("Receipt Number generated is: "+receiptNumber);
				
				billdetails.setReceiptNumber("receiptNumber");
				parametersMap.put("receiptnumber", billdetails.getReceiptNumber());
				parametersMap.put("receiptdate", billdetails.getReceiptDate());
				parametersMap.put("businessdetails", billdetails.getBusinessDetailsCode());
				parametersMap.put("collectiontype", billdetails.getCollectionType());
				parametersMap.put("reasonforcancellation", billdetails.getReasonForCancellation());
				parametersMap.put("minimumamount", billdetails.getMinimumAmount());
				parametersMap.put("totalamount", billdetails.getTotalAmount());
				parametersMap.put("collmodesnotallwd", billdetails.getCollectionModesNotAllowed().toString());
				parametersMap.put("consumercode", billdetails.getConsumerCode());
				parametersMap.put("channel", billdetails.getChannel());
				parametersMap.put("fund", fund);
				parametersMap.put("fundsource", fundSource);
				parametersMap.put("function", function);
				parametersMap.put("department", department);
				parametersMap.put("boundary", billdetails.getBoundary());
				parametersMap.put("voucherheader", billdetails.getVoucherHeader());
				parametersMap.put("depositedbranch", receiptInfo.getBankAccount().getBankBranch().getName());
				parametersMap.put("createdby", receiptInfo.getAuditDetails().getCreatedBy());
				parametersMap.put("createddate", receiptInfo.getAuditDetails().getCreatedDate());
				parametersMap.put("lastmodifiedby", receiptInfo.getAuditDetails().getLastModifiedBy());
				parametersMap.put("lastmodifieddate", receiptInfo.getAuditDetails().getLastModifiedDate());
				parametersMap.put("tenantid", receiptInfo.getTenantId());									
				parametersMap.put("referencedate", billdetails.getBillDate());
				parametersMap.put("referencedesc", billdetails.getBillDescription());
				parametersMap.put("manualreceiptnumber", null);
				parametersMap.put("manualreceiptdate", null);
				parametersMap.put("reference_ch_id", null);
				parametersMap.put("stateid", null);
				parametersMap.put("location", null);
				parametersMap.put("isreconciled", false);
				parametersMap.put("status", statusCode);
				
				try{
					logger.info("Inserting into receipt header");
					namedParameterJdbcTemplate.update(query, parametersMap);
				}catch(Exception e){
					logger.error("Persisting to DB FAILED! ",e.getCause());
					return isInsertionSuccessfull;
				}
				
				String receiptHeaderIdQuery = ReceiptDetailQueryBuilder.getreceiptHeaderId();
				Long receiptHeader = jdbcTemplate.queryForObject(receiptHeaderIdQuery, new Object[] {receiptInfo.getBillInfo().getPayeeName(),
						receiptInfo.getBillInfo().getPaidBy(), receiptInfo.getAuditDetails().getCreatedDate()}, Long.class);
				
				Map<String, Object>[] parametersReceiptDetails = (Map<String, Object>[]) new Map[billdetails.getBillAccountDetails().size()];
				int parametersReceiptDetailsCount = 0;
	
				for(BillAccountDetails billAccountDetails: billdetails.getBillAccountDetails()){
					final Map<String, Object> parameterMap = new HashMap<>();
					if(validateGLCode(billAccountDetails.getGlcode(), receiptReq.getTenantId(), receiptReq.getRequestInfo())){
						parameterMap.put("chartofaccount", billAccountDetails.getGlcode());
						parameterMap.put("dramount", billAccountDetails.getDebitAmount());
						parameterMap.put("cramount", billAccountDetails.getCreditAmount());
						parameterMap.put("ordernumber", billAccountDetails.getOrder());
						parameterMap.put("receiptheader", receiptHeader);
						parameterMap.put("actualcramounttobepaid", billAccountDetails.getCreditAmount());
						parameterMap.put("description", null);
						parameterMap.put("financialyear", null);
						parameterMap.put("isactualdemand", billAccountDetails.getIsActualDemand());
						parameterMap.put("purpose", billAccountDetails.getPurpose());
						parameterMap.put("tenantid", receiptInfo.getTenantId());
						
						parametersReceiptDetails[parametersReceiptDetailsCount] = parameterMap;
						parametersReceiptDetailsCount++;
					}else{
						logger.info("Glcode invalid, Hence record not inserted for COA/Gl Code: "+billAccountDetails.getGlcode());
					}
				}			
				try{
					String queryReceiptDetails = ReceiptDetailQueryBuilder.insertReceiptDetails();
					logger.info("Inserting into receipt details for receipt header record: "+receiptHeader);
					namedParameterJdbcTemplate.batchUpdate(queryReceiptDetails, parametersReceiptDetails);
				}catch(Exception e){
					logger.error("Persisting to receiptdetails table FAILED! ", e.getCause());
					isInsertionSuccessfull= false;
					return isInsertionSuccessfull;
				}		
		}else{
			logger.error("BuisnessDetails unavailable for the code: "+billdetails.getBusinessDetailsCode());
			logger.error("Record COULDN'T BE PERSISTED");
		}
	 }  	
		isInsertionSuccessfull= true;
		return isInsertionSuccessfull;
	}
	
	
	private Object getBusinessDetails(String businessDetailsCode, ReceiptReq receiptReq){
		logger.info("Searching for fund aand other businessDetails based on code.");	
		StringBuilder builder = new StringBuilder();
		String baseUri = CollectionServiceConstants.BD_SEARCH_URI;
		String searchCriteria="?businessDetailsCode="+businessDetailsCode+"&tenantId="+receiptReq.getReceipt().getTenantId();
		builder.append(baseUri).append(searchCriteria);
		
		logger.info("URI being hit: "+builder.toString());	
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(receiptReq.getRequestInfo());
		Object response = null;
		try{
			response = restTemplate.postForObject(builder.toString(), requestInfoWrapper , Object.class);
		}catch(Exception e){
			logger.error("Error while fetching buisnessDetails from coll-master service. "+e.getCause());
		}
		logger.info("Response from coll-master: "+response.toString());
		return response;
	}
	
	private boolean validateGLCode(String glcode, String tenantId,  RequestInfo requestInfo ){
		logger.info("Validating if the glcode exists in the financials system.");	
		boolean isCodeValid = true;
		
		StringBuilder builder = new StringBuilder();
		String baseUri = CollectionServiceConstants.COA_SEARCH_URI;
		String searchCriteria="?glcode="+glcode+"&tenantId="+tenantId;
		builder.append(baseUri).append(searchCriteria);
		
		logger.info("URI being hit: "+builder.toString());
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		Object response = null;

		try{
			response = restTemplate.postForObject(builder.toString(), requestInfoWrapper , Object.class);
		}catch(Exception e){
			logger.error("Error while fecthing COAs for validation from financial service. "+e.getCause());
		}
		
		logger.info("Response from financials: "+response.toString());

		List charOfAccounts = JsonPath.read(response, "$.chartOfAccounts");
		
		if(charOfAccounts.isEmpty())
			isCodeValid = false;
		
		return isCodeValid;
	}
	
/*	private String getStatusCode(RequestInfo requestInfo){
		logger.info("fetching status for the receipt.");	
		
		StringBuilder builder = new StringBuilder();
		String baseUri = CollectionServiceConstants.STATUS_SEARCH_URI;
		String searchCriteria="?objectType=ReceiptHeader&tenantId=default&code=SUBMITTED";
		builder.append(baseUri).append(searchCriteria);
		
		logger.info("URI being hit: "+builder.toString());
		
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		Object response = null;

		try{
			response = restTemplate.postForObject(builder.toString(), requestInfoWrapper , Object.class);
		}catch(Exception e){
			logger.error("Error while fecthing COAs for validation from financial service. "+e.getCause());
		}
		logger.info("Response from collection-masters: "+response.toString());
		
		String status = JsonPath.read(response, "$.StatusInfo[0].code");
		
		return status;
	} */
	
	private String generateReceiptNumber(ReceiptReq receiptRequest){
		logger.info("Generating receipt number for the receipt.");	
		
		StringBuilder builder = new StringBuilder();
		String baseUri = CollectionServiceConstants.ID_GEN_URI;
		builder.append(baseUri);
		
		logger.info("URI being hit: "+builder.toString());
		
		IdRequestWrapper idRequestWrapper = new IdRequestWrapper();
		IdGenRequestInfo idGenReq = new IdGenRequestInfo();
		
		//Because idGen Svc uses a slightly different form of requestInfo
		
		idGenReq.setAction(receiptRequest.getRequestInfo().getAction());
		idGenReq.setApiId(receiptRequest.getRequestInfo().getApiId());
		idGenReq.setAuthToken(receiptRequest.getRequestInfo().getAuthToken());
		idGenReq.setCorrelationId(receiptRequest.getRequestInfo().getCorrelationId());
		idGenReq.setDid(receiptRequest.getRequestInfo().getDid());
		idGenReq.setKey(receiptRequest.getRequestInfo().getKey());
		idGenReq.setMsgId(receiptRequest.getRequestInfo().getMsgId());
		idGenReq.setRequesterId(receiptRequest.getRequestInfo().getRequesterId());
		idGenReq.setTs(receiptRequest.getRequestInfo().getTs().getTime()); // this is the difference.
		idGenReq.setUserInfo(receiptRequest.getRequestInfo().getUserInfo());
		idGenReq.setVer(receiptRequest.getRequestInfo().getVer());
		
		IdRequest idRequest = new IdRequest();
		idRequest.setIdName(CollectionServiceConstants.COLL_ID_NAME);
		idRequest.setTenantId(receiptRequest.getReceipt().getTenantId());
		idRequest.setFormat(CollectionServiceConstants.COLL_ID_FORMAT);
		
		List<IdRequest> idRequests = new ArrayList<>();
		idRequests.add(idRequest);
		
		idRequestWrapper.setIdGenRequestInfo(idGenReq);
		idRequestWrapper.setIdRequests(idRequests);
		Object response = null;

		try{
			response = restTemplate.postForObject(builder.toString(), idRequestWrapper , Object.class);
		}catch(Exception e){
			logger.error("Error while generating receipt number. "+e.getCause());
		}
		logger.info("Response from id gen service: "+response.toString());
		
		String receiptNo = JsonPath.read(response, "$.idResponses[0].id");

		return receiptNo;
	}
	

}
