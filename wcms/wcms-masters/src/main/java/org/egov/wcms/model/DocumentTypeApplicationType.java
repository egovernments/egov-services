package org.egov.wcms.model;

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
public class DocumentTypeApplicationType {
    
    public static final String SEQ_DOCUMENT_TYPE_APPLICATION_TYPE = "SEQ_DOCUMENT_TYPE_APPLICATION_TYPE";

    @NotNull
    private Long id;
    
    @NotNull
    private long documentTypeId;
    
    @NotNull
    @Length(max=150)
    private String applicationType;
    
    @NotNull
    @Length(max=150)
    private String documentType;
    
    @NotNull
    private Boolean mandatory;

    @NotNull
    private Boolean active;

    private AuditDetails auditDetails;
    
    @Length(max = 250)
    @NotNull
    private String tenantId;

}
