package org.egov.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.egov.domain.model.Message;
import org.egov.domain.model.Tenant;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO implements Serializable {
    private String code;
    private String locale;
    private String tenant;
    private String message;

    public MessageDTO(Message domainMessage) {
        code = domainMessage.getCode();
        locale = domainMessage.getLocale();
        tenant = domainMessage.getTenant().getTenantId();
        message = domainMessage.getMessage();
    }

    @JsonIgnore
    public Message toDomainMessage() {
        return Message.builder()
            .code(code)
            .locale(locale)
            .tenant(new Tenant(tenant))
            .message(message)
            .build();
    }
}
