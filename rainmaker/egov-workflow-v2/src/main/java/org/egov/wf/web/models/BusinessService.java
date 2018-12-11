package org.egov.wf.web.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.egov.wf.web.models.AuditDetails;
import org.egov.wf.web.models.State;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * A Object holds the
 */
@ApiModel(description = "A Object holds the")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-12-04T11:26:25.532+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessService   {
        @JsonProperty("tenantId")
        private String tenantId = null;

        @JsonProperty("uuid")
        private String uuid = null;

        @JsonProperty("businessService")
        private String businessService = null;

        @JsonProperty("getUri")
        private String getUri = null;

        @JsonProperty("postUri")
        private String postUri = null;

        @JsonProperty("states")
        @Valid
        private List<State> states = null;

        @JsonProperty("auditDetails")
        private AuditDetails auditDetails = null;


        public BusinessService addStatesItem(State statesItem) {
            if (this.states == null) {
            this.states = new ArrayList<>();
            }
        this.states.add(statesItem);
        return this;
        }

}

