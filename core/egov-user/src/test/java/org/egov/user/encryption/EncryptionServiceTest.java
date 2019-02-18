package org.egov.user.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

@Slf4j
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