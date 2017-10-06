package org.egov.property.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Prasad
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PropertyUser {

	private Integer id;
	private Integer propertyId;
	private Integer owner;
	private Boolean isPrimaryOwner;

	private Boolean isSecondaryOwner;
	private Double ownerShipPercentage;

	private String ownerType;

}
