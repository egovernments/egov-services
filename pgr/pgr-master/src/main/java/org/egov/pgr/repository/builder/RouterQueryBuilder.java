package org.egov.pgr.repository.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RouterQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(RouterQueryBuilder.class);
	
	private static final String BASE_URL = "SELECT router.id, router.position, router.tenantid, router.bndryid, comp.id complaintid, comp.code servicecode, comp.name servicename, "
			+ " comp.description servicedescription, comp.category, adef.code attributecode, adef.datatype, adef.datatypedescription, adef.description attributedescription, "
			+ " adef.variable, adef.required, vdef.key, vdef.name FROM egpgr_router router LEFT JOIN egpgr_complainttype comp ON router.complainttypeid = comp.id " 
			+ " LEFT JOIN service_definition sdef ON sdef.code = comp.code LEFT JOIN attribute_definition adef ON sdef.code = adef.servicecode LEFT JOIN value_definition vdef ON vdef.attributecode = adef.code " ; 

	private static final String INSERT_ROUTER = "INSERT INTO egpgr_router(id, complainttypeid, position, bndryid, version, createdby, createddate, lastmodifiedby, lastmodifieddate,tenantid) values"
			+ "(?,?,?,?,?,?,?,?,?,?)";
	
	public static String insertRouter() {
		return INSERT_ROUTER;
	}
	
	public String getRouterDetail(){
		return BASE_URL;
	}
	
	
	/* Query not to be removed till the SEARCH API is complete
	 * SELECT * FROM egpgr_router router LEFT JOIN egpgr_complainttype comp ON router.complainttypeid = comp.id
			LEFT JOIN service_definition sdef ON sdef.code = comp.code 
			LEFT JOIN attribute_definition adef ON sdef.code = adef.servicecode
			LEFT JOIN value_definition vdef ON vdef.attributecode = adef.code
			WHERE 
			router.tenantid = ?
			AND
			router.boundaryid = ?
			AND 
			comp.id = ? 
			AND 
			router.position = ? */
	

}

