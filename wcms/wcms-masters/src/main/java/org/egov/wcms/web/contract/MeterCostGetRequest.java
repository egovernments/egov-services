package org.egov.wcms.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeterCostGetRequest {

	private List<Long> ids;

	private String code;

	private Long pipeSizeId;

	private String name;

	private Boolean active;

	@NotNull
	private String tenantId;

	private String sortBy;

	private String sortOrder;

}
