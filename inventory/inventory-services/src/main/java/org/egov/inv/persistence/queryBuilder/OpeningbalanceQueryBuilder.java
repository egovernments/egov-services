package org.egov.inv.persistence.queryBuilder;

import java.util.Set;

import org.egov.inv.model.OpeningBalanceSearchCriteria;
import org.springframework.stereotype.Component;
@Component
public class OpeningbalanceQueryBuilder {
	
	StringBuilder BASE_QUERY = new StringBuilder("SELECT matrcpt.financialyear as financialyear,"
                  +"matrcpt.receivingstore as storeName,matrcptdtl.material as materialcode,matrcpt.receiptType as materialtypename,"
                  +"matrcptdtl.uomno as uom,matrcptdtl.receivedqty as qty,matrcptdtl.unitrate as rate,"
                  +"(matrcptdtl.unitrate * matrcptdtl.receivedqty) as totalamount,matrcptdtl.remarks as remarks"
                +"FROM materialreceipt matrcpt,materialreceiptdetail matrcptdtl"
                +"WHERE matrcpt.financialyear = $financialyear"
                      +" matrcpt.receipttype ='OPENING BALANCE'"
                      +"AND matrcpt.tenantid= tenantId AND matrcpt.tenantid=matrcptdtl.tenantid AND matrcpt.mrnnumber = matrcptdtl.mrnnumber");

	
public String getQuery(OpeningBalanceSearchCriteria searchCriteria) {
		
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		buildSearchQuery(selectQuery, searchCriteria);
		addPagingClause(selectQuery, searchCriteria);
		return selectQuery.toString();
	}


public String buildSearchQuery(StringBuilder selectQuery,OpeningBalanceSearchCriteria searchCriteria) {

	
	if(!searchCriteria.isMrnNumberAbsent())
        addWhereClauseWithAnd(selectQuery,"financialYear","matrcpt.financialYear");

    if(!searchCriteria.isFinancialYearAbsent())
    	addWhereClauseWithAnd(selectQuery, "receiptNumber", "receiptNumber");

    if(!searchCriteria.ismaterialNameAbsent())
    	addWhereClauseWithAnd(selectQuery, "mrnNumber", "matrcpt.mrnNumber");
    
    if(!searchCriteria.isStoreNameAbsent())
    	addWhereClauseWithAnd(selectQuery, "supplierCode", "supplierCode");
    
    if(!searchCriteria.isMaterialcodeAbsent())
    	addWhereClauseWithAnd(selectQuery, "materialcode", "matrcptdtl.material");
    
    return selectQuery.toString();
}

	private String getOrderByQuery(Set<String> idList) {

		StringBuilder query = new StringBuilder();
		if (!idList.isEmpty()) {
			String[] list = idList.toArray(new String[idList.size()]);
			query.append(list[0]+" ASC");
			for (int i = 1; i < idList.size(); i++)
				query.append("," +list[i]+" ASC");
		}
		return query.toString();
	}
	
	private void addPagingClause(final StringBuilder selectQuery,final OpeningBalanceSearchCriteria searchCriteria) {
		if(searchCriteria.getSort() != null && !searchCriteria.getSort().isEmpty())
			selectQuery.append(" ORDER BY "+getOrderByQuery(searchCriteria.getSort()));
		else 
			selectQuery.append(" ORDER BY matrcpt.id ASC");

		long pageSize = 500 ;
		if (searchCriteria.getPageSize() != null)
			pageSize = searchCriteria.getPageSize();
		selectQuery.append(" LIMIT "+pageSize);

		// handle offset here
		long pageNumber = 0; // Default pageNo is zero meaning first page
		if (searchCriteria.getPageNumber() != null)
			pageNumber = searchCriteria.getPageNumber() - 1;
		selectQuery.append(" OFFSET "+pageNumber);
	}
	
	 private StringBuilder addWhereClauseWithAnd(StringBuilder query, String fieldName, String paramName){
	        return query.append(" AND ").append(fieldName).append("= :").append(paramName);
	    }

}
