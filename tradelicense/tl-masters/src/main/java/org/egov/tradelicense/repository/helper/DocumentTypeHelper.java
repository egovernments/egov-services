package org.egov.tradelicense.repository.helper;

import org.egov.models.DocumentType;
import org.egov.tradelicense.repository.builder.UtilityBuilder;
import org.egov.tradelicense.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DocumentTypeHelper {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * This will check whether any record exists with the given tenantId , name
	 * & applicationName in database or not
	 * 
	 * @param tenantId
	 * @param name
	 * @param tableName
	 * @param id
	 * @param applicationName
	 * @return True / false if record exists / record does n't exists
	 */
	public Boolean checkWhetherRecordExitswithName(String tenantId, String name, String tableName, Long id,
			String applicationName) {

		Boolean isExists = Boolean.TRUE;

		String query = UtilityBuilder.getUniqueTenantCodeQuerywithName(tableName, name, tenantId, applicationName, id);

		int count = 0;

		try {

			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		if (count == 0)
			isExists = Boolean.FALSE;

		return isExists;

	}

	/**
	 * This will check whether any duplicate DocumentType record exists in
	 * database or not
	 * 
	 * @param tenantId
	 * @param name
	 * @param tableName
	 * @param id
	 * @param applicationName
	 * @return True / false if record exists / record does n't exists
	 */
	public Boolean checkWhetherDocumentTypeExists(DocumentType documentType) {

		Boolean isExists = Boolean.FALSE;
		String tableName = ConstantUtility.DOCUMENT_TYPE_TABLE_NAME;
		String name = documentType.getName();
		String tantentId = documentType.getTenantId();
		String applicationName = documentType.getApplicationType().toString();
		String query = UtilityBuilder.getDocumentTypeValidationQuery(tantentId, name, applicationName, tableName);
		int count = 0;

		try {

			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		if (count > 0)
			isExists = Boolean.TRUE;

		return isExists;
	}
}