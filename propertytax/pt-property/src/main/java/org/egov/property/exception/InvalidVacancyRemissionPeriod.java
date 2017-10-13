package org.egov.property.exception;

import org.egov.models.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Description: This is the Exception class for Invalid Vacancy Remission
 * Period.
 * 
 * @author Yosadhara
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidVacancyRemissionPeriod extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String customMsg;

	private RequestInfo requestInfo;
}
