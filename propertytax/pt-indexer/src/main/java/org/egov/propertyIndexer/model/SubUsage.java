package org.egov.propertyIndexer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Prasad
 *This class will be used for Elasticseach indexing only
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubUsage {
	
	private String code;
	
	private String name;

}
