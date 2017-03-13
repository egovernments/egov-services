package org.egov.lams.model;

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
public class Location {

	private String locality;
	private String street;
	private String zone;
	private String revenueWard;
	private String block;
	private String electionWard;
}
