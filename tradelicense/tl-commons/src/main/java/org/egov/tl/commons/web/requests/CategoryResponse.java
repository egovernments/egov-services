package org.egov.tl.commons.web.requests;

import java.util.List;

import org.egov.tl.commons.web.contract.Category;
import org.egov.tl.commons.web.contract.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in a Trade license
 * CategoryResponse
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

	private ResponseInfo responseInfo;

	private List<Category> categories;
}