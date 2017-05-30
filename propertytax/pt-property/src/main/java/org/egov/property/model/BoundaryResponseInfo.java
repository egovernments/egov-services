package org.egov.property.model;

import java.util.List;

import org.egov.models.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoundaryResponseInfo {
	
	private ResponseInfo ResponseInfo;
	
	private List<Boundary> Boundary;
	
}
