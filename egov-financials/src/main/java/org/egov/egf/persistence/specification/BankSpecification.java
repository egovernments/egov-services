package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.entity.Bank_;
import org.egov.egf.web.contract.BankContract;
import org.springframework.data.jpa.domain.Specification;

public class BankSpecification implements Specification<Bank>
{

	 private BankContract criteria;

	    public BankSpecification(BankContract criteria) {
	        this.criteria = criteria;
	    }
	
	
	@Override
	public Predicate toPredicate(Root<Bank> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		  Path<String> code = root.get(Bank_.code);
		  Path<String> name= root.get(Bank_.name);
		  Path<Boolean> active= root.get(Bank_.active);
		  
		  final List<Predicate> predicates = new ArrayList<>();
		  
		  if (criteria.getCode() != null) {
	            predicates.add(criteriaBuilder.equal(code, criteria.getCode()));
	        }

	        if (criteria.getName() != null) {
	            predicates.add(criteriaBuilder.equal(name, criteria.getName()));
	        }
	        
	        if (criteria.getActive() != null) {
	            predicates.add(criteriaBuilder.equal(active, criteria.getActive()));
	        }
		  
		  return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}


}
