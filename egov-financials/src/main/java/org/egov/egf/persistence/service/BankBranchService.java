package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.BankBranch;
import org.egov.egf.persistence.queue.contract.BankBranchContract;
import org.egov.egf.persistence.repository.BankBranchRepository;
import org.egov.egf.persistence.specification.BankBranchSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class BankBranchService  {

  private final BankBranchRepository bankBranchRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public BankBranchService(final BankBranchRepository bankBranchRepository) {
   this.bankBranchRepository = bankBranchRepository;
  }

   @Transactional
   public BankBranch create(final BankBranch bankBranch) {
  return bankBranchRepository.save(bankBranch);
  } 
   @Transactional
   public BankBranch update(final BankBranch bankBranch) {
  return bankBranchRepository.save(bankBranch);
    } 
  public List<BankBranch> findAll() {
   return bankBranchRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public BankBranch findByName(String name){
  return bankBranchRepository.findByName(name);
  }
  public BankBranch findByCode(String code){
  return bankBranchRepository.findByCode(code);
  }
  public BankBranch findOne(Long id){
  return bankBranchRepository.findOne(id);
  }
  public List<BankBranch> search(BankBranchContract bankBranchContract){
final BankBranchSpecification specification = new BankBranchSpecification(bankBranchContract);
  return bankBranchRepository.findAll(specification);
  }
}