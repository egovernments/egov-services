package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EncryptionRequest {

    @JsonProperty("encryptionRequests")
    private List<EncReqObject> encryptionRequests;

}
