package org.egov.property.repository.builder;

/**
 * 
 * @author Prasad Khandagale
 *
 */
public class SpecialNoticeBuilder {

	public static final String INSERT_SPECIALNOTICE_QUERY = "insert into egpt_specialnotice (upicNo,tenantId,ulbName,ulbLogo,noticeDate,noticeNumber,applicationNo,applicationDate) VALUES (?,?,?,?,?,?,?,?)";

	public static final String INSERT_NOTICE_ADDRESS = "insert into egpt_specialnotice_address (notice,address) VALUES (?,?)";

	public static final String INSERT_NOTICE_OWNERS = "insert into egpt_specialnotice_owners (notice,owner) VALUES (?,?)";

	public static final String INSERT_NOTICE_FLOOR_SPEC = "insert into egpt_specialnotice_floorspec (notice,floorNo,unitDetails,usage,"
			+ "construction,assessableArea,alv,rv) VALUES (?,?,?,?,?,?,?,?)";

	public static final String INSERT_NOTICE_TAX_DETAILS = "insert into egpt_specialnotice_tax_details (notice,totalTax) VALUES (?,?)";

	public static final String INSERT_NOTICE_TAXWISE_DETAILS = "insert into egpt_specialnotice_taxwise_details (notice,taxdetails,taxName,taxDays,taxValue) VALUES (?,?,?,?,?)";

	public static final String UNIQUE_UPIC_NO = "select count(*) from egpt_specialnotice where upicNo = ?";

	public static final String GET_UPIC_NO = "select upicNo from egpt_specialnotice where upicNo =?";

	public static final String GET_NOTICE_NUMBER = "select noticenumber from egpt_specialnotice where upicNo =? LIMIT 1";

}
