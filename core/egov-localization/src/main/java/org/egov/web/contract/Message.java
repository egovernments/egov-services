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
    private String tenantId;
    private String module;
    public Message(org.egov.domain.model.Message domainMessage) {
        this.code = domainMessage.getCode();
        this.message = domainMessage.getMessage();
        this.tenantId = domainMessage.getTenant().getTenantId();
    }
}