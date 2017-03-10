package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.entity.BankBranch;
import org.egov.egf.persistence.entity.BankBranch_;
import org.egov.egf.persistence.queue.contract.BankBranchContract;
import org.springframework.data.jpa.domain.Specification;

public class BankBranchSpecification implements Specification<BankBranch> {
	private BankBranchContract criteria;

	public BankBranchSpecification(BankBranchContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<BankBranch> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(BankBranch_.id);
		Path<Bank> bank = root.get(BankBranch_.bank);
		Path<String> code = root.get(BankBranch_.code);
		Path<String> name = root.get(BankBranch_.name);
		Path<String> address = root.get(BankBranch_.address);
		Path<String> address2 = root.get(BankBranch_.address2);
		Path<String> city = root.get(BankBranch_.city);
		Path<String> state = root.get(BankBranch_.state);
		Path<String> pincode = root.get(BankBranch_.pincode);
		Path<String> phone = root.get(BankBranch_.phone);
		Path<String> fax = root.get(BankBranch_.fax);
		Path<String> contactPerson = root.get(BankBranch_.contactPerson);
		Path<Boolean> active = root.get(BankBranch_.active);
		Path<String> description = root.get(BankBranch_.description);
		Path<String> micr = root.get(BankBranch_.micr);
		final List<Predicate> predicates = new ArrayList<>();
		if(criteria!=null)
		{
		if (criteria.getId() != null) {
			predicates.add(criteriaBuilder.equal(id, criteria.getId()));
		}

		if (criteria.getBank() != null) {
			predicates.add(criteriaBuilder.equal(bank, criteria.getBank()));
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

		if (criteria.getAddress2() != null) {
			predicates.add(criteriaBuilder.equal(address2, criteria.getAddress2()));
		}

		if (criteria.getCity() != null) {
			predicates.add(criteriaBuilder.equal(city, criteria.getCity()));
		}

		if (criteria.getState() != null) {
			predicates.add(criteriaBuilder.equal(state, criteria.getState()));
		}

		if (criteria.getPincode() != null) {
			predicates.add(criteriaBuilder.equal(pincode, criteria.getPincode()));
		}

		if (criteria.getPhone() != null) {
			predicates.add(criteriaBuilder.equal(phone, criteria.getPhone()));
		}

		if (criteria.getFax() != null) {
			predicates.add(criteriaBuilder.equal(fax, criteria.getFax()));
		}

		if (criteria.getContactPerson() != null) {
			predicates.add(criteriaBuilder.equal(contactPerson, criteria.getContactPerson()));
		}

		if (criteria.getActive() != null) {
			predicates.add(criteriaBuilder.equal(active, criteria.getActive()));
		}

		if (criteria.getDescription() != null) {
			predicates.add(criteriaBuilder.equal(description, criteria.getDescription()));
		}

		if (criteria.getMicr() != null) {
			predicates.add(criteriaBuilder.equal(micr, criteria.getMicr()));
		}
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
