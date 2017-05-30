package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.FiscalPeriod;
import org.egov.egf.persistence.entity.FiscalPeriod_;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContract;
import org.springframework.data.jpa.domain.Specification;

public class FiscalPeriodSpecification implements Specification<FiscalPeriod> {
	private FiscalPeriodContract criteria;

	public FiscalPeriodSpecification(FiscalPeriodContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<FiscalPeriod> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(FiscalPeriod_.id);
		Path<String> name = root.get(FiscalPeriod_.name);
		Path<Long> financialYear = root.get(FiscalPeriod_.financialYear);
		Path<Date> startingDate = root.get(FiscalPeriod_.startingDate);
		Path<String> tenantId = root.get(FiscalPeriod_.tenantId);
		Path<Date> endingDate = root.get(FiscalPeriod_.endingDate);
		Path<Boolean> active = root.get(FiscalPeriod_.active);
		Path<Boolean> isActiveForPosting = root.get(FiscalPeriod_.isActiveForPosting);
		Path<Boolean> isClosed = root.get(FiscalPeriod_.isClosed);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getName() != null) {
				predicates.add(criteriaBuilder.equal(name, criteria.getName()));
			}

			if (criteria.getFinancialYear() != null) {
				predicates.add(criteriaBuilder.equal(financialYear, criteria.getFinancialYear().getId()));
			}

			if (criteria.getStartingDate() != null) {
				predicates.add(criteriaBuilder.equal(startingDate, criteria.getStartingDate()));
			}

			if (criteria.getEndingDate() != null) {
				predicates.add(criteriaBuilder.equal(endingDate, criteria.getEndingDate()));
			}

			if (criteria.getActive() != null) {
				predicates.add(criteriaBuilder.equal(active, criteria.getActive()));
			}

			if (criteria.getIsActiveForPosting() != null) {
				predicates.add(criteriaBuilder.equal(isActiveForPosting, criteria.getIsActiveForPosting()));
			}

			if (criteria.getIsClosed() != null) {
				predicates.add(criteriaBuilder.equal(isClosed, criteria.getIsClosed()));
			}
			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}

		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
