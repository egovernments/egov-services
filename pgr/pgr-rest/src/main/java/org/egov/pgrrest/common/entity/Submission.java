package org.egov.pgrrest.common.entity;

import lombok.*;
import org.egov.pgrrest.common.model.AttributeEntry;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Requester;
import org.egov.pgrrest.read.domain.model.ServiceRequestLocation;
import org.egov.pgrrest.read.domain.model.ServiceRequestType;
import org.egov.pgrrest.read.domain.model.Coordinates;
import org.egov.pgrrest.read.domain.model.ServiceRequest;

import javax.persistence.*;
import java.util.Arrays;
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
public class Submission extends AbstractAuditable<SubmissionKey> {
    private static List<String> RESOLVED_STATUES = Arrays.asList("COMPLETED", "APPROVED");

    @EmbeddedId
    private SubmissionKey id;

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

    @Transient
    private List<SubmissionAttribute> attributeValues;

    @Transient
    private ServiceType serviceType;

    public String getCrn() {
        return id.getCrn();
    }

    public ServiceRequest toDomain() {
        final Coordinates coordinates = new Coordinates(latitude, longitude);
        return ServiceRequest.builder()
            .serviceRequestLocation(new ServiceRequestLocation(coordinates, null, null))
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .address(landmarkDetails)
            .description(details)
            .requester(getComplainant())
            .serviceRequestType(getDomainServiceRequestType())
            .crn(id.getCrn())
            .createdDate(getCreatedDate())
            .lastModifiedDate(getLastModifiedDate())
            .mediaUrls(Collections.emptyList())
            .escalationDate(getEscalationDate())
            .closed(isCompleted())
            .department(getDepartment())
            .assignee(getAssignee())
            .tenantId(id.getTenantId())
            .serviceRequestStatus(status)
            .attributeEntries(getAttributeEntries())
            .build();
    }

    private ServiceRequestType getDomainServiceRequestType() {
        return new ServiceRequestType(this.serviceType.getName(),
            this.serviceType.getCode(),
            this.serviceType.getTenantId());
    }

    private Requester getComplainant() {
        return Requester.builder()
            .firstName(name)
            .mobile(mobile)
            .email(email)
            .address(requesterAddress)
            .build();
    }

    private boolean isCompleted() {
        return RESOLVED_STATUES.stream().anyMatch(status -> status.equalsIgnoreCase(this.status));
    }

    private List<AttributeEntry> getAttributeEntries() {
        return attributeValues.stream()
            .map(SubmissionAttribute::toDomain)
            .collect(Collectors.toList());
    }

}