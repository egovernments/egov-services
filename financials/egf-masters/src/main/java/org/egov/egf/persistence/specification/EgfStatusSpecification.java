package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.EgfStatus;
import org.egov.egf.persistence.entity.EgfStatus_;
import org.egov.egf.persistence.queue.contract.EgfStatusContract;
import org.springframework.data.jpa.domain.Specification;

public class EgfStatusSpecification implements Specification<EgfStatus> {
	private EgfStatusContract criteria;

	public EgfStatusSpecification(EgfStatusContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<EgfStatus> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(EgfStatus_.id);
		Path<String> code = root.get(EgfStatus_.code);
		Path<String> description = root.get(EgfStatus_.description);
		Path<String> moduleType = root.get(EgfStatus_.moduleType);
		Path<String> tenantId = root.get(EgfStatus_.tenantId);
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
				predicates.add(criteriaBuilder.equal(moduleType, criteria.getModuleType()));
			}

			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}

		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
