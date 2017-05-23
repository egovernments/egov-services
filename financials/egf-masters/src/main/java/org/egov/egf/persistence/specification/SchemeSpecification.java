package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.Fund;
import org.egov.egf.persistence.entity.Scheme;
import org.egov.egf.persistence.entity.Scheme_;
import org.egov.egf.persistence.queue.contract.SchemeContract;
import org.springframework.data.jpa.domain.Specification;

public class SchemeSpecification implements Specification<Scheme> {
	private SchemeContract criteria;

	public SchemeSpecification(SchemeContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Scheme> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(Scheme_.id);
		Path<Long> fund = root.get(Scheme_.fund);
		Path<String> code = root.get(Scheme_.code);
		Path<String> name = root.get(Scheme_.name);
		Path<Date> validFrom = root.get(Scheme_.validFrom);
		Path<Date> validTo = root.get(Scheme_.validTo);
		Path<Boolean> active = root.get(Scheme_.active);
		Path<String> description = root.get(Scheme_.description);
		Path<String> tenantId = root.get(Scheme_.tenantId);
		Path<Long> boundary = root.get(Scheme_.boundary);
		// Expression<Set<SubScheme>> subSchemes = root.get(Scheme_.subSchemes);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getFund() != null) {
				predicates.add(criteriaBuilder.equal(fund, criteria.getFund().getId()));
			}

			if (criteria.getCode() != null) {
				predicates.add(criteriaBuilder.equal(code, criteria.getCode()));
			}

			if (criteria.getName() != null) {
				predicates.add(criteriaBuilder.equal(name, criteria.getName()));
			}

			if (criteria.getValidFrom() != null) {
				predicates.add(criteriaBuilder.equal(validFrom, criteria.getValidFrom()));
			}

			if (criteria.getValidTo() != null) {
				predicates.add(criteriaBuilder.equal(validTo, criteria.getValidTo()));
			}

			if (criteria.getActive() != null) {
				predicates.add(criteriaBuilder.equal(active, criteria.getActive()));
			}

			if (criteria.getDescription() != null) {
				predicates.add(criteriaBuilder.equal(description, criteria.getDescription()));
			}

			if (criteria.getBoundary() != null) {
				predicates.add(criteriaBuilder.equal(boundary, criteria.getBoundary()));
			}

			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}

			/*
			 * if (criteria.getSubSchemes() != null) {
			 * predicates.add(criteriaBuilder.equal(subSchemes,
			 * criteria.getSubSchemes())); }
			 */
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
