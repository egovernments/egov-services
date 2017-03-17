package org.egov.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Uom {
	
	private Long id;
	private String tenantId;
	private UomCategory category;
	private String uom;
	private String narration;
	private Long convFactor;
	private Boolean baseuom;
	private Integer version;
}
