package org.egov.mr.web.errorhandler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class Error {
	private Integer code;

	private String message;

	private String description;

	private List<FieldError> fields = new ArrayList<FieldError>();
}
