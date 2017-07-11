package org.egov.demand.repository.querybuilder;

import java.util.List;

import org.egov.demand.model.BillSearchCriteria;
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
			+"(id,tenantid,billdetail,glcode,orderno,accountdescription,cramounttobepaid,creditamount,debitamount,isactualdemand,purpose,"
			+ "createdby,createddate,lastmodifiedby,lastmodifieddate)"
			+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String BILL_BASE_QUERY = ""; 
	
	
	public String getBillQuery(BillSearchCriteria billSearchCriteria,List<Object> preparedStatementValues){
		
		StringBuilder billQuery = new StringBuilder(BILL_BASE_QUERY);
		
		billQuery.append("bill.tenantid=?");
		preparedStatementValues.add(billSearchCriteria.getTenantId());
		
		
		
		return billQuery.toString();
	}

}
