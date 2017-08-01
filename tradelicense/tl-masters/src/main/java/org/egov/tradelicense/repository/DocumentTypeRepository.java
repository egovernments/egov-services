package org.egov.tradelicense.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.enums.ApplicationTypeEnum;
import org.egov.models.AuditDetails;
import org.egov.models.DocumentType;
import org.egov.tradelicense.repository.builder.DocumentTypeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Repository class for create/update/search DocumentType master
 * 
 * @author Shubham pratap Singh
 *
 */

@Repository
public class DocumentTypeRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Description : this method will create DocumentType in database
	 * 
	 * @param DocumentType
	 * @return documentTypeId
	 */
	public Long createDocumentType(DocumentType documentType) {

		String documentTypeInsert = DocumentTypeQueryBuilder.INSERT_DOCUMENT_TYPE_QUERY;
		AuditDetails auditDetails = documentType.getAuditDetails();
		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(documentTypeInsert, new String[] { "id" });

				ps.setString(1, documentType.getTenantId());
				ps.setString(2, documentType.getName());
				ps.setBoolean(3, documentType.getMandatory());
				ps.setBoolean(4, documentType.getEnabled());
				ps.setString(5, documentType.getApplicationType().toString());
				ps.setString(6, auditDetails.getCreatedBy());
				ps.setString(7, auditDetails.getLastModifiedBy());
				ps.setLong(8, auditDetails.getCreatedTime());
				ps.setLong(9, auditDetails.getLastModifiedTime());
				return ps;
			}
		};

		// The newly generated key will be saved in this object
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);

		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description : this method for updating DocumentType in deatabas
	 * 
	 * @param DocumentType
	 * @return DocumentType
	 */
	public DocumentType updateDocumentType(DocumentType documentType) {

		Long updatedTime = new Date().getTime();

		String socumentUpdateSql = DocumentTypeQueryBuilder.UPDATE_DOCUMENT_TYPE_QUERY;

		final PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
				final PreparedStatement ps = connection.prepareStatement(socumentUpdateSql);

				ps.setString(1, documentType.getTenantId());
				ps.setString(2, documentType.getName());
				ps.setBoolean(3, documentType.getMandatory());
				ps.setBoolean(4, documentType.getEnabled());
				ps.setString(5, documentType.getApplicationType().toString());
				ps.setString(6, documentType.getAuditDetails().getLastModifiedBy());
				ps.setLong(7, updatedTime);
				ps.setLong(8, documentType.getId());

				return ps;
			}
		};

		jdbcTemplate.update(psc);
		return documentType;
	}

	/**
	 * Description : this method for search DocumentType
	 * 
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param enabled
	 * @param applicationType
	 * @param pageSize
	 * @param offSet
	 * @return List<DocumentType>
	 */
	public List<DocumentType> searchDocumentType(String tenantId, Integer[] ids, String name, Boolean enabled,
			String applicationType, Integer pageSize, Integer offSet) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String documentTypeSearchQuery = DocumentTypeQueryBuilder.buildSearchQuery(tenantId, ids, name, enabled,
				applicationType, pageSize, offSet, preparedStatementValues);
		List<DocumentType> documentTypes = getDocumentType(documentTypeSearchQuery.toString(), preparedStatementValues);

		return documentTypes;
	}

	/**
	 * This method will execute the given query & will build the DocumentType
	 * object
	 * 
	 * @param query
	 *            String that need to be executed
	 * @return {@link DocumentType} List of DocumentType
	 */
	private List<DocumentType> getDocumentType(String query, List<Object> preparedStatementValues) {

		List<DocumentType> documentTypes = new ArrayList<DocumentType>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());

		for (Map<String, Object> row : rows) {
			DocumentType documentType = new DocumentType();
			documentType.setId(getLong(row.get("id")));
			documentType.setTenantId(getString(row.get("tenantid")));
			documentType.setName(getString(row.get("name")));
			documentType.setEnabled(getBoolean(row.get("enabled")));
			documentType.setApplicationType(ApplicationTypeEnum.fromValue(getString(row.get("applicationType"))));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			documentType.setAuditDetails(auditDetails);

			documentTypes.add(documentType);

		}

		return documentTypes;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? "" : object.toString();
	}

	/**
	 * This method will cast the given object to double
	 * 
	 * @param object
	 *            that need to be cast to Double
	 * @return {@link Double}
	 */
	@SuppressWarnings("unused")
	private Double getDouble(Object object) {
		return object == null ? 0.0 : Double.parseDouble(object.toString());
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? 0 : Long.parseLong(object.toString());
	}

	/**
	 * This method will cast the given object to Boolean
	 * 
	 * @param object
	 *            that need to be cast to Boolean
	 * @return {@link boolean}
	 */
	private Boolean getBoolean(Object object) {
		return object == null ? Boolean.FALSE : (Boolean) object;
	}
}