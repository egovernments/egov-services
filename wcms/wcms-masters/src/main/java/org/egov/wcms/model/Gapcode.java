package org.egov.wcms.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class Gapcode {

    public static final String SEQ_GAPCODE = "seq_egwtr_gapcode";

    @NotNull
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    private String code;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    private Boolean outSideUlb;

    @NotNull
    private String noOfMonths;

    @NotNull
    private String logic;

    @NotNull
    private Boolean active;

    @Size(max = 250)
    private String description;

    @NotNull
    private Long createdBy;

    private Long createdDate;

    @NotNull
    private Long lastUpdatedBy;

    private Long lastUpdatedDate;

    @Size(min = 4, max = 128)
    @NotNull
    private String tenantId;

}
