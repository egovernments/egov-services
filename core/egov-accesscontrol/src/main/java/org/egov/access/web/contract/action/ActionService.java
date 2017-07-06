package org.egov.access.web.contract.action;

import java.util.List;

import org.egov.access.domain.model.Action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class ActionService {

	
	List<Module> modules;	
	
	List<Action> actions;
}
