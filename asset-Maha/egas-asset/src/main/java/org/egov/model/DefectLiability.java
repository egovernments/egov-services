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
public class DefectLiability   {
	  @JsonProperty("year")
	  private Long year;

	  @JsonProperty("month")
	  private Long month;

	  @JsonProperty("day")
	  private Long day;

	  }