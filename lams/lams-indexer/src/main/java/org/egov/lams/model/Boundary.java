package org.egov.lams.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Boundary {

	private Long id;
	private String name;
	private Long boundaryNum;
	//private BoundaryType boundaryType;
	private Boundary parent;
	private Set<Boundary> children = new HashSet<>();
	private Date fromDate;
	private Date toDate;
	private boolean isHistory;
	private Long bndryId;
	private String localName;
	private Float longitude;
	private Float latitude;
	private String materializedPath;
	private String tenantid;
}
