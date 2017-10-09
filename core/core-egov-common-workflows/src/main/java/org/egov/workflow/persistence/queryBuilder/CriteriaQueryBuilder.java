/*package org.egov.workflow.persistence.queryBuilder;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.workflow.persistence.entity.WorkFlowMatrixSearchCriteria;
import org.springframework.stereotype.Component;

@Component
public class CriteriaQueryBuilder {

	StringBuilder query = new StringBuilder("SELECT * FROM EG_WF_MATRIX WHERE tenantid = :tenantid");
	
	
	public String buildSearchQuery(WorkFlowMatrixSearchCriteria workFlowMatrixSearchCriteria)
	{
		
		if(workFlowMatrixSearchCriteria.getDate() != null )
		{
		String crit1 = addWhereClauseWithLE(query, "fromDate",
				workFlowMatrixSearchCriteria.getDate() == null ? new Date() : workFlowMatrixSearchCriteria.getDate())
						.toString();
		String crit2 = addWhereClauseWithGE(query, "toDate",
				workFlowMatrixSearchCriteria.getDate() == null ? new Date() : workFlowMatrixSearchCriteria.getDate())
						.toString();
		String crit3 = addWhereClauseWithAND(query, crit1, crit2).toString();
				addWhereClauseWithOR(query, crit1, crit3).toString();
		
		}
		
		if(workFlowMatrixSearchCriteria.getDesignation() != null  && !"".equals(workFlowMatrixSearchCriteria.getDesignation().trim()))
		{
			addWhereClauseWithILike(query,"currentDesignation", workFlowMatrixSearchCriteria.getDesignation()).toString();
		}
		
		if(workFlowMatrixSearchCriteria.getDepartment() != null && !"".equals(workFlowMatrixSearchCriteria.getDepartment().trim()))
		{
			addWhereClauseWithAnd(query, "department", workFlowMatrixSearchCriteria.getDepartment()).toString();
		}
		else
		{
			addWhereClauseWithAnd(query, "department", "ANY").toString();

		}
		
		if (workFlowMatrixSearchCriteria.getAdditionalRule() != null && !"".equals(workFlowMatrixSearchCriteria.getAdditionalRule().trim()))
		{
			addWhereClauseWithAnd(query, "additionalRule", workFlowMatrixSearchCriteria.getAdditionalRule()).toString();
		}

		if (workFlowMatrixSearchCriteria.getType() != null && !"".equals(workFlowMatrixSearchCriteria.getType().trim()))
		{
			addWhereClauseWithAnd(query, "objectType", workFlowMatrixSearchCriteria.getType());
		}
		
		if (workFlowMatrixSearchCriteria.getAmountRule() != null && BigDecimal.ZERO.compareTo(workFlowMatrixSearchCriteria.getAmountRule()) != 0)
		{
			String crit1 = addWhereClauseWithLE(query, "fromQty", workFlowMatrixSearchCriteria.getAmountRule()).toString();
			String crit2 = addWhereClauseWithGEs(query, "toQty", workFlowMatrixSearchCriteria.getAmountRule()).toString();
			String amount1 = addWhereClauseWithAND(query, crit1, crit2).toString();
			String crit3 = addWhereClauseWitIsNull(query, "toQty").toString();
			String amount2 = addWhereClauseWithAND(query, crit1, crit3).toString();
			addWhereClauseWithOR(query, amount1, amount2);
			
		}
			
		if (workFlowMatrixSearchCriteria.getCurrentState() != null && !"".equals(workFlowMatrixSearchCriteria.getCurrentState().trim()))
		{
			addWhereClauseWithAnd(query, "nextState", workFlowMatrixSearchCriteria.getCurrentState());
		}
		
		if (workFlowMatrixSearchCriteria.getPendingActions() != null && !"".equals(workFlowMatrixSearchCriteria.getPendingActions().trim()))
		{
			addWhereClauseWithAnd(query, "nextAction", workFlowMatrixSearchCriteria.getPendingActions());
		}
		
		return query.toString();
	}
	
	
	public String actionQuery(WorkFlowMatrixSearchCriteria workFlowMatrixSearchCriteria)
	{
		if (workFlowMatrixSearchCriteria.getType() != null && !"".equals(workFlowMatrixSearchCriteria.getType().trim()))
		{
			addWhereClauseWithAnd(query, "objectType", workFlowMatrixSearchCriteria.getType()).toString();
		}
		
		if (workFlowMatrixSearchCriteria.getAdditionalRule() != null && !"".equals(workFlowMatrixSearchCriteria.getAdditionalRule().trim()))
		{
			addWhereClauseWithAnd(query, "additionalRule", workFlowMatrixSearchCriteria.getAdditionalRule()).toString();
		}
		
		if (workFlowMatrixSearchCriteria.getCurrentState() != null && !"".equals(workFlowMatrixSearchCriteria.getCurrentState().trim()))
		{
			addWhereClauseWithILike(query, "currentState", workFlowMatrixSearchCriteria.getAdditionalRule()).toString();
		}
		else
		{
			addWhereClauseWithILike(query, "currentState", "NEW").toString();
		}
		
		if (workFlowMatrixSearchCriteria.getPendingActions() != null && !"".equals(workFlowMatrixSearchCriteria.getPendingActions().trim()))
		{
			addWhereClauseWithILike(query, "currentState", workFlowMatrixSearchCriteria.getAdditionalRule()).toString();
		}
		return query.toString();

	}
	

	public String addCommonWfQuery(WorkFlowMatrixSearchCriteria workFlowMatrixSearchCriteria) {
		String fromDate = addWhereClauseWithLE(query, "fromDate",
				workFlowMatrixSearchCriteria.getDate() == null ? new Date() : workFlowMatrixSearchCriteria.getDate())
						.toString();
		String toDate = addWhereClauseWithGE(query, "toDate",
				workFlowMatrixSearchCriteria.getDate() == null ? new Date() : workFlowMatrixSearchCriteria.getDate())
						.toString();
		return addWhereClauseWithAnd(query, fromDate, toDate).toString();

	}

	private StringBuilder addWhereClauseWithAnd(StringBuilder query, String fieldName, String paramName) {
		return query.append(" AND ").append(fieldName).append("= :").append(paramName);
	}
	

	private StringBuilder addWhereClauseWithILike(StringBuilder query, String fieldName, String paramName) {
		return query.append(" AND ").append(fieldName).append("ilike '%").append(paramName).append("%'");
	}

	private StringBuilder addWhereClauseWithGE(StringBuilder query, String fieldName, Date paramName) {
		return query.append(" AND ").append(fieldName).append(">=").append(paramName);
	}
	
	private StringBuilder addWhereClauseWithGEs(StringBuilder query, String fieldName, BigDecimal paramName) {
		return query.append(" AND ").append(fieldName).append(">=").append(paramName);
	}
	
	private StringBuilder addWhereClauseWithLE(StringBuilder query, String fieldName, Date paramName) {
		return query.append(" AND ").append(fieldName).append("<=").append(paramName);
	}
	

	private StringBuilder addWhereClauseWithLE(StringBuilder query, String fieldName, BigDecimal paramName) {
		return query.append(" AND ").append(fieldName).append("<=").append(paramName);
	}

	private StringBuilder addWhereClauseWithAND(StringBuilder query, String fieldName1, String fieldName2) {
		return query.append(fieldName1).append(" AND ").append(fieldName2);
	}
	
	private StringBuilder addWhereClauseWithOR(StringBuilder query, String fieldName1, String fieldName2) {
		return query.append(fieldName1).append(" OR ").append(fieldName2);
	}
	
	private StringBuilder addWhereClauseWitIsNull(StringBuilder query, String paramName) {
		return query.append("AND").append(paramName).append("IS NULL");
	}

}

*/