package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Prasad
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MutationMasterResponse {

	private ResponseInfo responseInfo;

	private List<MutationMaster> mutationMasters;

}
