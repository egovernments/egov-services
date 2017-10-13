package org.egov.property.exception;

import org.egov.models.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Description: This is the Exception class for Invalid Propert type(Vacant Land
 * or Exempted property) related to Vacancy Remission.
 * 
 * @author Yosadhara
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidPropertyTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String customMsg;

	private RequestInfo requestInfo;
}
