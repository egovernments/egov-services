/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *  
 *       Copyright (C) <2015>  eGovernments Foundation
 *  
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *  
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *  
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *  
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *  
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *  
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *  
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *  
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *  
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.egf.budget.web.advice;

import java.util.List;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.exception.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParamsError(final Exception ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomBindException.class)
    public ErrorResponse handleBindingErrors(final CustomBindException ex) {
        final ErrorResponse errRes = new ErrorResponse();
        final BindingResult errors = ex.getErrors();
        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
        errRes.setResponseInfo(responseInfo);
        final Error error = new Error();
        if (errors.getGlobalError() != null) {
            error.setCode(Integer.valueOf(errors.getGlobalError().getCode()));
            error.setMessage(errors.getGlobalError().getObjectName());
            error.setDescription(errors.getGlobalError().getDefaultMessage());
        } else if (errors.getFieldErrorCount() > 0)
            error.setDescription("Missing fields");
        if (errors.hasFieldErrors()) {
            final List<org.springframework.validation.FieldError> fieldErrors = errors.getFieldErrors();
            for (final org.springframework.validation.FieldError errs : fieldErrors) {
                final ErrorField f = new ErrorField(errs.getCode(), errs.getDefaultMessage(), errs.getField());
                error.getFields().add(f);

            }
        }
        errRes.setError(error);
        return errRes;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataException.class)
    public ErrorResponse handleBindingErrors(final InvalidDataException ex) {
        final ErrorResponse errRes = new ErrorResponse();

        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
        errRes.setResponseInfo(responseInfo);
        final Error error = new Error();
        error.setCode(Integer.valueOf(InvalidDataException.code));
        error.setMessage(ex.getFieldName());
        error.setDescription(ex.getMessage());
        errRes.setError(error);

        return errRes;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResponse handleThrowable(final Exception ex) {
        final ErrorResponse errRes = new ErrorResponse();
        ex.printStackTrace();
        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
        errRes.setResponseInfo(responseInfo);
        final Error error = new Error();

        error.setCode(500);
        error.setMessage("Internal Server Error");
        error.setDescription(ex.getMessage());
        return errRes;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleServerError(final Exception ex) {
        ex.printStackTrace();
        final ErrorResponse errRes = new ErrorResponse();

        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        errRes.setResponseInfo(responseInfo);
        final Error error = new Error();

        error.setCode(500);
        error.setMessage("Internal Server Error");
        error.setDescription(ex.getMessage());
        errRes.setError(error);
        return errRes;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ErrorResponse handleAuthenticationError(final UnauthorizedAccessException ex) {
        ex.printStackTrace();
        final ErrorResponse errRes = new ErrorResponse();

        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.UNAUTHORIZED.toString());
        errRes.setResponseInfo(responseInfo);
        final Error error = new Error();

        error.setCode(404);
        error.setMessage("Un Authorized Access");
        error.setDescription(ex.getMessage());
        errRes.setError(error);
        return errRes;
    }

}
