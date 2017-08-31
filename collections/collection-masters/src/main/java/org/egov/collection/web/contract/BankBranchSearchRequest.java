package org.egov.collection.web.contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankBranchSearchRequest {

    private String tenantId;

    private String bankId;

    private boolean active;
}
