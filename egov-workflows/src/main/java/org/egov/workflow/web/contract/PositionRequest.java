package org.egov.workflow.web.contract;

import java.util.List;

import lombok.Data;

@Data
public class PositionRequest {

	private List<Position> position;
	
	private List<Position> Positions;
	
   
    private ResponseInfo responseInfo = null;

}
