package org.egov.lams.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import org.egov.lams.model.Allottee;
import org.egov.lams.model.ResponseInfo;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class AllotteeResponse {
    @JsonProperty("responseInfo")
    ResponseInfo responseInfo;

    @JsonProperty("user")
    List<Allottee> allottee;
}
