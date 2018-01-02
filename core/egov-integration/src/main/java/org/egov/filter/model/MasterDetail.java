package org.egov.filter.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MasterDetail {
	private String masterName;
	private FinalResponse finalResponse;
	private List<Request> requests;
}
