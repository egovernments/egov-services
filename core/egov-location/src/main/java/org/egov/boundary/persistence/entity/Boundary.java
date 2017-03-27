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

package org.egov.boundary.persistence.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "EG_BOUNDARY")
@NamedQuery(name = "Boundary.findBoundariesByBoundaryType", query = "select b from Boundary b where b.boundaryType.id = :boundaryTypeId")
@SequenceGenerator(name = Boundary.SEQ_BOUNDARY, sequenceName = Boundary.SEQ_BOUNDARY, allocationSize = 1)
public class Boundary extends AbstractAuditable {

	public static final String SEQ_BOUNDARY = "seq_eg_boundary";
	private static final long serialVersionUID = 3054956514161912026L;

	@Id
	@GeneratedValue(generator = SEQ_BOUNDARY, strategy = GenerationType.SEQUENCE)
	private Long id;

	@Length(max = 512)
	@SafeHtml
	@NotBlank
	private String name;

	private Long boundaryNum;
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne
	@JoinColumn(name = "boundaryType", updatable = false)
	private BoundaryType boundaryType;

	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent")
	@Fetch(value = FetchMode.SELECT)
	private Boundary parent;

	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "parent")
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonIgnore
	private Set<Boundary> children = new HashSet<>();

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date fromDate;

	private Date toDate;

	private boolean isHistory;

	private Long bndryId;

	@SafeHtml
	private String localName;

	private Float longitude;

	private Float latitude;

	@Length(max = 32)
	private String materializedPath;

	@Length(max = 256)
	private String tenantId;
}
