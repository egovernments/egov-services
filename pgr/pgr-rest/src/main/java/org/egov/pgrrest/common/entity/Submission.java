package org.egov.pgrrest.common.entity;

import lombok.*;
import org.egov.pgrrest.common.model.AttributeEntry;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.read.domain.model.ComplaintLocation;
import org.egov.pgrrest.read.domain.model.ComplaintType;
import org.egov.pgrrest.read.domain.model.Coordinates;
import org.egov.pgrrest.read.domain.model.ServiceRequest;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "submission")
public class Submission extends AbstractAuditable<String> {
    @Id
    @Column(name = "crn", unique = true)
    private String id;

    @Column(name = "servicecode")
    private String serviceCode;

    private String name;

    private String mobile;

    private String email;

    @Column(name = "loggedinrequester")
    private Long loggedInRequester;

    @Column(name = "requesteraddress")
    private String requesterAddress;

    private Long assignee;

    @Column(name = "status")
    private String status;

    private String details;

    @Column(name = "landmarkdetails")
    private String landmarkDetails;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "escalationdate", nullable = false)
    private Date escalationDate;

    private Long department;

    @Column(name = "tenantid")
    private String tenantId;

    @Transient
    private List<SubmissionAttribute> attributeValues;

    @Transient
    private ServiceType serviceType;

    public ServiceRequest toDomain() {
        final Coordinates coordinates = new Coordinates(latitude, longitude, tenantId);
        return ServiceRequest.builder()
            .complaintLocation(new ComplaintLocation(coordinates, null, null, tenantId))
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .address(landmarkDetails)
            .description(details)
            .complainant(getComplainant())
            .complaintType(getDomainComplaintType())
            .crn(id)
            .createdDate(getCreatedDate())
            .lastModifiedDate(getLastModifiedDate())
            .mediaUrls(Collections.emptyList())
            .escalationDate(getEscalationDate())
            .closed(isCompleted())
            .department(getDepartment())
            .lastAccessedTime(getLastModifiedDate())
            .assignee(getAssignee())
            .tenantId(tenantId)
            .complaintStatus(status)
            .attributeEntries(getAttributeEntries())
            .build();
    }

    private ComplaintType getDomainComplaintType() {
        return new ComplaintType(this.serviceType.getName(),
            this.serviceType.getCode(),
            this.serviceType.getTenantId());
    }

    private org.egov.pgrrest.common.model.Complainant getComplainant() {
        return org.egov.pgrrest.common.model.Complainant.builder()
            .firstName(name)
            .mobile(mobile)
            .email(email)
            .address(requesterAddress)
            .tenantId(tenantId)
            .build();
    }

    private boolean isCompleted() {
        return org.egov.pgrrest.common.entity.enums.ComplaintStatus
            .valueOf(getStatus()) == org.egov.pgrrest.common.entity.enums.ComplaintStatus.COMPLETED;
    }

    private List<AttributeEntry> getAttributeEntries() {
        return attributeValues.stream()
            .map(SubmissionAttribute::toDomain)
            .collect(Collectors.toList());
    }

}