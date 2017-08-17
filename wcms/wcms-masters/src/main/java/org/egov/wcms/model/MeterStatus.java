package org.egov.wcms.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeterStatus {
    public static final String SEQ_METER_STATUS="SEQ_EGWTR_METERSTATUS";
    @NotNull
    private Long id;
    
    @NotNull
    @Size(max=20)
    private String code;
    
    @NotNull
    private String meterStatus;
    
    @Size(max=124)
    private String description;
    
    @NotNull
    @Size(max=250)
    private String tenantId;
    
    @NotNull
    private Long createdBy;

    private Long createdDate;

    @NotNull
    private Long lastModifiedBy;

	private Long lastModifiedDate;
}
