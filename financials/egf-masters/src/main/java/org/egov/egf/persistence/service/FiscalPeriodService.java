package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.FiscalPeriod;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContract;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContractRequest;
import org.egov.egf.persistence.repository.FiscalPeriodRepository;
import org.egov.egf.persistence.specification.FiscalPeriodSpecification;
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
public class FiscalPeriodService  {

  private final FiscalPeriodRepository fiscalPeriodRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public FiscalPeriodService(final FiscalPeriodRepository fiscalPeriodRepository) {
   this.fiscalPeriodRepository = fiscalPeriodRepository;
  }

@Autowired
	private SmartValidator validator;   @Transactional
   public FiscalPeriod create(final FiscalPeriod fiscalPeriod) {
  return fiscalPeriodRepository.save(fiscalPeriod);
  } 
   @Transactional
   public FiscalPeriod update(final FiscalPeriod fiscalPeriod) {
  return fiscalPeriodRepository.save(fiscalPeriod);
    } 
  public List<FiscalPeriod> findAll() {
   return fiscalPeriodRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public FiscalPeriod findByName(String name){
  return fiscalPeriodRepository.findByName(name);
  }
  public FiscalPeriod findOne(Long id){
  return fiscalPeriodRepository.findOne(id);
  }
  public Page<FiscalPeriod> search(FiscalPeriodContractRequest fiscalPeriodContractRequest){
final FiscalPeriodSpecification specification = new FiscalPeriodSpecification(fiscalPeriodContractRequest.getFiscalPeriod());
Pageable page = new PageRequest(fiscalPeriodContractRequest.getPage().getOffSet(),fiscalPeriodContractRequest.getPage().getPageSize());
  return fiscalPeriodRepository.findAll(specification,page);
  }
public BindingResult validate(FiscalPeriodContractRequest fiscalPeriodContractRequest, String method,BindingResult errors) { 
	 
		try { 
			switch(method) 
			{ 
			case "update": 
				Assert.notNull(fiscalPeriodContractRequest.getFiscalPeriod(), "FiscalPeriod to edit must not be null"); 
				validator.validate(fiscalPeriodContractRequest.getFiscalPeriod(), errors); 
				break; 
			case "view": 
				//validator.validate(fiscalPeriodContractRequest.getFiscalPeriod(), errors); 
				break; 
			case "create": 
				Assert.notNull(fiscalPeriodContractRequest.getFiscalPeriods(), "FiscalPeriods to create must not be null"); 
				for(FiscalPeriodContract b:fiscalPeriodContractRequest.getFiscalPeriods()) 
				 validator.validate(b, errors); 
				break; 
			case "updateAll": 
				Assert.notNull(fiscalPeriodContractRequest.getFiscalPeriods(), "FiscalPeriods to create must not be null"); 
				for(FiscalPeriodContract b:fiscalPeriodContractRequest.getFiscalPeriods()) 
				 validator.validate(b, errors); 
				break; 
			default : validator.validate(fiscalPeriodContractRequest.getRequestInfo(), errors); 
			} 
		} catch (IllegalArgumentException e) { 
			 errors.addError(new ObjectError("Missing data", e.getMessage())); 
		} 
		return errors; 
 
	}
}