package org.egov.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
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
public class Location   {
	  @JsonProperty("locality")
	  private Long locality = null;

	  @JsonProperty("zone")
	  private Long zone = null;

	  @JsonProperty("revenueWard")
	  private Long revenueWard = null;

	  @JsonProperty("block")
	  private Long block = null;

	  @JsonProperty("street")
	  private Long street = null;

	  @JsonProperty("electionWard")
	  private String electionWard = null;//changed to string for boundary code to be saved

	  @JsonProperty("doorNo")
	  private String doorNo = null;

	  @JsonProperty("pinCode")
	  private Long pinCode = null;
	  }
