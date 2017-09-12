package org.egov.pgrrest.read.domain.service.validator;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.egov.pgrrest.common.domain.model.AttributeDataType;
import org.egov.pgrrest.common.domain.model.AttributeDefinition;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.domain.model.ServiceDefinition;
import org.egov.pgrrest.common.domain.model.ServiceStatus;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.junit.Test;
public class TextAreaAttributeValidatorTest {


	    @Test
	    public void test_should_not_throw_exception_when_Text_Area__format_is_valid() {
	        final TextareaAttributeValidator validator = new TextareaAttributeValidator();
	        final ServiceRequest serviceRequest = mock(ServiceRequest.class);
	        when(serviceRequest.getAttributeWithKey("TextArea1"))
	            .thenReturn(new AttributeEntry("TextArea1", "hai hello"));
	        final AttributeDefinition attributeDefinition = AttributeDefinition.builder()
	            .dataType(AttributeDataType.TEXTAREA)
	            .code("TextArea1")
	            .build();
	        final ServiceDefinition serviceDefinition = ServiceDefinition.builder()
	            .attributes(Collections.singletonList(attributeDefinition))
	            .build();

	        validator.validate(serviceRequest, serviceDefinition, ServiceStatus.CITIZEN_SERVICE_APPROVED);
	    }


	}

