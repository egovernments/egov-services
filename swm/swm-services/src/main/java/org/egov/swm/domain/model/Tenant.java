package org.egov.swm.domain.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tenant {

    public enum TypeEnum {
        CITY("CITY");

        private String value;

        TypeEnum(final String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static TypeEnum fromValue(final String text) {
            for (final TypeEnum b : TypeEnum.values())
                if (String.valueOf(b.value).equals(text))
                    return b;
            return null;
        }
    }

    @NotNull
    @JsonProperty("code")
    private String code = null;

    @NotNull
    @Length(max = 256, message = "Value of name shall less than 257")
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("description")
    private String description = null;

    @NotNull
    @JsonProperty("imageId")
    private String imageId = null;

    @NotNull
    @JsonProperty("logoId")
    private String logoId = null;

    @JsonProperty("domainUrl")
    private String domainUrl = null;

    @NotNull
    @JsonProperty("type")
    private TypeEnum type = null;

    @Length(max = 300, message = "Value of address shall less than 301")
    @JsonProperty("address")
    private String address = null;

    @Length(max = 16, message = "Please Enter 16 digit contact number")
    @JsonProperty("contactNumber")
    private String contactNumber = null;

    @Length(max = 16, message = "Please Enter 16 digit help line number")
    @JsonProperty("helpLineNumber")
    private String helpLineNumber = null;

    @Length(max = 100, message = "Value of twitterUrl shall less than 101")
    @JsonProperty("twitterUrl")
    private String twitterUrl = null;

    @Length(max = 100, message = "Value of facebookUrl shall less than 101")
    @JsonProperty("facebookUrl")
    private String facebookUrl = null;

    @Length(max = 100, message = "Value of emailId shall less than 101")
    @JsonProperty("emailId")
    private String emailId = null;

    @JsonProperty("city")
    private City city = null;

}
