package org.egov.tl.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.egov.tl.web.models.AuditDetails;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * A Object holds the basic data for a Trade License
 */
@ApiModel(description = "A Object holds the basic data for a Trade License")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-18T17:06:11.263+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class TradeUnit   {

        @JsonProperty("id")
        @Size(max=64)
        private String id;

        @JsonProperty("tenantId")
        @Size(max=64)
        private String tenantId = null;

        @JsonProperty("active")
        private Boolean active;

        @Size(max=64)
        @JsonProperty("tradeType")
        private String tradeType = null;

        @JsonProperty("uom")
        @Size(max=64)
        private String uom = null;

        @JsonProperty("uomValue")
        @Size(max=64)
        private String uomValue = null;

        @JsonProperty("auditDetails")
        private AuditDetails auditDetails = null;


}

