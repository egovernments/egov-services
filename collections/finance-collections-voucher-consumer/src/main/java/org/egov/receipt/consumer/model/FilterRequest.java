package org.egov.receipt.consumer.model;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Component
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FilterRequest {
	private List<Long> id;
    private String code;
    private List<String> codes;
    private String billingservicecode;
    private String taxhead;
    private String glcode;
    private long validFrom;
    private long validTo;
    private String voucherCreationEnabled;
    private String fund;
    private String function;
    private String department;
    private String functionary;
    private String scheme;
    private String subscheme;
}
