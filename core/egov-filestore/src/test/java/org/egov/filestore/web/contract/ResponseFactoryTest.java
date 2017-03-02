package org.egov.filestore.web.contract;

import org.egov.filestore.domain.model.FileInfo;
import org.egov.filestore.domain.model.FileLocation;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;


public class ResponseFactoryTest {

    private ResponseFactory responseFactory;

    private final String MODULE = "pgr";
    private final String JURISDICTION_ID = "mumbai";
    private final String TAG = "tag";
    private final String FILE_STORE_ID_1 = "FileStoreID1";
    private final String FILE_STORE_ID_2 = "FileStoreID2";
    private final String CONTENT_TYPE= "contentType";

    @Before
    public void setUp() throws Exception {
        responseFactory = new ResponseFactory("/fileStore");
    }

    @Test
    public void test_getFilesByTagResponse_from_FileInfo() throws Exception {
        FileLocation fileLocation1 = new FileLocation(FILE_STORE_ID_1, MODULE, JURISDICTION_ID, TAG);
        FileLocation fileLocation2 = new FileLocation(FILE_STORE_ID_2, MODULE, JURISDICTION_ID, TAG);
        FileInfo fileInfo1 = new FileInfo(CONTENT_TYPE, fileLocation1);
        FileInfo fileInfo2 = new FileInfo(CONTENT_TYPE, fileLocation2);
        List<FileInfo> listOfFileInfo = asList(fileInfo1, fileInfo2);

        FileRecord fileRecord1 = new FileRecord("/fileStore/files/FileStoreID1", CONTENT_TYPE);
        FileRecord fileRecord2 = new FileRecord("/fileStore/files/FileStoreID2", CONTENT_TYPE);
        GetFilesByTagResponse expected = new GetFilesByTagResponse(asList(fileRecord1, fileRecord2));

        GetFilesByTagResponse result = responseFactory.getFilesByTagResponse(listOfFileInfo);

        assertThat(result).isEqualTo(expected);
    }
}