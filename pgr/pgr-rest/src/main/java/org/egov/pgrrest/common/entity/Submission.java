package org.egov.pgrrest.common.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

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

//    @Column(name = "location")
//    private Long location;

//    @Column(name = "childlocation")
//    private Long childLocation;

    @Column(name = "status")
    private String status;

    private String details;

    @Column(name = "landmarkdetails")
    private String landmarkDetails;

//	@ManyToOne
//	@JoinColumn(name = "receivingmode")
//	private ReceivingMode receivingMode;

//    @ManyToOne
//    @JoinColumn(name = "receivingcenter")
//    private ReceivingCenter receivingCenter;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "escalationdate", nullable = false)
    private Date escalationDate;

    private Long department;

//    @Column(name = "citizenfeedback")
//    @Enumerated(EnumType.ORDINAL)
//    private org.egov.pgrrest.common.entity.enums.CitizenFeedback citizenFeedback;

//    @Transient
//    private Long crossHierarchyId;

//    @Column(name = "state_id")
//    private Long stateId;
//

	@Column(name = "tenantid")
	private String tenantId;

//    public boolean isCompleted() {
//        return org.egov.pgrrest.common.entity.enums.ComplaintStatus
//                .valueOf(getStatus()) == org.egov.pgrrest.common.entity.enums.ComplaintStatus.COMPLETED;
//    }
//
//    public org.egov.pgrrest.read.domain.model.ServiceRequest toDomain() {
//        final Coordinates coordinates = new Coordinates(latitude, longitude, tenantId);
//        return org.egov.pgrrest.read.domain.model.ServiceRequest.builder()
//                .complaintLocation(new ComplaintLocation(coordinates, null, null, tenantId))
//                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
//                .address(landmarkDetails)
//                .description(details)
//                .crn(id)
//                .createdDate(getCreatedDate())
//                .lastModifiedDate(getLastModifiedDate())
//                .mediaUrls(Collections.emptyList())
//                .escalationDate(getEscalationDate())
//                .closed(isCompleted())
//                .department(getDepartment())
//                .lastAccessedTime(getLastModifiedDate())
//                .assignee(getAssignee())
//                .tenantId(tenantId)
//                .complaintStatus(getComplaintStatus())
//                .build();
//    }

//    private String getComplaintStatus(){
//    	return status;
//    }

}