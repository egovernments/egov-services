package org.egov.user.domain.exception;

import lombok.Getter;
import org.egov.user.domain.model.UpdatePassword;

public class InvalidUpdatePasswordRequestException extends RuntimeException {
	private static final long serialVersionUID = 6391424774009868054L;
	@Getter
	private final UpdatePassword request;

	public InvalidUpdatePasswordRequestException(UpdatePassword updatePassword) {
		this.request = updatePassword;
	}
}
