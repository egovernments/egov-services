package org.egov.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.model.Revaluation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevaluationResponse   {
	
  @JsonProperty("ResposneInfo")
  private ResponseInfo resposneInfo;

  @JsonProperty("Revaluations")
  private List<Revaluation> revaluations;

}

