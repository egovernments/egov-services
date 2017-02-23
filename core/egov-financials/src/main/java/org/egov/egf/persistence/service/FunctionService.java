package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.Function;
import org.egov.egf.persistence.queue.contract.FunctionContract;
import org.egov.egf.persistence.queue.contract.FunctionContractRequest;
import org.egov.egf.persistence.repository.FunctionRepository;
import org.egov.egf.persistence.specification.FunctionSpecification;
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
public class FunctionService  {

  private final FunctionRepository functionRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public FunctionService(final FunctionRepository functionRepository) {
   this.functionRepository = functionRepository;
  }

@Autowired
	private SmartValidator validator;   @Transactional
   public Function create(final Function function) {
  return functionRepository.save(function);
  } 
   @Transactional
   public Function update(final Function function) {
  return functionRepository.save(function);
    } 
  public List<Function> findAll() {
   return functionRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public Function findByName(String name){
  return functionRepository.findByName(name);
  }
  public Function findByCode(String code){
  return functionRepository.findByCode(code);
  }
  public Function findOne(Long id){
  return functionRepository.findOne(id);
  }
  public Page<Function> search(FunctionContractRequest functionContractRequest){
final FunctionSpecification specification = new FunctionSpecification(functionContractRequest.getFunction());
Pageable page = new PageRequest(functionContractRequest.getPage().getOffSet(),functionContractRequest.getPage().getPageSize());
  return functionRepository.findAll(specification,page);
  }
public BindingResult validate(FunctionContractRequest functionContractRequest, String method,BindingResult errors) { 
	 
		try { 
			switch(method) 
			{ 
			case "update": 
				Assert.notNull(functionContractRequest.getFunction(), "Function to edit must not be null"); 
				validator.validate(functionContractRequest.getFunction(), errors); 
				break; 
			case "view": 
				//validator.validate(functionContractRequest.getFunction(), errors); 
				break; 
			case "create": 
				Assert.notNull(functionContractRequest.getFunctions(), "Functions to create must not be null"); 
				for(FunctionContract b:functionContractRequest.getFunctions()) 
				 validator.validate(b, errors); 
				break; 
			case "updateAll": 
				Assert.notNull(functionContractRequest.getFunctions(), "Functions to create must not be null"); 
				for(FunctionContract b:functionContractRequest.getFunctions()) 
				 validator.validate(b, errors); 
				break; 
			default : validator.validate(functionContractRequest.getRequestInfo(), errors); 
			} 
		} catch (IllegalArgumentException e) { 
			 errors.addError(new ObjectError("Missing data", e.getMessage())); 
		} 
		return errors; 
 
	}
}