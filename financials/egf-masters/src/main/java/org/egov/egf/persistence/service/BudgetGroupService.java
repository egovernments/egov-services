package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.BudgetGroup;
import org.egov.egf.persistence.queue.contract.BudgetGroupContract;
import org.egov.egf.persistence.queue.contract.BudgetGroupContractRequest;
import org.egov.egf.persistence.repository.BudgetGroupRepository;
import org.egov.egf.persistence.specification.BudgetGroupSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;


@Service 
@Transactional(readOnly = true)
public class BudgetGroupService  {

  private final BudgetGroupRepository budgetGroupRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public BudgetGroupService(final BudgetGroupRepository budgetGroupRepository) {
   this.budgetGroupRepository = budgetGroupRepository;
  }

@Autowired
	private SmartValidator validator;   @Transactional
   public BudgetGroup create(final BudgetGroup budgetGroup) {
  return budgetGroupRepository.save(budgetGroup);
  } 
   @Transactional
   public BudgetGroup update(final BudgetGroup budgetGroup) {
  return budgetGroupRepository.save(budgetGroup);
    } 
  public List<BudgetGroup> findAll() {
   return budgetGroupRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public BudgetGroup findByName(String name){
  return budgetGroupRepository.findByName(name);
  }
  public BudgetGroup findOne(Long id){
  return budgetGroupRepository.findOne(id);
  }
  public Page<BudgetGroup> search(BudgetGroupContractRequest budgetGroupContractRequest){
final BudgetGroupSpecification specification = new BudgetGroupSpecification(budgetGroupContractRequest.getBudgetGroup());
Pageable page = new PageRequest(budgetGroupContractRequest.getPage().getOffSet(),budgetGroupContractRequest.getPage().getPageSize());
  return budgetGroupRepository.findAll(specification,page);
  }
public BindingResult validate(BudgetGroupContractRequest budgetGroupContractRequest, String method,BindingResult errors) { 
	 
		try { 
			switch(method) 
			{ 
			case "update": 
				Assert.notNull(budgetGroupContractRequest.getBudgetGroup(), "BudgetGroup to edit must not be null"); 
				validator.validate(budgetGroupContractRequest.getBudgetGroup(), errors); 
				break; 
			case "view": 
				//validator.validate(budgetGroupContractRequest.getBudgetGroup(), errors); 
				break; 
			case "create": 
				Assert.notNull(budgetGroupContractRequest.getBudgetGroups(), "BudgetGroups to create must not be null"); 
				for(BudgetGroupContract b:budgetGroupContractRequest.getBudgetGroups()) 
				 validator.validate(b, errors); 
				break; 
			case "updateAll": 
				Assert.notNull(budgetGroupContractRequest.getBudgetGroups(), "BudgetGroups to create must not be null"); 
				for(BudgetGroupContract b:budgetGroupContractRequest.getBudgetGroups()) 
				 validator.validate(b, errors); 
				break; 
			default : validator.validate(budgetGroupContractRequest.getRequestInfo(), errors); 
			} 
		} catch (IllegalArgumentException e) { 
			 errors.addError(new ObjectError("Missing data", e.getMessage())); 
		} 
		return errors; 
 
	}
}