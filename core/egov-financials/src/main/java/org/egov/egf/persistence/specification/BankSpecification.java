package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.entity.BankBranch;
import org.egov.egf.persistence.entity.Bank_;
import org.egov.egf.persistence.queue.contract.BankContract;
import org.springframework.data.jpa.domain.Specification;

import com.jayway.jsonpath.Criteria;

public class BankSpecification implements Specification<Bank> {
	private BankContract criteria;

	public BankSpecification(BankContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Bank> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(Bank_.id);
		Path<String> code = root.get(Bank_.code);
		Path<String> name = root.get(Bank_.name);
		Path<String> description = root.get(Bank_.description);
		Path<Boolean> active = root.get(Bank_.active);
		Path<String> type = root.get(Bank_.type);
		
	    Expression<Set<BankBranch>> bankBranches = root.get(Bank_.bankBranches);
		final List<Predicate> predicates = new ArrayList<>();
	
		if(criteria!=null)
		{
		if (criteria.getId() != null) {
			predicates.add(criteriaBuilder.equal(id, criteria.getId()));
		}

		if (criteria.getCode() != null) {
			predicates.add(criteriaBuilder.equal(code, criteria.getCode()));
		}

		if (criteria.getName() != null) {
			predicates.add(criteriaBuilder.equal(name, criteria.getName()));
		}

		if (criteria.getDescription() != null) {
			predicates.add(criteriaBuilder.equal(description, criteria.getDescription()));
		}

		if (criteria.getActive() != null) {
			predicates.add(criteriaBuilder.equal(active, criteria.getActive()));
		}

		if (criteria.getType() != null) {
			predicates.add(criteriaBuilder.equal(type, criteria.getType()));
		}

	/*	if (criteria.getBankBranches() != null && !criteria.getBankBranches().isEmpty()) {
			predicates.add(criteriaBuilder.equal(bankBranches, criteria.getBankBranches()));
		}
*/
		}
		if(criteria.getIds() != null && !criteria.getIds().isEmpty())
		{
		    predicates.add(id.in(criteria.getIds()));
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
