package org.egov.lams.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Position   {
	
  private Long id;
  private String name;
  private DepartmentDesignation deptdesig;
  private Boolean isPostOutsourced;
  private Boolean active;
}

