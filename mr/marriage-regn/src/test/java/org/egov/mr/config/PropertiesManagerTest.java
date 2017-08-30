package org.egov.mr.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

@RunWith(MockitoJUnitRunner.class)
public class PropertiesManagerTest {

	@Mock
	Environment environment;

	@InjectMocks
	private PropertiesManager propertiesManager;

	@Test
	public void testGetPropertiesManager() {
		propertiesManager.getCreateFeeTopicName();
		propertiesManager.getCertNumberSequence();
		propertiesManager.getCreateMarriageDocumentTypeTopicName();
		propertiesManager.getCreateMarriageFeeGenerated();
		propertiesManager.getCreateMarriageRegnTopicName();
		propertiesManager.getCreateRegistrationUnitTopicName();
		propertiesManager.getCreateReissueCertificateTopicName();
		propertiesManager.getCreateReissueMarriageRegnKey();
		propertiesManager.getCreateReissueMarriageRegnTopicName();
		propertiesManager.getEnvironment();
		propertiesManager.getMarriageDocumentTypeKey();
		propertiesManager.getMarriageRegnKey();
		propertiesManager.getRegistrationUnitKey();
		propertiesManager.getRegnNumberSequence();
		propertiesManager.getUpdateFeeTopicName();
		propertiesManager.getUpdateMarriageDocumentTypeTopicName();
		propertiesManager.getUpdateMarriageRegnTopicName();
		propertiesManager.getUpdateRegistrationUnitTopicName();
		propertiesManager.getUpdateReissueMarriageRegnKey();
		propertiesManager.getUpdateReissueMarriageRegnTopicName();
	}

	@Test
	public void testSetPropertiesManager() {
		propertiesManager.setCreateFeeTopicName("");
		propertiesManager.setCertNumberSequence("");
		propertiesManager.setCreateMarriageDocumentTypeTopicName("");
		propertiesManager.setCreateMarriageRegnTopicName("");
		propertiesManager.setCreateRegistrationUnitTopicName("");
		propertiesManager.setCreateReissueCertificateTopicName("");
		propertiesManager.setCreateReissueMarriageRegnKey("");
		propertiesManager.setCreateReissueMarriageRegnTopicName("");
		propertiesManager.setEnvironment(environment);
		propertiesManager.setMarriageDocumentTypeKey("");
		propertiesManager.setMarriageRegnKey("");
		propertiesManager.setRegistrationUnitKey("");
		propertiesManager.setRegnNumberSequence("");
		propertiesManager.setUpdateFeeTopicName("");
		propertiesManager.setUpdateMarriageDocumentTypeTopicName("");
		propertiesManager.setUpdateMarriageRegnTopicName("");
		propertiesManager.setUpdateRegistrationUnitTopicName("");
		propertiesManager.setUpdateReissueMarriageRegnKey("");
		propertiesManager.setUpdateReissueMarriageRegnTopicName("");
	}

	@Test
	public void testNoArgsConstructor() {

	}

}
