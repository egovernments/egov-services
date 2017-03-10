package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.SubScheme;
import org.egov.egf.persistence.queue.contract.SubSchemeContract;
import org.egov.egf.persistence.queue.contract.SubSchemeContractRequest;
import org.egov.egf.persistence.repository.SubSchemeRepository;
import org.egov.egf.persistence.specification.SubSchemeSpecification;
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
public class SubSchemeService  {

  private final SubSchemeRepository subSchemeRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public SubSchemeService(final SubSchemeRepository subSchemeRepository) {
   this.subSchemeRepository = subSchemeRepository;
  }

@Autowired
	private SmartValidator validator;   @Transactional
   public SubScheme create(final SubScheme subScheme) {
  return subSchemeRepository.save(subScheme);
  } 
   @Transactional
   public SubScheme update(final SubScheme subScheme) {
  return subSchemeRepository.save(subScheme);
    } 
  public List<SubScheme> findAll() {
   return subSchemeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public SubScheme findByName(String name){
  return subSchemeRepository.findByName(name);
  }
  public SubScheme findByCode(String code){
  return subSchemeRepository.findByCode(code);
  }
  public SubScheme findOne(Long id){
  return subSchemeRepository.findOne(id);
  }
  public Page<SubScheme> search(SubSchemeContractRequest subSchemeContractRequest){
final SubSchemeSpecification specification = new SubSchemeSpecification(subSchemeContractRequest.getSubScheme());
Pageable page = new PageRequest(subSchemeContractRequest.getPage().getOffSet(),subSchemeContractRequest.getPage().getPageSize());
  return subSchemeRepository.findAll(specification,page);
  }
public BindingResult validate(SubSchemeContractRequest subSchemeContractRequest, String method,BindingResult errors) { 
	 
		try { 
			switch(method) 
			{ 
			case "update": 
				Assert.notNull(subSchemeContractRequest.getSubScheme(), "SubScheme to edit must not be null"); 
				validator.validate(subSchemeContractRequest.getSubScheme(), errors); 
				break; 
			case "view": 
				//validator.validate(subSchemeContractRequest.getSubScheme(), errors); 
				break; 
			case "create": 
				Assert.notNull(subSchemeContractRequest.getSubSchemes(), "SubSchemes to create must not be null"); 
				for(SubSchemeContract b:subSchemeContractRequest.getSubSchemes()) 
				 validator.validate(b, errors); 
				break; 
			case "updateAll": 
				Assert.notNull(subSchemeContractRequest.getSubSchemes(), "SubSchemes to create must not be null"); 
				for(SubSchemeContract b:subSchemeContractRequest.getSubSchemes()) 
				 validator.validate(b, errors); 
				break; 
			default : validator.validate(subSchemeContractRequest.getRequestInfo(), errors); 
			} 
		} catch (IllegalArgumentException e) { 
			 errors.addError(new ObjectError("Missing data", e.getMessage())); 
		} 
		return errors; 
 
	}
}