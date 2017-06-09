package org.egov.lams.notification.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role {
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("code")
    private String code;
    
    @JsonProperty("tenantId")
    private String tenantId;
}
