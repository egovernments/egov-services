package org.egov.tradelicense.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.masters.domain.enums.ApplicationTypeEnum;
import org.egov.tl.masters.domain.enums.BusinessNatureEnum;
import org.egov.tl.masters.domain.enums.FeeTypeEnum;
import org.egov.tl.masters.domain.model.FeeMatrix;
import org.egov.tl.masters.domain.model.FeeMatrixDetail;
import org.egov.tl.masters.persistence.entity.FeeMatrixDetailEntity;
import org.egov.tl.masters.persistence.entity.FeeMatrixEntity;
import org.egov.tl.masters.persistence.entity.FeeMatrixSearchEntity;
import org.egov.tl.masters.persistence.repository.FeeMatrixDetailJdbcRepository;
import org.egov.tl.masters.persistence.repository.FeeMatrixJdbcRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class FeeMatrixJdbcRepositoryTest {
		
	@Autowired
	FeeMatrixJdbcRepository feeMatrixJdbcRepository;
	
	@Autowired
	FeeMatrixDetailJdbcRepository feeMatrixDetaliJdbcRepository;
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	@Sql(scripts = { "/sql/clearFeeMatricDetails.sql","/sql/clearFeeMatrix.sql","/sql/clearUom.sql",
			"/sql/insertUom.sql","/sql/clearCategory.sql",
			"/sql/insertCategory.sql" ,"/sql/insertSubCategory.sql" })
	public void testCreate() {
		FeeMatrixEntity feeMatrixEntity = getFeeMatrixEntity(Boolean.TRUE);
		FeeMatrixEntity actualResult = feeMatrixJdbcRepository.create(feeMatrixEntity);
		assertThat(feeMatrixEntity.getTenantId()).isEqualTo(actualResult.getTenantId());
		assertThat(feeMatrixEntity.getCategoryId()).isEqualTo(actualResult.getCategoryId());
		assertThat(feeMatrixEntity.getFinancialYear()).isEqualTo(actualResult.getFinancialYear());
	}
	
	
	@Test(expected = DataIntegrityViolationException.class)
	@Sql(scripts = {"/sql/clearFeeMatrix.sql", "/sql/clearCategory.sql"})
	public void testCreateWithDataIntegrityTest() {
		FeeMatrixEntity feeMatrixEntity = getFeeMatrixEntity(Boolean.TRUE);
		FeeMatrixEntity actualResult = feeMatrixJdbcRepository.create(feeMatrixEntity);
	
	}
	
	
	@Test(expected = DataIntegrityViolationException.class)
	@Sql(scripts = { "/sql/clearFeeMatrix.sql","/sql/clearUom.sql",
			"/sql/insertUom.sql","/sql/clearCategory.sql",
			"/sql/insertCategory.sql" ,"/sql/insertSubCategory.sql","/sql/insertFeeMatrix.sql" })
	public void testCreateWithDataIntegrityWithDuplicateIdTest() {
		FeeMatrixEntity feeMatrixEntity = getFeeMatrixEntity(Boolean.TRUE);
		FeeMatrixEntity actualResult = feeMatrixJdbcRepository.create(feeMatrixEntity);
	
	}
	
	@Test
	@Sql(scripts = { "/sql/clearFeeMatrix.sql","/sql/clearUom.sql",
			"/sql/insertUom.sql","/sql/clearCategory.sql",
			"/sql/insertCategory.sql" ,"/sql/insertSubCategory.sql" })
	public void testCreateFeeMatrixWithDetails() {
		FeeMatrixEntity feeMatrixEntity = getFeeMatrixEntity(Boolean.TRUE);
		FeeMatrixEntity actualResult = feeMatrixJdbcRepository.create(feeMatrixEntity);
		assertThat(feeMatrixEntity.getTenantId()).isEqualTo(actualResult.getTenantId());
		assertThat(feeMatrixEntity.getCategoryId()).isEqualTo(actualResult.getCategoryId());
		assertThat(feeMatrixEntity.getFinancialYear()).isEqualTo(actualResult.getFinancialYear());
		FeeMatrixDetailEntity  feeMatrixDetailEntity = getFeeMatrixDetailEntity(Boolean.TRUE);
		FeeMatrixDetailEntity detailEntity = feeMatrixDetaliJdbcRepository.create(feeMatrixDetailEntity);
		assertThat(feeMatrixDetailEntity.getUomFrom()).isEqualTo(detailEntity.getUomFrom());
				
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	@Sql(scripts = { "/sql/clearFeeMatricDetails.sql","/sql/clearFeeMatrix.sql"})
	public void testCreateFeeMatrixWithoutFeeMatrix() {
		FeeMatrixDetailEntity  feeMatrixDetailEntity = getFeeMatrixDetailEntity(Boolean.TRUE);
		FeeMatrixDetailEntity detailEntity = feeMatrixDetaliJdbcRepository.create(feeMatrixDetailEntity);
		assertThat(feeMatrixDetailEntity.getUomFrom()).isEqualTo(detailEntity.getUomFrom());
				
	}
	
	
	@Test
	@Sql(scripts = { "/sql/clearFeeMatricDetails.sql","/sql/clearFeeMatrix.sql","/sql/clearUom.sql",
			"/sql/insertUom.sql","/sql/clearCategory.sql",
			"/sql/insertCategory.sql" ,"/sql/insertSubCategory.sql" })
	public void testUpdateFeeMatrix() {
		FeeMatrixEntity feeMatrixEntity = getFeeMatrixEntity(Boolean.TRUE);
		FeeMatrixEntity feeMatrixEntitytoUpdate = getFeeMatrixEntity(Boolean.TRUE);
		feeMatrixEntitytoUpdate.setApplicationType("RENEW");
		 feeMatrixJdbcRepository.create(feeMatrixEntity);
		FeeMatrixEntity actualResult = feeMatrixJdbcRepository.update(feeMatrixEntitytoUpdate);
		assertThat(feeMatrixEntitytoUpdate.getApplicationType()).isEqualTo(actualResult.getApplicationType());
	}
	
	
/*	@Test
	@Sql(scripts = { "/sql/clearFeeMatrix.sql", "/sql/insertFeeMatrix.sql" })
	public void testUpdate() {
		FeeMatrixEntity feeMatrixEntity = getFeeMatrixEntity(Boolean.FALSE);
		FeeMatrixEntity actualResult = feeMatrixJdbcRepository.update(feeMatrixEntity);
		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egtl_mstr_fee_matrix",
				new FeeMatrixResultExtractor());
		Map<String, Object> row = result.get(0);
		assertThat(row.get("tenantId")).isEqualTo(actualResult.getTenantId());
		assertThat(row.get("categoryId")).isEqualTo(actualResult.getCategoryId());
		assertThat(row.get("financialYear")).isEqualTo(actualResult.getFinancialYear());
	}
	*/
	/*@Test
	@Sql(scripts = { "/sql/clearFeeMatrix.sql", "/sql/insertFeeMatrix.sql" })
	public void testSearch() {
		List<FeeMatrixEntity> entity = feeMatrixJdbcRepository.search(getFeeMatrixSearch());
		assertThat(entity.get(0).getApplicationType()).isEqualTo("RENEW");
		assertThat(entity.get(0).getTenantId()).isEqualTo("default");
		assertThat(entity.get(0).getFinancialYear()).isEqualTo("2");
	}*/
	
	private FeeMatrixSearchEntity getFeeMatrixSearch() {
		FeeMatrixSearchEntity feeMatrixSearch = new FeeMatrixSearchEntity();
		feeMatrixSearch.setTenantId("default");
		feeMatrixSearch.setApplicationType("RENEW");
		feeMatrixSearch.setCategoryId(1l);
		feeMatrixSearch.setFinancialYear("2");
		
		return feeMatrixSearch;
	}

	private FeeMatrixEntity getFeeMatrixEntity(Boolean type) {
		FeeMatrixEntity feeMatrixEntity = new FeeMatrixEntity();
		FeeMatrix feeMatrix = getFeeMatrixDomain(type);

		feeMatrixEntity.setId(feeMatrix.getId());
		feeMatrixEntity.setTenantId(feeMatrix.getTenantId());
		feeMatrixEntity.setApplicationType(feeMatrix.getApplicationType().name());
		feeMatrixEntity.setBusinessNature(feeMatrix.getBusinessNature().name());
		feeMatrixEntity.setFeeType(feeMatrix.getFeeType().name());
		feeMatrixEntity.setFinancialYear(feeMatrix.getFinancialYear());
		feeMatrixEntity.setCategoryId(feeMatrix.getCategoryId());
		feeMatrixEntity.setSubCategoryId(feeMatrix.getSubCategoryId());
		feeMatrixEntity.setEffectiveFrom(new Timestamp(feeMatrix.getEffectiveFrom()));

		return feeMatrixEntity;
	}

	
	private FeeMatrixDetailEntity getFeeMatrixDetailEntity(Boolean type) {
		FeeMatrixEntity feeMatrixEntity = new FeeMatrixEntity();
		FeeMatrixDetail feeMatrix = getFeeMatrixDetailDomain(type);
        FeeMatrixDetailEntity entity = new FeeMatrixDetailEntity().toEntity(feeMatrix);
		return entity;
	}

	private FeeMatrix getFeeMatrixDomain(Boolean type) {
			FeeMatrix feeMatrix = new FeeMatrix();
			feeMatrix.setId(1l);
			feeMatrix.setTenantId("default");
			feeMatrix.setApplicationType(type ? ApplicationTypeEnum.NEW : ApplicationTypeEnum.RENEW);
			feeMatrix.setBusinessNature(type? BusinessNatureEnum.PERMANENT : BusinessNatureEnum.TEMPORARY);
			feeMatrix.setFeeType(FeeTypeEnum.LICENSE);
			feeMatrix.setFinancialYear("2");
			feeMatrix.setCategoryId(1l);
			feeMatrix.setSubCategoryId(2l);
			feeMatrix.setEffectiveFrom(1502628723l);

			feeMatrix.setFeeMatrixDetails(getFeeMatrixDetails(type));

			return feeMatrix;
		
	}
	
	
	private FeeMatrixDetail getFeeMatrixDetailDomain(Boolean type) {
		FeeMatrixDetail feeMatrix = new FeeMatrixDetail();
		feeMatrix.setId(1l);
		feeMatrix.setTenantId("default");
		feeMatrix.setAmount(55.44);
		feeMatrix.setUomFrom(10l);
		feeMatrix.setUomTo(20l);
		AuditDetails auditDetails = new AuditDetails("shubham", "pratap", null, null);
		feeMatrix.setAuditDetails(auditDetails);
		feeMatrix.setFeeMatrixId(1l);
	
		return feeMatrix;
	
}
	private List<FeeMatrixDetail> getFeeMatrixDetails(Boolean type) {
		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
		FeeMatrixDetail feeMatrixDetail1 = new FeeMatrixDetail();

		feeMatrixDetail1.setId(1l);
		feeMatrixDetail1.setFeeMatrixId(1l);
		feeMatrixDetail1.setAmount(type? 100.00 : 150.00);
		feeMatrixDetail1.setTenantId("Default");
		feeMatrixDetail1.setUomFrom(0l);
		feeMatrixDetail1.setUomTo(type? 10l : 15l);

		FeeMatrixDetail feeMatrixDetail2 = new FeeMatrixDetail();

		feeMatrixDetail2.setId(2l);
		feeMatrixDetail2.setFeeMatrixId(1l);
		feeMatrixDetail2.setAmount(type? 200.00 : 250.00);
		feeMatrixDetail2.setTenantId("Default");
		feeMatrixDetail2.setUomFrom(type? 10l : 15l);
		feeMatrixDetail2.setUomTo(type? 20l : 30l);

		feeMatrixDetail1.setAuditDetails(getAuditDetails());
		feeMatrixDetail2.setAuditDetails(getAuditDetails());

		feeMatrixDetails.add(feeMatrixDetail1);
		feeMatrixDetails.add(feeMatrixDetail2);
		return feeMatrixDetails;
	}
	
	private AuditDetails getAuditDetails() {

		return AuditDetails.builder().createdBy("1").createdTime(12345678912l).lastModifiedBy("1")
				.lastModifiedTime(12345678912l).build();
	}
	
	class FeeMatrixResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
		@Override
		public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			List<Map<String, Object>> rows = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> row = new HashMap<String, Object>() {
					{
						put("id", resultSet.getString("id"));
						put("tenantId", resultSet.getString("tenantId"));
						put("categoryId", resultSet.getLong("categoryId"));
						put("financialYear", resultSet.getString("financialYear"));
					}
				};

				rows.add(row);
			}
			return rows;
		}
	}

}
