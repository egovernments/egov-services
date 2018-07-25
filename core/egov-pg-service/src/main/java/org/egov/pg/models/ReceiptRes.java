package org.egov.pg.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class ReceiptRes {

    private String tenantId;

    @JsonProperty("ResponseInfo")
    private CollectionsResponseInfo responseInfo;

    @JsonProperty("Receipt")
    private List<Receipt> receipts;

    private PaginationContract page;

}
