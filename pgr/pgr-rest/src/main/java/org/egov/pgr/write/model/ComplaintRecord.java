package org.egov.pgr.write.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

/*
    Represents a complaint to be created or updated.
 */
@Builder
@Getter
public class ComplaintRecord {
    private String CRN;
    private double latitude;
    private double longitude;
    private String description;
    private String landmarkDetails;
    private Long createdBy;
    private Long lastModifiedBy;
    private Date createdDate;
    private Date lastModifiedDate;
    private String complainantName;
    private String complainantMobileNumber;
    private String complainantEmail;
    private String complainantAddress;
    private Long complainantUserId;
    private String receivingMode;
    private Long receivingCenter;
    private String complaintTypeCode;
    private String complaintStatus;
    private Long assigneeId;
    private Long location;
    private Long childLocation;
    private Date escalationDate;
    private Long workflowStateId;
    private Long department;
    private String tenantId;
    private String citizenFeedback;
}
