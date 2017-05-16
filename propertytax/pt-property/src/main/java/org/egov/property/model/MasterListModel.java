package org.egov.property.model;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;



@EnableConfigurationProperties
@ConfigurationProperties(prefix="masterList",ignoreUnknownFields=true)
@Component
@Getter
@Setter
public class MasterListModel {

	private	List<MasterModel> propertyType;

	private List<MasterModel> usageMaster;

	private	List<MasterModel> occupancyMaster;

	private	List<MasterModel> taxRateMaster;

	private	List<MasterModel> wallTypeMaster;

	private	List<MasterModel> roofTypeMaster;

	private	List<MasterModel> floorTypeMaster;

	private	List<MasterModel> woodTypeMaster;
	
	private	List<MasterModel> apartmentMaster;

	private	List<MasterModel> structureMaster;
	
	private	List<MasterModel> documentTypeMaster;
	
	private	List<MasterModel> mutationReasonMaster;
	
	private	List<MasterModel> mutationRateMaster;
	
	
}



