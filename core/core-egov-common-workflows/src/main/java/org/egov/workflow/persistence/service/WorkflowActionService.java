package org.egov.workflow.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.workflow.persistence.entity.WorkflowAction;
import org.egov.workflow.persistence.repository.WorkflowActionJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WorkflowActionService {

	private final WorkflowActionJdbcRepository workflowActionJdbcRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public WorkflowActionService(final WorkflowActionJdbcRepository workflowActionJdbcRepository) {
		this.workflowActionJdbcRepository = workflowActionJdbcRepository;
	}

	@Transactional
	public WorkflowAction create(final WorkflowAction workflowAction) {
		return workflowActionJdbcRepository.create(workflowAction);
	}

	@Transactional
	public WorkflowAction update(final WorkflowAction workflowAction) {
		return workflowActionJdbcRepository.update(workflowAction);
	}

	public List<WorkflowAction> findAll(final WorkflowAction workflowAction) {
		return workflowActionJdbcRepository.search(workflowAction);
	}

	public List<WorkflowAction> findByName(final WorkflowAction workflowAction) {
		return workflowActionJdbcRepository.search(workflowAction);
	}

	public WorkflowAction findOne(final WorkflowAction workflowAction){
		return workflowActionJdbcRepository.findById(workflowAction);
	}
}

/*
 * public Page<WorkflowAction> search(WorkflowActionContractRequest
 * workflowActionContractRequest){ final WorkflowActionSpecification
 * specification = new
 * WorkflowActionSpecification(workflowActionContractRequest.getWorkflowAction()
 * ); Pageable page = new
 * PageRequest(workflowActionContractRequest.getPage().getOffSet(),
 * workflowActionContractRequest.getPage().getPageSize()); return
 * workflowActionRepository.findAll(specification,page); } public BindingResult
 * validate(WorkflowActionContractRequest workflowActionContractRequest, String
 * method,BindingResult errors) {
 * 
 * try { switch(method) { case "update":
 * Assert.notNull(workflowActionContractRequest.getWorkflowAction(),
 * "WorkflowAction to edit must not be null");
 * validator.validate(workflowActionContractRequest.getWorkflowAction(),
 * errors); break; case "view":
 * //validator.validate(workflowActionContractRequest.getWorkflowAction(),
 * errors); break; case "create":
 * Assert.notNull(workflowActionContractRequest.getWorkflowActions(),
 * "WorkflowActions to create must not be null"); for(WorkflowActionContract
 * b:workflowActionContractRequest.getWorkflowActions()) validator.validate(b,
 * errors); break; case "updateAll":
 * Assert.notNull(workflowActionContractRequest.getWorkflowActions(),
 * "WorkflowActions to create must not be null"); for(WorkflowActionContract
 * b:workflowActionContractRequest.getWorkflowActions()) validator.validate(b,
 * errors); break; default :
 * validator.validate(workflowActionContractRequest.getRequestInfo(), errors); }
 * } catch (IllegalArgumentException e) { errors.addError(new ObjectError(
 * "Missing data", e.getMessage())); } return errors;
 * 
 * } public WorkflowActionContractRequest
 * fetchRelatedContracts(WorkflowActionContractRequest
 * workflowActionContractRequest) { return workflowActionContractRequest;} }
 */