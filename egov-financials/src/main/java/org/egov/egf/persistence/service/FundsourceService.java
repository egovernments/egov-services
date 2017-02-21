package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.Fundsource;
import org.egov.egf.persistence.queue.contract.FundsourceContract;
import org.egov.egf.persistence.repository.FundsourceRepository;
import org.egov.egf.persistence.specification.FundsourceSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class FundsourceService  {

  private final FundsourceRepository fundsourceRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public FundsourceService(final FundsourceRepository fundsourceRepository) {
   this.fundsourceRepository = fundsourceRepository;
  }

   @Transactional
   public Fundsource create(final Fundsource fundsource) {
  return fundsourceRepository.save(fundsource);
  } 
   @Transactional
   public Fundsource update(final Fundsource fundsource) {
  return fundsourceRepository.save(fundsource);
    } 
  public List<Fundsource> findAll() {
   return fundsourceRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public Fundsource findByName(String name){
  return fundsourceRepository.findByName(name);
  }
  public Fundsource findByCode(String code){
  return fundsourceRepository.findByCode(code);
  }
  public Fundsource findOne(Long id){
  return fundsourceRepository.findOne(id);
  }
  public List<Fundsource> search(FundsourceContract fundsourceContract){
final FundsourceSpecification specification = new FundsourceSpecification(fundsourceContract);
  return fundsourceRepository.findAll(specification);
  }
}