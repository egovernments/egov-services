package org.egov.demand.model;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DemandDetails   {
	
  @JsonProperty("id")
  private Long id ;

  @JsonProperty("taxAmount")
  private Double taxAmount ;

  @JsonProperty("collectionAmount")
  private Double collectionAmount ;

  @JsonProperty("rebateAmount")
  private Double rebateAmount ;

  @JsonProperty("taxReason")
  private String taxReason ;

  @JsonProperty("taxPeriod")
  private String taxPeriod ;

  @JsonProperty("glCode")
  private String glCode ;

  @JsonProperty("isActualDemand")
  private BigDecimal isActualDemand ;
}

