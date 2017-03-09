package org.egov.pgr.contracts.grievance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ResponseInfo {
    @JsonProperty("api_id")
    private String apiId = null;

    @JsonProperty("ver")
    private String ver = null;

    @JsonProperty("ts")
    private String ts = null;

    @JsonProperty("res_msg_id")
    private String resMsgId = null;

    @JsonProperty("msg_id")
    private String msgId = null;

    @JsonProperty("status")
    private String status = null;

    public ResponseInfo apiId(final String apiId) {
        this.apiId = apiId;
        return this;
    }

}
