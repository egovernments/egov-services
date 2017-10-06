package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OccuapancyMasterResponse {
	private ResponseInfo responseInfo;

	private List<OccuapancyMaster> occuapancyMasters;
}
