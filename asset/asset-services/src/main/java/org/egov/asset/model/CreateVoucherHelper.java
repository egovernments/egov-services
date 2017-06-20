package org.egov.asset.model;

import java.util.ArrayList;
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
public class CreateVoucherHelper    {
	
	  @JsonProperty("voucherName")
	  private String voucherName = null;
	  
	  @JsonProperty("voucherType")
	  private String voucherType = null;

	  @JsonProperty("description")
	  private String description = null;

	  @JsonProperty("voucherDate")
	  private Long voucherDate = null;

	  @JsonProperty("fund")
	  private Long fund = null;

	  @JsonProperty("department")
	  private Long department = null;

	  @JsonProperty("function")
	  private Long function = null;

	  @JsonProperty("module")
	  private Long module = null;

	  @JsonProperty("scheme")
	  private Long scheme = null;

	  @JsonProperty("subScheme")
	  private Long subScheme = null;

	  @JsonProperty("functionary")
	  private String functionary = null;

	  @JsonProperty("fundSource")
	  private String fundSource = null;

	  @JsonProperty("accountCodeDetails")
	  private List<VouchercreateAccountCodeDetails> accountCodeDetails = new ArrayList<VouchercreateAccountCodeDetails>();

	  @JsonProperty("subledgerDetails")
	  private List<VouchercreateSubledgerDetails> subledgerDetails = new ArrayList<VouchercreateSubledgerDetails>();
	  
	  
	 
}

