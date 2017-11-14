package org.egov.pgr.web.contract;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class ServiceGroup {
	
	@NotNull
	private long id;
	
	@NotNull
	private String code;

	@NotNull
	private String name;

	@NotNull
	private String description;

	@NotNull
	private String tenantId;

	private String localName;

}
