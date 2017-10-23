package org.egov.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Prasad
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Demolition {
	
	@JsonProperty("id")
	private Long id = null;
	
	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId =null;
	
	@JsonProperty("upicNumber")
	@NotNull
	@Size(min = 6, max = 128)
	private String upicNumber = null;
	
	@JsonProperty("applicationNo")
	@Size(min=1,max =128)
	private String applicationNo=null;
	
	@JsonProperty("propertySubType")
	private String propertySubType =null;
	
	@JsonProperty("usageType")
	private String usageType =null;
	
	@JsonProperty("usageSubType")
	private String usageSubType =null;
	
	@JsonProperty("totalArea")
	private Double totalArea =null;
	
	@JsonProperty("sequenceNo")
	private Integer sequenceNo =null;
	
	@JsonProperty("isLegal")
	private Boolean isLegal =null;
	
	@JsonProperty("demolitionReason")
	private String demolitionReason =null;
	
	@JsonProperty("documents")
	private List<Document> documents=null;
	
	@JsonProperty("comments")
	private String comments =null;
	
	@JsonProperty("stateId")
	private String stateId = null;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails=null;
	
	@JsonProperty("workFlowDetails")
	private WorkFlowDetails workFlowDetails;

}
