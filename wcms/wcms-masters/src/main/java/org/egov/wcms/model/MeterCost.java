package org.egov.wcms.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class MeterCost {
    
    public static final String SEQ_METERCOST = "SEQ_EGWTR_METER_COST";

    @NotNull
    private Long id;

    @NotNull
    @Length(min = 3, max = 20)
    private int pipeSize;

    @NotNull
    @Length(min=3, max=100)
    private String meterMake;

    @NotNull
    @Length(min=3, max=100)
    private double amount;

    @NotNull
    private Boolean active;

    @JsonIgnore
    private Long createdBy;

    @JsonIgnore
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdDate;

    @JsonIgnore
    private Long lastModifiedBy;

    @JsonIgnore
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date lastModifiedDate;

    @Length(max = 250)
    @NotNull
    private String tenantId;

}
