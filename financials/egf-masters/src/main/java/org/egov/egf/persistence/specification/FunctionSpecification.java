package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.Function;
import org.egov.egf.persistence.entity.Function_;
import org.egov.egf.persistence.queue.contract.FunctionContract;
import org.springframework.data.jpa.domain.Specification;

public class FunctionSpecification implements Specification<Function> {
	private FunctionContract criteria;

	public FunctionSpecification(FunctionContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Function> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(Function_.id);
		Path<String> name = root.get(Function_.name);
		Path<String> code = root.get(Function_.code);
		Path<Integer> level = root.get(Function_.level);
		Path<Boolean> active = root.get(Function_.active);
		Path<Boolean> isParent = root.get(Function_.isParent);
		Path<Long> parentId = root.get(Function_.parentId);
		Path<String> tenantId = root.get(Function_.tenantId);
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

			if (criteria.getLevel() != null) {
				predicates.add(criteriaBuilder.equal(level, criteria.getLevel()));
			}

			if (criteria.getActive() != null) {
				predicates.add(criteriaBuilder.equal(active, criteria.getActive()));
			}

			if (criteria.getIsParent() != null) {
				predicates.add(criteriaBuilder.equal(isParent, criteria.getIsParent()));
			}

			if (criteria.getParentId() != null) {
				predicates.add(criteriaBuilder.equal(parentId, criteria.getParentId().getId()));
			}
			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}

		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
