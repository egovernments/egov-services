package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.Fund;
import org.egov.egf.persistence.queue.contract.FundContract;
import org.egov.egf.persistence.queue.contract.FundContractRequest;
import org.egov.egf.persistence.repository.FundRepository;
import org.egov.egf.persistence.specification.FundSpecification;
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
public class FundService  {

  private final FundRepository fundRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public FundService(final FundRepository fundRepository) {
   this.fundRepository = fundRepository;
  }

@Autowired
	private SmartValidator validator;   @Transactional
   public Fund create(final Fund fund) {
  return fundRepository.save(fund);
  } 
   @Transactional
   public Fund update(final Fund fund) {
  return fundRepository.save(fund);
    } 
  public List<Fund> findAll() {
   return fundRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public Fund findByName(String name){
  return fundRepository.findByName(name);
  }
  public Fund findByCode(String code){
  return fundRepository.findByCode(code);
  }
  public Fund findOne(Long id){
  return fundRepository.findOne(id);
  }
  public Page<Fund> search(FundContractRequest fundContractRequest){
final FundSpecification specification = new FundSpecification(fundContractRequest.getFund());
Pageable page = new PageRequest(fundContractRequest.getPage().getOffSet(),fundContractRequest.getPage().getPageSize());
  return fundRepository.findAll(specification,page);
  }
public BindingResult validate(FundContractRequest fundContractRequest, String method,BindingResult errors) { 
	 
		try { 
			switch(method) 
			{ 
			case "update": 
				Assert.notNull(fundContractRequest.getFund(), "Fund to edit must not be null"); 
				validator.validate(fundContractRequest.getFund(), errors); 
				break; 
			case "view": 
				//validator.validate(fundContractRequest.getFund(), errors); 
				break; 
			case "create": 
				Assert.notNull(fundContractRequest.getFunds(), "Funds to create must not be null"); 
				for(FundContract b:fundContractRequest.getFunds()) 
				 validator.validate(b, errors); 
				break; 
			case "updateAll": 
				Assert.notNull(fundContractRequest.getFunds(), "Funds to create must not be null"); 
				for(FundContract b:fundContractRequest.getFunds()) 
				 validator.validate(b, errors); 
				break; 
			default : validator.validate(fundContractRequest.getRequestInfo(), errors); 
			} 
		} catch (IllegalArgumentException e) { 
			 errors.addError(new ObjectError("Missing data", e.getMessage())); 
		} 
		return errors; 
 
	}
}