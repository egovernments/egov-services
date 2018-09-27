package org.egov.tlcalculator.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.egov.tlcalculator.web.models.Accessory;
import org.egov.tlcalculator.web.models.Address;
import org.egov.tlcalculator.web.models.AuditDetails;
import org.egov.tlcalculator.web.models.Document;
import org.egov.tlcalculator.web.models.OwnerInfo;
import org.egov.tlcalculator.web.models.TradeUnit;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * A Object holds the basic data for a Trade License
 */
@ApiModel(description = "A Object holds the basic data for a Trade License")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-27T14:56:03.454+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeLicenseDetail   {
        @JsonProperty("surveyNo")
        
private String surveyNo = null;

        @JsonProperty("subOwnerShipCategory")
        @NotNull
private String subOwnerShipCategory = null;

        @JsonProperty("structureType")
        @Size(min=2,max=64) 
private String structureType = null;

        @JsonProperty("operationalArea")
        
private Double operationalArea = null;

        @JsonProperty("owners")
        @Valid
        private List<OwnerInfo> owners = new ArrayList<>();

              /**
   * License can be created from different channels
   */
  public enum ChannelEnum {
    COUNTER("COUNTER"),
    
    CITIZEN("CITIZEN"),
    
    DATAENTRY("DATAENTRY");

    private String value;

    ChannelEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ChannelEnum fromValue(String text) {
      for (ChannelEnum b : ChannelEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

        @JsonProperty("channel")
        @NotNull@Size(min=2,max=64) 
private ChannelEnum channel = null;

        @JsonProperty("address")
        @Valid
private Address address = null;

        @JsonProperty("tradeUnits")
        @Valid
        private List<TradeUnit> tradeUnits = new ArrayList<>();

        @JsonProperty("accessories")
        @Valid
        private List<Accessory> accessories = null;

        @JsonProperty("applicationDocuments")
        @Valid
        private List<Document> applicationDocuments = null;

        @JsonProperty("verificationDocuments")
        @Valid
        private List<Document> verificationDocuments = null;

        @JsonProperty("additionalDetail")
        
private String additionalDetail = null;

        @JsonProperty("auditDetails")
        @Valid
private AuditDetails auditDetails = null;


}

