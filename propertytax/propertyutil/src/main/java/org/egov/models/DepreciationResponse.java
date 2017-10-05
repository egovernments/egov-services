package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Prasad Depreciation response model class
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DepreciationResponse {

	private ResponseInfo responseInfo;

	private List<Depreciation> depreciations;

}
