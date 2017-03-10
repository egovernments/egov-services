package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.AccountDetailType;
import org.egov.egf.persistence.entity.AccountDetailType_;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContract;
import org.springframework.data.jpa.domain.Specification;

public class AccountDetailTypeSpecification implements Specification<AccountDetailType> {
	private AccountDetailTypeContract criteria;

	public AccountDetailTypeSpecification(AccountDetailTypeContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<AccountDetailType> root, CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(AccountDetailType_.id);
		Path<String> name = root.get(AccountDetailType_.name);
		Path<String> description = root.get(AccountDetailType_.description);
		Path<String> tableName = root.get(AccountDetailType_.tableName);
		Path<String> columnName = root.get(AccountDetailType_.columnName);
		Path<String> attributeName = root.get(AccountDetailType_.attributeName);
		Path<Boolean> active = root.get(AccountDetailType_.active);
		Path<String> fullyQualifiedName = root.get(AccountDetailType_.fullyQualifiedName);
		final List<Predicate> predicates = new ArrayList<>();
		if(criteria!=null)
		{
		if (criteria.getId() != null) {
			predicates.add(criteriaBuilder.equal(id, criteria.getId()));
		}

		if (criteria.getName() != null) {
			predicates.add(criteriaBuilder.equal(name, criteria.getName()));
		}

		if (criteria.getDescription() != null) {
			predicates.add(criteriaBuilder.equal(description, criteria.getDescription()));
		}

		if (criteria.getTableName() != null) {
			predicates.add(criteriaBuilder.equal(tableName, criteria.getTableName()));
		}

		if (criteria.getColumnName() != null) {
			predicates.add(criteriaBuilder.equal(columnName, criteria.getColumnName()));
		}

		if (criteria.getAttributeName() != null) {
			predicates.add(criteriaBuilder.equal(attributeName, criteria.getAttributeName()));
		}

		if (criteria.getActive() != null) {
			predicates.add(criteriaBuilder.equal(active, criteria.getActive()));
		}

		if (criteria.getFullyQualifiedName() != null) {
			predicates.add(criteriaBuilder.equal(fullyQualifiedName, criteria.getFullyQualifiedName()));
		}
		
                if(criteria.getIds() != null && !criteria.getIds().isEmpty())
                {
                    predicates.add(id.in(criteria.getIds()));
                }		
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
