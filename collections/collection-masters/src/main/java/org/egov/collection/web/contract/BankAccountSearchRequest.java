package org.egov.collection.web.contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountSearchRequest {

    private String tenantId;

    private String bankBranchId;

    private String businessDetailsId;

    private boolean active;
}
