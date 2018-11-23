package org.egov.infra.indexer.custom.pgr.models;

import org.springframework.stereotype.Component;

@Component
public class PGRCustomDecorator {
	
	public void dataTransformationForPGR(ServiceResponse serviceResponse) {
		PGRIndexObject indexObject = new PGRIndexObject();
		for(int i = 0; i < serviceResponse.getServices().size(); i++) {
			
		}
	}

}
