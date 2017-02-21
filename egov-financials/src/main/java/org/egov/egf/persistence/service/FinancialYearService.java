package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.FinancialYear;
import org.egov.egf.persistence.queue.contract.FinancialYearContract;
import org.egov.egf.persistence.repository.FinancialYearRepository;
import org.egov.egf.persistence.specification.FinancialYearSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class FinancialYearService  {

  private final FinancialYearRepository financialYearRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public FinancialYearService(final FinancialYearRepository financialYearRepository) {
   this.financialYearRepository = financialYearRepository;
  }

   @Transactional
   public FinancialYear create(final FinancialYear financialYear) {
  return financialYearRepository.save(financialYear);
  } 
   @Transactional
   public FinancialYear update(final FinancialYear financialYear) {
  return financialYearRepository.save(financialYear);
    } 
  public List<FinancialYear> findAll() {
   return financialYearRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public FinancialYear findOne(Long id){
  return financialYearRepository.findOne(id);
  }
  public List<FinancialYear> search(FinancialYearContract financialYearContract){
final FinancialYearSpecification specification = new FinancialYearSpecification(financialYearContract);
  return financialYearRepository.findAll(specification);
  }
}