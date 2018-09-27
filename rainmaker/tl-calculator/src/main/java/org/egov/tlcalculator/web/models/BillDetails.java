package org.egov.tlcalculator.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.egov.tlcalculator.web.models.BillAccountDetails;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * BillDetails
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-27T14:56:03.454+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDetails   {
        @JsonProperty("id")
        
private String id = null;

        @JsonProperty("tenantId")
        @Size(min=4,max=128) 
private String tenantId = null;

        @JsonProperty("bill")
        
private String bill = null;

        @JsonProperty("businessService")
        
private String businessService = null;

        @JsonProperty("billNumber")
        
private String billNumber = null;

        @JsonProperty("billDate")
        @Valid
private LocalDate billDate = null;

        @JsonProperty("consumerCode")
        
private String consumerCode = null;

        @JsonProperty("consumerType")
        
private String consumerType = null;

        @JsonProperty("billDescription")
        
private String billDescription = null;

        @JsonProperty("displayMessage")
        
private String displayMessage = null;

        @JsonProperty("receiptDate")
        
private Long receiptDate = null;

        @JsonProperty("receiptNumber")
        
private String receiptNumber = null;

        @JsonProperty("minimumAmount")
        
private Double minimumAmount = null;

        @JsonProperty("totalAmount")
        
private Double totalAmount = null;

        @JsonProperty("collectedAmount")
        
private Double collectedAmount = null;

        @JsonProperty("collectionModesNotAllowed")
        @Valid
        private List<String> collectionModesNotAllowed = null;

        @JsonProperty("callBackForApportioning")
        
private Boolean callBackForApportioning = null;

        @JsonProperty("partPaymentAllowed")
        
private Boolean partPaymentAllowed = null;

        @JsonProperty("billAccountDetails")
        @Valid
        private List<BillAccountDetails> billAccountDetails = null;

              /**
   * status if the bill detail
   */
  public enum StatusEnum {
    CREATED("CREATED"),
    
    CANCELLED("CANCELLED"),
    
    INSTRUMENT_BOUNCED("INSTRUMENT_BOUNCED");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

        @JsonProperty("status")
        
private StatusEnum status = null;


}

