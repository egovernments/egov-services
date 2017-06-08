package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * departmentResponseInfo
 * @author narendra
 *
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseInfo {

	private ResponseInfo responseInfo;
	
	private List<Department> departments;
}
