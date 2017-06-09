package org.egov.pgrrest.read.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class Error {
    public Error() {
		// TODO Auto-generated constructor stub
	}
	private int code;
    private String message;
    private String description;
    private List<ErrorField> fields;
}
