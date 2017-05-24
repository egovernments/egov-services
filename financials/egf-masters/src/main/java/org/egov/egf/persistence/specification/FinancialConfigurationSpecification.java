package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.FinancialConfiguration;
import org.egov.egf.persistence.entity.FinancialConfiguration_;
import org.egov.egf.persistence.queue.contract.FinancialConfigurationContract;
import org.springframework.data.jpa.domain.Specification;

public class FinancialConfigurationSpecification implements Specification<FinancialConfiguration> {
	private FinancialConfigurationContract criteria;

	public FinancialConfigurationSpecification(FinancialConfigurationContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<FinancialConfiguration> root, CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(FinancialConfiguration_.id);
		Path<String> keyName = root.get(FinancialConfiguration_.keyName);
		Path<String> description = root.get(FinancialConfiguration_.description);
		Path<String> tenantId = root.get(FinancialConfiguration_.tenantId);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getKeyName() != null) {
				predicates.add(criteriaBuilder.equal(keyName, criteria.getKeyName()));
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
