package org.egov.collection.web.contract;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BankAccountServiceMappingSearchReq {

    private List<String> businessDetails;

    private String bankAccount;

    private String tenantId;

}
