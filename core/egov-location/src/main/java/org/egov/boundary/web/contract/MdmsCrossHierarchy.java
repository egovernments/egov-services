package org.egov.boundary.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MdmsCrossHierarchy {
	private Long id;
	private Long parent;
	private Long child;
	private Long parenttype;
	private long childtype;
	private String code;
}
