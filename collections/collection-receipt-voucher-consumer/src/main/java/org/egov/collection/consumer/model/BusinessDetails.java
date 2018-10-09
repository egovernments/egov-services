package org.egov.collection.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDetails {

    private Long id;

    private String code;

    private String name;

    private Boolean active;

    private Long businessCategory;

    private String businessType;

    private String businessUrl;

    private String department;

    private String fundSource;

    private String functionary;

    private Boolean voucherCreation;

    private Boolean isVoucherApproved;

    private Boolean callBackForApportioning;

    private Long voucherCutoffDate;

    private Integer ordernumber;

    private String fund;

    private String function;

    private String tenantId;

}