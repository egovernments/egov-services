package org.egov.pgrrest.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.egov.pgr.common.model.OtpValidationRequest;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.AuthenticatedUser;
import org.egov.pgrrest.common.domain.model.Requester;
import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@AllArgsConstructor
@Builder
@Getter
public class ServiceRequest {
    private static final String DRAFT_ID = "draftId";
    private static final String SERVICE_STATUS = "systemStatus";
    public static final String COMPLAINT = "Complaint";
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
    private Long position;
    private String state;
    private String citizenFeedback;
    private String otpReference;
    private List<AttributeEntry> attributeEntries;

    public boolean isComplaintType() {
        return serviceRequestType.isComplaintType();
    }

    public boolean isAnonymous() {
        return authenticatedUser.isAnonymousUser();
    }

    public List<AttributeEntry> getAttributeEntries() {
        return attributeEntries == null ? Collections.emptyList() : attributeEntries;
    }

    public void validateAttributeEntries() {
        if (attributeEntries == null) {
            return;
        }
        attributeEntries.forEach(AttributeEntry::validate);
    }

    public boolean isRequesterAbsent() {
        return requester.isAbsent();
    }

    public boolean isValidMobileNumber() {
        return requester.isMobileNumberValid();
    }

    public boolean isComplainantPhoneAbsent() {
        return requester.isMobileAbsent();
    }

    public boolean isComplainantFirstNameAbsent() {
        return requester.isFirstNameAbsent();
    }

    public boolean isNewServiceRequest() {
        return !isModifyServiceRequest();
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
        return isEmpty(crn);
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public Long getDraftId() {
        return attributeEntries.stream()
            .filter(attributeEntry -> DRAFT_ID.equals(attributeEntry.getKey()))
            .map(AttributeEntry::getCode)
            .findFirst()
            .map(Long::valueOf)
            .orElse(null);
    }

    public ServiceStatus getServiceStatus() {
        final AttributeEntry attributeEntry = getAttributeWithKey(SERVICE_STATUS);
        return ServiceStatus.parse(attributeEntry.getCode());
    }

    public boolean descriptionLength() {
        return description.length() >= 10 && description.length() <= 1000;
    }

    public boolean firstNameLength() {
        return requester.getFirstName().length() > 0 && requester.getFirstName().length() <= 100;
    }

    public boolean emailLength() {
        if (isEmpty(requester.getEmail()))
            return false;
        else
            return requester.getEmail().length() > 0 && requester.getEmail().length() <= 100;
    }

    public boolean tenantIdLength() {
        if (isEmpty(tenantId))
            return false;
        else
            return tenantId.length() > 0 && tenantId.length() <= 256;
    }

    public boolean isEmailValid() {
        return requester.isValidEmailAddress();
    }

    public void maskUserDetails() {
        getRequester().maskMobileAndEmailDetails();
    }

    public OtpValidationRequest getOtpValidationRequest() {
        return OtpValidationRequest.builder()
            .mobileNumber(requester.getMobile())
            .otpReference(otpReference)
            .tenantId(tenantId)
            .build();
    }

    public void setServiceType(boolean isComplaintType) {
        serviceRequestType.setServiceType(isComplaintType);
    }

    public ServiceDefinitionSearchCriteria getServiceDefinitionSearchCriteria() {
        return serviceRequestType.getSearchCriteria();
    }

    public String getServiceTypeCode() {
        return getServiceRequestType().getCode();
    }

    public List<AttributeEntry> getAttributesWithKey(String attributeDefinitionCode) {
        return attributeEntries.stream()
            .filter(a -> attributeDefinitionCode.equals(a.getKey()))
            .collect(Collectors.toList());
    }

    public AttributeEntry getAttributeWithKey(String attributeDefinitionCode) {
        final List<AttributeEntry> matchingAttributes = getAttributesWithKey(attributeDefinitionCode);
        if (CollectionUtils.isEmpty(matchingAttributes)) {
            return null;
        }
        return matchingAttributes.get(0);
    }

    public boolean isMultipleAttributeEntriesPresent(String attributeCode) {
        return getAttributesWithKey(attributeCode).size() > 1;
    }

    public boolean isAttributeAbsent(String key) {
        List<AttributeEntry> attributeValueByKey = getAttributeValueByKey(key);
        return attributeValueByKey.isEmpty();
    }

    public List<AttributeEntry> getAttributeValueByKey(String key) {
        return attributeEntries.stream()
            .filter(attributeEntry -> key.equalsIgnoreCase(attributeEntry.getKey()))
            .collect(Collectors.toList());
    }

    public boolean isKeywordComplaint(String key) {
        List<AttributeEntry> attributeValueByKey = getAttributeValueByKey(key);
        return !attributeValueByKey.isEmpty() && attributeValueByKey.get(0).getCode().equalsIgnoreCase(COMPLAINT);
    }

    public boolean isCoordinatesAbsent() {
        return serviceRequestLocation.isCoordinatesAbsent();
    }
}
