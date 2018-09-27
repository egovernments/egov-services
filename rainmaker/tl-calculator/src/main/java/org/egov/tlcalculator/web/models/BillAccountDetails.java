package org.egov.tlcalculator.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * BillAccountDetails
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-27T14:56:03.454+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillAccountDetails   {
        @JsonProperty("id")
        
private String id = null;

        @JsonProperty("tenantId")
        @Size(min=4,max=128) 
private String tenantId = null;

        @JsonProperty("billDetail")
        
private String billDetail = null;

        @JsonProperty("glcode")
        
private String glcode = null;

        @JsonProperty("order")
        
private Integer order = null;

        @JsonProperty("accountDescription")
        
private String accountDescription = null;

        @JsonProperty("crAmountToBePaid")
        
private Double crAmountToBePaid = null;

        @JsonProperty("creditAmount")
        
private Double creditAmount = null;

        @JsonProperty("debitAmount")
        
private Double debitAmount = null;

        @JsonProperty("isActualDemand")
        
private Boolean isActualDemand = null;

              /**
   * Purpose of Account head.
   */
  public enum PurposeEnum {
    ARREAR_AMOUNT("ARREAR_AMOUNT"),
    
    CURRENT_AMOUNT("CURRENT_AMOUNT"),
    
    ADVANCE_AMOUNT("ADVANCE_AMOUNT"),
    
    ARREAR_LATEPAYMENT_CHARGES("ARREAR_LATEPAYMENT_CHARGES"),
    
    CURRENT_LATEPAYMENT_CHARGES("CURRENT_LATEPAYMENT_CHARGES"),
    
    CHEQUE_BOUNCE_PENALTY("CHEQUE_BOUNCE_PENALTY"),
    
    REBATE("REBATE"),
    
    OTHERS("OTHERS");

    private String value;

    PurposeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PurposeEnum fromValue(String text) {
      for (PurposeEnum b : PurposeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

        @JsonProperty("purpose")
        
private PurposeEnum purpose = null;


}

