package org.egov.domain.model;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Null;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Message {
    private String message;
    @Null
    private MessageIdentity messageIdentity;

    public boolean isMoreSpecificComparedTo(Message otherMessage) {
        return messageIdentity.equals(otherMessage.getMessageIdentity());
    }

    public String getCode() {
        return messageIdentity.getCode();
    }

    public String getModule() {
        return messageIdentity.getModule();
    }

    public String getLocale() {
        return messageIdentity.getLocale();
    }

    public String getTenant() {
        return messageIdentity.getTenant().getTenantId();
    }
}

