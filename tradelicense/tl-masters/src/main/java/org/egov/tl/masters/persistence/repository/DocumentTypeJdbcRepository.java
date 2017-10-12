package org.egov.tl.masters.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.masters.persistence.entity.DocumentTypeEntity;
import org.egov.tradelicense.util.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentTypeJdbcRepository extends JdbcRepository {

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	static {

		init(DocumentTypeEntity.class);

	}

	public DocumentTypeEntity create(DocumentTypeEntity entity) {
		super.create(entity);

		return entity;
	}

	public DocumentTypeEntity update(DocumentTypeEntity entity) {
		super.update(entity);

		return entity;
	}

	public List<DocumentType> search(String sql, MapSqlParameterSource parameters) {

		List<DocumentTypeEntity> entities = executeSearch(sql, parameters);

		List<DocumentType> documentTypes = new ArrayList<DocumentType>();

		for (DocumentTypeEntity documentTypeEntity : entities) {
			documentTypes.add(documentTypeEntity.toDomain());
		}

		return documentTypes;
	}

	List<DocumentTypeEntity> executeSearch(String sql, MapSqlParameterSource parameters) {

		List<DocumentTypeEntity> documentTypes = new ArrayList<DocumentTypeEntity>();

		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(sql, parameters);

		for (Map<String, Object> row : rows) {
			DocumentTypeEntity entity = new DocumentTypeEntity();
			entity.setId(getLong(row.get("id")));
			entity.setTenantId(getString(row.get("tenantid")));
			entity.setName(getString(row.get("name")));
			entity.setEnabled(getBoolean(row.get("enabled")));
			entity.setMandatory(getBoolean(row.get("mandatory")));
			entity.setApplicationType(getString(row.get("applicationType")));
			entity.setCategory(getString(row.get("category")));
			entity.setSubCategory(getString(row.get("subCategory")));
			entity.setCreatedBy(getString(row.get("createdby")));
			entity.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			entity.setCreatedTime(getLong(row.get("createdtime")));
			entity.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			documentTypes.add(entity);

		}
		return documentTypes;
	}

	public Integer checkForDuplicate(DocumentType documentType, RequestInfo requestInfo) {

		String sql = null;

		MapSqlParameterSource parameters = new MapSqlParameterSource();

		sql = getUniqueTenantDocumentQuery(ConstantUtility.DOCUMENT_TYPE_TABLE_NAME, documentType.getTenantId(),
				documentType.getName(),
				(documentType.getApplicationType() != null ? documentType.getApplicationType().name() : null),
				documentType.getId(), documentType.getCategory(), documentType.getSubCategory(), parameters);

		Integer count = null;
		try {

			count = (Integer) namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);
		} catch (Exception e) {
			log.error("error while executing the query :" + sql + " parameters : " + parameters.getValues().toString()
					+ ", error, message : " + e.getMessage());
		}

		return count;

	}

	public String getCategoryName(String category) {

		String categoryName = null;

		if (category != null && !category.isEmpty()) {

			MapSqlParameterSource parameters = new MapSqlParameterSource();
			String sql = getQueryToGetCategoryName(category, parameters);
			List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(sql, parameters);
			for (Map<String, Object> row : rows) {
				categoryName = getString(row.get("name"));
			}

		}
		return categoryName;
	}

	public static String getQueryToGetCategoryName(String category, MapSqlParameterSource parameters) {

		parameters.addValue("category", category);
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT name FROM " + ConstantUtility.CATEGORY_TABLE_NAME + " WHERE code = :category");
		return builder.toString();

	}

	public boolean validateCodeExistance(String category, String tableName) {
		Boolean isExists = Boolean.TRUE;
		String query;
		int count = 0;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		query = getCodeValidationQuery(category, parameters, tableName);

		try {

			count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);

		} catch (Exception e) {
			log.error("error while executing the query :" + query + " , error message : " + e.getMessage());
		}

		if (count == 0) {
			isExists = Boolean.FALSE;
		}
		return isExists;
	}
	
	
	public boolean validateIdExistance(Long id, String tableName) {
		Boolean isExists = Boolean.TRUE;
		String query;
		int count = 0;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		query = getIdValidationQuery(id, parameters, tableName);

		try {

			count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);

		} catch (Exception e) {
			log.error("error while executing the query :" + query + " , error message : " + e.getMessage());
		}

		if (count == 0) {
			isExists = Boolean.FALSE;
		}
		return isExists;
	}

	public static String getCodeValidationQuery(String category, MapSqlParameterSource parameters, String tableName) {

		StringBuffer categoryValidationQuery = new StringBuffer("select count(*) from " + tableName);
		categoryValidationQuery.append(" where upper(code) = :code");
		parameters.addValue("code", category.toUpperCase());

		return categoryValidationQuery.toString();
	}
	
	
	public static String getIdValidationQuery(Long id, MapSqlParameterSource parameters, String tableName) {

		StringBuffer categoryValidationQuery = new StringBuffer("select count(*) from " + tableName);
		categoryValidationQuery.append(" where id = :id");
		parameters.addValue("id", id);

		return categoryValidationQuery.toString();
	}

	public static String getSubCategoryIdValidationQuery(String category, MapSqlParameterSource parameters,
			String tableName) {

		StringBuffer categoryValidationQuery = new StringBuffer("select count(*) from " + tableName);
		categoryValidationQuery.append(" where upper(code) = :code AND parent IS NOT NULL ");
		parameters.addValue("code", category.toUpperCase());

		return categoryValidationQuery.toString();
	}

	public boolean validateSubCategoryIdExistance(String subCategory) {
		Boolean isExists = Boolean.TRUE;
		String query;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		query = getSubCategoryIdValidationQuery(subCategory, parameters, ConstantUtility.CATEGORY_TABLE_NAME);
		Integer count = 0;
		try {

			count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);

		} catch (Exception e) {
			log.error("error while executing the query :" + query + " , error message : " + e.getMessage());
		}

		if (count == 0) {
			isExists = Boolean.FALSE;
		}
		return isExists;
	}

	private String getUniqueTenantDocumentQuery(String tableName, String tenantId, String name, String applicationType,
			Long id, String category, String subCategory, MapSqlParameterSource parameters) {

		StringBuffer uniqueQuery = new StringBuffer("select count(*) from " + tableName);

		uniqueQuery.append(" where LOWER(name) = :name");
		parameters.addValue("name", name.toLowerCase());

		uniqueQuery.append(" AND LOWER(tenantId) = :tenantId");
		parameters.addValue("tenantId", tenantId.toLowerCase());

		if (applicationType != null && !applicationType.isEmpty()) {
			uniqueQuery.append(" AND applicationType = :applicationType");
			parameters.addValue("applicationType", applicationType);
		} else {
			uniqueQuery.append(" AND applicationType IS NULL");
		}

		if (id != null) {
			uniqueQuery.append(" AND id != :id");
			parameters.addValue("id", id);
		} 

		if (category != null && !category.isEmpty()) {
			uniqueQuery.append(" AND category = :category");
			parameters.addValue("category", category);
		} else {
			uniqueQuery.append(" AND category IS NULL");
		}

		if (subCategory != null && !subCategory.isEmpty()) {
			uniqueQuery.append(" AND subCategory = :subCategory");
			parameters.addValue("subCategory", subCategory);
		} else {
			uniqueQuery.append(" AND subCategory IS NULL");
		}

		return uniqueQuery.toString();
	}

	public Long getNextSequence() {

		String id = getSequence(DocumentTypeEntity.SEQUENCE_NAME);
		return Long.valueOf(id);
	}

	public List<DocumentType> getDocumentTypeContracts(String tenantId, Integer[] ids, String name, String enabled,
			String mandatory, String applicationType, String category, String subCategory, Integer pageSize,
			Integer offSet, Boolean fallback) {

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = null;

		query = createQueryToGetDocumentContracts(tenantId, ids, name, enabled, mandatory, applicationType, category,
				subCategory, pageSize, offSet, fallback, parameters);

		List<DocumentType> documentTypes = search(query, parameters);
		
		if(fallback != null && fallback == Boolean.TRUE){

		if (documentTypes == null || documentTypes.size() == 0) {
			
			if (parameters.hasValue("subCategory")) {
				
				parameters = new MapSqlParameterSource();

				query = createQueryToGetDocumentContracts(tenantId, ids, name, enabled, mandatory, applicationType,
						category, null, pageSize, offSet, fallback, parameters);

				documentTypes = search(query, parameters);

			}
		}

		if (documentTypes == null || documentTypes.size() == 0) {
			
			if (parameters.hasValue("category")) {
				
				parameters = new MapSqlParameterSource();
				// TODO add mandatory also in saerch query
				query = createQueryToGetDocumentContracts(tenantId, ids,  name, enabled, mandatory, applicationType,
						null, null, pageSize, offSet, fallback, parameters);

				documentTypes = search(query, parameters);

			}
		}

		if (documentTypes == null || documentTypes.size() == 0) {
			
			if (parameters.hasValue("applicationType")) {
				
				parameters = new MapSqlParameterSource();

				query = createQueryToGetDocumentContracts(tenantId, ids, name, enabled, mandatory, null, null, null,
						pageSize, offSet, fallback, parameters);

				documentTypes = search(query, parameters);

			}
		}
		}

		return documentTypes;

	}

	private String createQueryToGetDocumentContracts(String tenantId, Integer[] ids, String name, String enabled,
			String mandatory, String applicationType, String category, String subCategory, Integer pageSize,
			Integer offSet, Boolean fallback, MapSqlParameterSource parameters) {

		StringBuilder sql = new StringBuilder("SELECT * FROM ");
		sql.append(ConstantUtility.DOCUMENT_TYPE_TABLE_NAME);
		sql.append(" WHERE tenantId= :tenantId");
		parameters.addValue("tenantId", tenantId);

		if (name != null && !name.isEmpty()) {

			sql.append(" AND upper(name) like :name");
			parameters.addValue("name", "%" + name.toUpperCase() + "%");
		}

		if (ids != null && ids.length > 0) {

			String searchIds = "";
			int count = 1;
			for (Integer id : ids) {

				if (count < ids.length)
					searchIds = searchIds + id + ",";
				else
					searchIds = searchIds + id;

				count++;
			}
			sql.append(" AND id IN (" + searchIds + ") ");
		}

		if (subCategory != null && !subCategory.isEmpty()) {
			
			sql.append(" AND upper(subCategory)= :subCategory");
			parameters.addValue("subCategory", subCategory.toUpperCase());
			
		} else if(fallback != null && fallback == Boolean.TRUE){
			
			sql.append(" AND subCategory IS NULL");
		}
		
		if (category != null && !category.isEmpty()) {

			sql.append(" AND upper(category) = :category");
			parameters.addValue("category", category.toUpperCase());
			
		} else if(fallback != null && fallback == Boolean.TRUE){
			
			sql.append(" AND category IS NULL");
		}

		if (applicationType != null && !applicationType.isEmpty()) {

			sql.append(" AND lower(applicationType) = :applicationType");
			parameters.addValue("applicationType", applicationType.toLowerCase());
			
		} else if(fallback != null && fallback == Boolean.TRUE){
			
			sql.append(" AND applicationType IS NULL");
		}

		if (enabled != null && !enabled.isEmpty()) {
			if (enabled.equalsIgnoreCase("false")) {

				sql.append(" AND enabled= :enabled");
				parameters.addValue("enabled", false);

			} else if (enabled.equalsIgnoreCase("true")) {
				sql.append(" AND enabled= :enabled");
				parameters.addValue("enabled", true);
			}
		}

		if (mandatory != null && !mandatory.isEmpty()) {
			if (mandatory.equalsIgnoreCase("false")) {

				sql.append(" AND mandatory= :mandatory");
				parameters.addValue("mandatory", false);

			} else if (mandatory.equalsIgnoreCase("true")) {
				sql.append(" AND mandatory= :mandatory");
				parameters.addValue("mandatory", true);
			}
		}

		if (pageSize != null) {
			sql.append(" limit :limit ");
			parameters.addValue("limit", pageSize);
		}

		if (offSet != null) {
			sql.append(" offset :offset ");
			parameters.addValue("offset", offSet);
		}

		return sql.toString();
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