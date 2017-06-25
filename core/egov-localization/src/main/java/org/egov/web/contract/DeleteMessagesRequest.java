package org.egov.web.contract;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.domain.model.MessageIdentity;
import org.egov.domain.model.Tenant;

@Getter
@AllArgsConstructor
public class DeleteMessagesRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @NotNull
    private String tenantId;

    @Valid
    @Size(min = 1)
    private List<DeleteMessage> messages;

    public List<MessageIdentity> getMessageIdentities() {
        return messages.stream()
            .map(message -> MessageIdentity.builder()
                .code(message.getCode())
                .module(message.getModule())
                .locale(message.getLocale())
                .tenant(getTenant())
                .build())
            .collect(Collectors.toList());
    }

    public Tenant getTenant() {
        return new Tenant(tenantId);
    }
}
