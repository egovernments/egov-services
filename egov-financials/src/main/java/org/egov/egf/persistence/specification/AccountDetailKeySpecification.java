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
		Path<Integer> groupId = root.get(AccountDetailKey_.groupId);
		Path<String> name = root.get(AccountDetailKey_.name);
		Path<Integer> key = root.get(AccountDetailKey_.key);
		Path<AccountDetailType> accountDetailType = root.get(AccountDetailKey_.accountDetailType);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria.getId() != null) {
			predicates.add(criteriaBuilder.equal(id, criteria.getId()));
		}

		if (criteria.getGroupId() != null) {
			predicates.add(criteriaBuilder.equal(groupId, criteria.getGroupId()));
		}

		if (criteria.getName() != null) {
			predicates.add(criteriaBuilder.equal(name, criteria.getName()));
		}

		if (criteria.getKey() != null) {
			predicates.add(criteriaBuilder.equal(key, criteria.getKey()));
		}

		if (criteria.getAccountDetailType() != null) {
			predicates.add(criteriaBuilder.equal(accountDetailType, criteria.getAccountDetailType()));
		}

		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
