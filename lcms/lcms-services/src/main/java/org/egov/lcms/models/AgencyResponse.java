package org.egov.lcms.models;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 
 * @author Veswanth 
 * This object holds information about the Agency Response
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgencyResponse {

	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("agencies")
	private List<Agency> agencies;
}
