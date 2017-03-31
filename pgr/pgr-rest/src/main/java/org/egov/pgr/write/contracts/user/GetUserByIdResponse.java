package org.egov.pgr.write.contracts.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.egov.pgr.write.contracts.grievance.ResponseInfo;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GetUserByIdResponse {
    @JsonProperty("responseInfo")
    ResponseInfo responseInfo;

    @JsonProperty("user")
    List<User> user;

}
