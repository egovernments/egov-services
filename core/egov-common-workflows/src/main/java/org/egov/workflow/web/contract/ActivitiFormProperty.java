package org.egov.workflow.web.contract;

import java.util.ArrayList;
import java.util.List;

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
public class ActivitiFormProperty{

	protected String id;
	protected String name;
	protected String type;
	protected String value;
	protected boolean readable;
	protected boolean writable;
	protected boolean required;
	protected String datePattern;
	protected List<ActivitiEnumFormProperty> enumValues = new ArrayList<ActivitiEnumFormProperty>();

	public void addEnumValue(ActivitiEnumFormProperty enumValue) {
		enumValues.add(enumValue);
	}

}
