package org.egov.eis.indexer.model.es;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class EmployeeIndexGetWrapper {

	private String _index;

	private String _type;

	private Long _id;

	private Integer _version;

	private Boolean found;

	private EmployeeIndex _source;

}
	