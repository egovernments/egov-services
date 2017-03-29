package org.egov.pgr.employee.enrichment.factory;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DateFactory {

    public Date now() {
        return new Date();
    }
}
