package org.egov.collection.web.contract;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Remittance {

    @NotNull
    private String tenantId;

    private String id;

    @NotNull
    private String referenceNumber;

    @NotNull
    private Date referenceDate;

    private String voucherHeader;

    private String function;

    private String fund;

    private String remarks;

    private String reasonForDelay;

    @NotNull
    private String status;

    private String bankaccount;

    private Long createdBy;

    private Long lastModifiedBy;

    private Long createdDate;

    private Long lastModifiedDate;

    private Set<RemittanceDetail> remittanceDetails = new HashSet<>();

    private Set<RemittanceInstrument> remittanceInstruments = new HashSet<>();

}