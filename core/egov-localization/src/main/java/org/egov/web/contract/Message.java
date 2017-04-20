package org.egov.web.contract;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Message {
    @NotEmpty
    private String code;
    @NotEmpty
    private String message;
    @NotEmpty
    private String tenantId;

    public Message(org.egov.persistence.entity.Message entityMessage) {
        this.code = entityMessage.getCode();
        this.message = entityMessage.getMessage();
        this.tenantId = entityMessage.getTenantId();
    }
}
