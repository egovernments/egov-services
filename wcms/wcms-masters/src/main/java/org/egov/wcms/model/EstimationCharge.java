package org.egov.wcms.model;

import java.util.List;

import javax.validation.constraints.NotNull;


import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder

public class EstimationCharge {

	@NotNull
	private long id;
	
	@NotNull
	private long connectionId;
	
	@NotNull
	private List<Material> materials; 
	
	@NotNull
	private String existingDistributionPipeline;
	
	@NotNull
	private double pipelineToHomeDistance;
	
	@NotNull
	private double estimationCharges;;
	
	@NotNull
	private double supervisionCharges;
	
	@NotNull
	private double materialCharges;
	
	@NotNull
	private AuditDetails auditDetails;
	
	@NotNull
	private String tenantId;
	
	
	
	
}

