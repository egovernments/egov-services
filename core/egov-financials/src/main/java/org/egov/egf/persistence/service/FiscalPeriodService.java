package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.FiscalPeriod;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContract;
import org.egov.egf.persistence.repository.FiscalPeriodRepository;
import org.egov.egf.persistence.specification.FiscalPeriodSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class FiscalPeriodService  {

  private final FiscalPeriodRepository fiscalPeriodRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public FiscalPeriodService(final FiscalPeriodRepository fiscalPeriodRepository) {
   this.fiscalPeriodRepository = fiscalPeriodRepository;
  }

   @Transactional
   public FiscalPeriod create(final FiscalPeriod fiscalPeriod) {
  return fiscalPeriodRepository.save(fiscalPeriod);
  } 
   @Transactional
   public FiscalPeriod update(final FiscalPeriod fiscalPeriod) {
  return fiscalPeriodRepository.save(fiscalPeriod);
    } 
  public List<FiscalPeriod> findAll() {
   return fiscalPeriodRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public FiscalPeriod findByName(String name){
  return fiscalPeriodRepository.findByName(name);
  }
  public FiscalPeriod findOne(Long id){
  return fiscalPeriodRepository.findOne(id);
  }
  public List<FiscalPeriod> search(FiscalPeriodContract fiscalPeriodContract){
final FiscalPeriodSpecification specification = new FiscalPeriodSpecification(fiscalPeriodContract);
  return fiscalPeriodRepository.findAll(specification);
  }
}