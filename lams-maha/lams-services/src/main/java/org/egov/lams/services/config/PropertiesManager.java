package org.egov.lams.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
@Setter
@Getter
public class PropertiesManager {

	@Autowired
	Environment environment;

	@Value("${mdms.master.registername}")
	public String mdmsRegisterName;
	
	@Value("${mdms.master.usagetype}")
	public String mdmsUsageType;

	@Value("${mdms.master.subusagetype}")
	public String mdmsSubUsageType;
	
	@Value("${mdms.master.subregistername}")
	public String mdmsSubRegisterName;

	@Value("${mdms.master.propertytype}")
	public String mdmsPropertyType;

	@Value("${mdms.modulename}")
	public String mdmsModuleName;
	
	@Value("${kafka.topic.start.workflow.agreement}")
	public String startAgreementWorkflowTopic;
	
	@Value("${kafka.topic.update.workflow.agreement}")
	public String updateAgreementWorkflowTopic;
	
	@Value("${lams-agreement-create-seq}")
	public String createAgreementSequence;
	
	@Value("${lams-agreementdoc-create-seq}")
	public String createAgreementDocSequence;
	
	@Value("${kafka.topic.create.agreement}")
	public String createAgreementKafkaTopic;
	
	@Value("${kafka.topic.update.agreement}")
	public String updateAgreementKafkaTopic;
	
	@Value("${kafka.topic.save.estate}")
	public String createEstateKafkaTopic;

	@Value("${kafka.topic.save.landAcquisition}")
	public String createLandAcquisitionKafkaTopic;

	@Value("${kafka.topic.update.landAcquisition}")
	public String updateLandAcquisitionKafkaTopic;

	@Value("${kafka.topic.save.land}")
	public String createLandRegisterKafkaTopic;

	@Value("${kafka.topic.update.land}")
	public String updateLandRegisterKafkaTopic;
	
	@Value("${kafka.topic.update.estate}")
	public String updateEstateKafkaTopic;
	
	@Value("${kafka.topic.start.workflow.estate}")
	public String startEstateWorkflowTopic;
	
	@Value("${kafka.topic.update.workflow.estate}")
	public String updateEstateWorkflowTopic;
	
	@Value("${lams-estate-regn-seq}")
	public String createEstateSequence;
	
	@Value("${lams-estate-floor-seq}")
	public String createEstateFloorsSequence;
	
	@Value("${lams-estate-units-seq}")
	public String createEstateUnitsSequence;
	
	@Value("${kafka.topic.save.landPossession}")
	public String createLandPossessionKafkaTopic;

	@Value("${kafka.topic.update.landPossession}")
	public String updateLandPossessionKafkaTopic;
	
	@Value("${kafka.topic.save.landTransfer}")
	public String createLandTransferKafkaTopic;
	
	
	@Value("${kafka.topic.update.landTransfer}")
	public String updateLandTransferKafkaTopic;
	
}
