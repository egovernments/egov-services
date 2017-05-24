package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.FinancialStatus;
import org.egov.egf.persistence.entity.FinancialStatus_;
import org.egov.egf.persistence.queue.contract.FinancialStatusContract;
import org.springframework.data.jpa.domain.Specification;

public class FinancialStatusSpecification implements Specification<FinancialStatus> {
	private FinancialStatusContract criteria;

	public FinancialStatusSpecification(FinancialStatusContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<FinancialStatus> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(FinancialStatus_.id);
		Path<String> code = root.get(FinancialStatus_.code);
		Path<String> description = root.get(FinancialStatus_.description);
		Path<String> objectName = root.get(FinancialStatus_.objectName);
		Path<String> tenantId = root.get(FinancialStatus_.tenantId);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getCode() != null) {
				predicates.add(criteriaBuilder.equal(code, criteria.getCode()));
			}

			if (criteria.getDescription() != null) {
				predicates.add(criteriaBuilder.equal(description, criteria.getDescription()));
			}

			if (criteria.getModuleType() != null) {
				predicates.add(criteriaBuilder.equal(objectName, criteria.getModuleType()));
			}

			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}

		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
