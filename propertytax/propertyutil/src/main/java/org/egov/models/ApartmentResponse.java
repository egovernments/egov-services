package org.egov.models;

import java.util.List;

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
/**
 * 
 * @author Yosadhara
 *
 */
public class ApartmentResponse {

	private ResponseInfo responseInfo;

	private List<Apartment> apartments;
}
