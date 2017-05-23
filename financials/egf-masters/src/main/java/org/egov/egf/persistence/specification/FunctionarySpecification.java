package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.Functionary;
import org.egov.egf.persistence.entity.Functionary_;
import org.egov.egf.persistence.queue.contract.FunctionaryContract;
import org.springframework.data.jpa.domain.Specification;

public class FunctionarySpecification implements Specification<Functionary> {
	private FunctionaryContract criteria;

	public FunctionarySpecification(FunctionaryContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Functionary> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(Functionary_.id);
		Path<String> code = root.get(Functionary_.code);
		Path<String> name = root.get(Functionary_.name);
		Path<Boolean> active = root.get(Functionary_.active);
		Path<String> tenantId = root.get(Functionary_.tenantId);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getCode() != null) {
				predicates.add(criteriaBuilder.equal(code, criteria.getCode()));
			}

			if (criteria.getName() != null) {
				predicates.add(criteriaBuilder.equal(name, criteria.getName()));
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
