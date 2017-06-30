package org.egov.commons.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class BusinessDetailsGetRequest {
	private Boolean active;

	@NotNull
	private String tenantId;

	private List<Long> ids;

	private String businessDetailsCode;

	private String businessCategoryCode;

	private String sortBy;

	private String sortOrder;

}
