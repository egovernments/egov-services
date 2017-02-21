package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.ChartOfAccountDetail;
import org.egov.egf.persistence.queue.contract.ChartOfAccountDetailContract;
import org.egov.egf.persistence.repository.ChartOfAccountDetailRepository;
import org.egov.egf.persistence.specification.ChartOfAccountDetailSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class ChartOfAccountDetailService  {

  private final ChartOfAccountDetailRepository chartOfAccountDetailRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public ChartOfAccountDetailService(final ChartOfAccountDetailRepository chartOfAccountDetailRepository) {
   this.chartOfAccountDetailRepository = chartOfAccountDetailRepository;
  }

   @Transactional
   public ChartOfAccountDetail create(final ChartOfAccountDetail chartOfAccountDetail) {
  return chartOfAccountDetailRepository.save(chartOfAccountDetail);
  } 
   @Transactional
   public ChartOfAccountDetail update(final ChartOfAccountDetail chartOfAccountDetail) {
  return chartOfAccountDetailRepository.save(chartOfAccountDetail);
    } 
  public List<ChartOfAccountDetail> findAll() {
   return chartOfAccountDetailRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public ChartOfAccountDetail findOne(Long id){
  return chartOfAccountDetailRepository.findOne(id);
  }
  public List<ChartOfAccountDetail> search(ChartOfAccountDetailContract chartOfAccountDetailContract){
final ChartOfAccountDetailSpecification specification = new ChartOfAccountDetailSpecification(chartOfAccountDetailContract);
  return chartOfAccountDetailRepository.findAll(specification);
  }
}