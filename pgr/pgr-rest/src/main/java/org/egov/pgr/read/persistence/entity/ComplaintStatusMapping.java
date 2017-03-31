package org.egov.pgr.read.persistence.entity;

import org.egov.pgr.common.entity.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "egpgr_complaintstatus_mapping")
@SequenceGenerator(name = ComplaintStatusMapping.SEQ_STATUSMAP, sequenceName = ComplaintStatusMapping.SEQ_STATUSMAP, allocationSize = 1)
public class ComplaintStatusMapping extends AbstractPersistable<Long> {

    public static final String SEQ_STATUSMAP = "SEQ_EGPGR_COMPLAINTSTATUS_MAPPING";
    private static final long serialVersionUID = -1671713502661376820L;
    @Id
    @GeneratedValue(generator = SEQ_STATUSMAP, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_status_id")
    private ComplaintStatus currentStatus;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_status_id")
    private ComplaintStatus showStatus;

    @NotNull
    @Valid
    @Column(name="role_id")
    private Long role;

    @NotNull
    @Column(name="orderno")
    private Integer orderNo;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    public ComplaintStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(final ComplaintStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public ComplaintStatus getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(final ComplaintStatus showStatus) {
        this.showStatus = showStatus;
    }

	public Long getRole() {
		return role;
	}

	public void setRole(Long role) {
		this.role = role;
	}

	public Integer getOrderNo() {
        return orderNo;
    }


    public void setOrderNo(final Integer orderNo) {
        this.orderNo = orderNo;
    }

}
