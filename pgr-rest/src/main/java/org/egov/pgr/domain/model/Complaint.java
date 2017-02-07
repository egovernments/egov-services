package org.egov.pgr.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.egov.pgr.domain.exception.InvalidComplaintException;

import java.util.Date;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class Complaint {
    @NonNull
    private AuthenticatedUser authenticatedUser;
    @NonNull
    private ComplaintLocation complaintLocation;
    @NonNull
    private Complainant complainant;
    private String crn;
    private Map<String, String> additionalValues;
    private ComplaintType complaintType;
    private String address;
    private List<String> mediaUrls;
    private String jurisdictionId;
    private String description;
    private Date createdDate;
    private Date lastModifiedDate;
    private Date escalationDate;
    private boolean closed;

    public Complainant getComplainant() {
        return authenticatedUser.isCitizen() ? authenticatedUser.toComplainant() : complainant;
    }

    public boolean isMandatoryFieldsAbsentForAnonymousComplaint() {
        return authenticatedUser.isAnonymousUser() && complainant.isAbsent();
    }

    public boolean isLocationAbsent() {
        return complaintLocation.isAbsent();
    }

    public void validate() {
        if (isLocationAbsent() || isMandatoryFieldsAbsentForAnonymousComplaint()) {
            throw new InvalidComplaintException(this);
        }
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }
}

