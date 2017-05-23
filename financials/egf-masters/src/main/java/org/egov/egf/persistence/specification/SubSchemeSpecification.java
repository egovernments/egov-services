package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.Scheme;
import org.egov.egf.persistence.entity.SubScheme;
import org.egov.egf.persistence.entity.SubScheme_;
import org.egov.egf.persistence.queue.contract.SubSchemeContract;
import org.springframework.data.jpa.domain.Specification;

public class SubSchemeSpecification implements Specification<SubScheme> {
	private SubSchemeContract criteria;

	public SubSchemeSpecification(SubSchemeContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<SubScheme> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(SubScheme_.id);
		Path<Long> scheme = root.get(SubScheme_.scheme);
		Path<String> code = root.get(SubScheme_.code);
		Path<String> name = root.get(SubScheme_.name);
		Path<Date> validFrom = root.get(SubScheme_.validFrom);
		Path<Date> validTo = root.get(SubScheme_.validTo);
		Path<Boolean> active = root.get(SubScheme_.active);
		Path<Long> departmentId = root.get(SubScheme_.departmentId);
		Path<String> tenantId = root.get(SubScheme_.tenantId);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getScheme() != null) {
				predicates.add(criteriaBuilder.equal(scheme, criteria.getScheme().getId()));
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

			if (criteria.getDepartmentId() != null) {
				predicates.add(criteriaBuilder.equal(departmentId, criteria.getDepartmentId()));
			}
			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}

		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
