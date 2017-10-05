package org.egov.workflow.persistence.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.workflow.persistence.entity.WorkflowTypes;
import org.egov.workflow.persistence.repository.WorkflowTypesJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WorkflowTypesService {

	private final WorkflowTypesJdbcRepository workflowTypesjdbcRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public WorkflowTypesService(final WorkflowTypesJdbcRepository workflowTypesjdbcRepository) {
		this.workflowTypesjdbcRepository = workflowTypesjdbcRepository;
	}

	@Transactional
	public WorkflowTypes create(final WorkflowTypes workflowTypes) {
		return workflowTypesjdbcRepository.create(workflowTypes);
	}

	@Transactional
	public WorkflowTypes update(final WorkflowTypes workflowTypes) {
		return workflowTypesjdbcRepository.update(workflowTypes);
	}

	public List<WorkflowTypes> findAll(final WorkflowTypes workflowType) {
		return workflowTypesjdbcRepository.search(workflowType);
	}

	public WorkflowTypes findOne(final WorkflowTypes workflowTypes) {
		return workflowTypesjdbcRepository.findById(workflowTypes);
	}
	
	    public List<WorkflowTypes> getEnabledWorkflowTypeByType(final WorkflowTypes workflowTypes) {
	        return workflowTypesjdbcRepository.search(workflowTypes);
	    }
	    
	    public List<String> getEnabledWorkflowType(final WorkflowTypes workflowTypes) {
	    	final List<String> workFLowTypes = new ArrayList<>();
	    	if(null != workflowTypesjdbcRepository.search(workflowTypes) && !workflowTypesjdbcRepository.search(workflowTypes).isEmpty()) {
	    		for(WorkflowTypes type: workflowTypesjdbcRepository.search(workflowTypes)) {
	    			workFLowTypes.add(type.getType());
	    		}
	    	} 
	        return workFLowTypes;
	    }

	    
	    public List<WorkflowTypes> getByEnabledIsNull(final WorkflowTypes workflowTypes) {
	        return workflowTypesjdbcRepository.search(workflowTypes);
	    }
	    
	  
	    public List<WorkflowTypes> getWorkflowTypeByType(final WorkflowTypes workflowTypes) {
	        return workflowTypesjdbcRepository.search(workflowTypes);
	    }
	    
	    public List<WorkflowTypes> getWorkflowTypeByTypeAndTenantId(final WorkflowTypes workflowTypes) {
	        return workflowTypesjdbcRepository.search(workflowTypes);
	    }

	    public List<WorkflowTypes> getAllWorkflowTypes(final WorkflowTypes workFlowTypeSearchModel) {
	        return workflowTypesjdbcRepository.search(workFlowTypeSearchModel);
	    }

	    public WorkflowTypes getWorkflowTypeById(final WorkflowTypes workflowTypes) {
	        return workflowTypesjdbcRepository.findById(workflowTypes);
	    }

}