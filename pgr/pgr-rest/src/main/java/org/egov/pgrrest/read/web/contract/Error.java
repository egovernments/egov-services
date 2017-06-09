package org.egov.pgrrest.read.web.contract;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
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
