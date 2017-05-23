package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.Fund;
import org.egov.egf.persistence.entity.Fund_;
import org.egov.egf.persistence.queue.contract.FundContract;
import org.springframework.data.jpa.domain.Specification;

public class FundSpecification implements Specification<Fund> {
	private FundContract criteria;

	public FundSpecification(FundContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Fund> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(Fund_.id);
		Path<String> name = root.get(Fund_.name);
		Path<String> code = root.get(Fund_.code);
		Path<Character> identifier = root.get(Fund_.identifier);
		Path<Long> level = root.get(Fund_.level);
		Path<Long> parentId = root.get(Fund_.parentId);
		Path<Boolean> isParent = root.get(Fund_.isParent);
		Path<Boolean> active = root.get(Fund_.active);
		Path<String> tenantId = root.get(Fund_.tenantId);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getName() != null) {
				predicates.add(criteriaBuilder.equal(name, criteria.getName()));
			}

			if (criteria.getCode() != null) {
				predicates.add(criteriaBuilder.equal(code, criteria.getCode()));
			}

			if (criteria.getIdentifier() != null) {
				predicates.add(criteriaBuilder.equal(identifier, criteria.getIdentifier()));
			}

			if (criteria.getLevel() != null) {
				predicates.add(criteriaBuilder.equal(level, criteria.getLevel()));
			}

			if (criteria.getParentId() != null) {
				predicates.add(criteriaBuilder.equal(parentId, criteria.getParentId().getId()));
			}

			if (criteria.getIsParent() != null) {
				predicates.add(criteriaBuilder.equal(isParent, criteria.getIsParent()));
			}

			if (criteria.getActive() != null) {
				predicates.add(criteriaBuilder.equal(active, criteria.getActive()));
			}
			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}

		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
