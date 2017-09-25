package org.egov.property.repository.builder;

public class TitleTransferBuilder {

	public static final String INSERT_TITLETRANSFER_QUERY = "INSERT INTO egpt_titletransfer ("
			+ "tenantid,upicNo, transferReason, registrationDocNo, registrationDocDate, departmentGuidelineValue,"
			+ "partiesConsiderationValue, courtOrderNumber, subRegOfficeName, titleTrasferFee, stateId, receiptnumber,"
			+ "receiptdate, createdby, lastmodifiedby, createdtime, lastmodifiedtime, applicationNo,demandId)"
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String UPDATE_TITLETRANSFER_QUERY = "UPDATE egpt_titletransfer SET stateId=?,"
			+ "lastmodifiedby=?,lastmodifiedtime=? WHERE applicationNo = ?";
}
