package org.egov.property.repository.builder;

import java.util.List;

import org.egov.models.TaxExemptionSearchCriteria;
import org.egov.property.config.PropertiesManager;
import org.egov.property.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TaxExemptionBuilder {

	@Autowired
	PropertiesManager propertiesManager;

	public static final String INSERT_TAXEXEMPTION_QUERY = "INSERT INTO " + ConstantUtility.TAXEXEMPTION_TABLE_NAME
			+ " (" + "tenantId,upicNumber, applicationNo, exemptionReason, exemptionPercentage, comments,"
			+ "stateId, createdby, lastmodifiedby, createdtime, lastmodifiedtime)" + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_TAXEXEMPTION_QUERY = "UPDATE " + ConstantUtility.TAXEXEMPTION_TABLE_NAME + ""
			+ " SET tenantId = ?,upicNumber = ?, applicationNo= ?, exemptionReason = ?, exemptionPercentage = ?,"
			+ " comments = ?, stateId = ?," + "lastmodifiedby=?,lastmodifiedtime=? WHERE id = ?";

	public static final String UPDATE_TAXEXEMPTIONPROPERTYDETAIL_QUERY = "UPDATE egpt_propertydetails"
			+ " SET isExempted = ?, exemptionReason = ?, lastModifiedBy = ?, lastmodifiedtime = ? WHERE id=?";

	public static final String GET_UPIC_NO_BY_OLD_UPIC = "select upicnumber from egpt_property where oldupicnumber =?";

	public static final String BASE_SEARCH_QUERY = "select * from " + ConstantUtility.TAXEXEMPTION_TABLE_NAME
			+ " WHERE";

	public String getSearchQuery(TaxExemptionSearchCriteria taxExemptionSearchCriteria,
			List<Object> preparedStatementValues) {

		StringBuffer searchQuery = new StringBuffer(BASE_SEARCH_QUERY);
		searchQuery.append(" tenantid=?");
		preparedStatementValues.add(taxExemptionSearchCriteria.getTenantId());

		if (taxExemptionSearchCriteria.getUpicNo() != null && !taxExemptionSearchCriteria.getUpicNo().isEmpty()) {
			searchQuery.append("AND upicNumber=?");
			preparedStatementValues.add(taxExemptionSearchCriteria.getUpicNo());
		}

		if (taxExemptionSearchCriteria.getApplicationNo() != null
				&& !taxExemptionSearchCriteria.getApplicationNo().isEmpty()) {
			searchQuery.append("AND applicationNo=?");
			preparedStatementValues.add(taxExemptionSearchCriteria.getApplicationNo());
		}

		if (taxExemptionSearchCriteria.getTaxExemptionId() != null) {
			searchQuery.append("AND id=?");
			preparedStatementValues.add(taxExemptionSearchCriteria.getTaxExemptionId());
		}

		searchQuery.append(" ORDER BY ");

		if (taxExemptionSearchCriteria.getSort() != null && taxExemptionSearchCriteria.getSort().length > 0) {

			// Count loop to add the coma ,

			int orderBycount = 1;

			StringBuffer orderByCondition = new StringBuffer();
			for (String order : taxExemptionSearchCriteria.getSort()) {
				if (orderBycount < taxExemptionSearchCriteria.getSort().length)
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

		if (taxExemptionSearchCriteria.getPageNumber() == null || taxExemptionSearchCriteria.getPageNumber() == 0)
			taxExemptionSearchCriteria.setPageNumber(Integer.valueOf(propertiesManager.getDefaultPageNumber().trim()));

		if (taxExemptionSearchCriteria.getPageSize() == null)
			taxExemptionSearchCriteria.setPageSize(Integer.valueOf(propertiesManager.getDefaultPageSize().trim()));

		int offset = 0;
		int limit = taxExemptionSearchCriteria.getPageNumber() * taxExemptionSearchCriteria.getPageSize();

		if (taxExemptionSearchCriteria.getPageNumber() <= 1)
			offset = (limit - taxExemptionSearchCriteria.getPageSize());
		else
			offset = (limit - taxExemptionSearchCriteria.getPageSize()) + 1;

		searchQuery.append(" offset ?  limit ?");
		preparedStatementValues.add(offset);
		preparedStatementValues.add(limit);

		return searchQuery.toString();
	}

	public static final String GET_DOCUMENTSBY_TAX_EXEMPTION = "select * from "
			+ ConstantUtility.TAXEXEMPTION_DOCUMENT_TABLE_NAME + " where taxexemption=?";

	public static final String AUDIT_DETAILS_QUERY = "select createdBy,lastModifiedBy,createdTime,"
			+ "lastModifiedTime from " + ConstantUtility.TAXEXEMPTION_DOCUMENT_TABLE_NAME + " where id= ?";

}
