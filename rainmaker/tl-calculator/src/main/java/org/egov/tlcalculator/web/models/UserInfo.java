package org.egov.tlcalculator.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.egov.tlcalculator.web.models.Role;
import org.egov.tlcalculator.web.models.TenantRole;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * This is acting ID token of the authenticated user on the server. Any value provided by the clients will be ignored and actual user based on authtoken will be used on the server.
 */
@ApiModel(description = "This is acting ID token of the authenticated user on the server. Any value provided by the clients will be ignored and actual user based on authtoken will be used on the server.")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-27T14:56:03.454+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo   {
        @JsonProperty("tenantId")
        @NotNull
private String tenantId = null;

        @JsonProperty("uuid")
        
private String uuid = null;

        @JsonProperty("userName")
        @NotNull
private String userName = null;

        @JsonProperty("password")
        
private String password = null;

        @JsonProperty("idToken")
        
private String idToken = null;

        @JsonProperty("mobile")
        
private String mobile = null;

        @JsonProperty("email")
        
private String email = null;

        @JsonProperty("primaryrole")
        @Valid
        private List<Role> primaryrole = new ArrayList<>();

        @JsonProperty("additionalroles")
        @Valid
        private List<TenantRole> additionalroles = null;


}

