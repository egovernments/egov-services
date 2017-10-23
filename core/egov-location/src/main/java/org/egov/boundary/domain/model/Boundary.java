package org.egov.boundary.domain.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.boundary.web.contract.BoundaryType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Boundary {

	private Long id;
	private String name;
	private Long boundaryNum;
	private String code;
	private BoundaryType boundaryType;
	private Boundary parent;
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
	private Long createdBy;
	private Long createdDate;
	private Long lastModifiedBy;
	private Long lastModifiedDate;
	

}
