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

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.model.Task;
import org.egov.collection.model.TaskRequest;
import org.egov.collection.model.TaskResponse;
import org.egov.collection.model.WorkflowDetails;
import org.egov.collection.web.contract.Position;
import org.egov.collection.web.contract.ProcessInstanceRequest;
import org.egov.collection.web.contract.ProcessInstanceResponse;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowRepository {
	public static final Logger logger = LoggerFactory
			.getLogger(WorkflowRepository.class);
		
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

			
	public ProcessInstanceResponse startWorkflow(final ProcessInstanceRequest processInstanceRequest){
		ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
		StringBuilder uri = new StringBuilder();
		String basePath = applicationProperties.getWorkflowServiceHostName();
		String searchPath = applicationProperties.getWorkflowServiceStartPath();
		uri.append(basePath).append(searchPath);
		//ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		//processInstanceRequest = getProcessInstanceRequest(workflowDetails);
        final HttpEntity<ProcessInstanceRequest> request = new HttpEntity<>(processInstanceRequest);
		logger.info("ProcessInstanceRequest: "+request.toString());
		logger.info("URI: "+uri.toString());
		try{
            processInstanceResponse = restTemplate.postForObject(uri.toString(), request,
                    ProcessInstanceResponse.class);
		}catch(Exception e){
			logger.error("Exception caused while hitting the workflow service: ", e.getCause());
			processInstanceResponse = null;
			return processInstanceResponse;
		}
		
		logger.info("ProcessInstanceResponse: "+processInstanceResponse);
		return processInstanceResponse;
	}
	
	public TaskResponse updateWorkflow(WorkflowDetails workflowDetails){
		TaskResponse taskResponse = new TaskResponse();
	//	long stateId = getStateId(workflowDetails.getReceiptNumber());
		StringBuilder uri = new StringBuilder();
		String basePath = applicationProperties.getWorkflowServiceHostName();
		String searchPath = applicationProperties.getWorkflowServiceUpdatePath().replaceAll("\\{id\\}", ""+workflowDetails.getStateId()+"");
		uri.append(basePath).append(searchPath);
		TaskRequest taskRequest = new TaskRequest();
		taskRequest = getTaskRequest(workflowDetails);
		logger.info("TaskRequest: "+taskRequest.toString());
		logger.info("URI: "+uri.toString());
        final HttpEntity<TaskRequest> request = new HttpEntity<>(taskRequest);
		try{
			taskResponse = restTemplate.postForObject(uri.toString(), request,
					TaskResponse.class);
		}catch(Exception e){
			logger.error("Exception caused while hitting the workflow service: ", e.getCause());
			taskResponse = null;
			return taskResponse;
		}
		
		logger.info("TaskResponse: "+taskResponse);
		return taskResponse;
	}
	

    
    public TaskRequest getTaskRequest(final WorkflowDetails workflowDetails) {

        final RequestInfo requestInfo = workflowDetails.getRequestInfo();
		TaskRequest taskRequest = new TaskRequest();
        final Task task = new Task();
        final Position assignee = new Position();
        assignee.setId(workflowDetails.getAssignee());

        task.setBusinessKey(CollectionServiceConstants.BUSINESS_KEY);
        task.setType(CollectionServiceConstants.BUSINESS_KEY);
        task.setComments(workflowDetails.getComments());
        task.setAssignee(assignee);
        task.setTenantId(workflowDetails.getTenantId());
        task.setDetails("Receipt Create : " + workflowDetails.getReceiptNumber());
        
        task.setAction(workflowDetails.getAction());      
        task.setStatus(workflowDetails.getStatus());
        //logic based on dml and current state
        
        taskRequest.setRequestInfo(requestInfo);
        taskRequest.setTask(task);
  
        return taskRequest;
    }
    
    public boolean updateStateId(String receiptNumber, String proccessInstanceId){
    	logger.info("Updating stateId..");
    	boolean isUpdateSuccessful = false;
    	String query = "UPDATE egcl_receiptheader SET stateId=? WHERE receiptnumber=?";
    	try{
    		jdbcTemplate.update(query, new Object[] {Long.valueOf(proccessInstanceId), receiptNumber});
    	}catch(Exception e){
    		logger.error("Couldn't update stateId for the receipt: "+receiptNumber);
        	return isUpdateSuccessful;

    	}
    	isUpdateSuccessful = true;
    	return isUpdateSuccessful;
    }
    
    public long getStateId(String receiptNumber){
    	logger.info("Updating stateId..");
    	long stateId = 0L;
    	String query = "SELECT stateid FROM egcl_receiptheader WHERE receiptnumber=?";
    	try{
    		Long id = jdbcTemplate.queryForObject(query, new Object[] {receiptNumber}, Long.class);
    		stateId = Long.valueOf(id);
    	}catch(Exception e){
    		logger.error("Couldn't fetch stateId for the receipt: "+receiptNumber);
        	return stateId;
    	}
    	logger.info("StateId obtained for receipt: "+receiptNumber+" is: "+stateId);
    	return stateId;
    }


}
