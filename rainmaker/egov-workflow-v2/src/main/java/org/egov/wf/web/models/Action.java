package org.egov.wf.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.egov.wf.web.models.AuditDetails;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * A Object holds the basic data for a Trade License
 */
@ApiModel(description = "A Object holds the basic data for a Trade License")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-12-04T11:26:25.532+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"tenantId","currentState","action"})
public class Action   {
        @JsonProperty("uuid")
        private String uuid = null;

        @JsonProperty("tenantId")
        private String tenantId = null;

        @JsonProperty("currentState")
        private String currentState = null;

        @JsonProperty("action")
        private String action = null;

        @JsonProperty("nextState")
        private String nextState = null;

        @JsonProperty("roles")
        @Valid
        private List<String> roles = null;

        @JsonProperty("auditDetails")
        private AuditDetails auditDetails = null;


        public Action addRolesItem(String rolesItem) {
            if (this.roles == null) {
            this.roles = new ArrayList<>();
            }
        this.roles.add(rolesItem);
        return this;
        }

}

