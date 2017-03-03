package org.egov.pgr.persistence.specification;

import org.egov.pgr.domain.model.ComplaintSearchCriteria;
import org.egov.pgr.persistence.entity.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SevaSpecification implements Specification<Complaint> {
	private ComplaintSearchCriteria criteria;

	public SevaSpecification(ComplaintSearchCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Complaint> root, CriteriaQuery<?> criteriaQuery,
			CriteriaBuilder criteriaBuilder) {
		Path<String> crn = root.get(Complaint_.crn);
		Path<ComplaintType> complaintType = root.get(Complaint_.complaintType);
		Path<String> code = complaintType.get(ComplaintType_.code);
		Path<ComplaintStatus> complaintStatus = root.get(Complaint_.status);
		Path<String> status = complaintStatus.get(ComplaintStatus_.name);
		Path<Date> createdDate = root.get(AbstractAuditable_.createdDate);
		Path<Date> lastModifiedDate = root.get(AbstractAuditable_.lastModifiedDate);
		Path<Long> assignmentId = root.get(Complaint_.assignee);
		Path<Long> userId = root.get(AbstractAuditable_.createdBy);

		final List<Predicate> predicates = new ArrayList<>();
		if (criteria.getServiceRequestId() != null) {
			predicates.add(criteriaBuilder.equal(crn, criteria.getServiceRequestId()));
		}

		if (criteria.getServiceCode() != null) {
			predicates.add(criteriaBuilder.equal(code, criteria.getServiceCode()));
		}

		if (criteria.getStatus() != null) {
			predicates.add(criteriaBuilder.equal(status, criteria.getStatus()));
		}

		if (criteria.getStartDate() != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(createdDate, criteria.getStartDate()));
		}

		if (criteria.getEndDate() != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(createdDate, criteria.getEndDate()));
		}

		if (criteria.getLastModifiedDatetime() != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(lastModifiedDate, criteria.getLastModifiedDatetime()));
		}
		if (criteria.getUserId() != null) {

			predicates.add(criteriaBuilder.equal(userId, criteria.getUserId()));
		}

		if (criteria.getAssignmentId() != null) {

			predicates.add(criteriaBuilder.equal(assignmentId, criteria.getAssignmentId()));
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
