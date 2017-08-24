package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.time.DateTime;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ServiceRequestSearchCriteria {
    private String serviceRequestId;
    private String serviceCode;
    private DateTime startDate;
    private DateTime endDate;
    private DateTime escalationDate;
    private List<String> status;
    private DateTime startLastModifiedDate;
    private Long userId;
    private Long positionId;
    private String name;
    private String mobileNumber;
    private String emailId;
    private String receivingMode;
    private Long locationId;
    private Long childLocationId;
    private String tenantId;
    private String keyword;
    private String serviceCategory;
    private Integer fromIndex;
    private Integer pageSize;
    private boolean isAnonymous;
    private List<String> crnList;
    private boolean searchAttribute;

    public DateTime getEndDate() {
        return getNextDay(endDate);
    }

    public DateTime getLastModifiedDate() {
        return getNextDay(this.startLastModifiedDate);
    }

    private DateTime getNextDay(DateTime endDate) {
        return endDate == null ? null : endDate.plusDays(1);
    }

    public boolean isPaginationCriteriaPresent() {
        return fromIndex != null && pageSize != null;
    }

}