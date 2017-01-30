package org.egov.pgr.service;

import org.springframework.stereotype.Service;

@Service
public interface SevaNumberGeneratorService {

    String generate() throws Exception;

}
