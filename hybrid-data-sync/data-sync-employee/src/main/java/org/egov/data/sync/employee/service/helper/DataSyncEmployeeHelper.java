package org.egov.data.sync.employee.service.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DataSyncEmployeeHelper {

    private static final Logger logger = LoggerFactory.getLogger(DataSyncEmployeeHelper.class);

    public static String updateSignatureQuery() {
        return "UPDATE ?.eg_user SET signature = ? where username=? ";
    }

}