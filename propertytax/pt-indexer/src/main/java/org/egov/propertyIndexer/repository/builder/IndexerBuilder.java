package org.egov.propertyIndexer.repository.builder;

/**
 * 
 * @author Prasad
 * This class contains all the Queries which are used in the indexer
 *
 */
public class IndexerBuilder {

	public final static String GET_USAGE_NAME_BY_USAGE_CODE = "select data->>'name' from egpt_mstr_usage where data->>'code'=? AND tenantId=?";

	public final static String GET_ROOF_NAME_BY_ROOF_CODE = "select data->>'name' from egpt_mstr_rooftype where data->>'code'=? AND tenantId=?";

	public final static String GET_FLOOR_NAME_BY_FLOOR_CODE = "select data->>'name' from egpt_mstr_floortype where data->>'code'=? AND tenantId=?";

	public final static String GET_WOOD_NAME_BY_WOOD_CODE = "select data->>'name' from egpt_mstr_woodtype where data->>'code'=? AND tenantId=?";

	public final static String GET_WALL_NAME_BY_WALL_CODE = "select data->>'name' from egpt_mstr_walltype where data->>'code'=? AND tenantId=?";

	public final static String GET_PROPERTY_NAME_BY_PROPERTY_CODE = "select data->>'name' from egpt_mstr_propertytype where data->>'code'=? AND tenantId=?";

	public final static String GET_SUBUSAGE_NAME_BY_SUBUSAGE_CODE = "select data->>'name' from egpt_mstr_usage where data->>'code'=? AND tenantId=?";

	public final static String GET_OCCUPANCY_NAME_BY_OCCUPANCY_CODE = "select data->>'name' from egpt_mstr_occuapancy where data->>'code'=? AND tenantId=?";

	public final static String GET_DEPARTMENT_NAME_BY_DEPARTMENT_CODE = "select data->>'name' from egpt_mstr_department where data->>'code'=? AND tenantId=?";

	public final static String GET_APARTMENT_NAME_BY_APARTMENT_CODE = "select data->>'name' from egpt_mstr_apartment where data->>'code'=? AND tenantId=?";

	public final static String GET_STRUCTURE_NAME_BY_STRUCTURE_CODE = "select data->>'name' from egpt_mstr_structureclass where data->>'code'=? AND tenantId=?";

}
