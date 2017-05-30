package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.AccountDetailType;
import org.egov.egf.persistence.entity.AccountEntity;
import org.egov.egf.persistence.entity.AccountEntity_;
import org.egov.egf.persistence.queue.contract.AccountEntityContract;
import org.springframework.data.jpa.domain.Specification;

public class AccountEntitySpecification implements Specification<AccountEntity> {
	private AccountEntityContract criteria;

	public AccountEntitySpecification(AccountEntityContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<AccountEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(AccountEntity_.id);
		Path<Long> accountDetailType = root.get(AccountEntity_.accountDetailType);
		Path<String> code = root.get(AccountEntity_.code);
		Path<String> name = root.get(AccountEntity_.name);
		Path<Boolean> active = root.get(AccountEntity_.active);
		Path<String> description = root.get(AccountEntity_.description);
		Path<String> tenantId = root.get(AccountEntity_.tenantId);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getAccountDetailType() != null) {
				predicates.add(criteriaBuilder.equal(accountDetailType, criteria.getAccountDetailType().getId()));
			}

			if (criteria.getCode() != null) {
				predicates.add(criteriaBuilder.equal(code, criteria.getCode()));
			}

			if (criteria.getName() != null) {
				predicates.add(criteriaBuilder.equal(name, criteria.getName()));
			}

			if (criteria.getActive() != null) {
				predicates.add(criteriaBuilder.equal(active, criteria.getActive()));
			}

			if (criteria.getDescription() != null) {
				predicates.add(criteriaBuilder.equal(description, criteria.getDescription()));
			}
			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
