package org.egov.eis.web.validator;

import org.egov.eis.model.Employee;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class EmployeeValidator implements Validator {

	@Override
	public boolean supports(Class<?> paramClass) {
		return Employee.class.isAssignableFrom(paramClass);
	}

	@Override
	public void validate(Object targetObject, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "", "");

		Employee employee = (Employee) targetObject;
	}
}
