package org.egov.pgrrest.master.model;

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
public class ReceivingModeType {
	
	public static final String SEQ_RECEIVINGMODETYPE = "SEQ_EGPGER_RECEIVING_MODE_TYPE";
	
    @NotNull
    private Long id;

    @NotNull
    @Length(min = 3, max = 20)
    private String code;

    @NotNull
    @Length(min = 3, max = 100)
    private String name;

    @Length(max = 250)
    private String description;

    @NotNull
    private Boolean active;
    
    @NotNull
    private Boolean visible;

    private AuditDetails auditDetails;

    @Length(max = 250)
    @NotNull
    private String tenantId;


}
