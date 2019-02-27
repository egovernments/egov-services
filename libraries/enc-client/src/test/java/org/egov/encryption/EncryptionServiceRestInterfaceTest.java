package org.egov.encryption;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.mockito.Mock;

public class EncryptionServiceRestInterfaceTest {

    @Mock
    private EncryptionServiceRestInterface encryptionServiceRestInterface;

    private ObjectMapper mapper;

    @Before
    public void initialize() {
        encryptionServiceRestInterface = new EncryptionServiceRestInterface();
        mapper = new ObjectMapper(new JsonFactory());
    }


}