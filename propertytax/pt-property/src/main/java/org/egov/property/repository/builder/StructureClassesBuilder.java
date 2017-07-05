package org.egov.property.repository.builder;

/**
 * @author Yosadhara This Class contains INSERT, UPDATE and SELECT queries for
 *         Structureclasses API's
 */
public class StructureClassesBuilder {

	public static final String INSERT_STRUCTURECLASSES_QUERY = "INSERT INTO egpt_mstr_structureclass"
			+ " (tenantId,code,data,createdBy, lastModifiedBy, createdTime,lastModifiedTime)"
			+ " VALUES(?,?,?,?,?,?,?)";

	public static final String UPDATE_STRUCTURECLASSES_QUERY = "UPDATE egpt_mstr_structureclass"
			+ " SET tenantId = ?, code = ?, data = ?," + " lastModifiedBy = ?, lastModifiedTime = ?" + " WHERE id = ?";

	public static final String SELECT_STRUCTURECLASSES_CREATETIME = "SELECT createdTime From egpt_mstr_structureclass WHERE id = ?";
}
