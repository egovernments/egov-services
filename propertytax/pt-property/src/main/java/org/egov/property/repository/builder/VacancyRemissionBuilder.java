package org.egov.property.repository.builder;

import java.util.List;

import org.egov.property.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VacancyRemissionBuilder {

	@Autowired
	PropertiesManager propertiesManager;

	public static final String INSERT_VACANCYREMISSION_QUERY = "INSERT INTO egpt_vacancyremission ("
			+ "upicNo ,tenantId , applicationNo , fromDate , toDate , percentage ,"
			+ "reason , requestDate , approvedDate , isApproved , remarks,stateid ,"
			+ " createdby, lastmodifiedby, createdtime, lastmodifiedtime)" + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static String updateVacancyRemissionQuery() {

		StringBuffer vacancyRemissionUpdateSQL = new StringBuffer();

		vacancyRemissionUpdateSQL.append("UPDATE egpt_vacancyremission")
				.append(" SET upicNo  = ? , tenantId  = ?, applicationNo  = ?, fromDate  = ?,")
				.append("toDate  = ?, percentage  = ?, reason  = ?, requestDate  = ?,")
				.append(" approvedDate  = ?, isApproved  = ?, remarks  = ?,stateid = ?,")
				.append(" lastModifiedBy = ?, lastModifiedTime = ?").append(" WHERE id = ? ");

		return vacancyRemissionUpdateSQL.toString();
	}

	public static final String AUDIT_DETAILS_QUERY = "select createdBy,lastModifiedBy,createdTime,"
			+ "lastModifiedTime from egpt_vacancyremission where id= ?";

	public static final String BASE_SEARCH_QUERY = "select * from egpt_vacancyremission WHERE";

	public String getSearchQuery(String tenantId, Integer pageSize, Integer pageNumber, String upicNumber,
			String applicationNo, String approvedDate, Boolean isApproved, List<Object> preparedStatementvalues) {
		StringBuffer searchQuery = new StringBuffer();
		searchQuery.append(BASE_SEARCH_QUERY);
		searchQuery.append(" tenantid=?");
		preparedStatementvalues.add(tenantId);

		if (upicNumber != null && !upicNumber.isEmpty()) {
			searchQuery.append(" AND upicno=?");
			preparedStatementvalues.add(upicNumber);
		}

		if (applicationNo != null && !applicationNo.isEmpty()) {
			searchQuery.append(" AND applicationno=?");
			preparedStatementvalues.add(applicationNo);
		}

		if (approvedDate != null && !approvedDate.isEmpty()) {
			searchQuery.append(" AND approveddate = to_date(?,'dd/MM/yyyy')");
			preparedStatementvalues.add(approvedDate);
		}

		if (isApproved != null) {
			if (isApproved) {
				searchQuery.append(" AND isapproved=?");
				preparedStatementvalues.add(isApproved);
			}
		}

		if (pageNumber == null || pageNumber == 0)
			pageNumber = Integer.valueOf(propertiesManager.getDefaultPageNumber().trim());

		if (pageSize == null)
			pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize().trim());

		int offset = 0;
		int limit = pageNumber * pageSize;

		if (pageNumber <= 1) {
			offset = (limit - pageSize);
			searchQuery.append(" offset ?");
			preparedStatementvalues.add(offset);
		} else {
			offset = (limit - pageSize) + 1;
			searchQuery.append(" limit ?");
			preparedStatementvalues.add(offset);
		}
		return searchQuery.toString();

	}

	public static String getDocumentsByVacancyRemission(Long vacancyRemissionId, List<Object> preparedStatementValues) {
		StringBuffer query = new StringBuffer("select * from egpt_vacancyremission_document where vacancyremission=?");
		preparedStatementValues.add(vacancyRemissionId);
		return query.toString();
	}
}