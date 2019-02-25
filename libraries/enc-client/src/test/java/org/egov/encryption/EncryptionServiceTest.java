package org.egov.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class EncryptionServiceTest {

    ObjectMapper mapper;
    Configuration configuration;


    @Before
    public void initializeCommonObjects() throws IOException {
        mapper = new ObjectMapper(new JsonFactory());
        configuration = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS);
    }

    @Test
    public void encryptStringValue() {
        String value = "Private";
        String tenantId = "pb";
        String type = "Normal";


    }

    @Test
    public void test() throws IOException {

    }



}