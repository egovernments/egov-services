package org.egov.enc.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * Encryption / Decryption Request Meta-data and Values
 */
@ApiModel(description = "Encryption / Decryption Request Meta-data and Values")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-10-11T17:31:52.360+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CryptObject   {
    @JsonProperty("tenantId")
    private String tenantId = null;

    /**
     * Method to be used for encryption / decryption ( AES / RSA )
     */
    public enum MethodEnum {
        AES("AES"),

        RSA("RSA");

        private String value;

        MethodEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static MethodEnum fromValue(String text) {
            for (MethodEnum b : MethodEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("method")
    private MethodEnum method = null;

    @JsonProperty("value")
    @Valid
    private Object value = null;

}

