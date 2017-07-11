/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.workflow.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.egov.workflow.web.contract.Attribute;
import org.hibernate.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Builder
@AllArgsConstructor
@Immutable
@Table(name = "cs_wf_state_history")
@SequenceGenerator(name = StateHistory.SEQ_STATEHISTORY, sequenceName = StateHistory.SEQ_STATEHISTORY, allocationSize = 1)
public class StateHistory implements Serializable {
    private static final long serialVersionUID = -2286621991905578107L;
    static final String SEQ_STATEHISTORY = "SEQ_CS_WF_STATE_HISTORY";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_STATEHISTORY)
    private Long id;

    @Column(name = "createdBy")
    private Long createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "lastModifiedBy")
    private Long lastModifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "state_id")
    private State state;

    @NotNull
    private String value;

    @Column(name = "OWNER_POS")
    private Long ownerPosition;

    @Column(name = "OWNER_USER")
    private Long ownerUser;

    private String senderName;
    private String nextAction;
    private String comments;
    private String natureOfTask;
    private String extraInfo;
    private Date dateInfo;
    private Date extraDateInfo;

    @Column(name = "INITIATOR_POS")
    private Long initiatorPosition;
    
    @Column(name = "tenantid")
    private String tenantId;

    @Column(name = "PREVIOUS_OWNER")
    private Long previousOwner;

    StateHistory() {
    }

    public StateHistory(final State state) {
        this.state = state;
        createdBy = state.getCreatedBy();
        createdDate = state.getCreatedDate() != null ? state.getCreatedDate() : null;
        lastModifiedBy = state.getLastModifiedBy();
        lastModifiedDate = state.getLastModifiedDate() != null ? state.getLastModifiedDate() : null;
        value = state.getValue();
        ownerPosition = state.getOwnerPosition();
        ownerUser = state.getOwnerUser();
        senderName = state.getSenderName();
        nextAction = state.getNextAction();
        comments = state.getComments();
        extraInfo = state.getExtraInfo();
        dateInfo = state.getDateInfo();
        extraDateInfo = state.getExtraDateInfo();
        natureOfTask = state.getNatureOfTask();
        initiatorPosition = state.getInitiatorPosition();
        tenantId = state.getTenantId();
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(final String senderName) {
        this.senderName = senderName;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(final String nextAction) {
        this.nextAction = nextAction;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    public String getNatureOfTask() {
        return natureOfTask;
    }

    public void setNatureOfTask(final String natureOfTask) {
        this.natureOfTask = natureOfTask;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(final String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Date getDateInfo() {
        return dateInfo;
    }

    public void setDateInfo(final Date dateInfo) {
        this.dateInfo = dateInfo;
    }

    public Date getExtraDateInfo() {
        return extraDateInfo;
    }

    public void setExtraDateInfo(final Date extraDateInfo) {
        this.extraDateInfo = extraDateInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(final Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Long getOwnerPosition() {
        return ownerPosition;
    }

    public void setOwnerPosition(Long ownerPosition) {
        this.ownerPosition = ownerPosition;
    }

    public Long getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(Long ownerUser) {
        this.ownerUser = ownerUser;
    }

    public Long getInitiatorPosition() {
        return initiatorPosition;
    }

    public void setInitiatorPosition(Long initiatorPosition) {
        this.initiatorPosition = initiatorPosition;
    }
    
    public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

    public Long getPreviousOwner() {
        return previousOwner;
    }

    public void setPreviousOwner(Long previousOwner) {
        this.previousOwner = previousOwner;
    }

    public Task map() {
        Task t = Task.builder().businessKey(this.getState().getType()).
                comments(this.comments == null ? "" : this.comments)
                .createdDate(this.getCreatedDate())
                .id(this.getId().toString())
                .status(this.getValue())
                .lastupdatedSince(this.getLastModifiedDate())
                .description(this.getNatureOfTask())
                .owner(this.getOwnerPosition().toString())
                .details(this.extraInfo == null ? "" : this.extraInfo)
                .sender(this.senderName == null ? "" : this.senderName)
                .action(this.nextAction == null ? "" : this.nextAction)
                .attributes(new HashMap<String,Attribute>()).build();
        return t;
    
    }

}
