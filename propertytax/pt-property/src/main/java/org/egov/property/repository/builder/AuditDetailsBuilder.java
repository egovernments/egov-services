package org.egov.property.repository.builder;

public class AuditDetailsBuilder {

	public static String getCreatedAuditDetails(String tableName, Long id) {
		return "select createdby, createdtime from " + tableName + "  where id=" + id;
	}
}
