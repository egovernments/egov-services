package org.egov.asset.model;

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
public class VouchercreateSubledgerDetails {
	
	  @JsonProperty("glcode")
	  private String glcode = null;

	  @JsonProperty("detailTypeId")
	  private String detailTypeId = null;

	  @JsonProperty("detailKeyId")
	  private String detailKeyId = null;

	  @JsonProperty("debitAmount")
	  private Double debitAmount = null;

	  @JsonProperty("creditAmount")
	  private Double creditAmount = null;

}
