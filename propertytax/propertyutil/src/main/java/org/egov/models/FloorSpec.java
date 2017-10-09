package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
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
