package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.BankAccount;
import org.egov.egf.persistence.queue.contract.BankAccountContract;
import org.egov.egf.persistence.repository.BankAccountRepository;
import org.egov.egf.persistence.specification.BankAccountSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class BankAccountService  {

  private final BankAccountRepository bankAccountRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public BankAccountService(final BankAccountRepository bankAccountRepository) {
   this.bankAccountRepository = bankAccountRepository;
  }

   @Transactional
   public BankAccount create(final BankAccount bankAccount) {
  return bankAccountRepository.save(bankAccount);
  } 
   @Transactional
   public BankAccount update(final BankAccount bankAccount) {
  return bankAccountRepository.save(bankAccount);
    } 
  public List<BankAccount> findAll() {
   return bankAccountRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public BankAccount findOne(Long id){
  return bankAccountRepository.findOne(id);
  }
  public List<BankAccount> search(BankAccountContract bankAccountContract){
final BankAccountSpecification specification = new BankAccountSpecification(bankAccountContract);
  return bankAccountRepository.findAll(specification);
  }
}