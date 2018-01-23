package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetailsSearch extends PaymentDetails {
    private String paymentNo;
    private String vendorNo;
    private String codes;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
}