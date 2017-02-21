package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContract;
import org.egov.egf.persistence.repository.AccountCodePurposeRepository;
import org.egov.egf.persistence.specification.AccountCodePurposeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class AccountCodePurposeService  {

  private final AccountCodePurposeRepository accountCodePurposeRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public AccountCodePurposeService(final AccountCodePurposeRepository accountCodePurposeRepository) {
   this.accountCodePurposeRepository = accountCodePurposeRepository;
  }

   @Transactional
   public AccountCodePurpose create(final AccountCodePurpose accountCodePurpose) {
  return accountCodePurposeRepository.save(accountCodePurpose);
  } 
   @Transactional
   public AccountCodePurpose update(final AccountCodePurpose accountCodePurpose) {
  return accountCodePurposeRepository.save(accountCodePurpose);
    } 
  public List<AccountCodePurpose> findAll() {
   return accountCodePurposeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public AccountCodePurpose findByName(String name){
  return accountCodePurposeRepository.findByName(name);
  }
  public AccountCodePurpose findOne(Long id){
  return accountCodePurposeRepository.findOne(id);
  }
  public List<AccountCodePurpose> search(AccountCodePurposeContract accountCodePurposeContract){
final AccountCodePurposeSpecification specification = new AccountCodePurposeSpecification(accountCodePurposeContract);
  return accountCodePurposeRepository.findAll(specification);
  }
}