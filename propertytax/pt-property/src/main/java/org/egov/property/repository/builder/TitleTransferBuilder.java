package org.egov.property.repository.builder;

import java.util.List;

import org.egov.property.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TitleTransferBuilder {

	@Autowired
	PropertiesManager propertiesManager;

	public static final String INSERT_TITLETRANSFER_QUERY = "INSERT INTO egpt_titletransfer ("
			+ "tenantid,upicNo, transferReason, registrationDocNo, registrationDocDate, departmentGuidelineValue,"
			+ "partiesConsiderationValue, courtOrderNumber, subRegOfficeName, titleTrasferFee, stateId, receiptnumber,"
			+ "receiptdate, createdby, lastmodifiedby, createdtime, lastmodifiedtime, applicationNo,demandId)"
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_TITLETRANSFER_QUERY = "UPDATE egpt_titletransfer SET stateId=?,"
			+ "lastmodifiedby=?,lastmodifiedtime=? WHERE applicationNo = ?";

	public static final String GET_UPIC_NO_BY_OLD_UPIC = "select upicnumber from egpt_property where oldupicnumber =?";

	public static final String BASE_SEARCH_QUERY = "select * from egpt_titletransfer WHERE";

	public String getSearchQuery(String tenantId, Integer pageSize, Integer pageNumber, String[] sort, String upicNo,
			String oldUpicNo, String applicationNo, List<Object> preparedStatementvalues) {
		StringBuffer searchQuery = new StringBuffer();
		searchQuery.append(BASE_SEARCH_QUERY);
		searchQuery.append(" tenantid=?");
		preparedStatementvalues.add(tenantId);

		if (upicNo != null && !upicNo.isEmpty()) {
			searchQuery.append(" AND upicno=?");
			preparedStatementvalues.add(upicNo);
		}

		if (applicationNo != null && !applicationNo.isEmpty()) {
			searchQuery.append(" AND applicationno=?");
			preparedStatementvalues.add(applicationNo);
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

	public static final String ADDRES_BY_TITLE_TRANSFER_ID_QUERY = "select * from egpt_titletransfer_address where titletransfer= ?";

	public static final String AUDIT_DETAILS_FOR_ADDRESS = "select createdBy,lastModifiedBy,createdTime,"
			+ "lastModifiedTime from egpt_titletransfer_address where id= ?";

	public static String getOwnersByTitleTransfer(Long tilteTransferId, List<Object> preparedStatementValues) {

		StringBuffer query = new StringBuffer("select * from egpt_titletransfer_owner where titletransfer=?");
		preparedStatementValues.add(tilteTransferId);
		return query.toString();
	}

	public static String getDocumentsByTitleTransfer(Long tilteTransferId, List<Object> preparedStatementValues) {
		StringBuffer query = new StringBuffer("select * from egpt_titletransfer_document where titletransfer=?");
		preparedStatementValues.add(tilteTransferId);
		return query.toString();
	}
}
