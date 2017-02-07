package org.egov.pgr.persistence;

import org.egov.pgr.persistence.entity.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	
	List<Customer> findByLastName(String lastName);
}