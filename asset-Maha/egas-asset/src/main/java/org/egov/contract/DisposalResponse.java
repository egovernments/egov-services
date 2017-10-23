package org.egov.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.model.Disposal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisposalResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo;

  @JsonProperty("Disposals")
  private List<Disposal> disposals;

}

