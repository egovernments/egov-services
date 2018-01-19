package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendorContractSearch extends VendorContract {
    private String vendorNo;
    private String contractNos;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
    private String services;
}