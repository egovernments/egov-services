package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.AccountDetailKey;
import org.egov.egf.persistence.queue.contract.AccountDetailKeyContract;
import org.egov.egf.persistence.repository.AccountDetailKeyRepository;
import org.egov.egf.persistence.specification.AccountDetailKeySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class AccountDetailKeyService  {

  private final AccountDetailKeyRepository accountDetailKeyRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public AccountDetailKeyService(final AccountDetailKeyRepository accountDetailKeyRepository) {
   this.accountDetailKeyRepository = accountDetailKeyRepository;
  }

   @Transactional
   public AccountDetailKey create(final AccountDetailKey accountDetailKey) {
  return accountDetailKeyRepository.save(accountDetailKey);
  } 
   @Transactional
   public AccountDetailKey update(final AccountDetailKey accountDetailKey) {
  return accountDetailKeyRepository.save(accountDetailKey);
    } 
  public List<AccountDetailKey> findAll() {
   return accountDetailKeyRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public AccountDetailKey findByName(String name){
  return accountDetailKeyRepository.findByName(name);
  }
  public AccountDetailKey findOne(Long id){
  return accountDetailKeyRepository.findOne(id);
  }
  public List<AccountDetailKey> search(AccountDetailKeyContract accountDetailKeyContract){
final AccountDetailKeySpecification specification = new AccountDetailKeySpecification(accountDetailKeyContract);
  return accountDetailKeyRepository.findAll(specification);
  }
}