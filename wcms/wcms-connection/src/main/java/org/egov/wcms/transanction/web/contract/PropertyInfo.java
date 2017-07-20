package org.egov.wcms.transanction.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Object holds the basic data for a property
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PropertyInfo {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@JsonProperty("upicNumber")
	@Size(min = 6, max = 128)
	private String upicNumber = null;
	@JsonProperty("boundary")
        @NotNull
        private PropertyLocation boundary = null;
	

        @JsonProperty("owners")
        @Valid
        @NotNull
        private List<OwnerInfo> owners = new ArrayList<OwnerInfo>();
}
