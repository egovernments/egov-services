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

import org.egov.tradelicense.domain.enums.ApplicationType;
import org.egov.tradelicense.domain.enums.BusinessNature;
import org.egov.tradelicense.domain.enums.EstablishmentType;
import org.egov.tradelicense.domain.enums.Gender;
import org.egov.tradelicense.domain.enums.OwnerShipType;
import org.egov.tradelicense.domain.enums.OwnerType;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.model.TradePartner;
import org.egov.tradelicense.domain.model.TradeShift;
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
		tradeLicenseEntity.setOwnerAadhaarNumber(tradeLicense.getOwnerAadhaarNumber());
		tradeLicenseEntity.setAdminWard(tradeLicense.getAdminWard());
		tradeLicenseEntity.setApplicationDate(new Timestamp(tradeLicense.getApplicationDate()));
		tradeLicenseEntity.setApplicationNumber(tradeLicense.getApplicationNumber());
		tradeLicenseEntity.setAgreementNo(tradeLicense.getAgreementNo());
		tradeLicenseEntity.setApplicationType(tradeLicense.getApplicationType().toString());
		tradeLicenseEntity.setCategory(tradeLicense.getCategory());
		tradeLicenseEntity.setOwnerEmailId(tradeLicense.getOwnerEmailId());
		tradeLicenseEntity.setExpiryDate(new Timestamp(tradeLicense.getExpiryDate()));
		tradeLicenseEntity.setFatherSpouseName(tradeLicense.getFatherSpouseName());
		tradeLicenseEntity.setId(tradeLicense.getId());
		tradeLicenseEntity.setIsLegacy(tradeLicense.getIsLegacy());
		tradeLicenseEntity.setLicenseNumber(tradeLicense.getLicenseNumber());
		tradeLicenseEntity.setLicenseValidFromDate(new Timestamp(tradeLicense.getLicenseValidFromDate()));
		tradeLicenseEntity.setLocality(tradeLicense.getLocality());
		tradeLicenseEntity.setOwnerMobileNumber(tradeLicense.getOwnerMobileNumber());
		tradeLicenseEntity.setOldLicenseNumber(tradeLicense.getOldLicenseNumber());
		tradeLicenseEntity.setOwnerAddress(tradeLicense.getOwnerAddress());
		tradeLicenseEntity.setOwnerName(tradeLicense.getOwnerName());
		tradeLicenseEntity.setOwnerType(tradeLicense.getOwnerType().toString());
		tradeLicenseEntity.setOwnerShipType(tradeLicense.getOwnerShipType().toString());
		tradeLicenseEntity.setPropertyAssesmentNo(tradeLicense.getPropertyAssesmentNo());
		tradeLicenseEntity.setQuantity(tradeLicense.getQuantity());
		tradeLicenseEntity.setValidityYears(tradeLicense.getValidityYears());
		tradeLicenseEntity.setRemarks(tradeLicense.getRemarks());
		tradeLicenseEntity.setRevenueWard(tradeLicense.getRevenueWard());
		tradeLicenseEntity.setSubCategory(tradeLicense.getSubCategory());
		tradeLicenseEntity.setTenantId(tradeLicense.getTenantId());
		tradeLicenseEntity.setTradeAddress(tradeLicense.getTradeAddress());
		tradeLicenseEntity.setIsPropertyOwner(tradeLicense.getIsPropertyOwner());
		tradeLicenseEntity
				.setTradeCommencementDate(new Timestamp(tradeLicense.getTradeCommencementDate()));
		tradeLicenseEntity.setTradeTitle(tradeLicense.getTradeTitle());
		tradeLicenseEntity.setTradeType(tradeLicense.getTradeType().toString());
		tradeLicenseEntity.setUom(tradeLicense.getUom());
		tradeLicenseEntity.setStatus(tradeLicense.getStatus());
		tradeLicenseEntity.setEstablishmentCity(tradeLicense.getEstablishmentCity());
		tradeLicenseEntity.setEstablishmentCorrAddress(tradeLicense.getEstablishmentCorrAddress());
		tradeLicenseEntity.setEstablishmentEmailId(tradeLicense.getEstablishmentEmailId());
		tradeLicenseEntity.setEstablishmentMobNo(tradeLicense.getEstablishmentMobNo());
		tradeLicenseEntity.setEstablishmentName(tradeLicense.getEstablishmentName());
		tradeLicenseEntity.setEstablishmentPhoneNo(tradeLicense.getEstablishmentPhoneNo());
		tradeLicenseEntity.setEstablishmentPinCode(tradeLicense.getEstablishmentPinCode());
		tradeLicenseEntity.setEstablishmentRegNo(tradeLicense.getEstablishmentRegNo());
		tradeLicenseEntity.setEstablishmentType(tradeLicense.getEstablishmentType().toString());
		tradeLicenseEntity.setLandOwnerName(tradeLicense.getLandOwnerName());
		tradeLicenseEntity.setBusinessDescription(tradeLicense.getBusinessDescription());
		tradeLicenseEntity.setTotalEmployees(tradeLicense.getTotalEmployees());
		tradeLicenseEntity.setLicenseRejBefrForSamePremise(tradeLicense.getLicenseRejBefrForSamePremise());

		return tradeLicenseEntity;
	}

	private TradeLicense getTradeLicenseDomain() {

		TradeLicense tradeLicense = new TradeLicense();

		tradeLicense.setActive(true);
		tradeLicense.setOwnerAadhaarNumber("123456781234");
		tradeLicense.setAdminWard("7");
		tradeLicense.setAgreementDate((new Date("15/08/2017")).getTime()/1000);
		tradeLicense.setAgreementNo("232323");
		tradeLicense.setApplicationDate((new Date("15/08/2017")).getTime()/1000);
		tradeLicense.setApplicationNumber("12345");
		tradeLicense.setApplicationType(ApplicationType.NEW);
		tradeLicense.setCategory("Flammables");
		tradeLicense.setOwnerEmailId("abc@xyz.com");
		tradeLicense.setExpiryDate((new Date("15/08/2017")).getTime()/1000);
		tradeLicense.setFatherSpouseName("fatherSpouseName");
		tradeLicense.setFeeDetails(getLicenseFeeDetailsDomain());
		tradeLicense.setId(1l);
		tradeLicense.setIsLegacy(true);
		tradeLicense.setLicenseNumber("123456789");
		tradeLicense.setLicenseValidFromDate((new Date("15/08/2017")).getTime()/1000);
		tradeLicense.setLocality("20");
		tradeLicense.setOwnerMobileNumber("9999999999");
		tradeLicense.setOldLicenseNumber("123456789");
		tradeLicense.setOwnerAddress("ownerAddress");
		tradeLicense.setOwnerName("ownerName");
		tradeLicense.setOwnerType(OwnerType.INDIVIDUAL);
		tradeLicense.setOwnerShipType(OwnerShipType.RENTED);
		tradeLicense.setPropertyAssesmentNo(null);
		tradeLicense.setQuantity(10.0);
		tradeLicense.setValidityYears(1l);
		tradeLicense.setRemarks("remarks");
		tradeLicense.setRevenueWard("22");
		tradeLicense.setStatus(null);
		tradeLicense.setSubCategory("Crackers");
		tradeLicense.setIsPropertyOwner(Boolean.FALSE);
		tradeLicense.setSupportDocuments(getSupportDocumentsDomain());
		tradeLicense.setTenantId("default");
		tradeLicense.setTradeAddress("tradeAddress");
		tradeLicense.setTradeCommencementDate((new Date("15/08/2017")).getTime()/1000);
		tradeLicense.setTradeTitle("tradeTitle");
		tradeLicense.setTradeType(BusinessNature.PERMANENT);
		tradeLicense.setUom("Area");
		tradeLicense.setStatus("Inforce");
		tradeLicense.setPartners(getTradePartners());
		tradeLicense.setShifts(getTradeShifts());
		tradeLicense.setEstablishmentCorrAddress("establishmentCorrAddress");
		tradeLicense.setEstablishmentEmailId("test.gmail.com");
		tradeLicense.setEstablishmentMobNo("1234566543");
		tradeLicense.setEstablishmentName("establishmentName");
		tradeLicense.setEstablishmentPhoneNo("1234566543");
		tradeLicense.setEstablishmentPinCode("523260");
		tradeLicense.setEstablishmentRegNo("3423654");
		tradeLicense.setEstablishmentType(EstablishmentType.BANK);
		tradeLicense.setEstablishmentCity("hyderbad");
		tradeLicense.setLandOwnerName("hgfhsadf");
		tradeLicense.setBusinessDescription("gsdfghks");
		tradeLicense.setTotalEmployees(50);
		tradeLicense.setLicenseRejBefrForSamePremise(true);

		return tradeLicense;
	}

	private List<TradeShift> getTradeShifts() {
		
		List<TradeShift> tradeShifts = new ArrayList<TradeShift>();
		TradeShift tradeShift = new TradeShift();
		
		tradeShift.setFromTime(new Date().getTime());
		tradeShift.setRemarks("test");
		tradeShift.setShiftNo(1);
		tradeShift.setToTime(new Date().getTime());
		tradeShift.setTenantId("default");
		
		tradeShifts.add(tradeShift);
		
		return tradeShifts;
	}

	private List<TradePartner> getTradePartners() {
		
		List<TradePartner> tradePartners = new ArrayList<TradePartner>();
		TradePartner tradePartner = new TradePartner();
		
		tradePartner.setAadhaarNumber("123456781234");
		tradePartner.setBirthYear("1988");
		tradePartner.setCorrespondenceAddress("hyderabad");
		tradePartner.setDesignation("BA");
		tradePartner.setEmailId("test.gmail.com");
		tradePartner.setFullName("test");
		tradePartner.setGender(Gender.MALE);
		tradePartner.setMobileNumber("1234567890");
		tradePartner.setPhoneNumber("1234567890");
		tradePartner.setPhoto("photo");
		tradePartner.setResidentialAddress("hyderabad");
		tradePartner.setTenantId("default");
		
		tradePartners.add(tradePartner);
		
		return tradePartners;
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