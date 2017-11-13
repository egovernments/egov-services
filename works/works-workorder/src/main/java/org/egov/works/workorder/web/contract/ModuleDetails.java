package org.egov.works.workorder.web.contract;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleDetails {

	private MasterDetails[] masterDetails;

	private String moduleName;

}
