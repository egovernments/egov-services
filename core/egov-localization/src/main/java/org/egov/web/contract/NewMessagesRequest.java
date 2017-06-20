package org.egov.web.contract;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewMessagesRequest {
    @Valid
    @NotNull
    private RequestInfo requestInfo;
    @NotEmpty
    private String locale;
    @Size(min = 1)
    @Valid
    private List<Message> messages;
    
    private String tenantId;
}
