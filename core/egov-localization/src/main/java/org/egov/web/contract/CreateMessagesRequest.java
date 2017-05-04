package org.egov.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.domain.model.Tenant;
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

    public List<org.egov.domain.model.Message> toDomainMessages() {
        return messages.stream()
                .map(this::toDomainMessage)
                .collect(Collectors.toList());
    }

    private org.egov.domain.model.Message toDomainMessage(Message contractMessage) {
        return org.egov.domain.model.Message.builder()
                .code(contractMessage.getCode())
                .message(contractMessage.getMessage())
                .tenant(new Tenant(contractMessage.getTenantId()))
                .locale(locale)
                .build();
    }
}
