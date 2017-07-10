package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Prasad Depreciation response model class
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepreciationResponse {

	private ResponseInfo responseInfo;

	private List<Depreciation> depreciations;

}
