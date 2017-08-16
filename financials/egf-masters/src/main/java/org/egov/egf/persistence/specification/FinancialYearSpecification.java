package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.FinancialYear;
import org.egov.egf.persistence.entity.FinancialYear_;
import org.egov.egf.persistence.queue.contract.FinancialYearContract;
import org.springframework.data.jpa.domain.Specification;

public class FinancialYearSpecification implements Specification<FinancialYear> {
	private FinancialYearContract criteria;

	public FinancialYearSpecification(FinancialYearContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<FinancialYear> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(FinancialYear_.id);
		Path<String> finYearRange = root.get(FinancialYear_.finYearRange);
		Path<Date> startingDate = root.get(FinancialYear_.startingDate);
		Path<Date> endingDate = root.get(FinancialYear_.endingDate);
		Path<Boolean> active = root.get(FinancialYear_.active);
		Path<String> tenantId = root.get(FinancialYear_.tenantId);
		Path<Boolean> isActiveForPosting = root.get(FinancialYear_.isActiveForPosting);
		Path<Boolean> isClosed = root.get(FinancialYear_.isClosed);
		Path<Boolean> transferClosingBalance = root.get(FinancialYear_.transferClosingBalance);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getFinYearRange() != null) {
				predicates.add(criteriaBuilder.equal(finYearRange, criteria.getFinYearRange()));
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

			if (criteria.getAsOnDate() != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(startingDate, criteria.getAsOnDate()));
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(endingDate, criteria.getAsOnDate()));
			}
			
			if (criteria.getIsClosed() != null) {
				predicates.add(criteriaBuilder.equal(isClosed, criteria.getIsClosed()));
			}

			if (criteria.getTransferClosingBalance() != null) {
				predicates.add(criteriaBuilder.equal(transferClosingBalance, criteria.getTransferClosingBalance()));
			}

			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}

		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
