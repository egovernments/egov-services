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

package org.egov.collection.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.model.IdGenRequestInfo;
import org.egov.collection.model.IdRequest;
import org.egov.collection.model.IdRequestWrapper;
import org.egov.collection.model.ReceiptCommonModel;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.repository.ReceiptRepository;
import org.egov.collection.repository.QueryBuilder.ReceiptDetailQueryBuilder;
import org.egov.collection.web.contract.BillAccountDetail;
import org.egov.collection.web.contract.BillDetail;
import org.egov.collection.web.contract.BusinessDetailsRequestInfo;
import org.egov.collection.web.contract.BusinessDetailsResponse;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptReq;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;


@Service
public class ReceiptService {

	public static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);

	
	@Autowired
	private ReceiptRepository receiptRepository;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
    @Autowired
	private RestTemplate restTemplate;
	

	public ReceiptCommonModel getReceipts(ReceiptSearchCriteria receiptSearchCriteria) {
	 return  receiptRepository.findAllReceiptsByCriteria(receiptSearchCriteria);
	}
	
	public Receipt pushToQueue(ReceiptReq receiptReq){
		logger.info("Pushing recieptdetail to kafka queue");
		
		for(BillDetail billdetails: receiptReq.getReceipt().getBill().getBillDetails()){	
			String receiptNumber = generateReceiptNumber(receiptReq);
			logger.info("Receipt Number generated is: "+receiptNumber);
			billdetails.setReceiptNumber(receiptNumber);
			BusinessDetailsResponse businessDetailsRes = getBusinessDetails(billdetails.getBusinessService(), receiptReq);
			if(null == businessDetailsRes){
				logger.error("All business details fields are not available");
				return null;
			}
			String fund = null;
			String fundSource = null;
			String function = null;
			String department = null;
			try{
				fund = businessDetailsRes.getBusinessDetails().get(0).getFund();
				fundSource = businessDetailsRes.getBusinessDetails().get(0).getFundSource();
				function = businessDetailsRes.getBusinessDetails().get(0).getFunction();
				department = businessDetailsRes.getBusinessDetails().get(0).getDepartment();
			}catch(Exception e){
				logger.error("All business details fields are not available");
				return null;
			}
			if(((null == fund || null == fundSource) || null == function) || null == department ){
				logger.error("All business details fields are not available");
				return null;
			}else if(((fund.isEmpty() || fundSource.isEmpty()) || function.isEmpty()) || department.isEmpty()){
				logger.error("All business details fields are not available");
				return null;
			}
        	logger.info("FUND: "+fund+" FUNDSOURCE: "+fundSource+" FUNCTION: "+function+" DEPARTMENT: "+department);
        	
			for(BillAccountDetail billAccountDetails: billdetails.getBillAccountDetails()){
				List<Object> chartOfAccount = getChartOfAccountOnGlCode(billAccountDetails.getGlcode(), 
						receiptReq.getReceipt().getTenantId(), receiptReq.getRequestInfo());
				logger.info("chartOfAccount: "+chartOfAccount);
				if(chartOfAccount.size() <= 0){
					logger.error("Glcode invalid!: "+billAccountDetails.getGlcode());
					return null;
				}
			}
        	
        	
		}	
		return receiptRepository.pushToQueue(receiptReq);
	}
	
	@SuppressWarnings("unchecked") 
	public Receipt create(ReceiptReq receiptReq){
		logger.info("Persisting recieptdetail");
		
		Receipt receiptInfo = receiptReq.getReceipt();		
		String statusCode = null;
		long receiptHeaderId = 0L;
		
		for(BillDetail billdetails: receiptInfo.getBill().getBillDetails()){	
			
			if(billdetails.getCollectionType().equals("ONLINE")){
				statusCode = "PENDING";
			}else{
				statusCode = "TO BE SUBMITTED";
			}
			logger.info("StatusCode: "+statusCode);
			final Map<String, Object> parametersMap = new HashMap<>();
			BusinessDetailsResponse businessDetailsRes = getBusinessDetails(billdetails.getBusinessService(), receiptReq);
			if(null == businessDetailsRes){
				logger.error("All business details fields are not available");
				break;
			}
			String fund = null;
			String fundSource = null;
			String function = null;
			String department = null;
			try{
				fund = businessDetailsRes.getBusinessDetails().get(0).getFund();
				fundSource = businessDetailsRes.getBusinessDetails().get(0).getFundSource();
				function = businessDetailsRes.getBusinessDetails().get(0).getFunction();
				department = businessDetailsRes.getBusinessDetails().get(0).getDepartment();
			}catch(Exception e){
				logger.error("All business details fields are not available");
				break;
			}
        	if(((null != fund && null != fundSource) && null != function) && null != department){
        		if(((!fund.isEmpty() && !fundSource.isEmpty()) && !function.isEmpty()) && !department.isEmpty()){
					parametersMap.put("payeename", receiptInfo.getBill().getPayeeName());
					parametersMap.put("payeeaddress", receiptInfo.getBill().getPayeeAddress());
					parametersMap.put("payeeemail", receiptInfo.getBill().getPayeeEmail());
					parametersMap.put("paidby", receiptInfo.getBill().getPaidBy());
					parametersMap.put("referencenumber", billdetails.getBillNumber());
					parametersMap.put("receipttype", billdetails.getReceiptType());							
					parametersMap.put("receiptdate", billdetails.getReceiptDate());
					parametersMap.put("receiptnumber", billdetails.getReceiptNumber());
					parametersMap.put("businessdetails", billdetails.getBusinessService());
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
					
					receiptHeaderId = receiptRepository.persistToReceiptHeader(parametersMap, receiptInfo);
					
        		}else{
        			logger.error("BuisnessDetails unavailable for the code: "+billdetails.getBusinessService());
        			break;
        		}
        	}else{
    			logger.error("BuisnessDetails unavailable for the code: "+billdetails.getBusinessService());
    			break;
        	}
			Map<String, Object>[] parametersReceiptDetails = (Map<String, Object>[]) new Map[billdetails.getBillAccountDetails().size()];
			int parametersReceiptDetailsCount = 0;
			for(BillAccountDetail billAccountDetails: billdetails.getBillAccountDetails()){
				final Map<String, Object> parameterMap = new HashMap<>();
				List<Object> chartOfAccount = getChartOfAccountOnGlCode(billAccountDetails.getGlcode(), 
						receiptReq.getReceipt().getTenantId(), receiptReq.getRequestInfo());	
				logger.info("");
				if(!(chartOfAccount.size() <= 0)){
					parameterMap.put("chartofaccount", billAccountDetails.getGlcode());
					parameterMap.put("dramount", billAccountDetails.getDebitAmount());
					parameterMap.put("cramount", billAccountDetails.getCreditAmount());
					parameterMap.put("ordernumber", billAccountDetails.getOrder());
					parameterMap.put("receiptheader", receiptHeaderId);
					parameterMap.put("actualcramounttobepaid", billAccountDetails.getCreditAmount());
					parameterMap.put("description", null);
					parameterMap.put("financialyear", null);
					parameterMap.put("isactualdemand", billAccountDetails.getIsActualDemand());
					parameterMap.put("purpose", billAccountDetails.getPurpose().toString());
					parameterMap.put("tenantid", receiptInfo.getTenantId());
					
					parametersReceiptDetails[parametersReceiptDetailsCount] = parameterMap;
					parametersReceiptDetailsCount++;
				}else{
					logger.info("Glcode invalid, Hence record not inserted for COA/Gl Code: "+billAccountDetails.getGlcode());
					break;
				}
			}
			
			receiptRepository.persistToReceiptDetails(parametersReceiptDetails, receiptHeaderId);

		}
		return receiptReq.getReceipt();
	}
	
	
	public BusinessDetailsResponse getBusinessDetails(String businessDetailsCode, ReceiptReq receiptReq){
		logger.info("Searching for fund aand other businessDetails based on code.");	
		BusinessDetailsResponse businessDetailsResponse = new BusinessDetailsResponse();
		StringBuilder builder = new StringBuilder();
		String baseUri = applicationProperties.getBusinessDetailsSearch();
		String searchCriteria="?businessDetailsCode="+businessDetailsCode+"&tenantId="+receiptReq.getReceipt().getTenantId();
		builder.append(baseUri).append(searchCriteria);
		
		logger.info("URI being hit: "+builder.toString());	
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(receiptReq.getRequestInfo());
		try{
			businessDetailsResponse = restTemplate.postForObject(builder.toString(), requestInfoWrapper , BusinessDetailsResponse.class);
		}catch(Exception e){
			logger.error("Error while fetching buisnessDetails from coll-master service. "+e.getCause());
			return null;
		}
		
		logger.info("Response from coll-master: "+businessDetailsResponse.toString());
		return businessDetailsResponse;
	}
	
	public List<Object> getChartOfAccountOnGlCode(String glcode, String tenantId,  RequestInfo requestInfo ){
		logger.info("Validating if the glcode exists in the financials system.");	
		
		StringBuilder builder = new StringBuilder();
		String baseUri = applicationProperties.getChartOfAccountsSearch() ;
		String searchCriteria="?glcode="+glcode+"&tenantId="+tenantId;
		builder.append(baseUri).append(searchCriteria);
		List<Object> charOfAccounts = null;
		logger.info("URI being hit: "+builder.toString());
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		Object response = null;
		try{
			response = restTemplate.postForObject(builder.toString(), requestInfoWrapper , Object.class);
		}catch(Exception e){
			logger.error("Error while fecthing COAs for validation from financial service. "+e.getCause());
			return charOfAccounts;		
		}
		
		logger.info("Response from financials: "+response.toString());

		charOfAccounts = JsonPath.read(response, "$.chartOfAccounts");
				
		return charOfAccounts;
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
	
	public String generateReceiptNumber(ReceiptReq receiptRequest){
		logger.info("Generating receipt number for the receipt.");	
		
		StringBuilder builder = new StringBuilder();
		String baseUri = applicationProperties.getIdGeneration();
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
			return null;

		}
		logger.info("Response from id gen service: "+response.toString());
		
		String receiptNo = JsonPath.read(response, "$.idResponses[0].id");

		return receiptNo;
	}
        		
        		
	
}
	