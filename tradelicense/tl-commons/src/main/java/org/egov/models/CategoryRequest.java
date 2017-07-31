package org.egov.models;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in a Trade license
 * CategoryRequest
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@Valid
	private List<Category> categories;
}