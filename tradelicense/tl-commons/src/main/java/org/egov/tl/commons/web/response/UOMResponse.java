package org.egov.tl.commons.web.response;

import java.util.List;

import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.UOM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in UOMResponse
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UOMResponse {

	private ResponseInfo responseInfo;

	private List<UOM> uoms;
}