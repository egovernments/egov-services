package org.egov.mr.web.errorhandler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FieldErrorTest {

	@InjectMocks
	private FieldError fieldError;

	@Mock
	private FieldError feildErrorNoArgsConstructor = new FieldError();

	@Test
	public void test() {
		/**
		 * @Setter
		 */
		fieldError.setCode("00015");
		fieldError.setDescription("ErrorField");
		/**
		 * @ToSting
		 */
		fieldError.toString();
		/**
		 * @Builder & @EqualsAndHashCode
		 */
		assertTrue(fieldError.equals(FieldError.builder().code("00015").description("ErrorField").build()));
		/**
		 * @GetterMethods
		 */
		fieldError.getCode();
		fieldError.getDescription();
	}

}
