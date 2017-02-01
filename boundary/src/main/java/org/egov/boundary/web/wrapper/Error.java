package org.egov.boundary.web.wrapper;

import java.util.ArrayList;
import java.util.List;

public class Error {

	private Integer code = null;

	private String message = null;

	private String description = null;

	private List<Object> filelds = new ArrayList<Object>();

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Object> getFilelds() {
		return filelds;
	}

	public void setFilelds(List<Object> filelds) {
		this.filelds = filelds;
	}

}
