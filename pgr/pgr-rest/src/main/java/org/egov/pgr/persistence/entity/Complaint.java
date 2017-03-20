package org.egov.pgr.persistence.entity;

import lombok.*;
import org.egov.pgr.domain.model.AuthenticatedUser;
import org.egov.pgr.domain.model.ComplaintLocation;
import org.egov.pgr.domain.model.Coordinates;
import org.egov.pgr.persistence.entity.enums.CitizenFeedback;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Date;

import static org.egov.pgr.persistence.entity.Complaint.SEQ_COMPLAINT;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "egpgr_complaint")
@SequenceGenerator(name = SEQ_COMPLAINT, sequenceName = SEQ_COMPLAINT, allocationSize = 1)
public class Complaint extends AbstractAuditable {

    public static final String SEQ_COMPLAINT = "SEQ_EGPGR_COMPLAINT";
    private static final long serialVersionUID = 4020616083055647372L;
    @Id
    @GeneratedValue(generator = SEQ_COMPLAINT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "crn", unique = true)
    @Length(max = 32)
    @SafeHtml
    private String crn = "";

    @ManyToOne
    @JoinColumn(name = "complainttype", nullable = true)
    private ComplaintType complaintType;

    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name = "complainant", nullable = false)
    private Complainant complainant = new Complainant();

    private Long assignee;

    @Column(name = "location")
    private Long location;

    @Column(name = "childlocation")
    private Long childLocation;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "status")
    private ComplaintStatus status = new ComplaintStatus();

    @Length(min = 10, max = 500)
    @SafeHtml
    private String details;

    @Column(name = "landmarkdetails")
    @Length(max = 200)
    @SafeHtml
    private String landmarkDetails;

	@ManyToOne
	@JoinColumn(name = "receivingmode")
	private ReceivingMode receivingMode;

    @ManyToOne
    @JoinColumn(name = "receivingcenter", nullable = true)
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
    private CitizenFeedback citizenFeedback;

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

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return org.egov.pgr.persistence.entity.enums.ComplaintStatus
                .valueOf(getStatus().getName()) == org.egov.pgr.persistence.entity.enums.ComplaintStatus.COMPLETED;
    }

    public String getCrossHierarchyId() {
        return crossHierarchyId == null ? null : crossHierarchyId.toString();
    }

    public org.egov.pgr.domain.model.Complaint toDomain() {
        final Coordinates coordinates = new Coordinates(latitude, longitude);
        final String locationId = getLocationId();
        final org.egov.pgr.domain.model.ComplaintType complaintType =
                new org.egov.pgr.domain.model.ComplaintType(this.complaintType.getName(), this.complaintType.getCode());
        return org.egov.pgr.domain.model.Complaint.builder()
                .complaintLocation(new ComplaintLocation(coordinates, getCrossHierarchyId(), locationId))
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
                .state(getState())
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

}