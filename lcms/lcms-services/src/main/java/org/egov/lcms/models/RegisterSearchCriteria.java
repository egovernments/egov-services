package org.egov.lcms.models;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterSearchCriteria {
	
	@NotNull
	@NotEmpty
	private String tenantId;
	
	private String register;
	
	private String[] code;
	
	private Boolean isActive;
	
	private Integer pageSize;
	
	private Integer pageNumber;
	
	private String[] sort;
	
	private Integer offSet;
}
