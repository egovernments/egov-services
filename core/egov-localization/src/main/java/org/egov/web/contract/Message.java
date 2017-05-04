package org.egov.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

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

    public Message(org.egov.domain.model.Message domainMessage) {
        this.code = domainMessage.getCode();
        this.message = domainMessage.getMessage();
        this.tenantId = domainMessage.getTenant().getTenantId();
    }
}
