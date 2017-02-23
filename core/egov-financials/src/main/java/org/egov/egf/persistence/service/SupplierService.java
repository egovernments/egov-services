package org.egov.egf.persistence.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.Supplier;
import org.egov.egf.persistence.queue.contract.SupplierContract;
import org.egov.egf.persistence.queue.contract.SupplierContractRequest;
import org.egov.egf.persistence.repository.SupplierRepository;
import org.egov.egf.persistence.specification.SupplierSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;


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

@Autowired
	private SmartValidator validator;   @Transactional
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
  public Page<Supplier> search(SupplierContractRequest supplierContractRequest){
final SupplierSpecification specification = new SupplierSpecification(supplierContractRequest.getSupplier());
Pageable page = new PageRequest(supplierContractRequest.getPage().getOffSet(),supplierContractRequest.getPage().getPageSize());
  return supplierRepository.findAll(specification,page);
  }
public BindingResult validate(SupplierContractRequest supplierContractRequest, String method,BindingResult errors) { 
	 
		try { 
			switch(method) 
			{ 
			case "update": 
				Assert.notNull(supplierContractRequest.getSupplier(), "Supplier to edit must not be null"); 
				validator.validate(supplierContractRequest.getSupplier(), errors); 
				break; 
			case "view": 
				//validator.validate(supplierContractRequest.getSupplier(), errors); 
				break; 
			case "create": 
				Assert.notNull(supplierContractRequest.getSuppliers(), "Suppliers to create must not be null"); 
				for(SupplierContract b:supplierContractRequest.getSuppliers()) 
				 validator.validate(b, errors); 
				break; 
			case "updateAll": 
				Assert.notNull(supplierContractRequest.getSuppliers(), "Suppliers to create must not be null"); 
				for(SupplierContract b:supplierContractRequest.getSuppliers()) 
				 validator.validate(b, errors); 
				break; 
			default : validator.validate(supplierContractRequest.getRequestInfo(), errors); 
			} 
		} catch (IllegalArgumentException e) { 
			 errors.addError(new ObjectError("Missing data", e.getMessage())); 
		} 
		return errors; 
 
	}
}