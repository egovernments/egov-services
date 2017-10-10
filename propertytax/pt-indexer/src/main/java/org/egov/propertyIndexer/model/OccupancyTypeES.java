package org.egov.propertyIndexer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Prasad
 *This Model will be used for ElasticSearch indexing
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OccupancyTypeES {
	
	private String code;
	
	private String name;

}
