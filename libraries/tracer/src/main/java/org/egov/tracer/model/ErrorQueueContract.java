package org.egov.tracer.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ErrorQueueContract {

	private String source;
	private Object body;
	private Long ts;
	private ErrorRes errorRes;
	private List<StackTraceElement> exception;
	//private String couse;
	private String message;
	//private Exception exception;

}
