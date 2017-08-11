package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
/**
 * 
 * @author Yosadhara
 *
 */
public class ApartmentResponse {

	private ResponseInfo responseInfo;

	private List<Apartment> apartments;
}
