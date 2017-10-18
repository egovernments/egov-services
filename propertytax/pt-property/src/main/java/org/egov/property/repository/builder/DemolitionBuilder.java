package org.egov.property.repository.builder;

import java.util.List;

import org.egov.models.DemolitionSearchCriteria;
import org.egov.property.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Prasad
 *
 */
@Service
public class DemolitionBuilder {

	@Autowired
	PropertiesManager propertiesManager;

	public static final String INSERT_DEMOLITION = "INSERT INTO egpt_demolition(tenantId,upicNumber,"
			+ "applicationno,propertySubType,usageType,usageSubType,totalArea,sequenceNo,"
			+ "isLegal,demolitionReason,comments,stateId,createdby,lastmodifiedby,createdtime,"
			+ "lastmodifiedtime)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_DEMOLITION_DOCUMENT = "INSERT INTO egpt_demolition_document "
			+ "(filestore,documenttype,demolition,createdby,lastmodifiedby,createdtime,lastmodifiedtime) "
			+ "VALUES (?,?,?,?,?,?,?)";

	public static final String UPDATE_DEMOLITION = "UPDATE egpt_demolition SET tenantId=?,upicNumber=?,"
			+ "applicationno=?,propertySubType=?,usageType=?,usageSubType=?,totalArea=?,sequenceNo=?,"
			+ "isLegal=?,demolitionReason=?,comments=?,stateId=?,createdby=?,lastmodifiedby=?,createdtime=?,"
			+ "lastmodifiedtime=? where id=?";

	public static final String UPDATE_DEMOLITION_DOCUMENT = "UPDATE egpt_demolition_document SET filestore=?,documenttype=?,demolition=?::bigint,"
			+ "createdby=?,lastmodifiedby=?,createdtime=?,lastmodifiedtime=? where id=?";

	public static final String GET_DOCUMENTS_BY_DEMOLITION = "select * from egpt_demolition_document where demolition=?";

	public static final String GET_UPIC_NO_BY_OLD_UPIC = "select upicnumber from egpt_property where oldupicnumber =?";

	public static final String GET_DOCUMENTIDS_BY_DOCUMENT = "select id from egpt_demolition_document where demolition=?";

	public static final String DELETE_DOCUMENT_BY_DOCUMENT_ID = "delete from egpt_demolition_document where id=?";

	public String BASE_QUERY = "SELECT * from egpt_demolition WHERE tenantId =?";

	public String searchDemolitionsQuery(DemolitionSearchCriteria demolitionSearchCriteria,
			List<Object> preparedStatementValues) {

		StringBuffer searchQuery = new StringBuffer(BASE_QUERY);
		preparedStatementValues.add(demolitionSearchCriteria.getTenantId());

		if (demolitionSearchCriteria.getUpicNo() != null && !demolitionSearchCriteria.getUpicNo().isEmpty()) {
			searchQuery.append("AND upicNumber=?");
			preparedStatementValues.add(demolitionSearchCriteria.getUpicNo());
		}

		if (demolitionSearchCriteria.getApplicationNo() != null
				&& !demolitionSearchCriteria.getApplicationNo().isEmpty()) {
			searchQuery.append("AND applicationNo=?");
			preparedStatementValues.add(demolitionSearchCriteria.getApplicationNo());
		}

		if (demolitionSearchCriteria.getDemolitionId() != null) {
			searchQuery.append("AND id=?");
			preparedStatementValues.add(demolitionSearchCriteria.getDemolitionId());
		}

		searchQuery.append(" ORDER BY ");

		if (demolitionSearchCriteria.getSort() != null && demolitionSearchCriteria.getSort().length > 0) {

			// Count loop to add the coma ,

			int orderBycount = 1;

			StringBuffer orderByCondition = new StringBuffer();
			for (String order : demolitionSearchCriteria.getSort()) {
				if (orderBycount < demolitionSearchCriteria.getSort().length)
					orderByCondition.append(order + ",");
				else
					orderByCondition.append(order);
				orderBycount++;
			}

			if (orderBycount > 1)
				orderByCondition.append(" asc");

			searchQuery.append(orderByCondition.toString());
		}

		else {
			searchQuery.append("upicNumber asc");
		}

		if (demolitionSearchCriteria.getPageNumber() == null || demolitionSearchCriteria.getPageNumber() == 0)
			demolitionSearchCriteria.setPageNumber(Integer.valueOf(propertiesManager.getDefaultPageNumber().trim()));

		if (demolitionSearchCriteria.getPageSize() == null)
			demolitionSearchCriteria.setPageSize(Integer.valueOf(propertiesManager.getDefaultPageSize().trim()));

		int offset = 0;
		int limit = demolitionSearchCriteria.getPageNumber() * demolitionSearchCriteria.getPageSize();

		if (demolitionSearchCriteria.getPageNumber() <= 1)
			offset = (limit - demolitionSearchCriteria.getPageSize());
		else
			offset = (limit - demolitionSearchCriteria.getPageSize()) + 1;

		searchQuery.append(" offset ?  limit ?");
		preparedStatementValues.add(offset);
		preparedStatementValues.add(limit);

		return searchQuery.toString();
	}

}
