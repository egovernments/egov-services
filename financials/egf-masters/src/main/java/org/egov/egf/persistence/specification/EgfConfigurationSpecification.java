package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.EgfConfiguration;
import org.egov.egf.persistence.entity.EgfConfiguration_;
import org.egov.egf.persistence.queue.contract.EgfConfigurationContract;
import org.springframework.data.jpa.domain.Specification;

public class EgfConfigurationSpecification implements Specification<EgfConfiguration> {
	private EgfConfigurationContract criteria;

	public EgfConfigurationSpecification(EgfConfigurationContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<EgfConfiguration> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(EgfConfiguration_.id);
		Path<String> keyName = root.get(EgfConfiguration_.keyName);
		Path<String> description = root.get(EgfConfiguration_.description);
		Path<String> tenantId = root.get(EgfConfiguration_.tenantId);
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
