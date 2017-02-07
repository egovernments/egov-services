package org.egov.pgr.persistence.queue.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class Complaint {

    @JsonProperty("crn")
    private String crn;

    @JsonProperty("jurisdictionId")
    private String jurisdictionId;

    @JsonProperty("complaintTypeCode")
    private String complaintTypeCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("address")
    private String address;

    @JsonProperty("crossHierarchyId")
    private String crossHierarchyId;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("mediaUrls")
    private List<String> mediaUrls;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("action")
    private String action;

    @JsonProperty("additionalValues")
    private Map<String, String> additionalValues;

    public Complaint(org.egov.pgr.domain.model.Complaint complaint, String action) {
        this.crn = complaint.getCrn();
        this.jurisdictionId = complaint.getJurisdictionId();
        this.complaintTypeCode = complaint.getComplaintType().getCode();
        this.description = complaint.getDescription();
        this.address = complaint.getAddress();
        this.crossHierarchyId = complaint.getComplaintLocation().getCrossHierarchyId();
        this.latitude = complaint.getComplaintLocation().getCoordinates().getLatitude();
        this.longitude = complaint.getComplaintLocation().getCoordinates().getLongitude();
        this.mediaUrls = complaint.getMediaUrls();
        this.firstName = complaint.getComplainant().getFirstName();
        this.email = complaint.getComplainant().getEmail();
        this.phone = complaint.getComplainant().getPhone();
        this.additionalValues = complaint.getAdditionalValues();
        this.action = action;
    }
}
