package org.egov.collection.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountServiceMappingSearchCriteria {

    private String tenantId;

    private String businessDetails;

    private String bankAccount;

}
