package org.egov.tradelicense.domain.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.LicenseFeeDetailContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.SupportDocumentContract;
import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.contract.UserInfo;
import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;
import org.egov.tl.commons.web.contract.enums.OwnerShipTypeEnum;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.enums.BusinessNature;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.persistence.entity.TradeLicenseEntity;
import org.egov.tradelicense.persistence.queue.TradeLicenseQueueRepository;
import org.egov.tradelicense.persistence.repository.LicenseFeeDetailJdbcRepository;
import org.egov.tradelicense.persistence.repository.SupportDocumentJdbcRepository;
import org.egov.tradelicense.persistence.repository.TradeLicenseJdbcRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class TradeLicenseRepositoryTest {

	@InjectMocks
	TradeLicenseRepository tradeLicenseRepository;

	@Mock
	TradeLicenseQueueRepository tradeLicenseQueueRepository;

	@Mock
	TradeLicenseJdbcRepository tradeLicenseJdbcRepository;

	@Mock
	SupportDocumentJdbcRepository supportDocumentJdbcRepository;

	@Mock
	LicenseFeeDetailJdbcRepository licenseFeeDetailJdbcRepository;

	@Mock
	PropertiesManager propertiesManager;

	@Mock
	JdbcTemplate jdbcTemplate;

	private List<LicenseFeeDetailContract> feeDetails = new ArrayList<>();
	private List<SupportDocumentContract> supportDocuments = new ArrayList<>();

	@Test
	public void testAdd() {
		Mockito.doNothing().when(tradeLicenseQueueRepository).add(Mockito.any());
		TradeLicenseRequest request = new TradeLicenseRequest();
		request.setRequestInfo(getRequestInfo());
		request.setLicenses(new ArrayList<TradeLicenseContract>());
		request.getLicenses().add(getTradeLicenseContract());
		tradeLicenseRepository.add(request, true);
		//Map<String, Object> message = new HashMap<>();
		//message.put(propertiesManager.getCreateLegacyTradeValidated(), request);
		Mockito.verify(tradeLicenseQueueRepository).add(request);
	}

	/*@Test
	public void testSave() {
		TradeLicenseEntity tradeLicenseEntity = getTradeLicenseEntity();
		TradeLicense expectedResult = tradeLicenseEntity.toDomain();
		when(tradeLicenseJdbcRepository.create(any(TradeLicenseEntity.class))).thenReturn(tradeLicenseEntity);
		TradeLicense actualResult = tradeLicenseRepository.save(getTradeLicenseDomain());
		assertEquals(expectedResult.getId(), actualResult.getId());
		assertEquals(expectedResult.getActive(), actualResult.getActive());
		assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
	}*/

	private TradeLicenseEntity getTradeLicenseEntity() {
		TradeLicenseEntity tradeLicenseEntity = new TradeLicenseEntity();
		TradeLicense TradeLicense = getTradeLicenseDomain();
		tradeLicenseEntity.setId(TradeLicense.getId());
		tradeLicenseEntity.setIsLegacy(TradeLicense.getIsLegacy());
		tradeLicenseEntity.setActive(TradeLicense.getActive());
		tradeLicenseEntity.setTenantId(TradeLicense.getTenantId());
		tradeLicenseEntity.setTradeType(TradeLicense.getTradeType().toString());
		return tradeLicenseEntity;
	}

	private TradeLicense getTradeLicenseDomain() {
		TradeLicense tradeLicense = new TradeLicense();
		tradeLicense.setId(1l);
		tradeLicense.setIsLegacy(true);
		tradeLicense.setActive(true);
		tradeLicense.setTenantId("default");
		tradeLicense.setTradeType(BusinessNature.PERMANENT);
		return tradeLicense;
	}

	private TradeLicenseContract getTradeLicenseContract() {

		feeDetails.add(getFeeDetail());
		supportDocuments.add(getSupportDocument());
		return TradeLicenseContract.builder().id(1l).tenantId("default").applicationType(ApplicationTypeEnum.NEW)
				.active(true).applicationDate( (new Date("15/08/2017")).getTime()/1000).ownerEmailId("abc@xyz.com").isLegacy(true)
				.oldLicenseNumber("12345").ownerMobileNumber("9999999999").ownerName("pavan").fatherSpouseName("Venkat")
				.ownerAddress("1-12 kamma street").locality("7").adminWard("7").revenueWard("20").category("flammables")
				.subCategory("crackers").uom("area").quantity(10.0).tradeAddress("1-12 kamma street")
				.ownerShipType(OwnerShipTypeEnum.RENTED).tradeTitle("restaurants")
				.tradeType(BusinessNatureEnum.PERMANENT).feeDetails(feeDetails).supportDocuments(supportDocuments)
				.tradeCommencementDate( (new Date("15/08/2017")).getTime()/1000).auditDetails(getAuditDetails()).build();
	}

	private AuditDetails getAuditDetails() {

		return AuditDetails.builder().createdBy("1").createdTime(12345678912l).lastModifiedBy("1")
				.lastModifiedTime(12345678912l).build();
	}

	private SupportDocumentContract getSupportDocument() {

		return SupportDocumentContract.builder().id(1l).documentTypeId(1l).auditDetails(getAuditDetails()).build();
	}

	private LicenseFeeDetailContract getFeeDetail() {

		return LicenseFeeDetailContract.builder().id(1l).auditDetails(getAuditDetails()).build();
	}

	private RequestInfo getRequestInfo() {
		RequestInfo info = new RequestInfo();
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1);
		info.setAction("create");
		info.setDid("did");
		info.setApiId("apiId");
		info.setKey("key");
		info.setMsgId("msgId");
		info.setTs(12345l);
		info.setUserInfo(userInfo);
		info.setAuthToken("null");
		return info;
	}
}