package org.egov.tradelicense.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.persistence.repository.builder.DocumentTypeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	/**
	 * Description : this method will create DocumentType in database
	 * 
	 * @param DocumentType
	 * @return documentTypeId
	 */
	public Long createDocumentType(DocumentType documentType) {

		String documentTypeInsert = DocumentTypeQueryBuilder.INSERT_DOCUMENT_TYPE_QUERY;
		AuditDetails auditDetails = documentType.getAuditDetails();
		
		final KeyHolder holder = new GeneratedKeyHolder();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("tenantId", documentType.getTenantId());
		parameters.addValue("name", documentType.getName());
		parameters.addValue("mandatory", documentType.getMandatory());
		parameters.addValue("enabled", documentType.getEnabled());
		parameters.addValue("applicationType",
				documentType.getApplicationType() == null ? null : documentType.getApplicationType().toString());
		parameters.addValue("createdBy", auditDetails.getCreatedBy());
		parameters.addValue("lastModifiedBy", auditDetails.getLastModifiedBy());
		parameters.addValue("createdTime", auditDetails.getCreatedTime());
		parameters.addValue("lastModifiedTime", auditDetails.getLastModifiedTime());
		// executing the insert query
		namedParameterJdbcTemplate.update(documentTypeInsert, parameters, holder, new String[] { "id" });

		return Long.valueOf(holder.getKey().intValue());
	}

	/**
	 * Description : this method for updating DocumentType in deatabase
	 * 
	 * @param DocumentType
	 * @return DocumentType
	 */
	public DocumentType updateDocumentType(DocumentType documentType) {

		AuditDetails auditDetails = documentType.getAuditDetails();
		String documentTypeUpdateSql = DocumentTypeQueryBuilder.UPDATE_DOCUMENT_TYPE_QUERY;
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("tenantId", documentType.getTenantId());
		parameters.addValue("name", documentType.getName());
		parameters.addValue("mandatory", documentType.getMandatory());
		parameters.addValue("enabled", documentType.getEnabled());
		parameters.addValue("applicationType",
				documentType.getApplicationType() == null ? null : documentType.getApplicationType().toString());
		parameters.addValue("lastModifiedBy", auditDetails.getLastModifiedBy());
		parameters.addValue("lastModifiedTime", auditDetails.getLastModifiedTime());
		parameters.addValue("id", documentType.getId());

		namedParameterJdbcTemplate.update(documentTypeUpdateSql, parameters);

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
	public List<DocumentType> searchDocumentType(String tenantId, Integer[] ids, String name, String enabled,
			String applicationType, Integer pageSize, Integer offSet) {

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		if (pageSize == null) {
			pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		}
		if (offSet == null) {
			offSet = Integer.valueOf(propertiesManager.getDefaultOffset());
		}
		String documentTypeSearchQuery = DocumentTypeQueryBuilder.buildSearchQuery(tenantId, ids, name, enabled,
				applicationType, pageSize, offSet, parameters);
		List<DocumentType> documentTypes = getDocumentType(documentTypeSearchQuery.toString(), parameters);

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
	private List<DocumentType> getDocumentType(String query, MapSqlParameterSource parameters) {

		List<DocumentType> documentTypes = new ArrayList<DocumentType>();
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, parameters);

		for (Map<String, Object> row : rows) {
			DocumentType documentType = new DocumentType();
			documentType.setId(getLong(row.get("id")));
			documentType.setTenantId(getString(row.get("tenantid")));
			documentType.setName(getString(row.get("name")));
			documentType.setEnabled(getBoolean(row.get("enabled")));
			documentType.setMandatory(getBoolean(row.get("mandatory")));
			if(row.get("applicationType") != null){
				documentType.setApplicationType(ApplicationTypeEnum.fromValue(getString(row.get("applicationType"))));
			} else {
				documentType.setApplicationType(null);
			}		
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
		return object == null ? null : object.toString();
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
		return object == null ? null : Double.parseDouble(object.toString());
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
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