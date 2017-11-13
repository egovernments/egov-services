package org.egov.lcms.repository.builder;

import java.util.List;

/**
 * 
 * @author Prasad
 *
 */
public class SummonBuilder {

	public static final String GET_ADVOCATECODE_BY_CASECODE = "select code from egov_lcms_case_advocate where casecode =?";

	public static final String DELETE_RECORD_BY_ADVOCATE_CODE = "delete from egov_lcms_case_advocate where code=?";
	
	public static String deleteAdvocates(List<String> advocateCodes){
		
		StringBuffer queryString = new StringBuffer();
		queryString.append("delete from egov_lcms_case_advocate where ");

		int count = 1;
		String code = "";
		for (String advocateCode : advocateCodes) {
			if (count <advocateCodes.size())
				code = code + "'" + advocateCode + "',";
			else
				code = code + "'" + advocateCode + "'";
			count++;

		}
		
	 queryString.append("code IN("+code+")");
	 
	 
	 return queryString.toString();
		
		
	}

}
