package org.egov.asset.service;

import static org.junit.Assert.assertEquals;

import java.beans.PropertyEditor;
import java.util.List;
import java.util.Map;

import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@RunWith(MockitoJUnitRunner.class)
public class AssetCommonServiceTest {

    @InjectMocks
    private AssetCommonService assetCommonService;

    @Test
    public void test_populateErrors() {
        final ErrorResponse expectedErrResponse = getErrorResponse();
        final BindingResult errors = getBindingResultError();
        final ErrorResponse actualErrResponse = assetCommonService.populateErrors(errors);

        assertEquals(expectedErrResponse.toString(), actualErrResponse.toString());
    }

    @Test
    public void test_getDepreciationRate() {
        final Double depreciationRate = assetCommonService.getDepreciationRate(Double.valueOf("18.9745"));
        assertEquals(Double.valueOf("18.97"), depreciationRate);
    }

    private ErrorResponse getErrorResponse() {
        final ErrorResponse errRes = new ErrorResponse();

        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
        errRes.setResponseInfo(responseInfo);

        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        error.setMessage(null);
        errRes.setError(error);
        return errRes;
    }

    private BindingResult getBindingResultError() {
        final BindingResult error = new ErrorTest();
        error.rejectValue("name", "name", "Name is not present");
        error.reject(HttpStatus.BAD_REQUEST.toString());
        return error;
    }

    class ErrorTest implements BindingResult {

        @Override
        public String getObjectName() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setNestedPath(final String nestedPath) {
            // TODO Auto-generated method stub

        }

        @Override
        public String getNestedPath() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void pushNestedPath(final String subPath) {
            // TODO Auto-generated method stub

        }

        @Override
        public void popNestedPath() throws IllegalStateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void reject(final String errorCode) {
            // TODO Auto-generated method stub

        }

        @Override
        public void reject(final String errorCode, final String defaultMessage) {
            // TODO Auto-generated method stub

        }

        @Override
        public void reject(final String errorCode, final Object[] errorArgs, final String defaultMessage) {
            // TODO Auto-generated method stub

        }

        @Override
        public void rejectValue(final String field, final String errorCode) {
            // TODO Auto-generated method stub

        }

        @Override
        public void rejectValue(final String field, final String errorCode, final String defaultMessage) {
            // TODO Auto-generated method stub

        }

        @Override
        public void rejectValue(final String field, final String errorCode, final Object[] errorArgs,
                final String defaultMessage) {
            // TODO Auto-generated method stub

        }

        @Override
        public void addAllErrors(final Errors errors) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean hasErrors() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int getErrorCount() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public List<ObjectError> getAllErrors() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean hasGlobalErrors() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int getGlobalErrorCount() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public List<ObjectError> getGlobalErrors() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ObjectError getGlobalError() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean hasFieldErrors() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int getFieldErrorCount() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public List<FieldError> getFieldErrors() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public FieldError getFieldError() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean hasFieldErrors(final String field) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int getFieldErrorCount(final String field) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public List<FieldError> getFieldErrors(final String field) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public FieldError getFieldError(final String field) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object getFieldValue(final String field) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Class<?> getFieldType(final String field) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object getTarget() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Map<String, Object> getModel() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object getRawFieldValue(final String field) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public PropertyEditor findEditor(final String field, final Class<?> valueType) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public PropertyEditorRegistry getPropertyEditorRegistry() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void addError(final ObjectError error) {
            // TODO Auto-generated method stub

        }

        @Override
        public String[] resolveMessageCodes(final String errorCode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String[] resolveMessageCodes(final String errorCode, final String field) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void recordSuppressedField(final String field) {
            // TODO Auto-generated method stub

        }

        @Override
        public String[] getSuppressedFields() {
            // TODO Auto-generated method stub
            return null;
        }

    }

}
