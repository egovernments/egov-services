package org.egov.eis.web.contract;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("userName")
    private String userName = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("roles")
    private Set<Role> roles = null;

    @JsonProperty("tenantId")
    private String tenantId;

}
