package org.egov.asset.contract;

import javax.validation.Valid;

import org.egov.asset.model.Disposal;
import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * DisposalRequest
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisposalRequest   {
	
  @JsonProperty("RequestInfo")
  @Valid
  private RequestInfo requestInfo;

  @JsonProperty("Disposal")
  @Valid
  private Disposal disposal;

}

