package org.egov.mr.web.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
public class RegistrationUnitSearchCriteria {

	private Long id;

	private String name;

	private Long locality;

	private Long zone;

	// Default value is (true),
	// if null access both Active & InActive records
	private Boolean isActive;

	@NotNull
	@Size(min = 1, max = 256)
	private String tenantId;

}
