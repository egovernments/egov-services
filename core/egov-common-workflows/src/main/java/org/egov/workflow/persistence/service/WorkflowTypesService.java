package org.egov.workflow.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.workflow.persistence.entity.WorkflowTypes;
import org.egov.workflow.persistence.repository.WorkflowTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class WorkflowTypesService {

	private final WorkflowTypesRepository workflowTypesRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public WorkflowTypesService(final WorkflowTypesRepository workflowTypesRepository) {
		this.workflowTypesRepository = workflowTypesRepository;
	}

	@Autowired
	private SmartValidator validator;

	@Transactional
	public WorkflowTypes create(final WorkflowTypes workflowTypes) {
		return workflowTypesRepository.save(workflowTypes);
	}

	@Transactional
	public WorkflowTypes update(final WorkflowTypes workflowTypes) {
		return workflowTypesRepository.save(workflowTypes);
	}

	public List<WorkflowTypes> findAll() {
		return workflowTypesRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public WorkflowTypes findOne(Long id) {
		return workflowTypesRepository.findOne(id);
	}
	
	
	 

	    public WorkflowTypes getEnabledWorkflowTypeByType(String type) {
	        return workflowTypesRepository.findByTypeAndEnabledIsTrue(type);
	    }
	    public List<String> getEnabledWorkflowType(Boolean isEnabled,String tenantId) {
	        return workflowTypesRepository.findTypeEnabled(isEnabled,tenantId);
	    }

	    
	    public List<String> getByEnabledIsNull() {
	        return workflowTypesRepository.findTypeByEnabledIsNull();
	    }
	    
	  
	    public WorkflowTypes getWorkflowTypeByType(String type) {
	        return workflowTypesRepository.findByType(type);
	    }
	    
	    public WorkflowTypes getWorkflowTypeByTypeAndTenantId(String type,String tenantId) {
	        return workflowTypesRepository.findByTypeAndTenantId(type, tenantId);
	    }

	    public List<WorkflowTypes> getAllWorkflowTypes() {
	        return workflowTypesRepository.findAll(new Sort(Sort.Direction.ASC, "type"));
	    }

	    public WorkflowTypes getWorkflowTypeById(Long id) {
	        return workflowTypesRepository.findOne(id);
	    }

/*	public Page<WorkflowTypes> search(WorkflowTypesContractRequest workflowTypesContractRequest) {
		final WorkflowTypesSpecification specification = new WorkflowTypesSpecification(
				workflowTypesContractRequest.getWorkflowTypes());
		Pageable page = new PageRequest(workflowTypesContractRequest.getPage().getOffSet(),
				workflowTypesContractRequest.getPage().getPageSize());
		return workflowTypesRepository.findAll(specification, page);
	}

	public BindingResult validate(WorkflowTypesContractRequest workflowTypesContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(workflowTypesContractRequest.getWorkflowTypes(),
						"WorkflowTypes to edit must not be null");
				validator.validate(workflowTypesContractRequest.getWorkflowTypes(), errors);
				break;
			case "view":
				// validator.validate(workflowTypesContractRequest.getWorkflowTypes(),
				// errors);
				break;
			case "create":
				Assert.notNull(workflowTypesContractRequest.getWorkflowTypeses(),
						"WorkflowTypeses to create must not be null");
				for (WorkflowTypesContract b : workflowTypesContractRequest.getWorkflowTypeses())
					validator.validate(b, errors);
				break;
			case "updateAll":
				Assert.notNull(workflowTypesContractRequest.getWorkflowTypeses(),
						"WorkflowTypeses to create must not be null");
				for (WorkflowTypesContract b : workflowTypesContractRequest.getWorkflowTypeses())
					validator.validate(b, errors);
				break;
			default:
				validator.validate(workflowTypesContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public WorkflowTypesContractRequest fetchRelatedContracts(
			WorkflowTypesContractRequest workflowTypesContractRequest) {
		return workflowTypesContractRequest;
	}*/
}