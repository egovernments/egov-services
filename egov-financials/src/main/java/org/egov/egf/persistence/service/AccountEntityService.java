package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.AccountEntity;
import org.egov.egf.persistence.queue.contract.AccountEntityContract;
import org.egov.egf.persistence.repository.AccountEntityRepository;
import org.egov.egf.persistence.specification.AccountEntitySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class AccountEntityService  {

  private final AccountEntityRepository accountEntityRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public AccountEntityService(final AccountEntityRepository accountEntityRepository) {
   this.accountEntityRepository = accountEntityRepository;
  }

   @Transactional
   public AccountEntity create(final AccountEntity accountEntity) {
  return accountEntityRepository.save(accountEntity);
  } 
   @Transactional
   public AccountEntity update(final AccountEntity accountEntity) {
  return accountEntityRepository.save(accountEntity);
    } 
  public List<AccountEntity> findAll() {
   return accountEntityRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public AccountEntity findByName(String name){
  return accountEntityRepository.findByName(name);
  }
  public AccountEntity findByCode(String code){
  return accountEntityRepository.findByCode(code);
  }
  public AccountEntity findOne(Long id){
  return accountEntityRepository.findOne(id);
  }
  public List<AccountEntity> search(AccountEntityContract accountEntityContract){
final AccountEntitySpecification specification = new AccountEntitySpecification(accountEntityContract);
  return accountEntityRepository.findAll(specification);
  }
}