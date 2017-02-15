package org.egov.egf.web.contract;

import java.util.ArrayList;
import java.util.List;

public class Error {

	private Integer code = null;

	private String message = null;

	private String description = null;

	private List<FieldError> filelds = new ArrayList<FieldError>();

	

}
