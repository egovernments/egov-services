package org.egov.common.web.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class CommonResponse<T> {
	private ResponseInfo responseInfo;
	private List<T> data = new ArrayList<T>();
	private PaginationContract page;

}
