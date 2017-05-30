package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.BudgetGroup;
import org.egov.egf.persistence.entity.BudgetGroup_;
import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.entity.enums.BudgetAccountType;
import org.egov.egf.persistence.entity.enums.BudgetingType;
import org.egov.egf.persistence.queue.contract.BudgetGroupContract;
import org.springframework.data.jpa.domain.Specification;

public class BudgetGroupSpecification implements Specification<BudgetGroup> {
	private BudgetGroupContract criteria;

	public BudgetGroupSpecification(BudgetGroupContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<BudgetGroup> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(BudgetGroup_.id);
		Path<String> name = root.get(BudgetGroup_.name);
		Path<String> description = root.get(BudgetGroup_.description);
		Path<Long> majorCode = root.get(BudgetGroup_.majorCode);
		Path<Long> maxCode = root.get(BudgetGroup_.maxCode);
		Path<Long> minCode = root.get(BudgetGroup_.minCode);
		Path<BudgetAccountType> accountType = root.get(BudgetGroup_.accountType);
		Path<BudgetingType> budgetingType = root.get(BudgetGroup_.budgetingType);
		Path<Boolean> isActive = root.get(BudgetGroup_.active);
		Path<String> tenantId = root.get(BudgetGroup_.tenantId);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getName() != null) {
				predicates.add(criteriaBuilder.equal(name, criteria.getName()));
			}

			if (criteria.getDescription() != null) {
				predicates.add(criteriaBuilder.equal(description, criteria.getDescription()));
			}

			if (criteria.getMajorCode() != null) {
				predicates.add(criteriaBuilder.equal(majorCode, criteria.getMajorCode().getId()));
			}

			if (criteria.getMaxCode() != null) {
				predicates.add(criteriaBuilder.equal(maxCode, criteria.getMaxCode().getId()));
			}

			if (criteria.getMinCode() != null) {
				predicates.add(criteriaBuilder.equal(minCode, criteria.getMinCode().getId()));
			}

			if (criteria.getAccountType() != null) {
				predicates.add(criteriaBuilder.equal(accountType, criteria.getAccountType()));
			}

			if (criteria.getBudgetingType() != null) {
				predicates.add(criteriaBuilder.equal(budgetingType, criteria.getBudgetingType()));
			}

			if (criteria.getIsActive() != null) {
				predicates.add(criteriaBuilder.equal(isActive, criteria.getIsActive()));
			}
			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
