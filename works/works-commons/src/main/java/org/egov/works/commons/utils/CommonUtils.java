package org.egov.works.commons.utils;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by ramki on 1/11/17.
 */

@Service
public class CommonUtils {
    public String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
