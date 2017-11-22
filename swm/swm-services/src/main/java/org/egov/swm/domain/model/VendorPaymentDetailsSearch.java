package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendorPaymentDetailsSearch extends VendorPaymentDetails {
    private String paymentNos;
    private String contractNo;
    private String employeeCode;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}