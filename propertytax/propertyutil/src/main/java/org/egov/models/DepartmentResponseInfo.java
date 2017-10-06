package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * departmentResponseInfo
 * 
 * @author narendra
 *
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseInfo {

	private ResponseInfo responseInfo;

	private List<Department> departments;
}
