package org.egov.common.web.contract;

import java.util.List;

import javax.validation.Valid;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonRequest<T> {
	private RequestInfo requestInfo;
	@Valid
	private List<T> data;
}
