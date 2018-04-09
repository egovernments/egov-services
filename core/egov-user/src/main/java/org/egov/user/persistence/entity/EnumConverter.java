package org.egov.user.persistence.entity;

public class EnumConverter {
    public static <T extends Enum<T>> T toEnumType(Class<T> className, Enum enumValue) {
        return enumValue != null ? T.valueOf(className, enumValue.name()) : null;
    }
}