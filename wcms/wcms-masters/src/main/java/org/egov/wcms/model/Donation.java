package org.egov.wcms.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Donation {

    public static final String SEQ_CATEGORY = "SEQ_EGWTR_DONATION";
    
    @NotNull
    private Long id;

    @NotNull
    @Length(min = 1, max = 100)
    private String propertyType;
    
    @NotNull
    @Length(min = 1, max = 100)
    private String category;
    
    @NotNull
    @Length(min = 1, max = 100)
    private String usageType;
    
    @NotNull
    @Length(min = 1, max = 100)
    private String maxHSCPipeSize;
    
    @NotNull
    @Length(min = 1, max = 100)
    private String minHSCPipeSize;

    @NotNull
    @Length(min = 1, max = 100)
    private String donationAmount;
    
    @NotNull
    @JsonIgnore
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date fromDate;
    
    @NotNull
    @JsonIgnore
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date toDate;
    
    @Length(max = 250)
    @NotNull
    private String tenantId;
    
    @NotNull
    private boolean active;
    
    private AuditDetails auditDetails;
}