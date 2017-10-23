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
public class Department   {
	  @JsonProperty("id")
	  private Long id;

	  @JsonProperty("name")
	  private String name;

	  @JsonProperty("code")
	  private String code;

	  }