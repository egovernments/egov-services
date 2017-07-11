package org.egov.pgrrest.read.web.adapters.error;

import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DraftNotFoundExceptionAdapterTest {

    @InjectMocks
    private DraftNotFoundExceptionAdapter draftNotFoundExceptionAdapter;

    @Test
    public void test_should_capture_draft_not_found_exception() {

        final ErrorResponse errorResponse = draftNotFoundExceptionAdapter.adapt(null);

        final List<ErrorField> errorFields = errorResponse.getError().getFields();
        assertNotNull(errorFields);
    }

}