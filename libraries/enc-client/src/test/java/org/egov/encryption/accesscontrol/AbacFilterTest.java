package org.egov.encryption.accesscontrol;

import lombok.extern.slf4j.Slf4j;
import org.egov.encryption.models.AccessType;
import org.junit.Test;

import static org.junit.Assert.*;

@Slf4j
public class AbacFilterTest {

    @Test
    public void test() {
        AccessType accessType = AccessType.PLAIN;
        AccessType accessType1 = AccessType.NONE;

        log.info(String.valueOf(accessType.compareTo(accessType1)));

        if(accessType.compareTo(accessType1) < 0) {
            log.info("Plain");
        } else {
            log.info("Mask");
        }
    }

}