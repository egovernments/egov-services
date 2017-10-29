package org.egov.contract;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Position   {
	
  private Long id;
  private String name;
  private DepartmentDesignation deptdesig;
  private Boolean isPostOutsourced;
  private Boolean active;
}

