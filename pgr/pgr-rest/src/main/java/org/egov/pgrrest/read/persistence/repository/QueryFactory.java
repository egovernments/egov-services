package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.read.domain.model.ServiceRequestSearchCriteria;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class QueryFactory {
    public BoolQueryBuilder create(ServiceRequestSearchCriteria criteria) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        boolQueryBuilder = addServiceRequestFilter(criteria, boolQueryBuilder);
        boolQueryBuilder = addServiceCodeFilter(criteria, boolQueryBuilder);
        boolQueryBuilder = addRequesterNameFilter(criteria, boolQueryBuilder);
        boolQueryBuilder = addRequesterMobileFilter(criteria, boolQueryBuilder);
        boolQueryBuilder = addRequesterEmailFilter(criteria, boolQueryBuilder);
        boolQueryBuilder = addCreatedDateRangeFilter(criteria, boolQueryBuilder);
        boolQueryBuilder = addStatusFilter(criteria, boolQueryBuilder);
        boolQueryBuilder = addEscalationDateFilter(criteria, boolQueryBuilder);
        boolQueryBuilder = addRequesterIdFilter(criteria, boolQueryBuilder);
        boolQueryBuilder = addAssignmentFilter(criteria, boolQueryBuilder);
        boolQueryBuilder = addReceivingModeFilter(criteria, boolQueryBuilder);
        boolQueryBuilder = addLocationIdFilter(criteria, boolQueryBuilder);
        boolQueryBuilder = addChildLocationFilter(criteria, boolQueryBuilder);
        boolQueryBuilder = addKeywordFilter(criteria, boolQueryBuilder);
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addChildLocationFilter(ServiceRequestSearchCriteria criteria, BoolQueryBuilder
        boolQueryBuilder) {
        if (criteria.getChildLocationId() != null) {
            final String childLocationId = criteria.getChildLocationId().toString();
            boolQueryBuilder = boolQueryBuilder.filter(termQuery("localityNo", childLocationId));
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addKeywordFilter(ServiceRequestSearchCriteria criteria, BoolQueryBuilder
        boolQueryBuilder) {
        if (isNotEmpty(criteria.getKeyword())) {
            boolQueryBuilder = boolQueryBuilder.filter(termQuery("keyword", criteria.getKeyword()));
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addLocationIdFilter(ServiceRequestSearchCriteria criteria,
                                                 BoolQueryBuilder boolQueryBuilder) {
        if (criteria.getLocationId() != null) {
            boolQueryBuilder = boolQueryBuilder.filter(termQuery("wardNo", criteria.getLocationId().toString()));
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addReceivingModeFilter(ServiceRequestSearchCriteria criteria,
                                                    BoolQueryBuilder boolQueryBuilder) {
        if (criteria.getReceivingMode() != null) {
            boolQueryBuilder = boolQueryBuilder.filter(termQuery("receivingMode", criteria.getReceivingMode()));
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addAssignmentFilter(ServiceRequestSearchCriteria criteria,
                                                 BoolQueryBuilder boolQueryBuilder) {
        if (criteria.getAssignmentId() != null) {
            boolQueryBuilder = boolQueryBuilder.filter(termQuery("assigneeId", criteria.getAssignmentId()));
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addRequesterIdFilter(ServiceRequestSearchCriteria criteria,
                                                  BoolQueryBuilder boolQueryBuilder) {
        if (criteria.getUserId() != null) {
            boolQueryBuilder = boolQueryBuilder.filter(termQuery("requesterId", criteria.getUserId()));
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addEscalationDateFilter(ServiceRequestSearchCriteria criteria,
                                                     BoolQueryBuilder boolQueryBuilder) {
        if (criteria.getEscalationDate() != null) {
            final RangeQueryBuilder rangeQueryBuilder = rangeQuery("created")
                .from(criteria.getEscalationDate().getTime())
                .includeLower(true);
            boolQueryBuilder = boolQueryBuilder.filter(rangeQueryBuilder);
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addStatusFilter(ServiceRequestSearchCriteria criteria, BoolQueryBuilder boolQueryBuilder) {
        if (!isEmpty(criteria.getStatus())) {
            boolQueryBuilder = boolQueryBuilder.filter(termsQuery("serviceStatusCode", criteria.getStatus()));
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addCreatedDateRangeFilter(ServiceRequestSearchCriteria criteria,
                                                       BoolQueryBuilder boolQueryBuilder) {
        if (criteria.getStartDate() != null) {
            final RangeQueryBuilder rangeQueryBuilder = rangeQuery("created")
                .from(criteria.getStartDate().getTime())
                .to(criteria.getEndDate().getTime())
                .includeLower(true)
                .includeUpper(true);
            boolQueryBuilder = boolQueryBuilder.filter(rangeQueryBuilder);
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addRequesterEmailFilter(ServiceRequestSearchCriteria criteria,
                                                     BoolQueryBuilder boolQueryBuilder) {
        if (isNotEmpty(criteria.getEmailId())) {
            boolQueryBuilder = boolQueryBuilder.filter(termQuery("requesterEmail", criteria.getEmailId()));
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addRequesterMobileFilter(ServiceRequestSearchCriteria criteria,
                                                      BoolQueryBuilder boolQueryBuilder) {
        if (isNotEmpty(criteria.getMobileNumber())) {
            boolQueryBuilder = boolQueryBuilder.filter(termQuery("requesterMobile", criteria.getMobileNumber()));
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addRequesterNameFilter(ServiceRequestSearchCriteria criteria,
                                                    BoolQueryBuilder boolQueryBuilder) {
        if (isNotEmpty(criteria.getName())) {
            boolQueryBuilder = boolQueryBuilder.filter(termQuery("requesterName", criteria.getName()));
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addServiceCodeFilter(ServiceRequestSearchCriteria criteria,
                                                  BoolQueryBuilder boolQueryBuilder) {
        if (isNotEmpty(criteria.getServiceCode())) {
            boolQueryBuilder = boolQueryBuilder.filter(termQuery("serviceTypeCode", criteria.getServiceCode()));
        }
        return boolQueryBuilder;
    }

    private BoolQueryBuilder addServiceRequestFilter(ServiceRequestSearchCriteria criteria,
                                                     BoolQueryBuilder boolQueryBuilder) {
        if (isNotEmpty(criteria.getServiceRequestId())) {
            boolQueryBuilder = boolQueryBuilder.filter(termQuery("crn", criteria.getServiceRequestId()));
        }
        return boolQueryBuilder;
    }
}
