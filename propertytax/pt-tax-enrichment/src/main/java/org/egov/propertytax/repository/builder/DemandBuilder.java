package org.egov.propertytax.repository.builder;

public class DemandBuilder {
	
	
	public static final String GET_property_Id = "select property from egpt_propertydetails 	where applicationno=?";
	
	
	public static final String GET_TAX_CALC_FOR_PROPERTY= "select taxcalculations from egpt_propertydetails where property=?";
			
}
