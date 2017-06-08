package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * department
 * @author narendra
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {

	private Integer id;
	
	private String tenantId;
	
	private String category;
	
	private String name;
	
	private String code;
	
	private String nameLocal;
	
	private String description;
	
	private Integer createdBy;
	
	private Long createdDate;
	
	private Integer lastModifiedBy;
	
	private Long lastModifiedDate;
	
}
