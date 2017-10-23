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
	
	@Value("${kafka.topic.save.estate}")
	public String createEstateKafkaTopic;
	
	@Value("${kafka.topic.save.landAcquisition}")
	public String createLandAcquisitionKafkaTopic;
	
	@Value("${kafka.topic.update.landAcquisition}")
	public String updateLandAcquisitionKafkaTopic;
	
}
