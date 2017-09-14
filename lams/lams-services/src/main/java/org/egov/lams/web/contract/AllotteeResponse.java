package org.egov.lams.web.contract;

import java.util.List;

import lombok.*;
import org.egov.lams.model.Allottee;

import com.fasterxml.jackson.annotation.JsonProperty;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@ToString
@Setter
@Getter
public class AllotteeResponse {
    @JsonProperty("responseInfo")
    ResponseInfo responseInfo;

    @JsonProperty("user")
    List<Allottee> allottee;
}
