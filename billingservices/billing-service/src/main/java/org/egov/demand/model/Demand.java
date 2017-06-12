package org.egov.demand.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demand   {
  @JsonProperty("id")
  private Long id ;

  @JsonProperty("installment")
  private String installment ;

  @JsonProperty("moduleName")
  private String moduleName ;

  @JsonProperty("demandDetails")
  private List<DemandDetails> demandDetails = new ArrayList<>();

  @JsonProperty("paymentInfo")
  private List<CollectedReceipts> paymentInfo = new ArrayList<>();
}

