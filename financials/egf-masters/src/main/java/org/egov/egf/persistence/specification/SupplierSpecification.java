package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.entity.Supplier;
import org.egov.egf.persistence.entity.Supplier_;
import org.egov.egf.persistence.queue.contract.SupplierContract;
import org.springframework.data.jpa.domain.Specification;

public class SupplierSpecification implements Specification<Supplier> {
	private SupplierContract criteria;

	public SupplierSpecification(SupplierContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(Supplier_.id);
		Path<String> code = root.get(Supplier_.code);
		Path<String> name = root.get(Supplier_.name);
		Path<String> address = root.get(Supplier_.address);
		Path<String> mobile = root.get(Supplier_.mobile);
		Path<String> email = root.get(Supplier_.email);
		Path<String> description = root.get(Supplier_.description);
		Path<Boolean> active = root.get(Supplier_.active);
		Path<String> panNo = root.get(Supplier_.panNo);
		Path<String> tinNo = root.get(Supplier_.tinNo);
		Path<String> registationNo = root.get(Supplier_.registationNo);
		Path<String> bankAccount = root.get(Supplier_.bankAccount);
		Path<String> ifscCode = root.get(Supplier_.ifscCode);
		Path<Long> bank = root.get(Supplier_.bank);
		Path<String> tenantId = root.get(Supplier_.tenantId);
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

			if (criteria.getAddress() != null) {
				predicates.add(criteriaBuilder.equal(address, criteria.getAddress()));
			}

			if (criteria.getMobile() != null) {
				predicates.add(criteriaBuilder.equal(mobile, criteria.getMobile()));
			}

			if (criteria.getEmail() != null) {
				predicates.add(criteriaBuilder.equal(email, criteria.getEmail()));
			}

			if (criteria.getDescription() != null) {
				predicates.add(criteriaBuilder.equal(description, criteria.getDescription()));
			}

			if (criteria.getActive() != null) {
				predicates.add(criteriaBuilder.equal(active, criteria.getActive()));
			}

			if (criteria.getPanNo() != null) {
				predicates.add(criteriaBuilder.equal(panNo, criteria.getPanNo()));
			}

			if (criteria.getTinNo() != null) {
				predicates.add(criteriaBuilder.equal(tinNo, criteria.getTinNo()));
			}

			if (criteria.getRegistationNo() != null) {
				predicates.add(criteriaBuilder.equal(registationNo, criteria.getRegistationNo()));
			}

			if (criteria.getBankAccount() != null) {
				predicates.add(criteriaBuilder.equal(bankAccount, criteria.getBankAccount()));
			}

			if (criteria.getIfscCode() != null) {
				predicates.add(criteriaBuilder.equal(ifscCode, criteria.getIfscCode()));
			}

			if (criteria.getBank() != null) {
				predicates.add(criteriaBuilder.equal(bank, criteria.getBank().getId()));
			}
			if (criteria.getTenantId() != null) {
				predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
			}

		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
