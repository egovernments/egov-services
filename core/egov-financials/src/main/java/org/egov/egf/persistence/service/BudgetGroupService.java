package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.BudgetGroup;
import org.egov.egf.persistence.queue.contract.BudgetGroupContract;
import org.egov.egf.persistence.repository.BudgetGroupRepository;
import org.egov.egf.persistence.specification.BudgetGroupSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class BudgetGroupService  {

  private final BudgetGroupRepository budgetGroupRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public BudgetGroupService(final BudgetGroupRepository budgetGroupRepository) {
   this.budgetGroupRepository = budgetGroupRepository;
  }

   @Transactional
   public BudgetGroup create(final BudgetGroup budgetGroup) {
  return budgetGroupRepository.save(budgetGroup);
  } 
   @Transactional
   public BudgetGroup update(final BudgetGroup budgetGroup) {
  return budgetGroupRepository.save(budgetGroup);
    } 
  public List<BudgetGroup> findAll() {
   return budgetGroupRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public BudgetGroup findByName(String name){
  return budgetGroupRepository.findByName(name);
  }
  public BudgetGroup findOne(Long id){
  return budgetGroupRepository.findOne(id);
  }
  public List<BudgetGroup> search(BudgetGroupContract budgetGroupContract){
final BudgetGroupSpecification specification = new BudgetGroupSpecification(budgetGroupContract);
  return budgetGroupRepository.findAll(specification);
  }
}