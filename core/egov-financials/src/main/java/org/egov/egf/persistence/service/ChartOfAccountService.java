package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.queue.contract.ChartOfAccountContract;
import org.egov.egf.persistence.repository.ChartOfAccountRepository;
import org.egov.egf.persistence.specification.ChartOfAccountSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class ChartOfAccountService  {

  private final ChartOfAccountRepository chartOfAccountRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public ChartOfAccountService(final ChartOfAccountRepository chartOfAccountRepository) {
   this.chartOfAccountRepository = chartOfAccountRepository;
  }

   @Transactional
   public ChartOfAccount create(final ChartOfAccount chartOfAccount) {
  return chartOfAccountRepository.save(chartOfAccount);
  } 
   @Transactional
   public ChartOfAccount update(final ChartOfAccount chartOfAccount) {
  return chartOfAccountRepository.save(chartOfAccount);
    } 
  public List<ChartOfAccount> findAll() {
   return chartOfAccountRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public ChartOfAccount findByName(String name){
  return chartOfAccountRepository.findByName(name);
  }
  public ChartOfAccount findOne(Long id){
  return chartOfAccountRepository.findOne(id);
  }
  public List<ChartOfAccount> search(ChartOfAccountContract chartOfAccountContract){
final ChartOfAccountSpecification specification = new ChartOfAccountSpecification(chartOfAccountContract);
  return chartOfAccountRepository.findAll(specification);
  }
}