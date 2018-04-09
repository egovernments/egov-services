package org.egov.user.domain.model.enums;

import lombok.Getter;

@Getter
public enum BloodGroup {
    A_POSITIVE("A+"),
    B_POSITIVE("B+"),
    O_POSITIVE("O+"),
    AB_POSITIVE("AB+"),
    A_NEGATIVE("A-"),
    B_NEGATIVE("B-"),
    AB_NEGATIVE("AB-"),
    O_NEGATIVE("O-");

    private String value;

    BloodGroup(String value) {
        this.value = value;
    }
}
