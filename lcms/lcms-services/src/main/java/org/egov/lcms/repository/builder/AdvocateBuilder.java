package org.egov.lcms.repository.builder;

/**
 * 
 * @author wtc082
 *
 */
public class AdvocateBuilder {
	
	
	public static final String SEARCH_CASE_CODE_BY_ADVOCATE ="select casecode from egov_lcms_case_advocate where advocate->>'name'=?";

}
