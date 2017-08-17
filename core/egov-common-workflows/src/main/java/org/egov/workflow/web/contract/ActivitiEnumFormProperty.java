package org.egov.workflow.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
@Data
public class ActivitiEnumFormProperty {
	
	  protected String id;
	  protected String name;

}
