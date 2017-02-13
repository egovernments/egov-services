package org.egov.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@AllArgsConstructor
public class Message {
    @NotEmpty
    private String code;
    @NotEmpty
    private String message;

    public Message(org.egov.persistence.entity.Message entityMessage) {
        this.code = entityMessage.getCode();
        this.message = entityMessage.getMessage();
    }
}

