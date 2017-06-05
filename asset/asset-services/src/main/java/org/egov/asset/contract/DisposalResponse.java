package org.egov.asset.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.model.Disposal;
import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * DisposalResponse
 */

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DisposalResponse   {
  
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("Disposals")
  private List<Disposal> disposals = new ArrayList<Disposal>();

}

