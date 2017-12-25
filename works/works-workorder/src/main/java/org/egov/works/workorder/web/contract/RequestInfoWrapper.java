package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Created by ramki on 22/12/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class RequestInfoWrapper {

    @JsonProperty(value="RequestInfo")
    private RequestInfo requestInfo;

}
