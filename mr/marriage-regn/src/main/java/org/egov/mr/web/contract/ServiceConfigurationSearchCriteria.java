package org.egov.mr.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class ServiceConfigurationSearchCriteria {

	@NotNull
	@Size(min = 1, max = 256)
	private String tenantId;

	private String name;

	private List<Integer> ids;

	private Long effectiveFrom;
}
