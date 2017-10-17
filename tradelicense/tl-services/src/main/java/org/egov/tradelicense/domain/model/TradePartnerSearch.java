package org.egov.tradelicense.domain.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class TradePartnerSearch {

	@NotNull
	private String tenantId;

	private Long id;

	private Long licenseId;

	private Integer pageSize;

	private Integer pageNumber;

	private String sort;
}