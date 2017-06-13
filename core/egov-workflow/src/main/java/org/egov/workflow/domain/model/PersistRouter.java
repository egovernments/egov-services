package org.egov.workflow.domain.model;

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
public class PersistRouter {

	private Long id;
	
	private Long service;
	
	private Integer boundary;
	
	@NotNull
	private Integer position;
	
	@NotNull
	private String tenantId;
	
	private AuditDetails auditDetails;
	
}

