package org.egov.demand.web.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ModuleResponse {
	@JsonProperty("Module")
	private List<Module> modules = new ArrayList<Module>();
}
