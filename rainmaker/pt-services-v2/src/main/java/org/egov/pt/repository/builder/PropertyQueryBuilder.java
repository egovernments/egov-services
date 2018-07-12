package org.egov.pt.repository.builder;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.egov.pt.web.models.PropertyCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class PropertyQueryBuilder {

	private static final String INNER_JOIN_STRING = "INNER JOIN";
	private static final String LEFT_OUTER_JOIN_STRING = "LEFT OUTER JOIN";
	
	private static final String QUERY = "SELECT pt.*,ptdl.*,address.*,owner.*,doc.*,unit.*,insti.*,"
			+ " pt.propertyid as propertyid,ptdl.assessmentnumber as propertydetailid,doc.id as documentid,unit.id as unitid,"
			+ "address.id as addresskeyid,insti.id as instiid,"
			+ "ownerdoc.id as ownerdocid,ownerdoc.documenttype as ownerdocType,ownerdoc.filestore as ownerfileStore,"
			+ "ownerdoc.documentuid as ownerdocuid,"
			+ "ptdl.createdby as assesscreatedby,ptdl.lastModifiedBy as assesslastModifiedBy,ptdl.createdTime as assesscreatedTime,"
			+ "ptdl.lastModifiedTime as assesslastModifiedTime,"
			+ "insti.name as institutionname,insti.type as institutiontype,insti.tenantid as institenantId"
			+ " FROM eg_pt_property_v2 pt "
			+ INNER_JOIN_STRING
			+ " eg_pt_propertydetail_v2 ptdl ON pt.propertyid =ptdl.property "
			+ INNER_JOIN_STRING
			+ " eg_pt_owner_v2 owner ON ptdl.assessmentnumber=owner.propertydetail "
			+ INNER_JOIN_STRING
			+ " eg_pt_unit_v2 unit ON ptdl.assessmentnumber=unit.propertydetail "
			+ INNER_JOIN_STRING
			+" eg_pt_address_v2 address on address.property=pt.propertyid "
			+ LEFT_OUTER_JOIN_STRING
			+ " eg_pt_document_v2 doc ON ptdl.assessmentnumber=doc.foreignkeyid "
			+ LEFT_OUTER_JOIN_STRING
			+ " eg_pt_document_v2 ownerdoc ON owner.userid=ownerdoc.foreignkeyid "
			+ LEFT_OUTER_JOIN_STRING
			+ " eg_pt_institution_v2 insti ON ptdl.assessmentnumber=insti.propertydetail "
			+ " WHERE ";
	
	public String getPropertySearchQuery(PropertyCriteria criteria, List<Object> preparedStmtList) {
		
		StringBuilder builder = new StringBuilder(QUERY);
		builder.append(" pt.tenantid=? ");
		preparedStmtList.add(criteria.getTenantId());
		
		Set<String> ids = criteria.getIds();
		if(!CollectionUtils.isEmpty(ids)) {
			
			builder.append("and pt.propertyid IN (").append(convertSetToString(ids)).append(")");

		}

		Set<String> oldpropertyids = criteria.getOldpropertyids();
		if(!CollectionUtils.isEmpty(oldpropertyids)) {

			builder.append("and pt.oldpropertyid IN (").append(convertSetToString(oldpropertyids)).append(")");

		}

		Set<String> propertyDetailids = criteria.getPropertyDetailids();
		if(!CollectionUtils.isEmpty(propertyDetailids)) {

			builder.append("and ptdl.assessmentnumber IN (").append(convertSetToString(propertyDetailids)).append(")");

		}

		Set<String> addressids = criteria.getAddressids();
		if(!CollectionUtils.isEmpty(addressids)) {

			builder.append("and address.id IN (").append(convertSetToString(addressids)).append(")");

		}

		Set<String> ownerids = criteria.getOwnerids();
		if(!CollectionUtils.isEmpty(ownerids)) {

			builder.append("and owner.userid IN (").append(convertSetToString(ownerids)).append(")");

		}

		Set<String> unitids = criteria.getUnitids();
		if(!CollectionUtils.isEmpty(unitids)) {

			builder.append("and unit.id IN (").append(convertSetToString(unitids)).append(")");

		}

		Set<String> documentids = criteria.getDocumentids();
		if(!CollectionUtils.isEmpty(documentids)) {

			builder.append("and doc.id IN (").append(convertSetToString(documentids)).append(")");

		}

		if(criteria.getDoorNo()!=null && criteria.getLocality()!=null){
			builder.append(" and address.doorno = '").append(criteria.getDoorNo()).append("' and address.locality = '").append(criteria.getLocality()).append("'");
		}

		if(criteria.getAccountId()!=null) {
			builder.append(" and ptdl.accountid = '").append(criteria.getAccountId()).append("'");
		}

		return builder.toString();
	}
	
	private String convertSetToString(Set<String> ids) {
		
		final String quotes = "'";
		final String comma = ",";
		StringBuilder builder = new StringBuilder();
		Iterator<String> iterator = ids.iterator();
		while(iterator.hasNext()) {
			builder.append(quotes).append(iterator.next()).append(quotes);
			if(iterator.hasNext()) builder.append(comma);
		}
		return builder.toString();
	}
}
