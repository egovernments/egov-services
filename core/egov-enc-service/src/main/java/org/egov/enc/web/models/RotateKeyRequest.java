package org.egov.enc.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.enc.models.MethodEnum;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RotateKeyRequest {

    @JsonProperty("tenantId")
    private String tenantId;

}
