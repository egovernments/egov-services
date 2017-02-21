package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.Function;
import org.egov.egf.persistence.queue.contract.FunctionContract;
import org.egov.egf.persistence.repository.FunctionRepository;
import org.egov.egf.persistence.specification.FunctionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class FunctionService  {

  private final FunctionRepository functionRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public FunctionService(final FunctionRepository functionRepository) {
   this.functionRepository = functionRepository;
  }

   @Transactional
   public Function create(final Function function) {
  return functionRepository.save(function);
  } 
   @Transactional
   public Function update(final Function function) {
  return functionRepository.save(function);
    } 
  public List<Function> findAll() {
   return functionRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public Function findByName(String name){
  return functionRepository.findByName(name);
  }
  public Function findByCode(String code){
  return functionRepository.findByCode(code);
  }
  public Function findOne(Long id){
  return functionRepository.findOne(id);
  }
  public List<Function> search(FunctionContract functionContract){
final FunctionSpecification specification = new FunctionSpecification(functionContract);
  return functionRepository.findAll(specification);
  }
}