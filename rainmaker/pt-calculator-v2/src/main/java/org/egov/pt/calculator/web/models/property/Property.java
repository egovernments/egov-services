package org.egov.pt.calculator.web.models.property;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Property
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-05-11T14:12:44.497+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Property   {
        @JsonProperty("id")
        private String id;

        @JsonProperty("tenantId")
        private String tenantId;

        @JsonProperty("acknowldgementNumber")
        private String acknowldgementNumber;

        @JsonProperty("assessmentNumber")
        private String assessmentNumber;

              /**
   * status of the Property
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    INACTIVE("INACTIVE");

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
        private StatusEnum status;

        @JsonProperty("financialYear")
        private String financialYear;

        @JsonProperty("propertyType")
        private String propertyType;

        @JsonProperty("owners")
        @Valid
        private List<OwnerInfo> owners;

        @JsonProperty("address")
        private Address address;

        @JsonProperty("oldAssessmentNumber")
        private String oldAssessmentNumber;

        @JsonProperty("assessmentDate")
        private Long assessmentDate;

              /**
   * New property comes into system either property is newly constructed or existing property got sub divided. Here the reason for creation will be captured.
   */
  public enum CreationReasonEnum {
    NEWPROPERTY("NEWPROPERTY"),
    
    SUBDIVISION("SUBDIVISION");

    private String value;

    CreationReasonEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CreationReasonEnum fromValue(String text) {
      for (CreationReasonEnum b : CreationReasonEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

        @JsonProperty("creationReason")
        private CreationReasonEnum creationReason;

        @JsonProperty("occupancyDate")
        private Long occupancyDate;

        @JsonProperty("propertyDetail")
        private PropertyDetail propertyDetail;


        public Property addOwnersItem(OwnerInfo ownersItem) {
            if (this.owners == null) {
            this.owners = new ArrayList<>();
            }
        this.owners.add(ownersItem);
        return this;
        }

}

