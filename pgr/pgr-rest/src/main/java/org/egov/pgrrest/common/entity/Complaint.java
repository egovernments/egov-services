package org.egov.pgrrest.common.entity;

import lombok.*;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.read.domain.model.ServiceRequestLocation;
import org.egov.pgrrest.read.domain.model.Coordinates;
import org.egov.pgrrest.read.domain.model.ServiceRequest;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;

import static org.egov.pgrrest.common.entity.Complaint.SEQ_COMPLAINT;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "egpgr_complaint")
@SequenceGenerator(name = SEQ_COMPLAINT, sequenceName = SEQ_COMPLAINT, allocationSize = 1)
public class Complaint extends AbstractAuditable<Long> {

    public static final String SEQ_COMPLAINT = "SEQ_EGPGR_COMPLAINT";
    private static final long serialVersionUID = 4020616083055647372L;
    @Id
    @GeneratedValue(generator = SEQ_COMPLAINT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "crn", unique = true)
    private String crn = "";

    @ManyToOne
    @JoinColumn(name = "complainttype")
    private ServiceType complaintType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "complainant", nullable = false)
    private Complainant complainant = new Complainant();

    private Long assignee;

    @Column(name = "location")
    private Long location;

    @Column(name = "childlocation")
    private Long childLocation;


    @Column(name = "status")
    private String status;

    private String details;

    @Column(name = "landmarkdetails")
    private String landmarkDetails;

	@ManyToOne
	@JoinColumn(name = "receivingmode")
	private ReceivingMode receivingMode;

    @ManyToOne
    @JoinColumn(name = "receivingcenter")
    private ReceivingCenter receivingCenter;

    @Column(name = "lng")
    private double longitude;

    @Column(name = "lat")
    private double latitude;

    @Column(name = "escalation_date", nullable = false)
    private Date escalationDate;

    private Long department;

    @Column(name = "citizenfeedback")
    @Enumerated(EnumType.ORDINAL)
    private org.egov.pgrrest.common.entity.enums.CitizenFeedback citizenFeedback;

    @Transient
    private String latlngAddress;

    /*
     * For indexing the below fields are kept. These will not be added to the
     * database. This will be available only in index.
     */
    @Transient
    private Long crossHierarchyId;

    @Column(name = "state_id")
    private Long stateId;

    @Column(name = "lastaccessedtime")
    private Date lastAccessedTime;
    
	@Column(name = "tenantid")
	private String tenantId;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return org.egov.pgrrest.common.entity.enums.ComplaintStatus
                .valueOf(getStatus()) == org.egov.pgrrest.common.entity.enums.ComplaintStatus.COMPLETED;
    }

    public String getCrossHierarchyId() {
        return crossHierarchyId == null ? null : crossHierarchyId.toString();
    }

    public ServiceRequest toDomain() {
        final Coordinates coordinates = new Coordinates(latitude, longitude, tenantId);
        final String locationId = getLocationId();
        final org.egov.pgrrest.read.domain.model.ComplaintType complaintType =
                new org.egov.pgrrest.read.domain.model.ComplaintType(this.complaintType.getName(), this.complaintType.getCode(), this.complaintType.getTenantId());
        return ServiceRequest.builder()
                .serviceRequestLocation(new ServiceRequestLocation(coordinates, getCrossHierarchyId(), locationId, tenantId))
                .complaintType(complaintType)
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .requester(complainant.toDomain())
                .address(landmarkDetails)
                .description(details)
                .crn(crn)
                .createdDate(getCreatedDate())
                .lastModifiedDate(getLastModifiedDate())
                .mediaUrls(Collections.emptyList())
                .escalationDate(getEscalationDate())
                .closed(isCompleted())
                .department(getDepartmentId())
                .lastAccessedTime(lastAccessedTime)
                .receivingMode(getReceivingModeCode())
                .receivingCenter(getReceivingCenterId())
                .childLocation(getChildLocationId())
                .assignee(getAssigneeId())
                .tenantId(tenantId)
                .state(getState())
                .complaintStatus(getComplaintStatus())
                .citizenFeedback(getCitizenFeedback())
                .build();
    }

    public String getReceivingModeCode() {
        return receivingMode != null ? receivingMode.getCode() : null;
    }

    public String getLocationId() {
        return location != null ? location.toString() : null;
    }
    
    public String getCitizenFeedback()
    {
    	return citizenFeedback!=null ? citizenFeedback.toString():null;
    }

    private Long getDepartmentId() {
        return department;
    }

    private Long getAssigneeId() {
        return assignee;
    }

    private String getState() {
        return stateId != null ? stateId.toString() : null;
    }

    private String getReceivingCenterId() {
        return receivingCenter != null ? receivingCenter.getId().toString() : null;
    }

    private String getChildLocationId() {
        return childLocation != null ? childLocation.toString(): null;
    }
    
    private String getComplaintStatus(){
    	return status;
    }

}