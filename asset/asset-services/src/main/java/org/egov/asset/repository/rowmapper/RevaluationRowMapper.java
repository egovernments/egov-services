package org.egov.asset.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.enums.RevaluationStatus;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class RevaluationRowMapper implements RowMapper<Revaluation>{

	@Override
	public Revaluation mapRow(ResultSet rs, int rowNum) throws SQLException {
		Revaluation revaluation = new Revaluation();
		try{
			revaluation.setId((Long) rs.getObject("id"));
			revaluation.setTenantId(rs.getString("tenantid"));
			revaluation.setAssetId((Long) rs.getObject("assetid"));
			revaluation.setCurrentCapitalizedValue((Double)rs.getDouble("currentcapitalizedvalue"));
			revaluation.setTypeOfChange(TypeOfChangeEnum.fromValue(rs.getString("typeofchange")));
			revaluation.setRevaluationAmount((Double)rs.getDouble("revaluationamount"));
			revaluation.setValueAfterRevaluation((Double)rs.getDouble("valueafterrevaluation"));
			revaluation.setRevaluationDate((Long) rs.getLong("revaluationdate"));
			revaluation.setReevaluatedBy(rs.getString("reevaluatedby"));
			revaluation.setReasonForRevaluation(rs.getString("reasonforrevaluation"));
			revaluation.setFixedAssetsWrittenOffAccount((Long)rs.getLong("fixedassetswrittenoffaccount"));
			revaluation.setFunction((Long)rs.getLong("function"));
			revaluation.setFund((Long)rs.getLong("fund"));
			revaluation.setScheme((Long)rs.getLong("scheme"));
			revaluation.setSubScheme((Long)rs.getLong("subscheme"));
			revaluation.setComments(rs.getString("comments"));
			revaluation.setStatus(RevaluationStatus.fromValue(rs.getString("status")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(rs.getString("createdby"));
			auditDetails.setCreatedDate((Long)rs.getLong("createddate"));
			auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
			auditDetails.setLastModifiedDate((Long)rs.getLong("lastmodifieddate"));
			revaluation.setAuditDetails(auditDetails);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return revaluation;
	}

}
