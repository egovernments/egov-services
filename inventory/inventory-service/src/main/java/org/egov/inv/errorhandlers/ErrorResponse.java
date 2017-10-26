package org.egov.inv.errorhandlers;

import java.util.List;

import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
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
public class ErrorResponse {

    private ResponseInfo responseInfo;
    private Error error;

    @JsonIgnore
    public List<ErrorField> getErrorFields() {
        return error.getErrorFields();
    }

}
