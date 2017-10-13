package org.egov.property.exception;

import org.springframework.web.client.HttpStatusCodeException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemandUpdateException extends RuntimeException {

	public DemandUpdateException(HttpStatusCodeException ex) {
		super(ex);
	}
}
