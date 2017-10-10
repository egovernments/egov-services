package org.egov.propertyIndexer.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.models.AuditDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Prasad
 *This Model will be used for ElasticSearch indexing
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FloorES {
	
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("floorNo")
	@NotNull
	@Size(min = 1, max = 128)
	private String floorNo = null;

	@JsonProperty("units")
	@Valid
	private List<UnitES> units = new ArrayList<UnitES>();

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
