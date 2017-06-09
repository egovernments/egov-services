package org.egov.pgrrest.read.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Getter
@Setter
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

	public ErrorResponse() {
		// TODO Auto-generated constructor stub
	}
}

