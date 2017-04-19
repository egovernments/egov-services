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

    public Message(org.egov.domain.model.Message modelMessage) {
        this.code = modelMessage.getCode();
        this.message = modelMessage.getMessage();
    }
}

