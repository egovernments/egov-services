package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ServiceRequestSearchCriteria {
	private String serviceRequestId;
	private String serviceCode;
	private Date startDate;
	private Date endDate;
	private Date escalationDate;
	private List <String> status;
	private Date startLastModifiedDate;
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

    public Date getEndDate() {
        return getNextDay(endDate);
    }

    public Date getLastModifiedDate() {
        return getNextDay(this.startLastModifiedDate);
    }

    private Date getNextDay(Date endDate) {
        return endDate == null ? null : DateUtils.addDays(endDate, 1);
    }

    public boolean isPaginationCriteriaPresent() {
        return fromIndex != null && pageSize != null;
    }
}