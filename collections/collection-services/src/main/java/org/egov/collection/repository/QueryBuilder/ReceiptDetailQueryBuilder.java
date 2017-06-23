package org.egov.collection.repository.QueryBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiptDetailQueryBuilder {

	public static final Logger logger = LoggerFactory
			.getLogger(ReceiptDetailQueryBuilder.class);
	
	public static String insertReceiptDetails(){
		logger.info("Returning insertReceiptDetailsQuery query to the repository");
		
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
}
