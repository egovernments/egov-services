package org.egov.collection.repository.QueryBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiptDetailQueryBuilder {

	public static final Logger logger = LoggerFactory
			.getLogger(ReceiptDetailQueryBuilder.class);
	
	public static String insertReceiptHeader(){
		logger.info("Returning insertReceiptHeaderQuery query to the repository");
		
		return "INSERT INTO egcl_receiptheader(id, payeename, payeeaddress, payeeemail, paidby, referencenumber, receipttype, "
				+ "receiptnumber, receiptdate, businessdetails, collectiontype, reasonforcancellation, minimumamount, totalamount, "
				+ "collmodesnotallwd, consumercode, channel, fund, fundsource, function, boundary, department, voucherheader, "
				+ "depositedbranch, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid, referencedate, referencedesc, "
				+ "manualreceiptnumber, manualreceiptdate, partpaymentallowed, reference_ch_id, stateid, location, isreconciled, "
				+ "status) "
				+ "VALUES (NEXTVAL('SEQ_EGCL_RECEIPTHEADER'), :payeename, :payeeaddress, :payeeemail, :paidby, :referencenumber, :receipttype, "
				+ ":receiptnumber, :receiptdate, :businessdetails, :collectiontype, :reasonforcancellation, :minimumamount, :totalamount, "
				+ ":collmodesnotallwd, :consumercode, :channel, :fund, :fundsource, :function, :boundary, :department, :voucherheader, "
				+ ":depositedbranch, :createdby, :createddate, :lastmodifiedby, :lastmodifieddate, :tenantid, :referencedate, :referencedesc, "
				+ ":manualreceiptnumber, :manualreceiptdate, :partpaymentallowed, :reference_ch_id, :stateid, :location, :isreconciled, "
				+ ":status)";
	}
	
	public static String insertReceiptDetails(){
		logger.info("Returning insertReceiptDetailsQuery query to the repository");
		
		return "INSERT INTO egcl_receiptdetails(id, chartofaccount, dramount, cramount, ordernumber, receiptheader, actualcramounttobepaid, "
				+ "description, financialyear, isactualdemand, purpose, tenantid) "
				+ "VALUES (NEXTVAL('SEQ_EGCL_RECEIPTDETAILS'), :chartofaccount, :dramount, :cramount, :ordernumber, :receiptheader, :actualcramounttobepaid, "
				+ ":description, :financialyear, :isactualdemand, :purpose, :tenantid)";
	}
	
	public static String getreceiptHeaderId(){
		logger.info("Returning getreceiptHeaderId query to the repository");
		
		return "SELECT MAX(id) FROM egcl_receiptheader WHERE payeename = ? AND paidby = ? AND createddate = ?";
	}
}
