package org.egov.pgr.rest.numbergeneration;

import org.springframework.stereotype.Service;

@Service
public interface SevaNumberGenerator {

	public String generate() throws Exception;

}
