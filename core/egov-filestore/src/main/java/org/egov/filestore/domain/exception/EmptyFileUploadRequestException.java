package org.egov.filestore.domain.exception;

public class EmptyFileUploadRequestException extends RuntimeException {

	private static final long serialVersionUID = 469769329321629532L;
	private static final String MESSAGE = "No files present in upload request for jurisdiction: %s, module: %s, tag: %s";

	public EmptyFileUploadRequestException(String jurisdiction, String module, String tag) {
		super(String.format(MESSAGE, jurisdiction, module , tag));
	}
}
