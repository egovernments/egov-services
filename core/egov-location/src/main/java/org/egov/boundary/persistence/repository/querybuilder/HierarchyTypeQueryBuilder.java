package org.egov.boundary.persistence.repository.querybuilder;

import org.springframework.stereotype.Component;

@Component
public class HierarchyTypeQueryBuilder {

	public static String getHierarchyTypeInsertquery() {
		return "insert into eg_hierarchy_type(id,name,code,createddate,lastmodifieddate,createdby,lastmodifiedby,version,tenantid,localname) values (:id,:name,:code,:createddate,:lastmodifieddate,:createdby,:lastmodifiedby,:version,:tenantid,:localname)";
	}

	public static String getHierarchyTypeupdatequery() {
		return "update eg_hierarchy_type set name=:name,lastmodifieddate=:lastmodifieddate,lastmodifiedby=:lastmodifiedby,localname=:localname where code=:code and tenantid=:tenantid ";
	}

	public static String getHierarchyTypeByNameAndTenantId() {
		return "select * from eg_hierarchy_type where name=:name and tenantid=:tenantid";
	}

	public static String getHierarchyTypeByNameContainingIgnoreCase() {
		return "select * from eg_hierarchy_type where name=:name";
	}

	public static String getHierarchyTypeByCodeAndTenantId() {
		return "select * from eg_hierarchy_type where code=:code and tenantid=:tenantid ";
	}

	public static String getHierarchyTypeByIdAndTenantId() {
		return "select * from eg_hierarchy_type where id=:id and tenantid=:tenantid";
	}

	public static String getAllHierarchyTypeByTenantId() {
		return "select * from eg_hierarchy_type where tenantid=:tenantid";
	}

}
