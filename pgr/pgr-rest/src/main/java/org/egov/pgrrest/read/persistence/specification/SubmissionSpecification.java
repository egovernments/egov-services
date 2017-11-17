package org.egov.pgrrest.read.persistence.specification;


import org.egov.pgrrest.common.persistence.entity.*;
import org.egov.pgrrest.read.domain.model.ServiceRequestSearchCriteria;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubmissionSpecification implements Specification<Submission> {
    private ServiceRequestSearchCriteria criteria;

    public SubmissionSpecification(ServiceRequestSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Submission> root, CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {
        Path<SubmissionKey> key = root.get(Submission_.id);
        Path<String> crn = key.get(SubmissionKey_.crn);
        Path<String> tenantId = key.get(SubmissionKey_.tenantId);
        Path<String> code = root.get(Submission_.serviceCode);
        Path<String> status = root.get(Submission_.status);
        Path<Date> createdDate = root.get(AbstractAuditable_.createdDate);
        Path<String> name = root.get(Submission_.name);
        Path<String> emailId = root.get(Submission_.email);
        Path<String> mobileNumber = root.get(Submission_.mobile);
        Path<Date> escalationDate = root.get(Submission_.escalationDate);
        Path<Long> positionId = root.get(Submission_.position);
        Path<Long> loggedInRequester = root.get(Submission_.loggedInRequester);

        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getTenantId() != null && !criteria.getTenantId().isEmpty()) {
            predicates.add(criteriaBuilder.equal(tenantId, criteria.getTenantId()));
        }

        if (criteria.getServiceRequestId() != null) {
            predicates.add(criteriaBuilder.equal(crn, criteria.getServiceRequestId()));
        }

        if (criteria.getServiceCode() != null) {
            predicates.add(criteriaBuilder.equal(code, criteria.getServiceCode()));
        }

        if (criteria.getStatus() != null) {
            predicates.add(status.in(criteria.getStatus()));

        }

        if (criteria.getStartDate() != null) {
            predicates.add(
                criteriaBuilder.greaterThanOrEqualTo(createdDate, new DateTime(criteria.getStartDate()).toDate()));
        }
        
        if (criteria.getEndDate() != null) {
            predicates.add(
                criteriaBuilder.lessThan(createdDate, new DateTime(criteria.getEndDate()).toDate()));
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

        if (criteria.getEscalationDate() != null) {
            predicates.add(
                criteriaBuilder.lessThan(escalationDate, new DateTime(criteria.getEscalationDate()).plusDays(1).toDate()));
        }

        if (criteria.getPositionId() != null) {
            predicates.add(criteriaBuilder.equal(positionId, criteria.getPageSize()));
        }

        if (criteria.getCrnList() != null && !criteria.getCrnList().isEmpty() && criteria.isSearchAttribute()) {
            predicates.add(crn.in(criteria.getCrnList()));
        }

        if (null != criteria.getUserId()) {
            predicates.add(criteriaBuilder.equal(loggedInRequester, criteria.getUserId()));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}



