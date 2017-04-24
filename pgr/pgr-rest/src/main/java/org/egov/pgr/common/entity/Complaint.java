package org.egov.pgr.common.entity;

import lombok.*;
import org.egov.pgr.common.model.AuthenticatedUser;
import org.egov.pgr.read.domain.model.ComplaintLocation;
import org.egov.pgr.read.domain.model.Coordinates;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;

import static org.egov.pgr.common.entity.Complaint.SEQ_COMPLAINT;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "egpgr_complaint")
@SequenceGenerator(name = SEQ_COMPLAINT, sequenceName = SEQ_COMPLAINT, allocationSize = 1)
public class Complaint extends AbstractAuditable {

    public static final String SEQ_COMPLAINT = "SEQ_EGPGR_COMPLAINT";
    private static final long serialVersionUID = 4020616083055647372L;
    @Id
    @GeneratedValue(generator = SEQ_COMPLAINT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "crn", unique = true)
    private String crn = "";

    @ManyToOne
    @JoinColumn(name = "complainttype")
    private ComplaintType complaintType;

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
    private org.egov.pgr.common.entity.enums.CitizenFeedback citizenFeedback;

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
        return org.egov.pgr.common.entity.enums.ComplaintStatus
                .valueOf(getStatus()) == org.egov.pgr.common.entity.enums.ComplaintStatus.COMPLETED;
    }

    public String getCrossHierarchyId() {
        return crossHierarchyId == null ? null : crossHierarchyId.toString();
    }

    public org.egov.pgr.read.domain.model.Complaint toDomain() {
        final Coordinates coordinates = new Coordinates(latitude, longitude, tenantId);
        final String locationId = getLocationId();
        final org.egov.pgr.read.domain.model.ComplaintType complaintType =
                new org.egov.pgr.read.domain.model.ComplaintType(this.complaintType.getName(), this.complaintType.getCode(), this.complaintType.getTenantId());
        return org.egov.pgr.read.domain.model.Complaint.builder()
                .complaintLocation(new ComplaintLocation(coordinates, getCrossHierarchyId(), locationId, tenantId))
                .complaintType(complaintType)
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .complainant(complainant.toDomain())
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
                .build();
    }

    public String getReceivingModeCode() {
        return receivingMode != null ? receivingMode.getCode() : null;
    }

    public String getLocationId() {
        return location != null ? location.toString() : null;
    }

    private String getDepartmentId() {
        return department != null ? department.toString(): null;
    }

    private String getAssigneeId() {
        return assignee != null ? assignee.toString() : null;
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
    	return  status!=null ? status:null ;
    }

}