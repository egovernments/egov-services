package org.egov.property.model;

import java.util.List;

import org.egov.models.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoundaryResponseInfo {

	private ResponseInfo ResponseInfo;

	private List<Boundary> Boundary;

}
