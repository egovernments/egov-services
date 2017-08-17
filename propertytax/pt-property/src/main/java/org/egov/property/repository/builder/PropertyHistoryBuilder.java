package org.egov.property.repository.builder;

public class PropertyHistoryBuilder {

	public static final String PROPERTY_HISTORY = "INSERT INTO egpt_property_history select * from egpt_property where id= ?";

	public static final String ADDRESS_HISTORY = "INSERT INTO egpt_address_history select * from egpt_address where id= ?";

	public static final String PROPERTYDETAILS_HISTORY = "INSERT INTO egpt_propertydetails_history"
			+ " select * from egpt_propertydetails where id= ?";

	public static final String FLOOR_HISTORY = "INSERT INTO egpt_floors_history select * from egpt_floors where id= ?";

	public static final String UNIT_HISTORY = "INSERT INTO egpt_unit_history select * from egpt_unit where id= ?";

	public static final String DOCUMENT_HISTORY = "INSERT INTO egpt_document_history select * from egpt_document where id= ?";

	public static final String VACANTLAND_HISTORY = "INSERT INTO egpt_vacantland_history "
			+ "select * from egpt_vacantland where id= ?";

	public static final String OWNER_HISTORY = "INSERT INTO egpt_property_owner_history"
			+ " select * from egpt_property_owner where owner=? and property=?";

	public static final String BOUNDARY_HISTORY = "INSERT INTO egpt_propertylocation_history"
			+ " select * from egpt_propertylocation where id= ?";

	public static final String PROPERTYDETAILS_HISTORY_STATUS_UPDATE = "UPDATE egpt_propertydetails_history"
			+ " set status=? where id= ?";

}
