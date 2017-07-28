package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
/**
 * 
 * @author Prasad
 *
 */
public class FloorSpec {

	@JsonProperty("floorNo")
	private String floorNo;

	@JsonProperty("unitDetails")
	private String unitDetails;

	@JsonProperty("usage")
	private String usage;

	@JsonProperty("construction")
	private String construction;

	@JsonProperty("assessableArea")
	private String assessableArea;

	@JsonProperty("alv")
	private String alv;

	@JsonProperty("rv")
	private String rv;

}
