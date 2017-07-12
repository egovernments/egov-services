package org.egov.pgrrest.read.domain.model;

import lombok.Getter;

public enum SevaRequestAction {
    CREATE("CREATE"),
    UPDATE("UPDATE");

    @Getter
    private String actionName;

    SevaRequestAction(String actionName) {
        this.actionName = actionName;
    }
}
