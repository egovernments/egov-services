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
import org.egov.collection.model.Task;
import org.egov.collection.model.TaskResponse;
import org.egov.collection.model.WorkflowDetails;
import org.egov.collection.producer.WorkflowProducer;
import org.egov.collection.repository.WorkflowRepository;
import org.egov.collection.web.contract.ProcessInstance;
import org.egov.collection.web.contract.ProcessInstanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class WorkflowService {

	public static final Logger logger = LoggerFactory.getLogger(WorkflowService.class);

	
	@Autowired
	private WorkflowRepository workflowRepository;
	
	@Autowired
	private WorkflowProducer workflowProducer;
	
	@Autowired
	private ApplicationProperties applicationProperties;
			
	
	public ProcessInstance startWorkflow(WorkflowDetails workflowDetails){
		logger.info("Persisting workflow details");
		ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
		try{
			processInstanceResponse = workflowRepository.startWorkflow(workflowDetails);
		}catch(Exception e){
			logger.error("ProcessInstance id couldn't be fetched from workflow svc", e.getCause());
		}
		if(null == processInstanceResponse){
			logger.error("Repository returned null processInstanceResponse");
			return null;

		}
		logger.info("Proccess Instance Id received is: "+processInstanceResponse.getProcessInstance().getId());
		workflowDetails.setStateId(Long.valueOf(processInstanceResponse.getProcessInstance().getId()));
		workflowDetails.setStatus(processInstanceResponse.getProcessInstance().getStatus());
	    pushToQueue(workflowDetails);
		return processInstanceResponse.getProcessInstance();
	}
	
	public Task updateWorkflow(WorkflowDetails workflowDetails){
		TaskResponse taskResponse = new TaskResponse();
		try{
			taskResponse = workflowRepository.updateWorkflow(workflowDetails);
		}catch(Exception e){
			logger.error("Task Id id couldn't be fetched from workflow svc", e.getCause());
		}
		if(null == taskResponse){
			logger.error("Repository returned null taskResponse");
			return null;

		}
		logger.info("Task Id received is: "+taskResponse.getTask().getId());
		workflowDetails.setStateId(Long.valueOf(taskResponse.getTask().getId()));
		workflowDetails.setStatus(taskResponse.getTask().getStatus());
		pushToQueue(workflowDetails);
		return taskResponse.getTask();
	}
	
	public WorkflowDetails pushToQueue(WorkflowDetails workflowDetails) {
		logger.info("Pushing workflowDetails back to kafka queue");
	
		try {
			workflowProducer.producer(applicationProperties.getKafkaUpdateStateIdTopic(),
					applicationProperties.getKafkaUpdateStateIdTopicKey(), workflowDetails);

		} catch (Exception e) {
			logger.error("Pushing to Queue FAILED! ", e.getMessage());
			return null;
		}

		return workflowDetails;
	}

	
	
}

