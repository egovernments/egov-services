package org.egov.boundary.persistence.repository.querybuilder;

import org.springframework.stereotype.Component;

@Component
public class CrossHierarchyQueryBuilder {
	
	public static String getCrossHierarchiesByParentIdAndTenantId() {
		return "select * from eg_crosshierarchy where parent=:parent and tenantid =:tenantId";
}
	public static String getInsertCrossHierarchy(){
	  return "insert into eg_crosshierarchy(id,parent,child,parenttype,childtype,tenantid,code,createdby,lastmodifiedby,createddate,lastmodifieddate) values "
	  		+ " (:id,:parent,:child,:parenttype,:childtype,:tenantid,:code,:createdby,:lastmodifiedby,:createddate,:lastmodifieddate)";	
	}
	
	public static String getUpdateCrossHierarchy(){
		return "update eg_crosshierarchy  set parent =:parent,child=:child,parenttype=:parenttype,childtype=:childtype,lastmodifiedby=:lastmodifiedby,lastmodifieddate=:lastmodifieddate where code=:code and tenantid=:tenantid";
	}
	
	public static String getCrossHierarchyByIdAndTenant(){
		return "select * from eg_crosshierarchy where id=:id and tenantid=:tenantId";
	}
	
	public static String getCrossHierarchyChildernByParentIdAndTenant(){
		return "select * from eg_boundary where tenantid=:tenantId";
	}
	
	public static String getAllCrossHierarchyByTenantId(){
		return "select * from eg_crosshierarchy where tenantid=:tenantId";
	}
	
	public static String getCrossHierarchyByCodeAndTenant(){
		return "select * from eg_crosshierarchy where code=:code and tenantid=:tenantId";
	}
	
	public static String getActiveBoundariesByNameAndBndryTypeNameAndHierarchyTypeNameAndTenantId(){
		return "select ch.id as id,child.name as childName,locparent.name as parentName ,parent.name childParentname from eg_crosshierarchy ch,eg_boundary_type childType,eg_hierarchy_type cht,"
               +" eg_boundary_type parentType,eg_hierarchy_type pht,eg_boundary child,eg_boundary parent,eg_boundary locparent where ch.parent=locparent.id and child.parent = parent.id and "
               +" ch.childtype = childType.id and UPPER(childType.name) = UPPER(:boundaryTypeName) and childtype.hierarchytype = cht.id and UPPER(cht.name) = UPPER(:hierarchyTypeName) and ch.parenttype = parentType.id and" 
               +" parentType.hierarchytype = pht.id and UPPER(pht.name) = UPPER(:parenthierarchyTypeName) and ch.child = child.id and UPPER(child.name) like UPPER(:name) and ch.tenantid=:tenantId order by child.name";
	}
}
