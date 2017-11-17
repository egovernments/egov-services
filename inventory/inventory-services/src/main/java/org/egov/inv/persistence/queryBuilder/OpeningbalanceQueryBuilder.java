package org.egov.inv.persistence.queryBuilder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.inv.model.Material;
import org.egov.inv.model.OpeningBalanceSearchCriteria;
import org.egov.inv.persistence.entity.MaterialEntity;
import org.egov.inv.persistence.entity.OpeningBalanceEntity;
import org.springframework.stereotype.Component;

@Component
public class OpeningbalanceQueryBuilder {
	
	StringBuilder BASE_QUERY = new StringBuilder("SELECT matrcpt.financialyear as financialyear,"
													+"matrcpt.receivingstore as storeName,"
													+"matrcptdtl.material as materialcode,"
													+"matrcpt.receiptType as materialTypeName,"
													+"matrcpt.receiptDate as receiptDate,"
													+"matrcptdtl.uomno as uom,"
													+"matrcptdtl.receivedqty as receivedQty,"
													+"matrcpt.mrnNumber as mrnNumber,"
													+"matrcptdtl.receivedqty as qty,"
													+"matrcptdtl.unitrate as unitRate,"
													+"(matrcptdtl.unitrate * matrcptdtl.receivedqty) as totalamount,"
													+"matrcptdtl.remarks as remarks FROM "
													+"materialreceipt matrcpt,materialreceiptdetail matrcptdtl WHERE "
													+"matrcpt.tenantId= matrcptdtl.tenantId AND "
													+"matrcpt.mrnnumber = matrcptdtl.mrnnumber ");
	
public String getQuery(OpeningBalanceSearchCriteria searchCriteria) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		
		System.out.println(selectQuery);
		buildSearchQuery(selectQuery, searchCriteria);
		addPagingClause(selectQuery, searchCriteria);

		return selectQuery.toString();
	}


public String buildSearchQuery(StringBuilder selectQuery,OpeningBalanceSearchCriteria searchCriteria) {

    if(!searchCriteria.isFinancialYearAbsent())
    	addWhereClauseWithAnd(selectQuery, "matrcpt.financialYear", "financialYear");

    if(!searchCriteria.isMaterialTypeNameAbsent())
    	addWhereClauseWithAnd(selectQuery, "matrcpt.receiptType", "materialTypeName");
    
    if(!searchCriteria.isStoreNameAbsent())
    	addWhereClauseWithAnd(selectQuery, "matrcpt.receivingstore", "storeName");
    
    if(!searchCriteria.ismaterialNameAbsent())
    	addWhereClauseWithAnd(selectQuery, "mat.materialName", "materialName");
    
    addWhereClauseWithAnd(selectQuery, "matrcptdtl.tenantId", "tenantId");
       
		System.out.println(selectQuery);
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
