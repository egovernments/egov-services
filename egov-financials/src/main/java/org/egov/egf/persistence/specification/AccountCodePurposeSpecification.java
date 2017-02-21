package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContract;
import org.springframework.data.jpa.domain.Specification;

public class AccountCodePurposeSpecification implements Specification<AccountCodePurpose> {
	private AccountCodePurposeContract criteria;

	public AccountCodePurposeSpecification(AccountCodePurposeContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<AccountCodePurpose> root, CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder) {
	/*	Path<Long> id = root.get(AccountCodePurpose_.id);
		Path<String> name = root.get(AccountCodePurpose_.name);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria.getId() != null) {
			predicates.add(criteriaBuilder.equal(id, criteria.getId()));
		}

		if (criteria.getName() != null) {
			predicates.add(criteriaBuilder.equal(name, criteria.getName()));
		}

		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));*/
		return null;
	}
}
