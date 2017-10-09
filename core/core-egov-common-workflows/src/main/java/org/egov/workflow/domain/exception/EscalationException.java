package org.egov.workflow.domain.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EscalationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationRuntimeException.class);

    public EscalationException(final String msg) {
        super(msg);
        LOG.error(msg);
    }

    public EscalationException(final String msg, final Throwable throwable) {
        super(msg, throwable);
        LOG.error(msg, throwable);
    }
}
