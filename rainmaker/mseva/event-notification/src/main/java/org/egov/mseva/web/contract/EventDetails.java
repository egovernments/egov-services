package org.egov.mseva.web.contract;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Validated
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class EventDetails {
	
	private Long fromDate;
	
	private Long toDate;
	
	private Long latitude;
	
	private Long longitude;
	
	private List<String> documents;

}
