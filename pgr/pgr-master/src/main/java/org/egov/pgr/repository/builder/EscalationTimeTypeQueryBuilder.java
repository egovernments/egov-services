package org.egov.pgr.repository.builder;

import org.springframework.stereotype.Component;

@Component
public class EscalationTimeTypeQueryBuilder {
	
	public String insertEscalationTimeType(){
		return "INSERT INTO egpgr_escalation(complaint_type_id, no_of_hrs, designation_id, tenantid, createdby"
				+ ", lastmodifiedby, createddate, lastmodifieddate) VALUES(?,?,?,?,?,?,?,?)";
	}
	
	public String updateEscalationTimeType(){
		return "UPDATE egpgr_escalation set complaint_type_id = ?, no_of_hrs = ?, designation_id = ?, tenantid = ?, "
				+ "createdby = ?, lastmodifiedby = ?, createddate = ?, lastmodifieddate = ? where id = ?";
	}

}