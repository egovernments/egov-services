package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.egov.pgrrest.common.model.AttributeEntry;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Requester;
import org.egov.pgrrest.read.domain.exception.InvalidComplaintException;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@AllArgsConstructor
@Builder
@Getter
public class ServiceRequest {
    public static final String PROCESSINGFEE = "PROCESSINGFEE";
    @NonNull
    private AuthenticatedUser authenticatedUser;
    @NonNull
    private ServiceRequestLocation serviceRequestLocation;
    @NonNull
    private Requester requester;
    private String crn;
    @NonNull
    private ServiceRequestType serviceRequestType;
    private String serviceRequestStatus;
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
    private String childLocation;
    private Long assignee;
    private String state;
    private String citizenFeedback;
    private List<AttributeEntry> attributeEntries;

    public List<AttributeEntry> getAttributeEntries() {
        return attributeEntries == null ? Collections.emptyList() : attributeEntries;
    }

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
            return serviceRequestLocation.isLocationIdAbsent();
        }
        return serviceRequestLocation.isRawLocationAbsent();
    }

    public boolean isLocationIdAbsent() {
        return serviceRequestLocation.isLocationIdAbsent();
    }

    public boolean isRawLocationAbsent() {
        return serviceRequestLocation.isRawLocationAbsent();
    }

    public void validate() {
        if (isRequesterAbsent()
            || isTenantIdAbsent()
            || isServiceRequestTypeAbsent()
            || isDescriptionAbsent()
            || isCrnAbsent()
            || descriptionLength()
            || isProcessingFeePresentForCreation()
        	|| !emailValidate()) {
            throw new InvalidComplaintException(this);
        }
    }

    public boolean isTenantIdAbsent() {
        return isEmpty(tenantId);
    }

    public boolean isDescriptionAbsent() {
        return isEmpty(description);
    }

    public boolean isServiceRequestTypeAbsent() {
        return serviceRequestType.isAbsent();
    }

    public boolean isCrnAbsent() {
        return isModifyServiceRequest() && isEmpty(crn);
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public boolean isProcessingFeePresentForCreation(){
        return  modifyServiceRequest == false && attributeEntries.stream().anyMatch(a -> PROCESSINGFEE.equals(a.getKey()));
    }

	public boolean descriptionLength() {
		return description.length() < 10 || description.length() >500;
	}
	 public boolean emailValidate(){
		return requester.isValidEmailAddress();
	} 
}
