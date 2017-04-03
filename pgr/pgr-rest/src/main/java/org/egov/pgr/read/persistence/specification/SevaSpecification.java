package org.egov.pgr.read.persistence.specification;

import org.egov.pgr.common.entity.*;
import org.egov.pgr.read.domain.model.ComplaintSearchCriteria;
import org.joda.time.DateTime;
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
        Path<Complainant> complainant = root.get(Complaint_.complainant);
        Path<String> name = complainant.get(Complainant_.name);
        Path<String> emailId = complainant.get(Complainant_.email);
        Path<String> mobileNumber = complainant.get(Complainant_.mobile);
        Path<Long> locationId = root.get(Complaint_.location);
        Path<Long> childLocationId = root.get(Complaint_.childLocation);
        Path<ReceivingMode> receivingMode = root.get(Complaint_.receivingMode);
        Path<Long> mode = receivingMode.get(ReceivingMode_.id);

        List<Predicate> predicates = new ArrayList<>();
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
            predicates.add(
                criteriaBuilder.lessThan(createdDate, new DateTime(criteria.getEndDate()).plusDays(1).toDate()));
        }

        if (criteria.getLastModifiedDatetime() != null) {
            predicates.add(criteriaBuilder.greaterThan(lastModifiedDate, criteria.getLastModifiedDatetime()));
        }
        if (criteria.getUserId() != null) {

            predicates.add(criteriaBuilder.equal(userId, criteria.getUserId()));
        }

        if (criteria.getAssignmentId() != null) {

            predicates.add(criteriaBuilder.equal(assignmentId, criteria.getAssignmentId()));
        }

        if (criteria.getName() != null) {
            predicates.add(criteriaBuilder.equal(name, criteria.getName()));
        }

        if (criteria.getEmailId() != null) {
            predicates.add(criteriaBuilder.equal(emailId, criteria.getEmailId()));
        }

        if (criteria.getMobileNumber() != null) {
            predicates.add(criteriaBuilder.equal(mobileNumber, criteria.getMobileNumber()));
        }

        if (criteria.getLocationId() != null) {
            predicates.add(criteriaBuilder.equal(locationId, criteria.getLocationId()));

        }
        if (criteria.getChildLocationId() != null) {
            predicates.add(criteriaBuilder.equal(childLocationId, criteria.getChildLocationId()));
        }
        if (criteria.getReceivingMode() != null) {
            predicates.add(criteriaBuilder.equal(mode, criteria.getReceivingMode()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
