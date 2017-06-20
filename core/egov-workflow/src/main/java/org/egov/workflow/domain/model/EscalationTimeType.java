package org.egov.workflow.domain.model;


import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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
public class EscalationTimeType {
	
    public static final String SEQ_CATEGORY = "SEQ_EGPGR_ESCILATIONTIMETYPE";
   
    private Long id; 
    
    @Length(max = 250)
    @NotNull
    private String tenantId;

    @NotNull
    private ServiceType grievanceType;
    
    @NotNull
    private long noOfHours;
    
    @NotNull
    private long designation;
    
    @NotNull
    private boolean active;
    
    @NotNull
    private AuditDetails auditDetails;
}
