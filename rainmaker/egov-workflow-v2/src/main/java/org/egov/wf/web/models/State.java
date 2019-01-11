package org.egov.wf.web.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@ToString
@EqualsAndHashCode(of = {"tenantId","businessServiceId","state"})
public class State   {
        @JsonProperty("uuid")
        private String uuid = null;

        @JsonProperty("tenantId")
        private String tenantId = null;

        @JsonProperty("businessServiceId")
        private String businessServiceId = null;

        @JsonProperty("sla")
        private Long sla = null;

        @JsonProperty("state")
        private String state = null;

        @JsonProperty("applicationStatus")
        private String applicationStatus = null;

        @JsonProperty("docUploadRequired")
        private Boolean docUploadRequired = null;

        @JsonProperty("isStartState")
        private Boolean isStartState = null;

        @JsonProperty("isTerminateState")
        private Boolean isTerminateState = null;

        @JsonProperty("actions")
        @Valid
        private List<Action> actions = null;

        @JsonProperty("auditDetails")
        private AuditDetails auditDetails = null;


        public State addActionsItem(Action actionsItem) {
            if (this.actions == null) {
            this.actions = new ArrayList<>();
            }
        this.actions.add(actionsItem);
        return this;
        }

}

