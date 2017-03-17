package org.egov.commons.web.contract;

import java.util.List;

import org.egov.commons.model.Uom;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UomResponse {
	
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("uoms")
	private List<Uom> uoms;

}
