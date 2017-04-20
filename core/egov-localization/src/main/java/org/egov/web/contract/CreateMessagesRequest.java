package org.egov.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CreateMessagesRequest {
    @Valid
    @NotNull
    private RequestInfo requestInfo;
    @NotEmpty
    private String locale;
    @Size(min = 1)
    @Valid
    private List<Message> messages;

    public List<org.egov.persistence.entity.Message> toEntityMessages() {
        return messages.stream()
                .map(this::toEntityMessage)
                .collect(Collectors.toList());
    }

    private org.egov.persistence.entity.Message toEntityMessage(Message contractMessage) {
        return org.egov.persistence.entity.Message.builder()
                .code(contractMessage.getCode())
                .message(contractMessage.getMessage())
                .tenantId(contractMessage.getTenantId())
                .locale(locale)
                .build();
    }
}
