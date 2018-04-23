package org.egov.common.contract.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorRes   {
	
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo;

  @JsonProperty("Errors")
  private List<Error> errors;

  
}

