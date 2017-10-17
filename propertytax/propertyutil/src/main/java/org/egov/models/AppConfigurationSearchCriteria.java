package org.egov.models;

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
public class AppConfigurationSearchCriteria {
	
	@NotNull
	@NotEmpty
	private String tenantId;
	
	private Long[] ids;
	
	private String keyName;
	
	private String effectiveFrom;
	
	private Integer pageSize;
	
	private Integer offSet;
}