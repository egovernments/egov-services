package org.egov.demand.model;

import java.util.Date;
import org.egov.demand.model.enums.InstallmentType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Installment   {
	
  @JsonProperty("fromDate")
  private Date fromDate = null;

  @JsonProperty("toDate")
  private Date toDate = null;

  @JsonProperty("installmentYear")
  private Date installmentYear = null;

  @JsonProperty("module")
  private String module = null;

  @JsonProperty("installmentNumber")
  private String installmentNumber = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("installmentType")
  private InstallmentType installmentType = null;

  @JsonProperty("financialYear")
  private String financialYear = null;
  }

