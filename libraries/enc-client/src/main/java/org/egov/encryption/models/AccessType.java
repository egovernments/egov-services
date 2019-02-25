package org.egov.encryption.models;

import lombok.Getter;

@Getter
public enum AccessType {

    PLAIN(1),

    MASK(2),

    NONE(3);

    AccessType(int i) {
    }

}
