package org.egov.tradelicense.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.commons.web.contract.DocumentTypeContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.UserInfo;
import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.requests.DocumentTypeV2Request;
import org.egov.tl.masters.domain.repository.DocumentTypeDomainRepository;
import org.egov.tl.masters.persistence.entity.DocumentTypeEntity;
import org.egov.tl.masters.persistence.queue.DocumentTypeQueueRepository;
import org.egov.tl.masters.persistence.repository.DocumentTypeJdbcRepository;
import org.egov.tradelicense.config.PropertiesManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeRepositoryTest {

	@InjectMocks
	DocumentTypeDomainRepository documentTypeDomainRepository;

	@Mock
	DocumentTypeQueueRepository documentTypeQueueRepository;

	@Mock
	DocumentTypeJdbcRepository documentTypeJdbcRepository;

	@Mock
	PropertiesManager propertiesManager;

	@Mock
	JdbcTemplate jdbcTemplate;

	

	@Test
	public void testAdd() {
		Mockito.doNothing().when(documentTypeQueueRepository).add(Mockito.any());
		DocumentTypeV2Request request = new DocumentTypeV2Request();
		request.setRequestInfo(getRequestInfo());
		request.setDocumentTypes(new ArrayList<DocumentTypeContract>());
		request.getDocumentTypes().add(getDocumentTypeContract());
		documentTypeDomainRepository.add(request);
		Map<String, Object> message = new HashMap<>();
		message.put(propertiesManager.getCreateDocumentTypeV2Validated(), request);
		Mockito.verify(documentTypeQueueRepository).add(message);
	}

	@Test
	public void testSave() {
		DocumentTypeEntity documentTypeEntity = getDocumentTypeEntity();
		DocumentType expectedResult = documentTypeEntity.toDomain();
		when(documentTypeJdbcRepository.create(any(DocumentTypeEntity.class))).thenReturn(documentTypeEntity);
		DocumentType actualResult = documentTypeDomainRepository.save(getDocumentTypeDomain());
		assertEquals(expectedResult.getApplicationType().name(), actualResult.getApplicationType().name());
		assertEquals(expectedResult.getEnabled(), actualResult.getEnabled());
		assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
	}
	
	
	@Test
	public void testAddUpdate() {
		Mockito.doNothing().when(documentTypeQueueRepository).add(Mockito.any());
		DocumentTypeV2Request request = new DocumentTypeV2Request();
		request.setRequestInfo(getRequestInfo());
		request.setDocumentTypes(new ArrayList<DocumentTypeContract>());
		request.getDocumentTypes().add(getDocumentTypeContract());
		documentTypeDomainRepository.update(request);
		Map<String, Object> message = new HashMap<>();
		message.put(propertiesManager.getCreateDocumentTypeV2Validated(), request);
		Mockito.verify(documentTypeQueueRepository).add(message);
	}

	
	
	@Test
	public void testUpdate() {
		DocumentTypeEntity documentTypeEntity = getDocumentTypeEntity();
		DocumentType expectedResult = documentTypeEntity.toDomain();
		when(documentTypeJdbcRepository.update(any(DocumentTypeEntity.class))).thenReturn(documentTypeEntity);
		DocumentType actualResult = documentTypeDomainRepository.update(getDocumentTypeDomain());
		assertEquals(expectedResult.getApplicationType().name(), actualResult.getApplicationType().name());
		assertEquals(expectedResult.getEnabled(), actualResult.getEnabled());
		assertEquals(expectedResult.getTenantId(), actualResult.getTenantId());
	}
	
	private DocumentTypeEntity getDocumentTypeEntity() {
		DocumentTypeEntity documentTypeEntity = new DocumentTypeEntity();
		DocumentType documentType = getDocumentTypeDomain();
		documentTypeEntity.setId(documentType.getId());
		documentTypeEntity.setEnabled(documentType.getEnabled());
		documentTypeEntity.setMandatory(documentType.getMandatory());
		documentTypeEntity.setTenantId(documentType.getTenantId());
		documentTypeEntity.setApplicationType(documentType.getApplicationType().name());
	
		return documentTypeEntity;
	}
	
	private DocumentType getDocumentTypeDomain() {
		DocumentType documentType = new DocumentType();
		documentType.setId(1l);
		documentType.setEnabled(true);
		documentType.setMandatory(true);
		documentType.setTenantId("default");
		documentType.setApplicationType(ApplicationTypeEnum.NEW);
		return documentType;
	}

	private DocumentTypeContract getDocumentTypeContract() {

		return DocumentTypeContract.builder().id(1l).tenantId("default").applicationType(ApplicationTypeEnum.NEW)
				.enabled(true).category("1").subCategory("2").auditDetails(getAuditDetails()).build();
	}

	private AuditDetails getAuditDetails() {

		return AuditDetails.builder().createdBy("1").createdTime(12345678912l).lastModifiedBy("1")
				.lastModifiedTime(12345678912l).build();
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