package org.egov.collection.web.contract;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RemittanceSearchRequest {

    private List<String> referenceNumbers;

    private Date referenceDate;

    private String voucherHeader;

    private String function;

    private String fund;

    private String remarks;

    private String reasonForDelay;

    private String status;

    private String bankaccount;

    @NotNull
    private String tenantId;

    private Integer pageSize;

    private Integer offset;

}
