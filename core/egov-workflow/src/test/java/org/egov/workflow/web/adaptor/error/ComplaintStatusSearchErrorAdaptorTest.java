package org.egov.workflow.web.adaptor.error;

import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.workflow.domain.model.ComplaintStatusSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintStatusSearchErrorAdaptorTest {

    @Mock
    ComplaintStatusSearchCriteria complaintStatusSearchCriteria;

    @InjectMocks
    ComplaintStatusSearchErrorAdaptor complaintStatusSearchErrorAdaptor;

    @Test
    public void should_set_error_fields_when_name_is_not_present() {
        when(complaintStatusSearchCriteria.isCodeAbsent()).thenReturn(true);

        ErrorResponse errorResponse = complaintStatusSearchErrorAdaptor.adapt(complaintStatusSearchCriteria);

        List<ErrorField> fields = errorResponse.getError().getFields();

        assertThat(fields.size()).isEqualTo(1);
        assertThat(fields.get(0).getMessage()).isEqualTo("Current status is required.");
    }

    @Test
    public void should_set_error_fields_when_user_roles_are_missing() {
        when(complaintStatusSearchCriteria.isRolesAbsent()).thenReturn(true);

        ErrorResponse errorResponse = complaintStatusSearchErrorAdaptor.adapt(complaintStatusSearchCriteria);

        List<ErrorField> fields = errorResponse.getError().getFields();

        assertThat(fields.size()).isEqualTo(1);
        assertThat(fields.get(0).getMessage()).isEqualTo("User has no roles associated with.");
    }
}