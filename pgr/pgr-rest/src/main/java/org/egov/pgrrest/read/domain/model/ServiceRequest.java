package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.egov.pgrrest.common.model.AttributeEntry;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Complainant;
import org.egov.pgrrest.read.domain.exception.InvalidComplaintException;

import java.util.Date;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@AllArgsConstructor
@Builder
@Getter
public class ServiceRequest {
    private static final String MANUAL_RECEIVING_MODE = "MANUAL";
    @NonNull
    private AuthenticatedUser authenticatedUser;
    @NonNull
    private ComplaintLocation complaintLocation;
    @NonNull
    private Complainant requester;
    private String crn;
    @NonNull
    private ComplaintType complaintType;
    private String complaintStatus;
    private String address;
    private List<String> mediaUrls;
    private String tenantId;
    private String description;
    private Date createdDate;
    private Date lastModifiedDate;
    private Date escalationDate;
    private boolean closed;
    private boolean modifyServiceRequest;
    private String receivingMode;
    private String receivingCenter;
    private Long department;
    private Date lastAccessedTime;
    private String childLocation;
    private Long assignee;
    private String state;
    private String citizenFeedback;
    private List<AttributeEntry> attributeEntries;

    public boolean isRequesterAbsent() {
        return requester.isAbsent();
    }

    public boolean isComplainantPhoneAbsent() {
        return requester.isMobileAbsent();
    }

    public boolean isComplainantFirstNameAbsent() {
        return requester.isFirstNameAbsent();
    }

    public boolean isLocationAbsent() {
        if (isModifyServiceRequest()) {
            return complaintLocation.isLocationIdAbsent();
        }
        return complaintLocation.isRawLocationAbsent();
    }

    public boolean isLocationIdAbsent() {
        return complaintLocation.isLocationIdAbsent();
    }

    public boolean isRawLocationAbsent() {
        return complaintLocation.isRawLocationAbsent();
    }

    public void validate() {
        if (isRequesterAbsent()
            || isTenantIdAbsent()
            || isComplaintTypeAbsent()
            || isDescriptionAbsent()
            || isCrnAbsent()
            || descriptionLength()) {
            throw new InvalidComplaintException(this);
        }
    }

    public boolean isTenantIdAbsent() {
        return isEmpty(tenantId);
    }

    public boolean isDescriptionAbsent() {
        return isEmpty(description);
    }

    public boolean isComplaintTypeAbsent() {
        return complaintType.isAbsent();
    }

    public boolean isCrnAbsent() {
        return isModifyServiceRequest() && isEmpty(crn);
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public boolean isReceivingModeAbsent() {
        return authenticatedUser.isEmployee() && isEmpty(receivingMode);
    }

    public boolean isReceivingCenterAbsent() {
        return authenticatedUser.isEmployee()
            && MANUAL_RECEIVING_MODE.equalsIgnoreCase(receivingMode)
            && isEmpty(receivingCenter);
    }

	public boolean descriptionLength() {
		return description.length() < 10 || description.length() >500;
	}
}
