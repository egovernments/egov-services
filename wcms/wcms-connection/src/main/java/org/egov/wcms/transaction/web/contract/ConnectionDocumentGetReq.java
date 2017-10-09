package org.egov.wcms.transaction.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionDocumentGetReq {
    private List<Long> ids;

    private List<String> consumerNumbers;

    private List<Long> connectionIds;

    private String referenceNumber;

    private String documentType;

    @NotNull
    private String tenantId;

}
