package org.egov.pt.web.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is lightweight property object that can be used as reference by definitions needing property linking. Actual Property Object extends this to include more elaborate attributes of the property.
 */
@ApiModel(description = "This is lightweight property object that can be used as reference by definitions needing property linking. Actual Property Object extends this to include more elaborate attributes of the property.")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-05-11T14:12:44.497+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyInfo   {
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

        @JsonProperty("propertySubType")
        private String propertySubType;

        @JsonProperty("usageCategoryMajor")
        private String usageCategoryMajor;

        @JsonProperty("owners")
        @Valid
        private List<OwnerInfo> owners;

        @JsonProperty("address")
        private Address address;


        public PropertyInfo addOwnersItem(OwnerInfo ownersItem) {
            if (this.owners == null) {
            this.owners = new ArrayList<>();
            }
        this.owners.add(ownersItem);
        return this;
        }

}

