/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.asset.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.enums.Sequence;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@RunWith(MockitoJUnitRunner.class)
public class AssetCommonServiceTest {

    @InjectMocks
    private AssetCommonService assetCommonService;

    @Mock
    private SequenceGenService sequenceGenService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testPopulateErrors() {
        final ErrorResponse expectedErrResponse = getErrorResponse();
        final BindingResult errors = getBindingResultError();
        final ErrorResponse actualErrResponse = assetCommonService.populateErrors(errors);

        assertEquals(expectedErrResponse.toString(), actualErrResponse.toString());
    }

    @Test
    public void testGetDepreciationRate() {
        final Double depreciationRate = assetCommonService.getDepreciationRate(Double.valueOf("18.9745"));
        assertEquals(Double.valueOf("18.97"), depreciationRate);
    }

    @Test
    public void testGetCode() {
        final String expectedCode = "006";
        final List<Long> seqNumber = new ArrayList<Long>();
        seqNumber.add(Long.valueOf("6"));

        when(sequenceGenService.getIds(any(Integer.class), any(String.class))).thenReturn(seqNumber);
        final String actualCode = assetCommonService.getCode("%03d", Sequence.ASSETCATEGORYCODESEQUENCE);

        assertEquals(expectedCode, actualCode);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetNextId() {
        final Long expectedId = Long.valueOf("6");

        when(jdbcTemplate.queryForObject(any(String.class), any(Class.class))).thenReturn(6);
        final Long actualId = assetCommonService.getNextId(Sequence.ASSETCATEGORYSEQUENCE);

        assertEquals(expectedId, actualId);
    }

    private ErrorResponse getErrorResponse() {
        final ErrorResponse errRes = new ErrorResponse();

        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
        errRes.setResponseInfo(responseInfo);

        final Map<String, Object> fields = new LinkedHashMap<String, Object>();
        fields.put("name", null);

        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        error.setMessage(null);
        error.setFields(fields);
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
            return true;
        }

        @Override
        public int getFieldErrorCount() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public List<FieldError> getFieldErrors() {
            final List<FieldError> fieldErrors = new ArrayList<FieldError>();
            fieldErrors.add(getFieldError());
            return fieldErrors;
        }

        @Override
        public FieldError getFieldError() {
            final FieldError fieldError = new FieldError("name", "name", "name is mandatory");
            return fieldError;
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
