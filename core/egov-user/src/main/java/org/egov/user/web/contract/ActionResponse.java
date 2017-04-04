package org.egov.user.web.contract;

import java.util.List;

import lombok.NoArgsConstructor;
import org.egov.user.web.contract.auth.Action;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActionResponse {

	@JsonProperty("actions")
	List<Action> actions;

	public boolean isActions(){
		return this.getActions().isEmpty();
	}
}
