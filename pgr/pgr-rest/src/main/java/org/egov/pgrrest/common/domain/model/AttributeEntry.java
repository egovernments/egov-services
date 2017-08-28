package org.egov.pgrrest.common.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.egov.pgrrest.read.domain.exception.InvalidAttributeEntryException;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class AttributeEntry {
    private static final int MAX_LENGTH = 500;
    private static final String KEY_FIELD_NAME = "key";
    private static final String CODE_FIELD_NAME = "code";
    private String key;
    private String code;

    public void validate() {
        validateKey();
        validateCode();
    }

    public boolean isKeyInvalid() {
        return isEmpty(key) || key.length() > MAX_LENGTH;
    }

    public boolean isCodeInvalid() {
        return isEmpty(code) || code.length() > MAX_LENGTH;
    }

    private void validateKey() {
        if (isKeyInvalid()) {
            throw new InvalidAttributeEntryException(KEY_FIELD_NAME);
        }
    }

    private void validateCode() {
        if (isCodeInvalid()) {
            throw new InvalidAttributeEntryException(getKey());
        }
    }

}
