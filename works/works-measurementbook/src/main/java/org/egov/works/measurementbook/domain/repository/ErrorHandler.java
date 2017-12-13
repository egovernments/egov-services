package org.egov.works.measurementbook.domain.repository;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class ErrorHandler implements ResponseErrorHandler {

	private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.error("Response error: {} {}", response.getStatusCode(), response.getStatusText());
    }

    @Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return hasError(response.getStatusCode());
	}

	protected boolean hasError(HttpStatus statusCode) {
		return (statusCode.series() == HttpStatus.Series.CLIENT_ERROR
				|| statusCode.series() == HttpStatus.Series.SERVER_ERROR);
	}

}
