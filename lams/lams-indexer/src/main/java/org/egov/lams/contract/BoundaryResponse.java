package org.egov.lams.contract;

import java.util.ArrayList;
import java.util.List;
import org.egov.lams.model.Boundary;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoundaryResponse {
	
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;
	@JsonProperty("Boundary")
	private List<Boundary> boundarys = new ArrayList<Boundary>();

	 
}
