package org.egov.lams.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDesignation {

	private Long id;
	private Long departmentId;
	private Designation designation;


}

 
