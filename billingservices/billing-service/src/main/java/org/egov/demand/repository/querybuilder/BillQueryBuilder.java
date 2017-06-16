package org.egov.demand.repository.querybuilder;

import org.springframework.stereotype.Component;

@Component
public class BillQueryBuilder {
	
	public final String INSERT_BILL_QUERY = "INSERT into egbs_bill "
			+"(id,tenantid,payeename,payeeaddress,payeeemail,isactive,iscancelled,createdby,createddate,lastmodifiedby,lastmodifieddate)"
			+"values(?,?,?,?,?,?,?,?,?,?,?)";
	
	public final String INSERT_BILLDETAILS_QUERY = "INSERT into egbs_billdetail "
			+"(id,tenantid,billid,businessservice,billno,billdate,consumercode,consumertype,billdescription,displaymessage,"
			+ "minimumamount,totalamount,callbackforapportioning,partpaymentallowed,collectionmodesnotallowed,"
			+ "createdby,createddate,lastmodifiedby,lastmodifieddate)"
			+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public final String INSERT_BILLACCOUNTDETAILS_QUERY = "INSERT into egbs_billaccountdetail "
			+"(id,tenantid,billdetailid,glcode,orderno,accountdescription,creditamount,debitamount,isactualdemand,purpose,"
			+ "createdby,createddate,lastmodifiedby,lastmodifieddate)"
			+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

}
