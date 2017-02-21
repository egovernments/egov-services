package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.Supplier;
import org.egov.egf.persistence.queue.contract.SupplierContract;
import org.egov.egf.persistence.repository.SupplierRepository;
import org.egov.egf.persistence.specification.SupplierSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service 
@Transactional(readOnly = true)
public class SupplierService  {

  private final SupplierRepository supplierRepository;
  @PersistenceContext
private EntityManager entityManager;

  @Autowired
public SupplierService(final SupplierRepository supplierRepository) {
   this.supplierRepository = supplierRepository;
  }

   @Transactional
   public Supplier create(final Supplier supplier) {
  return supplierRepository.save(supplier);
  } 
   @Transactional
   public Supplier update(final Supplier supplier) {
  return supplierRepository.save(supplier);
    } 
  public List<Supplier> findAll() {
   return supplierRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
     }
  public Supplier findByName(String name){
  return supplierRepository.findByName(name);
  }
  public Supplier findByCode(String code){
  return supplierRepository.findByCode(code);
  }
  public Supplier findOne(Long id){
  return supplierRepository.findOne(id);
  }
  public List<Supplier> search(SupplierContract supplierContract){
final SupplierSpecification specification = new SupplierSpecification(supplierContract);
  return supplierRepository.findAll(specification);
  }
}