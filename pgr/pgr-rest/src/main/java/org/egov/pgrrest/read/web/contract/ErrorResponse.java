package org.egov.pgrrest.read.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.egov.common.contract.response.ResponseInfo;


import java.util.List;

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
        return error.getFields();
    }
}

