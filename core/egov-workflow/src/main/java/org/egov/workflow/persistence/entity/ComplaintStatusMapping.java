package org.egov.workflow.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "egpgr_complaintstatus_mapping")
@SequenceGenerator(name = ComplaintStatusMapping.SEQ_STATUSMAP,
    sequenceName = ComplaintStatusMapping.SEQ_STATUSMAP,
    allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintStatusMapping extends AbstractPersistable<Long> {

    public static final String SEQ_STATUSMAP = "SEQ_EGPGR_COMPLAINTSTATUS_MAPPING";
    private static final long serialVersionUID = -1671713502661376820L;
    @Id
    @GeneratedValue(generator = SEQ_STATUSMAP, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_status_id")
    private ComplaintStatus currentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_status_id")
    private ComplaintStatus showStatus;

    @Column(name="role_id")
    private Long role;

    @Column(name="orderno")
    private Integer orderNo;

    @Column(name = "tenantid")
    private String tenantId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }
    
}
