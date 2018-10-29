package org.egov.pt.repository.builder;

import java.util.*;

import org.egov.pt.web.models.PropertyCriteria;
import org.egov.pt.web.models.PropertyInfo;
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
			+ "ptdl.lastModifiedTime as assesslastModifiedTime,unit.occupancyDate as unitoccupancyDate,"
			+ "insti.name as institutionname,insti.type as institutiontype,insti.tenantid as institenantId,"
			+ "ownerdoc.userid as docuserid,ownerdoc.propertydetail as docassessmentnumber,"
			+ "unit.usagecategorymajor as unitusagecategorymajor,unit.usagecategoryminor as unitusagecategoryminor"
			+ " FROM eg_pt_property_v2 pt "
			+ INNER_JOIN_STRING
			+ " eg_pt_propertydetail_v2 ptdl ON pt.propertyid =ptdl.property "
			+ INNER_JOIN_STRING
			+ " eg_pt_owner_v2 owner ON ptdl.assessmentnumber=owner.propertydetail "
			+ INNER_JOIN_STRING
			+" eg_pt_address_v2 address on address.property=pt.propertyid "
			+ LEFT_OUTER_JOIN_STRING
			+ " eg_pt_unit_v2 unit ON ptdl.assessmentnumber=unit.propertydetail "
			+ LEFT_OUTER_JOIN_STRING
			+ " eg_pt_document_propertydetail_v2 doc ON ptdl.assessmentnumber=doc.propertydetail "
			+ LEFT_OUTER_JOIN_STRING
			+ " eg_pt_document_owner_v2 ownerdoc ON ownerdoc.userid=owner.userid "
			+ LEFT_OUTER_JOIN_STRING
			+ " eg_pt_institution_v2 insti ON ptdl.assessmentnumber=insti.propertydetail "
			+ " WHERE ";
	
	private static final String LIKE_QUERY = "SELECT pt.*,ptdl.*,address.*,owner.*,doc.*,unit.*,insti.*,"
			+ " pt.propertyid as propertyid,ptdl.assessmentnumber as propertydetailid,doc.id as documentid,unit.id as unitid,"
			+ "address.id as addresskeyid,insti.id as instiid,"
			+ "ownerdoc.id as ownerdocid,ownerdoc.documenttype as ownerdocType,ownerdoc.filestore as ownerfileStore,"
			+ "ownerdoc.documentuid as ownerdocuid,"
			+ "ptdl.createdby as assesscreatedby,ptdl.lastModifiedBy as assesslastModifiedBy,ptdl.createdTime as assesscreatedTime,"
			+ "ptdl.lastModifiedTime as assesslastModifiedTime,unit.occupancyDate as unitoccupancyDate,"
			+ "insti.name as institutionname,insti.type as institutiontype,insti.tenantid as institenantId,"
			+ "ownerdoc.userid as docuserid,ownerdoc.propertydetail as docassessmentnumber,"
			+ "unit.usagecategorymajor as unitusagecategorymajor,unit.usagecategoryminor as unitusagecategoryminor"
			+ " FROM eg_pt_property_v2 pt "
			+ INNER_JOIN_STRING
			+ " eg_pt_propertydetail_v2 ptdl ON pt.propertyid =ptdl.property "
			+ INNER_JOIN_STRING
			+ " eg_pt_owner_v2 owner ON ptdl.assessmentnumber=owner.propertydetail "
			+ INNER_JOIN_STRING
			+" eg_pt_address_v2 address on address.property=pt.propertyid "
			+ LEFT_OUTER_JOIN_STRING
			+ " eg_pt_unit_v2 unit ON ptdl.assessmentnumber=unit.propertydetail "
			+ LEFT_OUTER_JOIN_STRING
			+ " eg_pt_document_propertydetail_v2 doc ON ptdl.assessmentnumber=doc.propertydetail "
			+ LEFT_OUTER_JOIN_STRING
			+ " eg_pt_document_owner_v2 ownerdoc ON ownerdoc.userid=owner.userid "
			+ LEFT_OUTER_JOIN_STRING
			+ " eg_pt_institution_v2 insti ON ptdl.assessmentnumber=insti.propertydetail "
			+ " WHERE tenantid LIKE '%pb%'";
	
	
	public String getPropertyLikeQuery() {
		return LIKE_QUERY;
	}

	public String getPropertySearchQuery(PropertyCriteria criteria, List<Object> preparedStmtList) {

		StringBuilder builder = new StringBuilder(QUERY);

		Set<String> statuses =new HashSet<>();
		criteria.getStatuses().forEach(statusEnum -> {statuses.add(statusEnum.toString());});
		if(!CollectionUtils.isEmpty(statuses)) {
			builder.append(" pt.status IN (").append(createQuery(statuses)).append(")");
			addToPreparedStatement(preparedStmtList,statuses);
		}

		if(criteria.getAccountId()!=null) {
			builder.append(" and ptdl.accountid = ? ");
			preparedStmtList.add(criteria.getAccountId());
			return builder.toString();
		}

		builder.append(" and pt.tenantid=? ");
		preparedStmtList.add(criteria.getTenantId());

		Set<String> ids = criteria.getIds();
		if(!CollectionUtils.isEmpty(ids)) {

			builder.append("and pt.propertyid IN (").append(createQuery(ids)).append(")");
			addToPreparedStatement(preparedStmtList,ids);
		}

		Set<String> oldpropertyids = criteria.getOldpropertyids();
		if(!CollectionUtils.isEmpty(oldpropertyids)) {

			builder.append("and pt.oldpropertyid IN (").append(createQuery(oldpropertyids)).append(")");
			addToPreparedStatement(preparedStmtList,oldpropertyids);
		}

		Set<String> propertyDetailids = criteria.getPropertyDetailids();
		if(!CollectionUtils.isEmpty(propertyDetailids)) {

			builder.append("and ptdl.assessmentnumber IN (").append(createQuery(propertyDetailids)).append(")");
			addToPreparedStatement(preparedStmtList,propertyDetailids);
		}

		Set<String> addressids = criteria.getAddressids();
		if(!CollectionUtils.isEmpty(addressids)) {

			builder.append("and address.id IN (").append(createQuery(addressids)).append(")");
			addToPreparedStatement(preparedStmtList,addressids);
		}

		Set<String> ownerids = criteria.getOwnerids();
		if(!CollectionUtils.isEmpty(ownerids)) {

			builder.append("and owner.userid IN (").append(createQuery(ownerids)).append(")");
			addToPreparedStatement(preparedStmtList,ownerids);
		}

		Set<String> unitids = criteria.getUnitids();
		if(!CollectionUtils.isEmpty(unitids)) {

			builder.append("and unit.id IN (").append(createQuery(unitids)).append(")");
			addToPreparedStatement(preparedStmtList,unitids);
		}

		Set<String> documentids = criteria.getDocumentids();
		if(!CollectionUtils.isEmpty(documentids)) {

			builder.append("and doc.id IN (").append(createQuery(documentids)).append(")");
			addToPreparedStatement(preparedStmtList,documentids);
		}

		if(criteria.getDoorNo()!=null && criteria.getLocality()!=null){
			builder.append(" and address.doorno = ? ").append(" and address.locality = ? ");
			preparedStmtList.add(criteria.getDoorNo());
			preparedStmtList.add(criteria.getLocality());
		}



		return builder.toString();
	}

	/*private String createQuery(Set<String> ids) {

		final String quotes = "'";
		final String comma = ",";
		StringBuilder builder = new StringBuilder();
		Iterator<String> iterator = ids.iterator();
		while(iterator.hasNext()) {
			builder.append(quotes).append(iterator.next()).append(quotes);
			if(iterator.hasNext()) builder.append(comma);
		}
		return builder.toString();
	}*/


	private String createQuery(Set<String> ids) {
		StringBuilder builder = new StringBuilder();
		int length = ids.size();
		for( int i = 0; i< length; i++){
			builder.append(" ?");
			if(i != length -1) builder.append(",");
		}
		return builder.toString();
	}

	private void addToPreparedStatement(List<Object> preparedStmtList,Set<String> ids)
	{ ids.forEach(id ->{ preparedStmtList.add(id);});
	}


}
