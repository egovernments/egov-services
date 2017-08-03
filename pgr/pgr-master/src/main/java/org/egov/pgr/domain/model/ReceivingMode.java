package org.egov.pgr.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceivingMode {
    private String code;

    private String name;

    private String description;

    private Boolean active;

    private List<String> channels;

    private AuditDetails auditDetails;

    private String tenantId;



}
