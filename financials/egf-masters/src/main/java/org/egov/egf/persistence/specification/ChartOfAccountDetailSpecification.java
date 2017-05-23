package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.AccountDetailType;
import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.entity.ChartOfAccountDetail;
import org.egov.egf.persistence.entity.ChartOfAccountDetail_;
import org.egov.egf.persistence.queue.contract.ChartOfAccountDetailContract;
import org.springframework.data.jpa.domain.Specification;

public class ChartOfAccountDetailSpecification implements Specification<ChartOfAccountDetail> {
	private ChartOfAccountDetailContract criteria;

	public ChartOfAccountDetailSpecification(ChartOfAccountDetailContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<ChartOfAccountDetail> root, CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(ChartOfAccountDetail_.id);
		Path<String> tenantId = root.get(ChartOfAccountDetail_.tenantId);
		Path<Long> chartOfAccount = root.get(ChartOfAccountDetail_.chartOfAccount);
		Path<Long> accountDetailType = root.get(ChartOfAccountDetail_.accountDetailType);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getChartOfAccount() != null) {
				predicates.add(criteriaBuilder.equal(chartOfAccount, criteria.getChartOfAccount().getId()));
			}

			if (criteria.getAccountDetailType() != null) {
				predicates.add(criteriaBuilder.equal(accountDetailType, criteria.getAccountDetailType().getId()));
			}
			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
