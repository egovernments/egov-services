package org.egov.commons.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Module {
	@NotNull
	private Long id;

	@NotNull
	@Size(max=100)
	private String name;

	@NotNull
	private Boolean enabled;

	@Size(max=10)
	private String contextRoot;
	
	private Long parentModule;
	
	@Size(max=50)
	private String displayName;
	
	private Long orderNumber;
}
