/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.entity;

import static org.egov.pgr.entity.Complaint.SEQ_COMPLAINT;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.egov.pgr.entity.enums.CitizenFeedback;
import org.hibernate.validator.constraints.Length;

@Entity
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
	@JoinColumn(name = "complaintType", nullable = true)
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
	// @Column(name = "landmarkdetails")
	private String landmarkDetails;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "receivingmode")
	private ReceivingMode receivingMode;

	@ManyToOne
	@JoinColumn(name = "receivingCenter", nullable = true)
	private ReceivingCenter receivingCenter;

	private double lng;
	private double lat;

	@Column(name = "escalation_date", nullable = false)
	private Date escalationDate;

	private Long department;

	@Enumerated(EnumType.ORDINAL)
	private CitizenFeedback citizenFeedback;

	// @Column(name = "childlocation")
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

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public ComplaintType getComplaintType() {
		return complaintType;
	}

	public void setComplaintType(ComplaintType complaintType) {
		this.complaintType = complaintType;
	}

	public Complainant getComplainant() {
		return this.complainant;
	}

	public void setComplainant(Complainant complainant) {
		this.complainant = complainant;
	}

	public Long getAssignee() {
		return this.assignee;
	}

	public void setAssignee(Long assignee) {
		this.assignee = assignee;
	}

	public ComplaintStatus getStatus() {
		return this.status;
	}

	public void setStatus(ComplaintStatus status) {
		this.status = status;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public ReceivingMode getReceivingMode() {
		return this.receivingMode;
	}

	public void setReceivingMode(ReceivingMode receivingMode) {
		this.receivingMode = receivingMode;
	}

	public ReceivingCenter getReceivingCenter() {
		return this.receivingCenter;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setReceivingCenter(ReceivingCenter receivingCenter) {
		this.receivingCenter = receivingCenter;
	}

	public Long getLocation() {
		return this.location;
	}

	public void setLocation(Long location) {
		this.location = location;
	}

	public String getLandmarkDetails() {
		return this.landmarkDetails;
	}

	public void setLandmarkDetails(String landmarkDetails) {
		this.landmarkDetails = landmarkDetails;
	}

	public double getLat() {
		return this.lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return this.lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public Date getEscalationDate() {
		return this.escalationDate;
	}

	public void setEscalationDate(Date escalationDate) {
		this.escalationDate = escalationDate;
	}

	public Long getDepartment() {
		return this.department;
	}

	@Override
	public String toString() {
		return "Complaint [id=" + id + ", crn=" + crn + ", complaintType=" + complaintType + ", complainant="
				+ complainant + ", assignee=" + assignee + ", location=" + location + ", status=" + status
				+ ", details=" + details + ", landmarkDetails=" + landmarkDetails + ", receivingMode=" + receivingMode
				+ ", receivingCenter=" + receivingCenter + ", lng=" + lng + ", lat=" + lat + ", escalationDate="
				+ escalationDate + ", department=" + department + ", citizenFeedback=" + citizenFeedback
				+ ", childLocation=" + childLocation + ", latlngAddress=" + latlngAddress + ", crossHierarchyId="
				+ crossHierarchyId + "]";
	}

	public void setDepartment(Long department) {
		this.department = department;
	}

	public CitizenFeedback getCitizenFeedback() {
		return this.citizenFeedback;
	}

	public void setCitizenFeedback(CitizenFeedback citizenFeedback) {
		this.citizenFeedback = citizenFeedback;
	}

	public Long getChildLocation() {
		return this.childLocation;
	}

	public void setChildLocation(Long childLocation) {
		this.childLocation = childLocation;
	}

	public Long getCrossHierarchyId() {
		return this.crossHierarchyId;
	}

	public void setCrossHierarchyId(Long crossHierarchyId) {
		this.crossHierarchyId = crossHierarchyId;
	}

	public String getLatlngAddress() {
		return this.latlngAddress;
	}

	public void setLatlngAddress(String latlngAddress) {
		this.latlngAddress = latlngAddress;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

}