package org.egov.mr.web.errorhandler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import org.egov.common.contract.response.ResponseInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ErrorResponseTest {

	@InjectMocks
	private ErrorResponse errorResponse;

	@Mock
	private ErrorResponse errorResponseNoArgsConstructuor = new ErrorResponse();

	@Mock
	private Error error;

	@Mock
	private List<FieldError> fields = new ArrayList<>();

	@Mock
	private ResponseInfo responseInfo = new ResponseInfo();

	@Test
	public void test() {
		error.setCode(Integer.valueOf(987456321));
		error.setDescription("ErrorDescription");
		error.setFields(fields);
		error.setMessage("ErrorMessage");

		/**
		 * @Setter
		 */
		errorResponse.setError(error);
		errorResponse.setResponseInfo(responseInfo);
		/**
		 * @Builder & @EqualsAndHashCode
		 */
		assertTrue(errorResponse.equals(ErrorResponse.builder().error(error).responseInfo(responseInfo).build()));
		/**
		 * @ToSting
		 */
		errorResponse.toString();
		/**
		 * @GetterMethods
		 */
		errorResponse.getError();
		errorResponse.getResponseInfo();
	}

}
