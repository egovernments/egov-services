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

	@Mock
	private PropertiesManager propertiesManagerForNoArgsContructor = new PropertiesManager();

	@InjectMocks
	private PropertiesManager propertiesManager;

	@Test
	public void testSetPropertiesManager() {
		propertiesManager.setCreateFeeTopicName("kafka.topics.create.fee");
		propertiesManager.setCertNumberSequence("egov.mr.services.certnumber_sequence");
		propertiesManager.setCreateMarriageDocumentTypeTopicName("kafka.topics.create.marriagedocumenttype");
		propertiesManager.setCreateMarriageRegnTopicName("kafka.topics.create.marriageregn");
		propertiesManager.setCreateRegistrationUnitTopicName("kafka.topics.create.registrationunit");
		propertiesManager.setCreateReissueCertificateTopicName("kafka.topics.create.reissueCertificate");
		propertiesManager.setCreateReissueMarriageRegnKey("kafka.topics.create.reissueofcertkey");
		propertiesManager.setCreateReissueMarriageRegnTopicName("kafka.topics.create.reissueappl");

		propertiesManager.setEnvironment(environment);
		propertiesManager.setMarriageDocumentTypeKey("kafka.key.marriagedocumenttype");
		propertiesManager.setMarriageRegnKey("kafka.key.marriageregn");
		propertiesManager.setRegistrationUnitKey("kafka.key.registrationunit");
		propertiesManager.setRegnNumberSequence("egov.mr.services.regnnumber_sequence");

		propertiesManager.setUpdateFeeTopicName("kafka.topics.update.fee");
		propertiesManager.setUpdateMarriageDocumentTypeTopicName("kafka.topics.update.marriagedocumenttype");
		propertiesManager.setUpdateMarriageRegnTopicName("kafka.topics.update.marriageregn");
		propertiesManager.setUpdateRegistrationUnitTopicName("kafka.topics.update.registrationunit");
		propertiesManager.setUpdateReissueMarriageRegnKey("kafka.topics.update.reissueofcertkey");
		propertiesManager.setUpdateReissueMarriageRegnTopicName("kafka.topics.update.reissueappl");

		assertEquals(propertiesManager.getCreateFeeTopicName(), "kafka.topics.create.fee");
		assertEquals(propertiesManager.getCertNumberSequence(), "egov.mr.services.certnumber_sequence");
		assertEquals(propertiesManager.getCreateMarriageDocumentTypeTopicName(),
				"kafka.topics.create.marriagedocumenttype");

		when(propertiesManager.getCreateMarriageFeeGenerated()).thenReturn("egov.marriageregn.property.fee.generated");
		assertEquals(propertiesManager.getCreateMarriageFeeGenerated(), "egov.marriageregn.property.fee.generated");
		assertEquals(propertiesManager.getCreateMarriageRegnTopicName(), "kafka.topics.create.marriageregn");
		assertEquals(propertiesManager.getCreateRegistrationUnitTopicName(), "kafka.topics.create.registrationunit");
		assertEquals(propertiesManager.getCreateReissueCertificateTopicName(),
				"kafka.topics.create.reissueCertificate");
		assertEquals(propertiesManager.getCreateReissueMarriageRegnKey(), "kafka.topics.create.reissueofcertkey");
		assertEquals(propertiesManager.getCreateReissueMarriageRegnTopicName(), "kafka.topics.create.reissueappl");

		assertEquals(propertiesManager.getEnvironment(), environment);
		assertEquals(propertiesManager.getMarriageDocumentTypeKey(), "kafka.key.marriagedocumenttype");
		assertEquals(propertiesManager.getMarriageRegnKey(), "kafka.key.marriageregn");
		assertEquals(propertiesManager.getRegistrationUnitKey(), "kafka.key.registrationunit");
		assertEquals(propertiesManager.getRegnNumberSequence(), "egov.mr.services.regnnumber_sequence");

		assertEquals(propertiesManager.getUpdateFeeTopicName(), "kafka.topics.update.fee");
		assertEquals(propertiesManager.getUpdateMarriageDocumentTypeTopicName(),
				"kafka.topics.update.marriagedocumenttype");
		assertEquals(propertiesManager.getUpdateMarriageRegnTopicName(), "kafka.topics.update.marriageregn");
		assertEquals(propertiesManager.getUpdateRegistrationUnitTopicName(), "kafka.topics.update.registrationunit");
		assertEquals(propertiesManager.getUpdateReissueMarriageRegnKey(), "kafka.topics.update.reissueofcertkey");
		assertEquals(propertiesManager.getUpdateReissueMarriageRegnTopicName(), "kafka.topics.update.reissueappl");
	}

	@Test
	public void testToString() {
		assertEquals(propertiesManager.toString(),
				PropertiesManager.builder().environment(environment).build().toString());
	}
}