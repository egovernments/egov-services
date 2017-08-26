/*package org.egov.tradelicense.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.masters.persistence.entity.DocumentTypeEntity;
import org.egov.tl.masters.persistence.repository.DocumentTypeJdbcRepository;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TradeLicenseApplication.class)
public class DocumentTypeJdbcRepositoryTest {

	@Autowired
	private DocumentTypeJdbcRepository documentTypeJdbcRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	



	@Before
	public void setUp() throws Exception {
	}

	
	@Test
	@Sql(scripts = { "/sql/clearDocumentType.sql" })
	public void testCreate() {
		DocumentTypeEntity documentTypeEntity = getDocumentTypeEntity();
		DocumentTypeEntity actualResult = documentTypeJdbcRepository.create(documentTypeEntity);
		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egtl_mstr_document_type",
				new DocumentTypeResultExtractor());
		Map<String, Object> row = result.get(0);
		assertThat(row.get("tenantId")).isEqualTo(documentTypeEntity.getTenantId());
		assertThat(row.get("enabled")).isEqualTo(documentTypeEntity.getEnabled());
		assertThat(row.get("applicationType")).isEqualTo(documentTypeEntity.getApplicationType());
	}

	
	
	@Test(expected = DataIntegrityViolationException.class)
	@Sql(scripts = { "/sql/clearDocumentType.sql" })
	public void test_create_with_tenantId_null() {

			DocumentTypeEntity documentTypeEntity = getDocumentTypeEntity();
			documentTypeEntity.setTenantId(null);
			DocumentTypeEntity actualResult = documentTypeJdbcRepository.create(documentTypeEntity);
		
		}

	
	@Test
	@Sql(scripts = { "/sql/clearDocumentType.sql" })
	 @Sql(scripts = { "/sql/insertDocumentType.sql" })
	public void testUpdate() {
		DocumentTypeEntity documentTypeEntity = getDocumentTypeEntityToUpadte();
		
		DocumentTypeEntity actualResult = documentTypeJdbcRepository.update(documentTypeEntity);
		
		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egtl_mstr_document_type",
				new DocumentTypeResultExtractor());
		
		Map<String, Object> row = result.get(0);
		assertThat(row.get("tenantId")).isEqualTo(documentTypeEntity.getTenantId());
		assertThat(row.get("enabled")).isEqualTo(documentTypeEntity.getEnabled());
		assertThat(row.get("applicationType")).isEqualTo(documentTypeEntity.getApplicationType());
	}
	
	
	@Test(expected = DuplicateKeyException.class)
	@Sql(scripts = { "/sql/clearDocumentType.sql" })
	public void test_create_Duplicate_ConstraintViolation() {

			DocumentTypeEntity documentTypeEntity = getDocumentTypeEntity();
			
			 documentTypeJdbcRepository.create(documentTypeEntity);
			 documentTypeEntity.setId(6l);
			 documentTypeJdbcRepository.create(documentTypeEntity);
			
		
		}

	@Test
	@Sql(scripts = { "/sql/clearDocumentType.sql" })
	public void testsearch() {
		DocumentTypeEntity documentTypeEntity = getDocumentTypeEntityToUpadte();
		
			
		DocumentTypeEntity actualResult = documentTypeJdbcRepository.create(documentTypeEntity);
		
		List<DocumentType> result = documentTypeJdbcRepository.getDocumentTypeContracts(documentTypeEntity.getTenantId(), null,
				documentTypeEntity.getName(), String.valueOf(documentTypeEntity.getEnabled()),
				null, null, null, Integer.valueOf(String.valueOf(documentTypeEntity.getSubCategoryId())),null,null);
		
	  DocumentType entity = (DocumentType)result.get(0);
		assertThat(entity.getTenantId().equalsIgnoreCase((documentTypeEntity.getTenantId())));
		assertThat(entity.getEnabled() == documentTypeEntity.getEnabled());
		assertThat(entity.getApplicationType().name().equalsIgnoreCase(documentTypeEntity.getApplicationType()));
	}
	
	@Test
	@Sql(scripts = { "/sql/clearDocumentType.sql" })
	public void testsearchwithCategoryId() {
		DocumentTypeEntity documentTypeEntity = getDocumentTypeEntityToUpadte();
		
		
		DocumentTypeEntity actualResult = documentTypeJdbcRepository.create(documentTypeEntity);
		
		List<DocumentType> result = documentTypeJdbcRepository.getDocumentTypeContracts(documentTypeEntity.getTenantId(), null,
				documentTypeEntity.getName(), String.valueOf(documentTypeEntity.getEnabled()),
				null, null, Integer.valueOf(String.valueOf(documentTypeEntity.getCategoryId())), null,null,null);
		
	  DocumentType entity = (DocumentType)result.get(0);
		assertThat(entity.getTenantId().equalsIgnoreCase((documentTypeEntity.getTenantId())));
		assertThat(entity.getEnabled() == documentTypeEntity.getEnabled());
		assertThat(entity.getApplicationType().name().equalsIgnoreCase(documentTypeEntity.getApplicationType()));
	}
	



	private DocumentTypeEntity getDocumentTypeEntity() {
		DocumentTypeEntity documentTypeEntity = new DocumentTypeEntity();
		documentTypeEntity.setTenantId("default");
		documentTypeEntity.setName("Environment");
		documentTypeEntity.setApplicationType("NEW");
		documentTypeEntity.setCategoryId(1l);
		documentTypeEntity.setSubCategoryId(2l);
		documentTypeEntity.setEnabled(true);
		documentTypeEntity.setMandatory(false);
		documentTypeEntity.setCreatedBy("shubham");
		documentTypeEntity.setLastModifiedBy("nitin");
		documentTypeEntity.setId(5l);

		return documentTypeEntity;
	}
	
	
	private DocumentTypeEntity getDocumentTypeEntityToUpadte() {
		DocumentTypeEntity documentTypeEntity = new DocumentTypeEntity();
		documentTypeEntity.setTenantId("default");
		documentTypeEntity.setName("Environment");
		documentTypeEntity.setApplicationType("NEW");
		documentTypeEntity.setCategoryId(1l);
		documentTypeEntity.setSubCategoryId(2l);
		documentTypeEntity.setEnabled(true);
		documentTypeEntity.setMandatory(false);
		documentTypeEntity.setCreatedBy("shubham");
		documentTypeEntity.setLastModifiedBy("nitin");
		documentTypeEntity.setId(5l);

		return documentTypeEntity;
	}



	class DocumentTypeResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
		@Override
		public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			List<Map<String, Object>> rows = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> row = new HashMap<String, Object>() {
					{
						put("id", resultSet.getString("id"));
						put("tenantId", resultSet.getString("tenantId"));
						put("enabled", resultSet.getBoolean("enabled"));
						put("applicationType", resultSet.getString("applicationType"));
					}
				};

				rows.add(row);
			}
			return rows;
		}
	}
}*/