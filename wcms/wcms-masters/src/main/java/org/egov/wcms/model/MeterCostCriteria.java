package org.egov.wcms.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class MeterCostCriteria {
	private List<Long> ids;

	private String code;

	private Long pipeSizeId;

	private Boolean active;

	private String name;

	private String tenantId;

	private String sortby;

	private String sortOrder;

}
