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

package org.egov.pgr.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.domain.model.AuthenticatedUser;
import org.egov.pgr.domain.model.ComplaintLocation;
import org.egov.pgr.domain.model.Coordinates;
import org.egov.pgr.persistence.entity.enums.CitizenFeedback;
import org.egov.pgr.persistence.entity.enums.ReceivingMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

import static org.egov.pgr.persistence.entity.Complaint.SEQ_COMPLAINT;

@Getter
@Setter
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
	@SafeHtml
	private String crn = "";

	@ManyToOne
	@JoinColumn(name = "complaintType", nullable = true)
	private ComplaintType complaintType;

	@ManyToOne(cascade = CascadeType.ALL)
	@Valid
	@NotNull
	@JoinColumn(name = "complainant", nullable = false)
	private Complainant complainant = new Complainant();

	private Long assignee;

	@ManyToOne
	@JoinColumn(name = "location")
	private Boundary location;

	@ManyToOne
	@JoinColumn(name = "childlocation")
	private Boundary childLocation;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "status")
	private ComplaintStatus status = new ComplaintStatus();

	@Length(min = 10, max = 500)
	@SafeHtml
	private String details;

	@Length(max = 200)
	@SafeHtml
	private String landmarkDetails;

	@Enumerated(EnumType.ORDINAL)
	@NotNull
	private ReceivingMode receivingMode = ReceivingMode.WEBSITE;

	@ManyToOne
	@JoinColumn(name = "receivingCenter", nullable = true)
	private ReceivingCenter receivingCenter;

	// private Set<Long> supportDocs;

	@Column(name = "lng")
	private double longitude;

	@Column(name = "lat")
	private double latitude;

	@Column(name = "escalation_date", nullable = false)
	private Date escalationDate;

	private Long department;

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
		final String locationId = Objects.isNull(location) ? StringUtils.EMPTY : String.valueOf(location.getId());
		final org.egov.pgr.domain.model.ComplaintType complaintType = new org.egov.pgr.domain.model.ComplaintType(
				this.complaintType.getName(), complainant.getId().toString());
		return org.egov.pgr.domain.model.Complaint.builder()
				.complaintLocation(new ComplaintLocation(coordinates, getCrossHierarchyId(), locationId))
				.additionalValues(getAdditionalValues()).complaintType(complaintType)
				.authenticatedUser(AuthenticatedUser.createAnonymousUser()).complainant(complainant.toDomain())
				.address(landmarkDetails).description(details).crn(crn).createdDate(getCreatedDate())
				.lastModifiedDate(getLastModifiedDate()).mediaUrls(Collections.emptyList())
				.escalationDate(getEscalationDate()).closed(isCompleted()).build();
	}

	private Map<String, String> getAdditionalValues() {
		HashMap<String, String> map = new HashMap<>();
		map.put("ReceivingMode", getReceivingMode().toString());
		map.put("ComplaintStatus", getStatus().getName());
		if (getReceivingCenter() != null) {
			map.put("ReceivingCenter", getReceivingCenter().getName());
		}
		if (getLocation() != null) {
			map.put("LocationName", getLocation().getName());
			map.put("LocationId", getLocation().getId().toString());
		}
		if (getChildLocation() != null) {
			map.put("ChildLocationId", getChildLocation().getId().toString());
			map.put("ChildLocationName", getChildLocation().getName());
		}
		if (getStateId() != null) {
			map.put("stateId", getStateId().toString());
		}
		return map;
	}

}