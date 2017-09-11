package org.egov.collection.web.contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccount {

    private Long id;

    private String accountNumber;

    private String description;

    private Boolean active;

    private String type;

    private ChartOfAccount chartOfAccount;

}
