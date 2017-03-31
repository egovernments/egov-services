package org.egov.pgr.write.entity;

import lombok.Getter;
import lombok.Setter;
import org.egov.pgr.write.entity.enums.CitizenFeedback;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static org.egov.pgr.write.entity.Complaint.SEQ_COMPLAINT;

@Entity(name = "complaint_write")
@Getter
@Setter
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
    private String crn = "";

    @ManyToOne
    @JoinColumn(name = "complainttype", nullable = true)
    private ComplaintType complaintType;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(name = "complainant", nullable = false)
    private Complainant complainant = new Complainant();

    private Long assignee;

    private Long location;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "status")
    private ComplaintStatus status = new ComplaintStatus();

    @Length(min = 10, max = 500)
    private String details;

    @Column(name = "state_id")
    private Long stateId;

    @Length(max = 200)
    @Column(name = "landmarkdetails")
    private String landmarkDetails;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "receivingmode")
    private ReceivingMode receivingMode;

    @ManyToOne
    @JoinColumn(name = "receivingcenter", nullable = true)
    private ReceivingCenter receivingCenter;

    private double lng;
    private double lat;

    @Column(name = "escalation_date", nullable = false)
    private Date escalationDate;

    private Long department;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "citizenfeedback")
    private CitizenFeedback citizenFeedback;

    @Column(name = "childlocation")
    private Long childLocation;

    @Transient
    private String latlngAddress;

    @Transient
    private String locationName;

    @Transient
    private Long crossHierarchyId;

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

}