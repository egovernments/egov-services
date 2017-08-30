package org.egov.mr.web.errorhandler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ErrorTest {

	@InjectMocks
	private Error error;

	@Mock
	private Error errorNoArgsConstructor = new Error();

	@Test
	public void test() {

		/**
		 * @GetterMethods
		 */
		error.getCode();
		error.getDescription();
		error.getFields();
		error.getMessage();
		/**
		 * @ToSting
		 */
		error.toString();

		/**
		 * @Setter
		 */
		List<FieldError> fields = new ArrayList<>();
		error.setCode(Integer.valueOf(987456321));
		error.setDescription("Error");
		error.setMessage("ErrorMessage");
		error.setFields(fields);
		/**
		 * @Builder & @EqualsAndHashCode
		 */
		assertTrue(error.equals(Error.builder().code(Integer.valueOf(987456321)).description("Error")
				.message("ErrorMessage").fields(fields).build()));
	}

}
