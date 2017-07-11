package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Prasad
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MutationMasterResponse {

	private ResponseInfo responseInfo;

	private List<MutationMaster> mutationMasters;

}
