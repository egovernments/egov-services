package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.BankAccount;
import org.egov.egf.persistence.entity.BankAccount_;
import org.egov.egf.persistence.entity.BankBranch;
import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.entity.Fund;
import org.egov.egf.persistence.entity.enums.BankAccountType;
import org.egov.egf.persistence.queue.contract.BankAccountContract;
import org.springframework.data.jpa.domain.Specification;

public class BankAccountSpecification implements Specification<BankAccount> {
	private BankAccountContract criteria;

	public BankAccountSpecification(BankAccountContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<BankAccount> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(BankAccount_.id);
		Path<Long> bankBranch = root.get(BankAccount_.bankBranch);
		Path<Long> chartOfAccount = root.get(BankAccount_.chartOfAccount);
		Path<Long> fund = root.get(BankAccount_.fund);
		Path<String> accountNumber = root.get(BankAccount_.accountNumber);
		Path<String> accountType = root.get(BankAccount_.accountType);
		Path<String> description = root.get(BankAccount_.description);
		Path<Boolean> active = root.get(BankAccount_.active);
		Path<String> payTo = root.get(BankAccount_.payTo);
		Path<BankAccountType> type = root.get(BankAccount_.type);
		Path<String> tenantId = root.get(BankAccount_.tenantId);
		final List<Predicate> predicates = new ArrayList<>();
		if (criteria != null) {
			if (criteria.getId() != null) {
				predicates.add(criteriaBuilder.equal(id, criteria.getId()));
			}

			if (criteria.getBankBranch() != null) {
				predicates.add(criteriaBuilder.equal(bankBranch, criteria.getBankBranch().getId()));
			}

			if (criteria.getChartOfAccount() != null) {
				predicates.add(criteriaBuilder.equal(chartOfAccount, criteria.getChartOfAccount().getId()));
			}

			if (criteria.getFund() != null) {
				predicates.add(criteriaBuilder.equal(fund, criteria.getFund().getId()));
			}

			if (criteria.getAccountNumber() != null) {
				predicates.add(criteriaBuilder.equal(accountNumber, criteria.getAccountNumber()));
			}

			if (criteria.getAccountType() != null) {
				predicates.add(criteriaBuilder.equal(accountType, criteria.getAccountType()));
			}

			if (criteria.getDescription() != null) {
				predicates.add(criteriaBuilder.equal(description, criteria.getDescription()));
			}

			if (criteria.getActive() != null) {
				predicates.add(criteriaBuilder.equal(active, criteria.getActive()));
			}

			if (criteria.getPayTo() != null) {
				predicates.add(criteriaBuilder.equal(payTo, criteria.getPayTo()));
			}

			if (criteria.getType() != null) {
				predicates.add(criteriaBuilder.equal(type, criteria.getType()));
			}
			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
