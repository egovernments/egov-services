package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.FinancialYear;
import org.egov.egf.persistence.queue.contract.FinancialYearContract;
import org.egov.egf.persistence.queue.contract.FinancialYearContractRequest;
import org.egov.egf.persistence.repository.FinancialYearRepository;
import org.egov.egf.persistence.specification.FinancialYearSpecification;
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
public class FinancialYearService  {

  private final FinancialYearRepository financialYearRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public FinancialYearService(final FinancialYearRepository financialYearRepository) {
   this.financialYearRepository = financialYearRepository;
  }

@Autowired
	private SmartValidator validator;   @Transactional
   public FinancialYear create(final FinancialYear financialYear) {
  return financialYearRepository.save(financialYear);
  } 
   @Transactional
   public FinancialYear update(final FinancialYear financialYear) {
  return financialYearRepository.save(financialYear);
    } 
  public List<FinancialYear> findAll() {
   return financialYearRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public FinancialYear findOne(Long id){
  return financialYearRepository.findOne(id);
  }
  public Page<FinancialYear> search(FinancialYearContractRequest financialYearContractRequest){
final FinancialYearSpecification specification = new FinancialYearSpecification(financialYearContractRequest.getFinancialYear());
Pageable page = new PageRequest(financialYearContractRequest.getPage().getOffSet(),financialYearContractRequest.getPage().getPageSize());
  return financialYearRepository.findAll(specification,page);
  }
public BindingResult validate(FinancialYearContractRequest financialYearContractRequest, String method,BindingResult errors) { 
	 
		try { 
			switch(method) 
			{ 
			case "update": 
				Assert.notNull(financialYearContractRequest.getFinancialYear(), "FinancialYear to edit must not be null"); 
				validator.validate(financialYearContractRequest.getFinancialYear(), errors); 
				break; 
			case "view": 
				//validator.validate(financialYearContractRequest.getFinancialYear(), errors); 
				break; 
			case "create": 
				Assert.notNull(financialYearContractRequest.getFinancialYears(), "FinancialYears to create must not be null"); 
				for(FinancialYearContract b:financialYearContractRequest.getFinancialYears()) 
				 validator.validate(b, errors); 
				break; 
			case "updateAll": 
				Assert.notNull(financialYearContractRequest.getFinancialYears(), "FinancialYears to create must not be null"); 
				for(FinancialYearContract b:financialYearContractRequest.getFinancialYears()) 
				 validator.validate(b, errors); 
				break; 
			default : validator.validate(financialYearContractRequest.getRequestInfo(), errors); 
			} 
		} catch (IllegalArgumentException e) { 
			 errors.addError(new ObjectError("Missing data", e.getMessage())); 
		} 
		return errors; 
 
	}
}