package org.egov.inv.errorhandlers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.egov.common.contract.response.ErrorField;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Error {

    @NotNull
    private Integer code;

    @NotNull
    private String message;

    private String description;

    private List<ErrorField> errorFields;

    /**
     * FIXME : If we take List of Object, it will generate twice the actual result. On first line, the key & on next line the
     * value. PROPOSITION : Can take Map instead where Key is fieldName, Value is Error description
     */
    private Map<String, Object> fields = new LinkedHashMap<>();
}
