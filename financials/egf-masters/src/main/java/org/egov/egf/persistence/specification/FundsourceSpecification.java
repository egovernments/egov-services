package org.egov.egf.persistence.specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.Fundsource;
import org.egov.egf.persistence.entity.Fundsource_;
import org.egov.egf.persistence.queue.contract.FundsourceContract;
import org.springframework.data.jpa.domain.Specification;

public class FundsourceSpecification implements Specification<Fundsource> {
	private FundsourceContract criteria;

	public FundsourceSpecification(FundsourceContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Fundsource> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(Fundsource_.id);
		Path<String> code = root.get(Fundsource_.code);
		Path<String> name = root.get(Fundsource_.name);
		Path<String> type = root.get(Fundsource_.type);
		Path<Long> fundSource = root.get(Fundsource_.fundSource);
		Path<BigDecimal> llevel = root.get(Fundsource_.llevel);
		Path<Boolean> active = root.get(Fundsource_.active);
		Path<Boolean> isParent = root.get(Fundsource_.isParent);
		Path<String> tenantId = root.get(Fundsource_.tenantId);
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

			if (criteria.getType() != null) {
				predicates.add(criteriaBuilder.equal(type, criteria.getType()));
			}

			if (criteria.getFundSource() != null) {
				predicates.add(criteriaBuilder.equal(fundSource, criteria.getFundSource().getId()));
			}

			if (criteria.getLlevel() != null) {
				predicates.add(criteriaBuilder.equal(llevel, criteria.getLlevel()));
			}

			if (criteria.getActive() != null) {
				predicates.add(criteriaBuilder.equal(active, criteria.getActive()));
			}

			if (criteria.getIsParent() != null) {
				predicates.add(criteriaBuilder.equal(isParent, criteria.getIsParent()));
			}
			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}

		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
