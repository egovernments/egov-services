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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.AuditDetails;
import org.egov.collection.model.ReceiptHeader;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.repository.QueryBuilder.ReceiptDetailQueryBuilder;
import org.egov.collection.repository.rowmapper.ReceiptRowMapper;
import org.egov.collection.web.contract.BillAccountDetails;
import org.egov.collection.web.contract.BillDetails;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


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
					applicationProperties.getCreateReceiptTopicKey(), receiptInfo);
			
		}catch(Exception e){
			logger.error("Pushing to Queue FAILED! ", e.getMessage());
			return null;
		}
		return receiptInfo;
	}
		
	@SuppressWarnings("unchecked") 
	public boolean persistCreateRequest(Receipt receiptInfo){
		logger.info("Insert process intiated");
		boolean isInsertionSuccessfull = false;
		String query = ReceiptDetailQueryBuilder.insertReceiptHeader();
		
		for(BillDetails billdetails: receiptInfo.getBillInfo().getBillDetails()){				
			final Map<String, Object> parametersMap = new HashMap<>();
			
			parametersMap.put("payeename", receiptInfo.getBillInfo().getPayeeName());
			parametersMap.put("payeeaddress", receiptInfo.getBillInfo().getPayeeAddress());
			parametersMap.put("payeeemail", receiptInfo.getBillInfo().getPayeeEmail());
			parametersMap.put("paidby", receiptInfo.getBillInfo().getPaidBy());
			parametersMap.put("referencenumber", billdetails.getRefNo());
			parametersMap.put("receipttype", billdetails.getReceiptType());
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
			parametersMap.put("fund", billdetails.getFund().getName()); //get from master using businessDetailsCode
			parametersMap.put("fundsource", billdetails.getFundSource()); //get from master using businessDetailsCode
			parametersMap.put("function", billdetails.getFunction().getName()); //get from master using businessDetailsCode
			parametersMap.put("boundary", billdetails.getBoundary());
			parametersMap.put("department", billdetails.getDepartment()); //get from master using businessDetailsCode
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
			parametersMap.put("partpaymentallowed", null);
			parametersMap.put("reference_ch_id", null);
			parametersMap.put("stateid", null);
			parametersMap.put("location", null);
			parametersMap.put("isreconciled", false);
			parametersMap.put("status", 1); //This should be retrieved from egw_status table of common service
			
			
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
				
				//validate glcode
				parameterMap.put("chartofaccount", receiptInfo.getBankAccount().getChartOfAccount().getName());
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

			}
			
			
			try{
				String queryReceiptDetails = ReceiptDetailQueryBuilder.insertReceiptDetails();
				logger.info("Inserting into receipt details for receipt header record: "+receiptHeader);
				namedParameterJdbcTemplate.batchUpdate(queryReceiptDetails, parametersReceiptDetails);
			}catch(Exception e){
				e.printStackTrace();
				logger.error("Persisting to receiptdetails table FAILED! ", e.getCause());
				isInsertionSuccessfull= false;
				return isInsertionSuccessfull;
			}

			
			
		}
				
		isInsertionSuccessfull= true;
		return isInsertionSuccessfull;
	}

}
