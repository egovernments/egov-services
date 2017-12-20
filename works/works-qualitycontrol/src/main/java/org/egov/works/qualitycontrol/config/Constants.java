package org.egov.works.qualitycontrol.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {

    public static final String KEY_QUALITYTESTING_WORKS_WORKORDR_NOT_EXISTS = "WORKS_WORKORDR_NOT_EXISTS";
    public static final String MESSAGE_QUALITYTESTING_WORKS_WORKORDR_NOT_EXISTS = "Work order does not exist for given LOA";

    public static final String KEY_QUALITYTESTING_WORKS_LOA_NOT_EXISTS = "QUALITYTESTING_WORKS_LOA_NOT_EXISTS";
    public static final String MESSAGE_QUALITYTESTING_WORKS_LOA_NOT_EXISTS = "Letter of acceptance does not exist for given LOA";

    public static final String KEY_QUALITYTESTING_INVALID_FILESTOREID = "QUALITYTESTING_INVALID_FILESTOREID";
    public static final String MESSAGE_QUALITYTESTING_INVALID_FILESTOREID = "Letter of acceptance does not exist for given LOA";
}
