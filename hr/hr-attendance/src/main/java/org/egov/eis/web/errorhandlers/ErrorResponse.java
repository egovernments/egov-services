package org.egov.eis.web.errorhandlers;

import org.egov.eis.web.contract.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse {

    private ResponseInfo responseInfo;
    private Error error;
}
