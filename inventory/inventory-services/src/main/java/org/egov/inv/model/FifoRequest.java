package org.egov.inv.model;



import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FifoRequest {
	
	  @JsonProperty("RequestInfo")
	  private RequestInfo requestInfo = null;
	  
	  @JsonProperty("Fifo")
	  private Fifo fifo = new Fifo();
	
}
