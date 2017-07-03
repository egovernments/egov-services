package org.egov.pgrrest.write.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.egov.pgrrest.common.domain.model.AttributeEntry;

import java.util.Date;
import java.util.List;

/*
    Represents a service request to be created or updated.
 */
@Builder
@Getter
public class ServiceRequestRecord {
    private String CRN;
    private double latitude;
    private double longitude;
    private String description;
    private String landmarkDetails;
    private Long createdBy;
    private Long lastModifiedBy;
    private Date createdDate;
    private Date lastModifiedDate;
    private String requesterName;
    private String requesterMobileNumber;
    private String requesterEmail;
    private String requesterAddress;
    private Long loggedInRequesterUserId;
    private String serviceRequestTypeCode;
    private String serviceRequestStatus;
    private Long positionId;
    private Date escalationDate;
    private Long department;
    private String tenantId;
    private List<AttributeEntry> attributeEntries;
}
