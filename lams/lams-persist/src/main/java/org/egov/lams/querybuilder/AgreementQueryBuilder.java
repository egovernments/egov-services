package org.egov.lams.querybuilder;

public class AgreementQueryBuilder {

	public static String insertAgreementQuery() {
		// FIXME explicitly mention columns
		return "INSERT INTO eglams_agreement values "
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	}

	public static String updateAgreementQuery() {

		return "UPDATE eglams_agreement SET " + "Id=?,Agreement_Date=?,Agreement_No=?,"
				+ "Bank_Guarantee_Amount=?,Bank_Guarantee_Date=?,Case_No=?,Commencement_Date=?,"
				+ "Council_Date=?,Council_Number=?,Expiry_Date=?,Nature_Of_Allotment=?,"
				+ "Order_Date=?,Order_Details=?,Order_No=?,Payment_Cycle=?,Registration_Fee=?,"
				+ "Remarks=?,Rent=?,Rr_Reading_No=?,Security_Deposit=?,Security_Deposit_Date=?,"
				+ "solvency_certificate_date=?,solvency_certificate_no=?,status=?,tin_number=?,"
				+ "Tender_Date=?,Tender_Number=?,Trade_license_Number=?,"
				+ "created_by=?,last_modified_by=?,created_date=?,last_modified_date=?,"
				+ "allottee=?,asset=?,Rent_Increment_Method=?,"
				+ "AcknowledgementNumber=?,stateid=?,Tenant_id=?,goodwillamount=?,timeperiod=?,"
				+ "collectedsecuritydeposit=?,collectedgoodwillamount=?,source=?"
				+ " WHERE acknowledgementNumber=? and Tenant_id=?";
	}
}
