package org.egov.pg.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.egov.pg.web.models.RequestInfo;

@AllArgsConstructor
@Getter
@ToString
public class TransactionDumpRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("TransactionDump")
    private TransactionDump transactionDump;
}
