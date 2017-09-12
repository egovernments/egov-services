package org.egov.tradelicense.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tradelicense.common.util.TimeStampUtil;
import org.egov.tradelicense.domain.enums.ApplicationType;
import org.egov.tradelicense.domain.enums.BusinessNature;
import org.egov.tradelicense.domain.enums.OwnerShipType;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.persistence.entity.TradeLicenseEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TradeLicenseJdbcRepositoryTest {

	@Autowired
	private TradeLicenseJdbcRepository tradeLicenseJdbcRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	@Sql(scripts = { "/sql/clearLicense.sql" })
	public void testCreate() {
		TradeLicenseEntity tradeLicenseEntity = getTradeLicenseEntity();
		TradeLicenseEntity actualResult = tradeLicenseJdbcRepository.create(tradeLicenseEntity);
		List<Map<String, Object>> result = namedParameterJdbcTemplate.query("SELECT * FROM egtl_license",
				new TradeLicenseResultExtractor());
		Map<String, Object> row = result.get(0);
		assertThat(row.get("tenantId")).isEqualTo(actualResult.getTenantId());
		assertThat(row.get("active")).isEqualTo(actualResult.getActive());
		assertThat(row.get("oldLicenseNumber")).isEqualTo(actualResult.getOldLicenseNumber());
	}

	private TradeLicenseEntity getTradeLicenseEntity() {
		TradeLicenseEntity tradeLicenseEntity = new TradeLicenseEntity();
		TradeLicense tradeLicense = getTradeLicenseDomain();
		tradeLicenseEntity.setActive(tradeLicense.getActive());
		tradeLicenseEntity.setAdhaarNumber(tradeLicense.getAdhaarNumber());
		tradeLicenseEntity.setAdminWardId(tradeLicense.getAdminWardId());
		tradeLicenseEntity.setApplicationDate(new Timestamp(tradeLicense.getApplicationDate()));
		tradeLicenseEntity.setApplicationNumber(tradeLicense.getApplicationNumber());
		tradeLicenseEntity.setAgreementNo(tradeLicense.getAgreementNo());
		tradeLicenseEntity.setApplicationType(tradeLicense.getApplicationType().toString());
		tradeLicenseEntity.setCategoryId(tradeLicense.getCategoryId());
		tradeLicenseEntity.setEmailId(tradeLicense.getEmailId());
		tradeLicenseEntity.setExpiryDate(new Timestamp(tradeLicense.getExpiryDate()));
		tradeLicenseEntity.setFatherSpouseName(tradeLicense.getFatherSpouseName());
		tradeLicenseEntity.setId(tradeLicense.getId());
		tradeLicenseEntity.setIsLegacy(tradeLicense.getIsLegacy());
		tradeLicenseEntity.setLicenseNumber(tradeLicense.getLicenseNumber());
		tradeLicenseEntity.setLicenseValidFromDate(new Timestamp(tradeLicense.getLicenseValidFromDate()));
		tradeLicenseEntity.setLocalityId(tradeLicense.getLocalityId());
		tradeLicenseEntity.setMobileNumber(tradeLicense.getMobileNumber());
		tradeLicenseEntity.setOldLicenseNumber(tradeLicense.getOldLicenseNumber());
		tradeLicenseEntity.setOwnerAddress(tradeLicense.getOwnerAddress());
		tradeLicenseEntity.setOwnerName(tradeLicense.getOwnerName());
		tradeLicenseEntity.setOwnerShipType(tradeLicense.getOwnerShipType().toString());
		tradeLicenseEntity.setPropertyAssesmentNo(tradeLicense.getPropertyAssesmentNo());
		tradeLicenseEntity.setQuantity(tradeLicense.getQuantity());
		tradeLicenseEntity.setValidityYears(tradeLicense.getValidityYears());
		tradeLicenseEntity.setRemarks(tradeLicense.getRemarks());
		tradeLicenseEntity.setRevenueWardId(tradeLicense.getRevenueWardId());
		tradeLicenseEntity.setSubCategoryId(tradeLicense.getSubCategoryId());
		tradeLicenseEntity.setTenantId(tradeLicense.getTenantId());
		tradeLicenseEntity.setTradeAddress(tradeLicense.getTradeAddress());
		tradeLicenseEntity.setIsPropertyOwner(tradeLicense.getIsPropertyOwner());
		tradeLicenseEntity
				.setTradeCommencementDate(new Timestamp(tradeLicense.getTradeCommencementDate()));
		tradeLicenseEntity.setTradeTitle(tradeLicense.getTradeTitle());
		tradeLicenseEntity.setTradeType(tradeLicense.getTradeType().toString());
		tradeLicenseEntity.setUomId(tradeLicense.getUomId());
		tradeLicenseEntity.setStatus(tradeLicense.getStatus());

		return tradeLicenseEntity;
	}

	private TradeLicense getTradeLicenseDomain() {

		TradeLicense tradeLicense = new TradeLicense();

		tradeLicense.setActive(true);
		tradeLicense.setAdhaarNumber("123456781234");
		tradeLicense.setAdminWardId(7);
		tradeLicense.setAgreementDate((new Date("15/08/2017")).getTime()/1000);
		tradeLicense.setAgreementNo("232323");
		tradeLicense.setApplicationDate((new Date("15/08/2017")).getTime()/1000);
		tradeLicense.setApplicationNumber("12345");
		tradeLicense.setApplicationType(ApplicationType.NEW);
		tradeLicense.setCategoryId(1l);
		tradeLicense.setEmailId("abc@xyz.com");
		tradeLicense.setExpiryDate((new Date("15/08/2017")).getTime()/1000);
		tradeLicense.setFatherSpouseName("fatherSpouseName");
		tradeLicense.setFeeDetails(getLicenseFeeDetailsDomain());
		tradeLicense.setId(1l);
		tradeLicense.setIsLegacy(true);
		tradeLicense.setLicenseNumber("123456789");
		tradeLicense.setLicenseValidFromDate((new Date("15/08/2017")).getTime()/1000);
		tradeLicense.setLocalityId(20);
		tradeLicense.setMobileNumber("9999999999");
		tradeLicense.setOldLicenseNumber("123456789");
		tradeLicense.setOwnerAddress("ownerAddress");
		tradeLicense.setOwnerName("ownerName");
		tradeLicense.setOwnerShipType(OwnerShipType.RENTED);
		tradeLicense.setPropertyAssesmentNo(null);
		tradeLicense.setQuantity(10.0);
		tradeLicense.setValidityYears(1l);
		tradeLicense.setRemarks("remarks");
		tradeLicense.setRevenueWardId(22);
		tradeLicense.setStatus(null);
		tradeLicense.setSubCategoryId(2l);
		tradeLicense.setIsPropertyOwner(Boolean.FALSE);
		tradeLicense.setSupportDocuments(getSupportDocumentsDomain());
		tradeLicense.setTenantId("default");
		tradeLicense.setTradeAddress("tradeAddress");
		tradeLicense.setTradeCommencementDate((new Date("15/08/2017")).getTime()/1000);
		tradeLicense.setTradeTitle("tradeTitle");
		tradeLicense.setTradeType(BusinessNature.PERMANENT);
		tradeLicense.setUomId(1l);
		tradeLicense.setStatus(1L);

		return tradeLicense;
	}

	private List<LicenseFeeDetail> getLicenseFeeDetailsDomain() {

		List<LicenseFeeDetail> licenseFeeDetails = new ArrayList<LicenseFeeDetail>();
		LicenseFeeDetail licenseFeeDetail = new LicenseFeeDetail();
		licenseFeeDetail.setAmount(100.0);
		licenseFeeDetail.setFinancialYear("1");
//		licenseFeeDetail.setLicenseId(1l);
		licenseFeeDetail.setPaid(true);

		licenseFeeDetails.add(licenseFeeDetail);
		return licenseFeeDetails;
	}

	private List<SupportDocument> getSupportDocumentsDomain() {

		List<SupportDocument> supportDocuments = new ArrayList<SupportDocument>();
		SupportDocument supportDocument = new SupportDocument();
		supportDocument.setComments("comments");
		supportDocument.setDocumentTypeId(1l);
		supportDocument.setFileStoreId("1");
//		supportDocument.setLicenseId(1l);

		supportDocuments.add(supportDocument);
		return supportDocuments;
	}

	class TradeLicenseResultExtractor implements ResultSetExtractor<List<Map<String, Object>>> {
		@Override
		public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			List<Map<String, Object>> rows = new ArrayList<>();
			while (resultSet.next()) {
				Map<String, Object> row = new HashMap<String, Object>() {
					{
						put("id", resultSet.getString("id"));
						put("tenantId", resultSet.getString("tenantId"));
						put("active", resultSet.getBoolean("active"));
						put("oldLicenseNumber", resultSet.getString("oldLicenseNumber"));
					}
				};

				rows.add(row);
			}
			return rows;
		}
	}
}