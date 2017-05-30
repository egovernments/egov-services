package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.EgfConfigurationValues;
import org.egov.egf.persistence.entity.EgfConfigurationValues_;
import org.egov.egf.persistence.queue.contract.EgfConfigurationValuesContract;
import org.springframework.data.jpa.domain.Specification;

public class EgfConfigurationValuesSpecification implements Specification<EgfConfigurationValues> {
	private EgfConfigurationValuesContract criteria;

	public EgfConfigurationValuesSpecification(EgfConfigurationValuesContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<EgfConfigurationValues> root, CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(EgfConfigurationValues_.id);
		Path<Long> keyId = root.get(EgfConfigurationValues_.keyId);
		Path<String> value = root.get(EgfConfigurationValues_.value);
		Path<Date> effectiveFrom = root.get(EgfConfigurationValues_.effectiveFrom);
		Path<String> tenantId = root.get(EgfConfigurationValues_.tenantId);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getKeyId() != null) {
				predicates.add(criteriaBuilder.equal(keyId, criteria.getKeyId()));
			}

			if (criteria.getValue() != null) {
				predicates.add(criteriaBuilder.equal(value, criteria.getValue()));
			}

			if (criteria.getEffectiveFrom() != null) {
				predicates.add(criteriaBuilder.equal(effectiveFrom, criteria.getEffectiveFrom()));
			}

			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}

		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
