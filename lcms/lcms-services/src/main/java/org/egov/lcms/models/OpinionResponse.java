package org.egov.lcms.models;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Veswanth
 *	This object holds information about the opinion Response
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpinionResponse {
	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("opinions")
	private List<Opinion> opinions = null;

}
