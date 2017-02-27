package org.egov.egf.persistence.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.entity.ChartOfAccountDetail;
import org.egov.egf.persistence.entity.ChartOfAccount_;
import org.egov.egf.persistence.queue.contract.ChartOfAccountContract;
import org.springframework.data.jpa.domain.Specification;

public class ChartOfAccountSpecification implements Specification<ChartOfAccount> {
	private ChartOfAccountContract criteria;

	public ChartOfAccountSpecification(ChartOfAccountContract criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<ChartOfAccount> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Path<Long> id = root.get(ChartOfAccount_.id);
		Path<String> glcode = root.get(ChartOfAccount_.glcode);
		Path<String> name = root.get(ChartOfAccount_.name);
		//Path<AccountCodePurpose> accountCodePurpose = root.get(ChartOfAccount_.accountCodePurpose);
		Path<String> desciption = root.get(ChartOfAccount_.desciption);
		Path<Boolean> isActiveForPosting = root.get(ChartOfAccount_.isActiveForPosting);
		/*
		 * Path<ChartOfAccount> parentId = root.get(ChartOfAccount_.parentId);
		 */
		Path<Character> type = root.get(ChartOfAccount_.type);
		Path<Long> classification = root.get(ChartOfAccount_.classification);
		Path<Boolean> functionRequired = root.get(ChartOfAccount_.functionRequired);
		Path<Boolean> budgetCheckRequired = root.get(ChartOfAccount_.budgetCheckRequired);
		Path<String> majorCode = root.get(ChartOfAccount_.majorCode);

		//Expression<Set<ChartOfAccountDetail>> chartOfAccountDetails = root.get(ChartOfAccount_.chartOfAccountDetails);
		final List<Predicate> predicates = new ArrayList<>();
		if(criteria!=null)
		{
		if (criteria.getId() != null) {
			predicates.add(criteriaBuilder.equal(id, criteria.getId()));
		}

		if (criteria.getGlcode() != null) {
			predicates.add(criteriaBuilder.equal(glcode, criteria.getGlcode()));
		}

		if (criteria.getName() != null) {
			predicates.add(criteriaBuilder.equal(name, criteria.getName()));
		}

	/*	if (criteria.getAccountCodePurpose() != null) {
			predicates.add(criteriaBuilder.equal(accountCodePurpose, criteria.getAccountCodePurpose()));
		}*/

		if (criteria.getDesciption() != null) {
			predicates.add(criteriaBuilder.equal(desciption, criteria.getDesciption()));
		}

		if (criteria.getIsActiveForPosting() != null) {
			predicates.add(criteriaBuilder.equal(isActiveForPosting, criteria.getIsActiveForPosting()));
		}

		/*
		 * if (criteria.getParentId() != null) {
		 * predicates.add(criteriaBuilder.equal(parentId,
		 * criteria.getParentId())); }
		 */

		if (criteria.getType() != null) {
			predicates.add(criteriaBuilder.equal(type, criteria.getType()));
		}

		if (criteria.getClassification() != null) {
			predicates.add(criteriaBuilder.equal(classification, criteria.getClassification()));
		}

		if (criteria.getFunctionRequired() != null) {
			predicates.add(criteriaBuilder.equal(functionRequired, criteria.getFunctionRequired()));
		}

		if (criteria.getBudgetCheckRequired() != null) {
			predicates.add(criteriaBuilder.equal(budgetCheckRequired, criteria.getBudgetCheckRequired()));
		}

		if (criteria.getMajorCode() != null) {
			predicates.add(criteriaBuilder.equal(majorCode, criteria.getMajorCode()));
		}
 

		/*if (criteria.getChartOfAccountDetails() != null) {
			predicates.add(criteriaBuilder.equal(chartOfAccountDetails, criteria.getChartOfAccountDetails()));
		}*/
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
