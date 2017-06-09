package org.egov.property.model;

import org.egov.models.ResponseInfo;

import lombok.Getter;
import lombok.Setter;

/**
 * This class will use wrapper model for master response 
 * @author Narendra
 *
 */


@Getter
@Setter
public class MasterResponse {
	
	private MasterModel masterModel;
	
	private ResponseInfo resonseInfo;

}
