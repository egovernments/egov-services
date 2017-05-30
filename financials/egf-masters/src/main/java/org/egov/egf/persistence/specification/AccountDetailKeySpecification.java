package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.AccountDetailKey;
import org.egov.egf.persistence.entity.AccountDetailKey_;
import org.egov.egf.persistence.entity.AccountDetailType;
import org.egov.egf.persistence.queue.contract.AccountDetailKeyContract;
import org.springframework.data.jpa.domain.Specification;

public class AccountDetailKeySpecification implements Specification<AccountDetailKey> {
	private AccountDetailKeyContract criteria;

	public AccountDetailKeySpecification(AccountDetailKeyContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<AccountDetailKey> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(AccountDetailKey_.id);
		Path<Long> key = root.get(AccountDetailKey_.key);
		Path<Long> accountDetailType = root.get(AccountDetailKey_.accountDetailType);
		Path<String> tenantId = root.get(AccountDetailKey_.tenantId);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getKey() != null) {
				predicates.add(criteriaBuilder.equal(key, criteria.getKey()));
			}

			if (criteria.getAccountDetailType() != null) {
				predicates.add(criteriaBuilder.equal(accountDetailType, criteria.getAccountDetailType().getId()));
			}

			if (criteria.getIds() != null && !criteria.getIds().isEmpty()) {
				predicates.add(id.in(criteria.getIds()));
			}
			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
