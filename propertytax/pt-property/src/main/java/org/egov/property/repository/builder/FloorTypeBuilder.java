package org.egov.property.repository.builder;

/**
 * 
 * @author Prasad
 * This Class will have all the queries which are used in the floor type API's
 * 
 *
 */
public class FloorTypeBuilder {


    public static final String INSERT_FLOOR_QUERY = "INSERT INTO egpt_mstr_floortype ( "
            + "tenantid,code,data,createdby,lastModifiedBy, createdTime,lastModifiedtime) "
            + "VALUES( ?, ?, ?, ?, ?, ?,?)";

    public static final String UPDATE_FLOOR_QUERY = "UPDATE egpt_mstr_floortype SET tenantid = "
            + "? ,code = ?, data=?, createdby =?,lastModifiedBy =? ,createdTime = ?,"
            + "lastModifiedtime= ? WHERE id = ?";
    
    
   

   
}

