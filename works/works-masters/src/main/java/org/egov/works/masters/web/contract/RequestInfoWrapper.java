package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
/**
 * Created by ramki on 14/12/17.
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
