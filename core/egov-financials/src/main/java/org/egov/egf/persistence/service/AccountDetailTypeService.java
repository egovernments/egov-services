package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.AccountDetailType;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContract;
import org.egov.egf.persistence.repository.AccountDetailTypeRepository;
import org.egov.egf.persistence.specification.AccountDetailTypeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

   @Transactional
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
  public List<AccountDetailType> search(AccountDetailTypeContract accountDetailTypeContract){
final AccountDetailTypeSpecification specification = new AccountDetailTypeSpecification(accountDetailTypeContract);
  return accountDetailTypeRepository.findAll(specification);
  }
}