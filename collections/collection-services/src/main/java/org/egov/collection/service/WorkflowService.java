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

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.exception.CustomException;
import org.egov.collection.model.PositionSearchCriteriaWrapper;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.repository.ReceiptRepository;
import org.egov.collection.repository.WorkflowRepository;
import org.egov.collection.web.contract.WorkflowDetailsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;


@Service
public class WorkflowService {

	public static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);

	
	@Autowired
	private WorkflowRepository workflowRepository;
	
	@Autowired
	private ReceiptRepository receiptRepository;
	
	@Autowired
	private CollectionProducer collectionProducer;
	
	@Autowired
	private ApplicationProperties applicationProperties;
		
	public Long getPositionForUser(PositionSearchCriteriaWrapper positionSearchCriteriaWrapper){
		logger.info("PositionSearchCriteria:"+positionSearchCriteriaWrapper.toString());

		Integer position = null;
		Object response = workflowRepository.getPositionForUser(positionSearchCriteriaWrapper);
		try{
			position = JsonPath.read(response, "$.Position[0].id");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("No position returned from the service: "+e.getCause());
			throw new CustomException(Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
					CollectionServiceConstants.POSITION_EXCEPTION_MSG, CollectionServiceConstants.POSITION_EXCEPTION_DESC);
		}
		logger.info("Position fetched is: "+position);
		return Long.valueOf(position);
	}
	
	public WorkflowDetailsRequest start(WorkflowDetailsRequest workflowDetails) {		
		try{
			collectionProducer.producer(applicationProperties.getKafkaStartWorkflowTopic(),
					applicationProperties.getKafkaStartWorkflowTopicKey(), workflowDetails);
			
		}catch(Exception e){
			logger.error("Pushing to Queue FAILED! ", e.getMessage());
			return null;
		}
		return workflowDetails;
	}
	
	public WorkflowDetailsRequest update(WorkflowDetailsRequest workflowDetails) {
		workflowDetails.setStateId(receiptRepository.getStateId(workflowDetails.getReceiptHeaderId()));
		try{
			collectionProducer.producer(applicationProperties.getKafkaUpdateworkflowTopic(),
					applicationProperties.getKafkaUpdateworkflowTopicKey(), workflowDetails);
			 	
		}catch(Exception e){
			logger.error("Pushing to Queue FAILED! ", e.getMessage());
			return null;
		}
		return workflowDetails;
	}
	
	
}

