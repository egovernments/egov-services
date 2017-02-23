package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.AccountDetailType;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContract;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContractRequest;
import org.egov.egf.persistence.repository.AccountDetailTypeRepository;
import org.egov.egf.persistence.specification.AccountDetailTypeSpecification;
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
public class AccountDetailTypeService  {

  private final AccountDetailTypeRepository accountDetailTypeRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public AccountDetailTypeService(final AccountDetailTypeRepository accountDetailTypeRepository) {
   this.accountDetailTypeRepository = accountDetailTypeRepository;
  }

@Autowired
	private SmartValidator validator;   @Transactional
   public AccountDetailType create(final AccountDetailType accountDetailType) {
  return accountDetailTypeRepository.save(accountDetailType);
  } 
   @Transactional
   public AccountDetailType update(final AccountDetailType accountDetailType) {
  return accountDetailTypeRepository.save(accountDetailType);
    } 
  public List<AccountDetailType> findAll() {
   return accountDetailTypeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public AccountDetailType findByName(String name){
  return accountDetailTypeRepository.findByName(name);
  }
  public AccountDetailType findOne(Long id){
  return accountDetailTypeRepository.findOne(id);
  }
  public Page<AccountDetailType> search(AccountDetailTypeContractRequest accountDetailTypeContractRequest){
final AccountDetailTypeSpecification specification = new AccountDetailTypeSpecification(accountDetailTypeContractRequest.getAccountDetailType());
Pageable page = new PageRequest(accountDetailTypeContractRequest.getPage().getOffSet(),accountDetailTypeContractRequest.getPage().getPageSize());
  return accountDetailTypeRepository.findAll(specification,page);
  }
public BindingResult validate(AccountDetailTypeContractRequest accountDetailTypeContractRequest, String method,BindingResult errors) { 
	 
		try { 
			switch(method) 
			{ 
			case "update": 
				Assert.notNull(accountDetailTypeContractRequest.getAccountDetailType(), "AccountDetailType to edit must not be null"); 
				validator.validate(accountDetailTypeContractRequest.getAccountDetailType(), errors); 
				break; 
			case "view": 
				//validator.validate(accountDetailTypeContractRequest.getAccountDetailType(), errors); 
				break; 
			case "create": 
				Assert.notNull(accountDetailTypeContractRequest.getAccountDetailTypes(), "AccountDetailTypes to create must not be null"); 
				for(AccountDetailTypeContract b:accountDetailTypeContractRequest.getAccountDetailTypes()) 
				 validator.validate(b, errors); 
				break; 
			case "updateAll": 
				Assert.notNull(accountDetailTypeContractRequest.getAccountDetailTypes(), "AccountDetailTypes to create must not be null"); 
				for(AccountDetailTypeContract b:accountDetailTypeContractRequest.getAccountDetailTypes()) 
				 validator.validate(b, errors); 
				break; 
			default : validator.validate(accountDetailTypeContractRequest.getRequestInfo(), errors); 
			} 
		} catch (IllegalArgumentException e) { 
			 errors.addError(new ObjectError("Missing data", e.getMessage())); 
		} 
		return errors; 
 
	}
}