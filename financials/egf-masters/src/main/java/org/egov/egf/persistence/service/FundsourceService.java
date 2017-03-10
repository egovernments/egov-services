package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.Fundsource;
import org.egov.egf.persistence.queue.contract.FundsourceContract;
import org.egov.egf.persistence.queue.contract.FundsourceContractRequest;
import org.egov.egf.persistence.repository.FundsourceRepository;
import org.egov.egf.persistence.specification.FundsourceSpecification;
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
public class FundsourceService  {

  private final FundsourceRepository fundsourceRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public FundsourceService(final FundsourceRepository fundsourceRepository) {
   this.fundsourceRepository = fundsourceRepository;
  }

@Autowired
	private SmartValidator validator;   @Transactional
   public Fundsource create(final Fundsource fundsource) {
  return fundsourceRepository.save(fundsource);
  } 
   @Transactional
   public Fundsource update(final Fundsource fundsource) {
  return fundsourceRepository.save(fundsource);
    } 
  public List<Fundsource> findAll() {
   return fundsourceRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public Fundsource findByName(String name){
  return fundsourceRepository.findByName(name);
  }
  public Fundsource findByCode(String code){
  return fundsourceRepository.findByCode(code);
  }
  public Fundsource findOne(Long id){
  return fundsourceRepository.findOne(id);
  }
  public Page<Fundsource> search(FundsourceContractRequest fundsourceContractRequest){
final FundsourceSpecification specification = new FundsourceSpecification(fundsourceContractRequest.getFundsource());
Pageable page = new PageRequest(fundsourceContractRequest.getPage().getOffSet(),fundsourceContractRequest.getPage().getPageSize());
  return fundsourceRepository.findAll(specification,page);
  }
public BindingResult validate(FundsourceContractRequest fundsourceContractRequest, String method,BindingResult errors) { 
	 
		try { 
			switch(method) 
			{ 
			case "update": 
				Assert.notNull(fundsourceContractRequest.getFundsource(), "Fundsource to edit must not be null"); 
				validator.validate(fundsourceContractRequest.getFundsource(), errors); 
				break; 
			case "view": 
				//validator.validate(fundsourceContractRequest.getFundsource(), errors); 
				break; 
			case "create": 
				Assert.notNull(fundsourceContractRequest.getFundsources(), "Fundsources to create must not be null"); 
				for(FundsourceContract b:fundsourceContractRequest.getFundsources()) 
				 validator.validate(b, errors); 
				break; 
			case "updateAll": 
				Assert.notNull(fundsourceContractRequest.getFundsources(), "Fundsources to create must not be null"); 
				for(FundsourceContract b:fundsourceContractRequest.getFundsources()) 
				 validator.validate(b, errors); 
				break; 
			default : validator.validate(fundsourceContractRequest.getRequestInfo(), errors); 
			} 
		} catch (IllegalArgumentException e) { 
			 errors.addError(new ObjectError("Missing data", e.getMessage())); 
		} 
		return errors; 
 
	}
}