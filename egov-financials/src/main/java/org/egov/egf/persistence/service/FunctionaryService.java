package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.Functionary;
import org.egov.egf.persistence.queue.contract.FunctionaryContract;
import org.egov.egf.persistence.repository.FunctionaryRepository;
import org.egov.egf.persistence.specification.FunctionarySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class FunctionaryService  {

  private final FunctionaryRepository functionaryRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public FunctionaryService(final FunctionaryRepository functionaryRepository) {
   this.functionaryRepository = functionaryRepository;
  }

   @Transactional
   public Functionary create(final Functionary functionary) {
  return functionaryRepository.save(functionary);
  } 
   @Transactional
   public Functionary update(final Functionary functionary) {
  return functionaryRepository.save(functionary);
    } 
  public List<Functionary> findAll() {
   return functionaryRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public Functionary findByName(String name){
  return functionaryRepository.findByName(name);
  }
  public Functionary findByCode(String code){
  return functionaryRepository.findByCode(code);
  }
  public Functionary findOne(Long id){
  return functionaryRepository.findOne(id);
  }
  public List<Functionary> search(FunctionaryContract functionaryContract){
final FunctionarySpecification specification = new FunctionarySpecification(functionaryContract);
  return functionaryRepository.findAll(specification);
  }
}